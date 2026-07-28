# Auditoria RuneCore

Varredura de `src/main/java` (58 arquivos, ~6k linhas). Como o RuneCore é consumido por
outros mods (o SimTale usa `EffectHelper` e `StatHelper`), a prioridade foi a superfície
pública e o pipeline de combate.

Não foi possível compilar aqui (o projeto pede JDK 25 e o ambiente só tem JRE 11). A
verificação foi estática: balanceamento estrutural de todos os arquivos, checagem de que todo
`LOG` usado tem declaração, imports sem duplicata, e conferência de assinaturas reais no
bytecode do `HytaleServer.jar` (`InventoryComponent.getItemInHand`, `EntityStatValue.getMin/getMax`).
**Compile antes de publicar.**

---

## 1. Corrigido

### 1.1 PvP causava dano zero
O achado principal, e vale detalhar porque envolve três arquivos.

`CombatDamageInterceptor` resolve PvP com
`defenderStats.calculateFinalDamage(attackerStats)` — o dano final sai **inteiramente** dos
stats do atacante, o `damage.getAmount()` original é descartado.

Só que os stats ofensivos do atacante nunca eram preenchidos:

- `CombatStatsManager.onPlayerReady` cria um `CombatStats` **zerado** ao entrar.
- `EquipmentStatsListener` é a única coisa que popula esses stats, e ele filtra
  `event.getComponentType() != InventoryComponent.Armor.getComponentType()` — ou seja, lê
  **apenas o container de armadura**.
- `CombatStatsDefaults` registra 41 itens **incluindo espadas com 6 a 50 de dano físico**,
  que jamais chegam ao container de armadura.

Resultado: `physRaw = 0`, `magRaw = 0`, `trueRaw = 0` → `damage.setAmount(0)`. Um jogador
batia em outro com uma Adamantite e causava **zero**.

Corrigido lendo a arma empunhada no momento do hit
(`InventoryComponent.getItemInHand`), o que também faz troca de slot na hotbar refletir na
hora. A contribuição transitória entra por `CombatStats.Offense`, um record novo no pacote
`api` — assim o `api` não passa a depender do registry em `systems`.

### 1.2 `DEBUG = true` fixo no código publicado
`CombatDamageInterceptor` tinha `private static final boolean DEBUG = true` e emitia até
cinco linhas em **INFO por evento de dano**, com concatenação de string no caminho quente.
Agora é `-Drunecore.combat.debug=true`, desligado por padrão.

Mesmo problema em `StatusEffectHelper`, que logava a cada tick de sangramento e a cada tick
de náusea.

### 1.3 Buffs não tickavam fora de um único mundo
`EffectTickSystemBridge` guardava o último step processado em **um** `AtomicLong` estático
compartilhado por todos os mundos. Como cada mundo tem seu próprio contador de tick, o
primeiro a chegar num step vencia o compare-and-set e todos os outros caíam no guard
`step <= currentLast` — buffs de entidades fora daquele mundo **nunca tickavam**, e qual
mundo "vencia" dependia da ordem de tick. Agora o step é rastreado por mundo.

### 1.4 Teto de HP fixo, e duas APIs discordando entre si
`StatHelper.modifyHealth` limitava cura a `Math.min(100f, ...)`. `PlayerStats.modifyStat`
limitava o **mesmo stat** a `Math.min(1000f, ...)`. Duas APIs da mesma biblioteca com tetos
diferentes, e ambos arbitrários.

`EntityStatValue` expõe `getMin()`/`getMax()`. Os dois agora clampam pelos limites reais do
stat.

### 1.5 Código morto com número mágico
`EffectHelper.spawnParticleEffect` fazia um lookup de `MovementManager` para alimentar um
`if` **de corpo vazio** testando `Math.abs(pos.y - 83.0) < 1.0` — resquício de um bug de
altura que estava sendo caçado na época. Custava um lookup de componente por partícula
gerada e não fazia nada. Removido, junto com o `catch (Exception)` que virou
`catch (RuntimeException)`.

### 1.6 `System.out`/`System.err` (40 ocorrências, 13 arquivos)
Convertidos para `java.util.logging.Logger`, com `err` → `warning` e `out` → `fine`. Uma
biblioteca não deve escrever direto no stdout do servidor que a hospeda.

---

## 2. Pendente — não mexi

### 2.1 Stats de combate só valem quando o alvo é um jogador
`CombatDamageInterceptor.getQuery()` devolve `Player.getComponentType()`, então o sistema só
intercepta dano **recebido por jogadores**. Dano de jogador → criatura passa intocado: nem
armadura da criatura, nem penetração, nem dano mágico da arma entram na conta. Metade do
modelo de combat stats não tem efeito em PvE ofensivo.

Não mexi porque pode ser intencional, mas se for, vale documentar na API — hoje `armorPenetration`
num item sugere um comportamento que só existe em PvP.

### 2.2 `build.gradle` fixa o caminho da casa do autor
```gradle
destinationDirectory = file("/home/cookie/.var/app/com.hypixel.HytaleLauncher/.../Mods/")
```
Num projeto que é biblioteca, isso quebra o build de qualquer outra pessoa (e de qualquer
CI). Devia ser um caminho relativo com override opcional via propriedade do Gradle.

### 2.3 `calculateFinalDamage` tem efeito colateral
Ela drena `shieldHP` via `absorbDamage`. Um nome que parece cálculo puro mas consome
recurso — chamar duas vezes para "simular" o dano gasta o escudo de verdade. Documentei o
comportamento no Javadoc, mas o ideal é separar cálculo de aplicação.

### 2.4 Singletons por efeito colateral de construtor
`CombatStatsRegistry`, `CreatureCombatRegistry`, `CombatStatsManager` e `RuneCoreHudManager`
fazem `instance = this` no construtor, com o campo `static` **não-volátil**. Consequências:
construir uma segunda instância sequestra o singleton em silêncio; `get()` devolve `null`
se chamado antes do `setup()`; e a publicação entre a thread de setup e as threads de tick
não tem barreira de memória formal.

### 2.5 `sumModifiers` é O(n) por getter
`CombatStats.calculateFinalDamage` chama ~6 getters, e cada um varre **todos** os
modificadores procurando os da sua stat. Seis varreduras completas por evento de dano. Um
`Map<String, Float>` acumulado por stat resolveria.

### 2.6 `PlayerStats` e o sentinela `-1f`
`getStat` devolve `CompletableFuture<Float>` que completa com `-1f` quando o stat não existe
— indistinguível de um valor real. E o future **nunca expira**: se o mundo descarregar entre
o agendamento e a execução, quem chamar `.join()` trava para sempre.
`getMaxMana()` devolve `100f` fixo com um `// Default for now`.

### 2.7 Armadilha no `ActiveBuff`
O `Builder` tem `intervalTicks = 0` por padrão, e `tick()` só dispara `onTick` se
`intervalTicks > 0`. Quem esquecer o `.interval(...)` recebe um buff que expira normalmente
mas **nunca aplica seu efeito periódico**, sem erro nenhum. Conferi os 24 buffs de
`CoreEffects` e nenhum está nessa situação hoje — mas é uma armadilha aberta para quem
consumir a API.

### 2.8 Menores
- `EffectTickSystem` compara mundos por `getName().equals(...)` em vez de identidade.
- `EffectTickSystem.tick(world)` percorre **todos** os buffs a cada mundo, filtrando depois.
- 22 `toLowerCase()` sem `Locale` (quebra em locale turco).
- `SwitchSpellCommand:24` tem `catch (Exception ignored) {}` vazio.
- `test/DevSimulation.java` não é referenciado por nada e vai junto no jar.
- `Main.setup()` registra 26 codecs de splash num laço, todos apontando para a mesma classe e
  o mesmo `CODEC` já registrado como `runecore:potion_splash_generic`.
- Os registries do `RuneCore` (singleton) são `HashMap` comum. Só é seguro porque tudo é
  registrado no `setup()`; qualquer `registerEffect`/`on()` em runtime é uma corrida.

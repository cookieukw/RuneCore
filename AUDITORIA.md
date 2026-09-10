# Auditoria RuneCore

Varredura de `src/main/java` (58 arquivos, ~6k linhas). Como o RuneCore é consumido por
outros mods (o SimTale usa `EffectHelper` e `StatHelper`), a prioridade foi a superfície
pública e o pipeline de combate.

A auditoria foi feita sem compilar (o projeto pede JDK 25 e o ambiente da análise só tinha
JRE 11): balanceamento estrutural de todos os arquivos, checagem de que todo `LOG` usado tem
declaração, imports sem duplicata, e conferência de assinaturas reais no bytecode do
`HytaleServer.jar` (`InventoryComponent.getItemInHand`, `EntityStatValue.getMin/getMax`).

**Desde então o projeto ganhou suíte de testes** (JUnit 5, `./gradlew test`): 63 testes sobre a
lógica pura — atributos, contêiner de modificadores, fórmula de dano, pipeline, tabela de
apelidos do comando e o DSL de defesa das criaturas. Inclui uma regressão explícita para o bug
1.1 (dano zero em PvP). Todos passando.

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

### 2.1 ~~Stats de combate só valem quando o alvo é um jogador~~ — implementado

Era: `getQuery()` devolvia `Player.getComponentType()`, então o sistema só interceptava dano
**recebido por jogadores**, e metade do modelo (penetração, dano mágico da arma) não tinha
efeito nenhum em PvE.

Estendido para criaturas, com três decisões:

- **Alvos:** `Query.or(Player, ModelComponent)`, mas criatura ausente do
  `CreatureCombatRegistry` é ignorada — o dano dela continua exatamente como o engine
  calculou. O alcance efetivo é o que o `CreatureCombatDefaults` declara.
- **Modelo:** simetria total com PvP. Jogador → criatura agora resolve
  `defenderStats.calculateFinalDamage(attackerStats, armaEmpunhada)`, descartando o bruto do
  engine, igual PvP já fazia. Criatura → criatura mantém o bruto como base e só aplica
  mitigação, porque criatura não tem bloco de stats ofensivos (o registry só descreve perfil
  e penetração).
- **Dados:** `CreatureCombatData` ganhou `armor`, `magicResist` e `damageReduction`, que não
  existiam — o registry só descrevia criaturas como atacantes.

Os valores foram atribuídos **por família**, via um DSL de init (`setGroupDefense`) que evita
tocar nas 196 linhas de registro. Cada família tem uma linha e uma justificativa:

| Família | Armadura | Resist. mágica | DR | Racional |
| :--- | ---: | ---: | ---: | :--- |
| Wildlife | 2 | 0 | — | bichos e gado, praticamente sem proteção |
| Zombies | 4 | 2 | — | podres e lentos |
| Goblins | 5 | 3 | — | couro improvisado |
| Trorks | 6 | 0 | — | guerreiros tribais, sem proteção mágica |
| Ferans | 8 | 8 | — | feras ágeis, equilibradas |
| Misc | 8 | 8 | — | não classificados |
| Skeletons | 10 | 4 | — | osso apara lâmina melhor que magia |
| Outlanders | 12 | 6 | — | saqueadores equipados |
| Void | 12 | 28 | — | invertido: a magia é o escudo deles |
| Scaraks | 18 | 4 | — | quitina: dura contra aço, fraca contra magia |
| Spirits | 4 | 32 | — | incorpóreos, lâmina atravessa |
| Dragons | 32 | 28 | — | escamados e antigos |
| Golems | 34 | 12 | — | a parede física |
| Bosses | 40 | 34 | 15% | ainda ganham redução plana |

**Isso é chute meu e precisa de balanceamento in-game.** Com a fórmula
`dano * 100 / (100 + armadura)`, uma Espada de Ferro (22 físico) entrega ~21.6 num bicho e
~15.7 num boss (13.4 com o DR) — a curva é suave de propósito, para não invalidar arma nenhuma.
Um `withDefense(...)` explícito em qualquer criatura sobrescreve o tier da família.

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

**Atualização — parcialmente mexido, não fechado.** As duas refatorações recentes
(`18df27b` em `CombatStatsManager`, `d334c7e` em `InvisibilityManager`) adiaram o `instance =
this` para o fim do construtor, o que resolve o "`get()` devolve algo pela metade" — mas só
`InvisibilityManager` ganhou `volatile` no campo. `CombatStatsRegistry`, `CreatureCombatRegistry`,
`CombatStatsManager` e `RuneCoreHudManager` continuam com `static` puro, sem barreira de
memória — e `CombatStatsManager` é justamente o que `CombatDamageInterceptor.handle()` chama em
**todo** evento de dano via `CombatStatsManager.get()`. Risco prático baixo (o `setup()` roda
antes de qualquer thread de tick nascer, na prática), mas o item continua tecnicamente aberto
exatamente como descrito acima — só ficou inconsistente entre os cinco singletons.

**Atualização 2 — fechado.** Os quatro singletons que faltavam (`CombatStatsRegistry`,
`CreatureCombatRegistry`, `CombatStatsManager`, `RuneCoreHudManager`) ganharam `volatile` no
campo `instance`, igualando-os ao `InvisibilityManager`. Os cinco agora têm barreira de memória
formal na publicação do singleton; item 2.4 considerado resolvido.

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

---

## 3. Invisibilidade

Três bugs distintos, todos corrigidos. Ver `scripts/create-issues.sh` para as issues.

### 3.1 O jogador era ocultado do próprio cliente
`applyInvisibility` percorria `world.getPlayerRefs()` e chamava `hidePlayer(uuid)` em **todo**
observador — inclusive no próprio alvo. Como o `HiddenPlayersManager` é por observador (campo
`playerRef` = quem olha, mais um `Set<UUID>`), isso mandava o cliente do jogador parar de
rastrear a própria entidade.

Daí os dois sintomas: não se ver, e **cair pelo chão e morrer** — cliente sem a própria entidade
não tem no que colidir. O laço agora pula o alvo.

### 3.2 Quem entrava no meio do efeito enxergava normalmente
O hide era um disparo único para quem estava online. Sem hook de entrada, ao contrário do
`RuneCoreHudManager` e do `CombatStatsManager`. O novo `InvisibilityManager` mantém o conjunto
autoritativo e atualiza quem chega.

### 3.3 Morrer ou deslogar deixava invisível para sempre
O revert só existia no `onExpire` do buff, e duas coisas o impediam de rodar: o
`EffectTickSystem` descartava buff com ref inválida sem chamar `onExpire`, e o `ActiveBuff`
ainda protegia a chamada atrás de `ref.isValid()`.

Corrigido na raiz — `ActiveBuff.expire` roda sempre, o `EffectTickSystem` o invoca nos caminhos
de órfão e de cancelamento, e o revert da invisibilidade passou a ser chaveado pelo UUID em vez
do ref. **Isso vale para todos os efeitos**, não só invisibilidade: qualquer buff que precisasse
reverter algo ao morrer agora reverte.

### 3.4 Translúcido só para si — não é possível
Pedido levantado e investigado: `HiddenPlayersManager` é binário e `protocol.Opacity` é
iluminação de bloco/fluido. Não há canal por observador para alterar renderização de entidade.

Alternativa viável: um `EntityEffect` visual preso na própria entidade seria naturalmente
só-seu, já que todos os outros têm o jogador oculto. Falta decidir o asset.


---

## 4. Integração com o SimTale

O SimTale consome uma fatia pequena e específica da API pública: `StatHelper.addHealth` /
`subtractHealth` (cura por comida, custo de vida do parto), `EffectHelper.modifyMovement` +
`EffectHelper.DEFAULT_SPEED` (debuff de velocidade da gravidez), `StatusEffectHelper.applyBleeding`
/ `revertBleeding` (aviso visual de "NPC morrendo", ver `testing_checklist.md` seção 11) e
`RuneCoreItemManager.register` (despacho dos itens customizados via `RuneCore_GenericItemUse`).

`StatHelper` e `RuneCoreItemManager`/`RuneCoreGenericItemInteraction` estão corretos e bem
desenhados para esse uso — `StatHelper` funciona em cima de `EntityStatMap`, um componente
genérico que qualquer entidade com stats tem (jogador ou NPC), e o item 1.4 já corrigido (clamp
pelos limites reais do stat) vale para os dois lados igualmente. O despacho por sufixo do
`RuneCoreItemManager`, com o cuidado de não derrubar a interação num item não reconhecido (ver
`RuneCoreGenericItemInteraction`), é exatamente o que os ~10 itens do SimTale precisam.

Os outros dois pontos, porém, são chamados **em cima de uma NPC do SimTale** (`Ref<EntityStore>`
sem `PlayerRef`) — e tanto `EffectHelper.modifyMovement` quanto o `applyBleeding`/`revertBleeding`
foram desenhados em volta de um jogador real conectado. O resultado é o mesmo dos dois lados: a
chamada roda, não lança exceção nenhuma, e não faz **absolutamente nada**.

### 4.1 Debuff de velocidade da gravidez não afeta NPC nenhuma

`PregnancyManager.applyPregnancySpeedDebuff` e o reset em `birthBaby()` chamam
`EffectHelper.modifyMovement(mother.entityRef, s -> s.baseSpeed = ...)` com o `Ref` da própria
NPC grávida. `modifyMovement` resolve o efeito lendo o componente
`com.hypixel.hytale.server.core.entity.entities.player.movement.MovementManager` do alvo — e
esse componente **só é atribuído a jogador**. Confirmado no bytecode do `HytaleServer.jar`: quem
atribui esse `ComponentType` é literalmente uma classe chamada
`PlayerMovementManagerSystems$AssignmentSystem`, e o próprio `MovementManager.resetDefaultsAndUpdate`
consulta `PlayerRef` por dentro. NPC não tem esse componente — `store.getComponent(ref,
MovementManager.getComponentType())` devolve `null`, `modifyMovement` cai no `if (mm == null)
return;` e não faz nada, silenciosamente, sempre.

Bate exatamente com o que o próprio `NPCMovementHelper` do SimTale já deixa implícito: ele não
toca em `MovementManager`/`MovementSettings`/`baseSpeed` em lugar nenhum — a NPC anda via
`NPCEntity.setLeashPoint` + `StateSupport.setState`, o sistema nativo de role/estado, que nem
sabe que `MovementSettings` existe. Ou seja: mesmo que o componente existisse na NPC, mudar
`baseSpeed` não mudaria a velocidade de patrulha/trabalho dela de qualquer forma — são dois
sistemas de movimento completamente diferentes.

**Único caminho que funciona hoje**: `applyPlayerPregnancyBehavior`/`applyPregnancySpeedDebuff`
chamado com um `playerRef` de verdade (jogadora grávida). Toda NPC grávida do SimTale anda no
ritmo normal o tempo todo, em qualquer trimestre — o código não quebra, só não faz o que o
comentário do próprio arquivo descreve.

**Atualização — workaround aplicado no SimTale, não é uma correção de verdade.** Como não há
nenhuma API pública nativa pra velocidade de NPC (nem em `MovementManager`, exclusivo de
jogador, nem em `NPCEntity`, que só expõe getter/invalidador de cache, nem um stat genérico de
velocidade em `DefaultEntityStatTypes`), a solução ficou inteiramente do lado do SimTale:
`PregnancyManager.isPausedTick` (novo) decide, por trimestre, se o tick atual deve "congelar" a
NPC, e `NPCMovementHelper.moveTo`, quando congelado, prende o leash na própria posição atual da
NPC em vez do destino real — 2º trimestre pausa 4 de cada 20 ticks, 3º pausa 10 de cada 20. O
resultado é uma cadência de "anda um pouco, para um pouco" (tipo um rengueio), não uma
velocidade real reduzida — é aproximação visual, não mexe em física nem em pathfinding. Compila
limpo, mas **não foi testado em jogo ainda**; os números de pausa (4/20 e 10/20) são um chute
inicial e provavelmente vão precisar de ajuste depois de ver rodando de verdade.

### 4.2 Aviso visual de sangramento da NPC morrendo nunca aparece

`RoutineAISystem` chama `StatusEffectHelper.applyBleeding(ref)` / `revertBleeding(ref)` num `Ref`
de NPC como aviso visual nos ~10s antes do Ceifador chegar (`testing_checklist.md` seção 11). Só
que `applyBleeding` **não é** "aplique o efeito de sangramento no mundo" — é só a metade
"atualize o ícone no HUD de quem já está sangrando":

```java
// DamageOverTimeEffects.java
// Bleeding and burn: HUD state only. The visuals come from the native entity effect assets.
static void applyBleeding(Ref<EntityStore> ref) {
    EffectHelper.updateHud(ref, hud -> hud.setBleeding(true));
}
```

E `EffectHelper.updateHud` só roda a ação se o **próprio alvo** tiver um `PlayerRef` — é o HUD da
tela de quem está sangrando, não um efeito visível para quem olha de fora:

```java
static void updateHud(Ref<EntityStore> ref, Consumer<RuneCoreHud> action) {
    ...
    PlayerRef pr = (PlayerRef) store.getComponent(ref, PlayerRef.getComponentType());
    if (pr != null) { ... }
}
```

NPC não tem `PlayerRef`. `pr` é sempre `null`, a lambda nunca roda, e nada acontece — nem pra
quem está perto olhando a NPC, nem pra ninguém.

O efeito "bleeding" de verdade (`CoreEffects.java`, `core.registerEffect(new
RuneEffect("bleeding", 300).withAsset("Bleeding")...)`) é mais do que só isso: tem o
`.withAsset("Bleeding")` (o efeito visual nativo de verdade, esse sim visível no mundo) e um
`ActiveBuff` que dá `StatHelper.subtractHealth(ref, 1.0f)` a cada segundo. `applyBleeding` e
`revertBleeding` são só os dois ganchos de HUD que esse `RuneEffect` chama por dentro do próprio
`.withAction`/`.withBuff` — nunca foram pensados pra ser chamados soltos, de fora, num `Ref`
qualquer. O SimTale pegou a peça errada do conjunto: parece a API pública certa (é `public
static`, compila, não lança exceção), mas sozinha ela não aplica o efeito nativo nem causa dano
nenhum — só o ícone de HUD, que também não serve pra NPC.

**Se quiser o efeito de verdade** (visível para quem está por perto, o que é o objetivo descrito
no checklist), o caminho é `RuneCore.get().getEffect("bleeding")` e aplicar esse `RuneEffect` de
verdade no alvo — não chamar `StatusEffectHelper.applyBleeding` direto. Não mexi em nada disso
agora, só documentei; avise se quiser que eu implemente a correção de um lado ou do outro.

**Atualização — corrigido.** Em vez de rotear pelo `RuneEffect`/`ActiveBuff` completo (que
traria dano junto, indesejado aqui — é só um aviso visual, não um efeito de sangramento de
verdade), o RuneCore ganhou uma API nova e genuinamente genérica em `EffectHelper`:
`applyVisualEffect(ref, effectId, durationSeconds)` / `removeVisualEffect(ref, effectId)`,
construída direto em cima de `EntityEffect` + `EffectControllerComponent` (sem checagem de
`PlayerRef` em lugar nenhum — funciona em qualquer entidade). O `RoutineAISystem` do SimTale
trocou a chamada quebrada por `EffectHelper.applyVisualEffect(ref, "Bleeding", 10.0f)` /
`removeVisualEffect(ref, "Bleeding")`. Validado por compilação cruzada (RuneCore isolado +
SimTale inteiro contra um jar novo do RuneCore, ambos limpos); ainda não visto rodando ao vivo.

### 4.3 O que não é um problema

O pipeline de combate novo (`CombatDamageInterceptor`, `DamagePipeline`, `CreatureCombatRegistry`)
não tem nenhum ponto de contato com o SimTale hoje — o Guarda do SimTale mata o hostil removendo a
entidade direto (`commandBuffer.removeEntity`), sem gerar nenhum `Damage` de verdade, então esse
sistema nunca chega a rodar para esse caso. `CombatDamageInterceptor.getQuery()` bate em qualquer
entidade com `ModelComponent` — incluindo as NPCs humanoides do próprio SimTale — mas
`CombatParticipants.creatureFor` não reconhece os ids de modelo do SimTale (não estão em
`CreatureCombatRegistry`), então o handler devolve `null` e sai sem tocar em nada. Não é bug, é o
comportamento de "não tenho opinião sobre essa criatura" funcionando como descrito — só vale
registrar que é exatamente o gancho que a Fase 2 do combate do Guarda (dano de verdade, projétil)
vai precisar usar se decidir se apoiar no sistema de dano do RuneCore em vez de reinventar um.

# 🛠️ Guia de Uso da API RuneCore

Bem-vindo ao guia de desenvolvimento do RuneCore! Este documento explica como usar o motor RuneCore para criar seu próprio conteúdo mágico para o Hytale.

---

## 1. Primeiros Passos

O RuneCore é um motor baseado em **Entity-Component-System (ECS)**. Para usá-lo, geralmente você precisa registrar seus sistemas no ponto de entrada (onEnable) do seu plugin Hytale.

```java
public class MyMagicPlugin extends BasePlugin {
    @Override
    public void onEnable(EventRegistry eventRegistry) {
        // Registrar os sistemas do RuneCore
        eventRegistry.registerGlobal(EffectTimerListener.class);
        eventRegistry.registerGlobal(CastListener.class);
    }
}
```

---

## 2. Registrando Essências

As essências são o "combustível" para seus feitiços. Cada essência está ligada a um dos 20 elementos.

```java
// Criar uma Essência de Fogo nível 1
Essence fireEssence = new Essence("essence_fire", RuneElement.FIRE, 1);
RuneCore.get().registerEssence(fireEssence);
```

---

## 3. Criando Efeitos de Status Personalizados

O RuneCore possui um sistema robusto de **ActiveBuff**. Você pode criar efeitos que executam lógica ao longo do tempo (ticking), possuem intervalos personalizados e se auto-limpam automaticamente.

### Exemplo: Um Efeito de Regeneração
```java
RuneEffect regen = new RuneEffect("regeneration", 400) // Duração de 400 ticks
    .withAsset("runecore:Regeneration")
    .withBuff(ctx -> {
        // Gerar um ID único para este buff do jogador
        String uid = ctx.source.getUuid().toString();
        
        return ActiveBuff.builder(uid, "regeneration", 400)
            .interval(50) // Executa a cada 50ms (aproximadamente a cada 1 tick)
            .onTick(ref -> EffectHelper.addHealth(ref, 1.0f)) // Cura em cada tick
            .build();
    });

RuneCore.get().registerEffect(regen);
```

### Exemplo: Um Aumento de Velocidade
```java
RuneEffect speedBuff = new RuneEffect("speed", 1200)
    .withBuff(ctx -> {
        String uid = ctx.source.getUuid().toString();
        
        // Use EffectHelper para aplicar a mudança de status imediatamente
        EffectHelper.applySpeed(ctx.source.getReference(), 0.15f);
        
        return ActiveBuff.builder(uid, "speed", 1200)
            .onExpire(ref -> EffectHelper.revertSpeed(ref)) // Reverter quando acabar
            .build();
    });
```

---

## 4. Usando o EffectHelper

O `EffectHelper` fornece métodos padronizados para modificar os atributos das entidades e sincronizá-los com o servidor Hytale.

*   **Vida:** `addHealth(ref, amount)`, `subtractHealth(ref, amount)`
*   **Movimento:** `applySpeed(ref, amount)`, `applySlowness(ref, amount)`, `revertSpeed(ref)`
*   **Mineração:** `applyHaste(ref, amount)`, `revertHaste(ref)`

---

## 5. Conjurando Feitiços (Spells)

Você pode agrupar vários efeitos em um único `Spell` e conjurá-lo usando um `CastContext`.

```java
// Definir o feitiço
Spell fireBlast = new Spell("fire_blast")
    .addCost("mana", 20)
    .addEffect("burn")
    .addEffect("instant_damage");

// Conjurar o feitiço
CastContext ctx = new CastContext(playerRef, targetRef, world, 1.0);
RuneCore.get().castSpell("fire_blast", ctx);
```

---

## 6. Atributos de Combate

Atributos de combate (armadura, penetração, dano, ...) são **dados registráveis**, não uma lista
fixa. O RuneCore traz oito deles; seu mod pode adicionar o próprio e fazê-lo participar do combate.

### Lendo e escrevendo

`RuneAttributes` é o ponto de entrada. Todo método é null-safe e devolve `Optional` em vez de
lançar exceção durante um evento de dano.

```java
// built-ins
RuneAttributes.of(playerUuid).ifPresent(attrs -> {
    float armadura = attrs.get(CoreAttributes.ARMOR);
    attrs.setBase(CoreAttributes.MAGIC_RESIST, 25f);
});

// a partir de um ref de entidade ou de um PlayerRef
RuneAttributes.of(entityRef).ifPresent(attrs -> ...);
```

### Valor base vs modificadores

Um atributo resolvido é `base + soma(modificadores)`, limitado aos bounds declarados. Os
modificadores têm nome, então podem ser removidos com precisão — é assim que equipamento aplica
e desfaz os bônus dele.

```java
attrs.setBase(CoreAttributes.ARMOR, 10f);
attrs.addModifier("meumod:bencao", CoreAttributes.ARMOR, 5f);   // resolve em 15
attrs.removeModifier("meumod:bencao");                          // volta pra 10
```

Registrar o mesmo id de modificador de novo **substitui** em vez de empilhar, então reaplicar a
cada troca de equipamento é seguro.

### Declarando o seu atributo

```java
public static final RuneAttribute ROUBO_DE_VIDA =
        AttributeRegistry.register(RuneAttribute.fraction("meumod:lifesteal", 1f));

// positive() → 0..∞, fraction(max) → 0..max
```

Ids têm namespace e são normalizados para minúsculo. Registrar um id que outro mod já pegou
lança `IllegalStateException` — substituir em silêncio corromperia a matemática de dano dele.

---

## 7. Pipeline de Dano

O RuneCore não tem como saber o que o seu atributo *significa*, então o comportamento é
contribuído, não inferido: registre o atributo e depois registre uma etapa que o leia.

```java
DamagePipeline.register("meumod:critico", DamagePipeline.AFTER_MITIGATION, (ctx, dano) ->
        ThreadLocalRandom.current().nextFloat() < ctx.attacker().get(CHANCE_CRITICO)
                ? dano * 2f
                : dano);
```

### Prioridades

| Âncora | Quando roda |
| :--- | :--- |
| `BEFORE_MITIGATION` | antes de armadura/resistência — mudanças planas no valor de entrada |
| `MITIGATION` | onde acontece a matemática de armadura/resistência/escudo do RuneCore |
| `AFTER_MITIGATION` | efeitos multiplicativos, como crítico |
| `FINAL` | última palavra sobre o número |

Menor roda antes; qualquer `int` serve se você precisar ficar entre duas âncoras.

### O que vale saber

- **Em PvP, etapas de `BEFORE_MITIGATION` são ignoradas.** Esse caminho deriva o dano dos
  atributos e da arma do atacante em vez de escalar o número do engine, então não há o que uma
  etapa anterior modifique. Etapas em `AFTER_MITIGATION` ou depois sempre valem.
- **Etapa que lança exceção é logada e pulada**, nunca aborta o hit — uma etapa quebrada não
  pode deixar jogador invulnerável.
- As etapas rodam na thread que levantou o evento de dano. Mantenha-as baratas e sem bloqueio.
- Registrar o mesmo id duas vezes substitui a etapa, então recarregar seu conteúdo não empilha.

---

## 8. Registrando Itens e Criaturas

```java
// arma: contribui enquanto empunhada, no momento do hit
RuneAttributes.registerItem("MeuMod_Lamina",
        ItemCombatData.builder().physicalDamage(30f).armorPenetration(5f).build());

// armadura: contribui enquanto equipada
RuneAttributes.registerItem("MeuMod_Peitoral",
        ItemCombatData.builder().armor(20f).magicResist(8f).build());

// criatura: como ela causa dano, e como ela recebe
RuneAttributes.registerCreature("MeuBoss",
        CreatureCombatData.magic(20f).withDefense(35f, 40f, 0.1f));
```

A chave da criatura é o nome do arquivo do modelo, sem caminho e sem namespace — exatamente o
que o interceptor de dano parseia em runtime. Criatura não registrada é deixada totalmente em paz.

Os dois métodos devolvem `false` quando o registry ainda não subiu, então dá para logar ou tentar
de novo em vez de adivinhar.

---

## 🔮 Dicas de Especialista

1.  **IDs Únicos:** Sempre use `playerRef.getUuid().toString()` como UID para buffs para garantir que eles sejam removidos corretamente quando o jogador desconectar.
2.  **Reversão de Status:** Se você modificar um atributo (como velocidade), sempre especifique um callback `onExpire` no seu `ActiveBuff` para revertê-lo.
3.  **Verificar Contexto:** Sempre verifique se `ctx.source` ou `ctx.target` é nulo antes de aplicar efeitos.

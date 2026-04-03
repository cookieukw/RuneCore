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

## 🔮 Dicas de Especialista

1.  **IDs Únicos:** Sempre use `playerRef.getUuid().toString()` como UID para buffs para garantir que eles sejam removidos corretamente quando o jogador desconectar.
2.  **Reversão de Status:** Se você modificar um atributo (como velocidade), sempre especifique um callback `onExpire` no seu `ActiveBuff` para revertê-lo.
3.  **Verificar Contexto:** Sempre verifique se `ctx.source` ou `ctx.target` é nulo antes de aplicar efeitos.

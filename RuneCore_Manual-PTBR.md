# Manual da API RuneCore

Bem-vindo à documentação do **RuneCore**. Este manual detalha a arquitetura, classes e fluxos de uso do sistema de magia RuneCore.

## 1. Visão Geral do Sistema

O RuneCore foi projetado como um **motor de magia orientado a dados**. Em vez de codificar a lógica de magia diretamente em itens ou blocos, você define:

1.  **Recursos** (o que as magias custam)
2.  **Essências** (os tipos de magia disponíveis)
3.  **Efeitos** (o que a magia _faz_)
4.  **Magias** (como os efeitos são combinados e acionados)

O sistema principal (`RuneCore.java`) atua como um registro e um motor de execução que processa essas definições.

---

## 2. Referência da API (Classe por Classe)

### 2.1. Enum: `RuneElement`

**Arquivo:** `api/RuneElement.java`

Define os tipos elementais disponíveis no jogo. É dividido em categorias conceituais:

- **Básicos:** `FIRE`, `EARTH`, `WATER`, etc.
- **Avançados:** `LIGHT`, `SHADOW`, `LIFE`, etc.
- **Instáveis:** `CHAOS`, `AETHER`, `VOID`, `TIME`, etc.
- **Científicos:** `METAL`, `CRYSTAL`, `POISON`, `ACID`, etc.

**Uso:** utilizado principalmente para categorizar a `Essence` e definir requisitos de magia.

### 2.2. Classe: `Essence`

**Arquivo:** `api/Essence.java`

Representa uma substância mágica específica. Um elemento (como Fogo) pode ter múltiplas essências (ex: "Essência de Fogo Fraca", "Essência de Fogo Concentrada").

- `id`: Identificador único string (ex: `"essence_fire_weak"`).
- `element`: O `RuneElement` ao qual pertence.
- `tier`: Nível de poder (1, 2, 3...).
- `stability`: Modificador para taxas de falha (padrão 1.0).

### 2.3. Classe: `GameResource`

**Arquivo:** `api/GameResource.java`

Representa um atributo consumível necessário para conjurar magias.

- `id`: Identificador único (ex: `"mana"`, `"stamina"`).
- `max`: O limite máximo para este recurso.
- `regenRate`: Quão rápido ele regenera passivamente.
- `overflowPenalty`: O que acontece se você tiver demais (ex: `"burnout"`).

### 2.4. Classe: `RuneEffect`

**Arquivo:** `api/RuneEffect.java`

Esta é a **unidade de lógica** do sistema. Uma magia é composta por um ou mais efeitos.

- `id`: Identificador (ex: `"damage_fire"`).
- `action`: Uma lambda `IEffectAction` que contém o código a ser executado.
- `isInstant`: Se verdadeiro, acontece imediatamente. Se falso, pode ser um efeito de status (DoT).

**Método Crucial:** `execute(CastContext ctx)` aciona o efeito.

### 2.5. Classe: `Spell`

**Arquivo:** `api/Spell.java`

O **Projeto** para uma habilidade mágica. Ele conecta custos a efeitos.

- **Custos:** Pode exigir `GameResource` (Mana) e/ou itens de `Essence`.
- **Condições:** Um predicado (verificação lógica) que deve ser verdadeiro para conjurar (ex: "Deve ter um alvo").
- **Efeitos:** Uma lista de IDs de `RuneEffect` para acionar quando conjurado.

### 2.6. Classe: `CastContext`

**Arquivo:** `api/CastContext.java`

Um objeto transitório passado durante uma única conjuração. Ele mantém o "Estado" do evento.

- `source`: O conjurador (Jogador/Mob).
- `target`: A vítima/localização.
- `world`: O mundo do jogo.
- `power`: Um multiplicador para a força do efeito.
- `metadata`: Um mapa para compartilhar dados entre diferentes efeitos na mesma magia.

---

## 3. Como Criar Magia (Passo a Passo)

### Passo 1: Inicialize o Núcleo

Obtenha a instância do motor.

```java
RuneCore core = RuneCore.get();
```

### Passo 2: Registre as Definições

Defina do que sua magia é feita.

```java
// Define Mana
core.registerResource(new GameResource("mana", 100, 1, "none"));

// Define Essência de Fogo
core.registerEssence(new Essence("fire_T1", RuneElement.FIRE, 1));
```

### Passo 3: Criar Lógica (Efeitos)

Defina o que acontece. **É aqui que você escreve o código real do jogo.**

```java
RuneEffect explosion = new RuneEffect("explosion", true, 0, (ctx) -> {
    // 1. Obter localização de ctx.target
    // 2. Spawnar partícula
    // 3. Causar dano
    System.out.println("BOOM! Poder: " + ctx.power);
});
core.registerEffect(explosion);
```

### Passo 4: Criar a Magia

Monte as peças.

```java
Spell fireball = new Spell("fireball", (ctx) -> ctx.target != null);

fireball.addCost("mana", 10);
fireball.addEssenceRequirement(RuneElement.FIRE, 1);
fireball.addEffect("explosion");

core.registerSpell(fireball);
```

### Passo 5: Conjure!

Quando um jogador clica em uma varinha:

```java
CastContext ctx = new CastContext(player, target, world, 1.0);
core.castSpell("fireball", ctx);
```

---

## 4. O Sistema de Eventos (Hooks)

O RuneCore possui um barramento de eventos que permite que mods externos (ou itens) modifiquem magias **enquanto estão sendo conjuradas**.

**Eventos Principais:**

1.  `spell:pre_cast`: Dispara antes de qualquer coisa acontecer. Use para modificar `ctx.power` ou cancelar a magia.
2.  `resource:consume`: Dispara quando a mana é retirada.
3.  `effect:applied`: Dispara após cada efeito individual.
4.  `spell:post_cast`: Dispara quando a magia está totalmente finalizada.

**Exemplo: Um "Cajado de Fogo" que aumenta o dano**

```java
core.on("spell:pre_cast", (ctx) -> {
    // Verificar se o jogador está segurando o cajado de fogo
    // if (player.holds("fire_staff")) {
        ctx.power *= 1.5; // 50% mais dano
        System.out.println("O cajado impulsionou a magia!");
    // }
});
```

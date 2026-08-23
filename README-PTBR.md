# RuneCore

Motor e framework extensível de sistemas de magia para mods de Hytale.

[Read in English](README.md) | [Guia da API](API_USAGE-PTBR.md) | [Referência da API](docs/API_REFERENCE.md) | [Docs Técnicos](docs/ELEMENTS-PTBR.md) | [Manual](RuneCore_Manual-PTBR.md)

<p align="center">
  <img src="docs/assets/banner.png" alt="Banner do RuneCore" width="100%">
</p>

<p align="center">
  <img src="icons/logo/runecore-logo.png" alt="Logo do RuneCore" height="180">
  &nbsp;&nbsp;&nbsp;&nbsp;
  <img src="icons/logo/runecore-logo-construction.png" alt="Logo do RuneCore em camadas" height="180">
</p>

> **Status do Projeto: Em Desenvolvimento**
>
> - **Em Progresso:** Poções e receitas para novos efeitos de status.
> - **Funcional:** Comandos base, gerenciamento de status do jogador e drop de essências.
> - **API:** Fase de testes.
> - **Visuais:** Logo própria e ícones de essência. Modelos 3D reutilizam assets do jogo.
> - **Próximos Passos:** Sistema completo de **RuneAlchemy** e pipeline de alquimia.

---

## 1. Visão Geral

O RuneCore expande a base nativa do Hytale em um motor modular para interações elementais, efeitos de status, atributos customizados de RPG e receitas de alquimia. O mod oferece tanto mecânicas diretas quanto uma API extensível para integração de outros mods.

## 2. Galeria Visual

### Essências Elementais
| | | | | |
| :---: | :---: | :---: | :---: | :---: |
| <img src="icons/essences/Ingredient_Fire_Essence.png" height="48"> | <img src="icons/essences/Ingredient_Water_Essence.png" height="48"> | <img src="icons/essences/Ingredient_Earth_Essence.png" height="48"> | <img src="icons/essences/Ingredient_Wind_Essence.png" height="48"> | <img src="icons/essences/Ingredient_Ice_Essence.png" height="48"> |
| <img src="icons/essences/Ingredient_Lightning_Essence.png" height="48"> | <img src="icons/essences/Ingredient_Life_Essence.png" height="48"> | <img src="icons/essences/Ingredient_Death_Essence.png" height="48"> | <img src="icons/essences/Ingredient_Light_Essence.png" height="48"> | <img src="icons/essences/Ingredient_Shadow_Essence.png" height="48"> |
| <img src="icons/essences/Ingredient_Mind_Essence.png" height="48"> | <img src="icons/essences/Ingredient_Blood_Essence.png" height="48"> | <img src="icons/essences/Ingredient_Chaos_Essence.png" height="48"> | <img src="icons/essences/Ingredient_Aether_Essence.png" height="48"> | <img src="icons/essences/Ingredient_Void_Essence.png" height="48"> |
| <img src="icons/essences/Ingredient_Time_Essence.png" height="48"> | <img src="icons/essences/Ingredient_Metal_Essence.png" height="48"> | <img src="icons/essences/Ingredient_Crystal_Essence.png" height="48"> | <img src="icons/essences/Ingredient_Poison_Essence.png" height="48"> | <img src="icons/essences/Ingredient_Acid_Essence.png" height="48"> |

### Poções
| | | | | |
| :---: | :---: | :---: | :---: | :---: |
| <img src="icons/potions/Potion_Drinkable_Speed.png" height="48"> | <img src="icons/potions/Potion_Drinkable_Slowness.png" height="48"> | <img src="icons/potions/Potion_Drinkable_Haste.png" height="48"> | <img src="icons/potions/Potion_Drinkable_Mining_Fatigue.png" height="48"> | <img src="icons/potions/Potion_Drinkable_Jump_Boost.png" height="48"> |
| <img src="icons/potions/Potion_Drinkable_High_Jump.png" height="48"> | <img src="icons/potions/Potion_Drinkable_Slow_Falling.png" height="48"> | <img src="icons/potions/Potion_Drinkable_Levitation.png" height="48"> | <img src="icons/potions/Potion_Drinkable_Regeneration.png" height="48"> | <img src="icons/potions/Potion_Drinkable_Poison.png" height="48"> |
| <img src="icons/potions/Potion_Drinkable_Decay.png" height="48"> | <img src="icons/potions/Potion_Drinkable_Burn.png" height="48"> | <img src="icons/potions/Potion_Drinkable_Nausea.png" height="48"> | <img src="icons/potions/Potion_Drinkable_Bleeding.png" height="48"> | <img src="icons/potions/Potion_Drinkable_Frozen.png" height="48"> |
| <img src="icons/potions/Potion_Drinkable_Instant_Health.png" height="48"> | <img src="icons/potions/Potion_Drinkable_Instant_Damage.png" height="48"> | <img src="icons/potions/Potion_Drinkable_Invisibility.png" height="48"> | <img src="icons/potions/Potion_Drinkable_Glowing.png" height="48"> | <img src="icons/potions/Potion_Drinkable_Blindness.png" height="48"> |
| <img src="icons/potions/Potion_Drinkable_Night_Vision.png" height="48"> | <img src="icons/potions/Potion_Drinkable_Water_Breathing.png" height="48"> | <img src="icons/potions/Potion_Drinkable_Fire_Resistance.png" height="48"> | <img src="icons/potions/Potion_Drinkable_Resistance.png" height="48"> | <img src="icons/potions/Potion_Drinkable_Strength.png" height="48"> |
| <img src="icons/potions/Potion_Drinkable_Weakness.png" height="48"> | | | | |

### Efeitos de Status
| | | | | |
| :---: | :---: | :---: | :---: | :---: |
| <img src="icons/128x/speed.png" height="48"> | <img src="icons/128x/slowness.png" height="48"> | <img src="icons/128x/haste.png" height="48"> | <img src="icons/128x/mining_fatigue.png" height="48"> | <img src="icons/128x/jump_boost.png" height="48"> |
| <img src="icons/128x/high_jump.png" height="48"> | <img src="icons/128x/slow_falling.png" height="48"> | <img src="icons/128x/levitation.png" height="48"> | <img src="icons/128x/regeneration.png" height="48"> | <img src="icons/128x/poison.png" height="48"> |
| <img src="icons/128x/decay.png" height="48"> | <img src="icons/128x/burn.png" height="48"> | <img src="icons/128x/nausea.png" height="48"> | <img src="icons/128x/bleeding.png" height="48"> | <img src="icons/128x/frozen.png" height="48"> |
| <img src="icons/128x/invisibility.png" height="48"> | <img src="icons/128x/glowing.png" height="48"> | <img src="icons/128x/blindness.png" height="48"> | <img src="icons/128x/night_vision.png" height="48"> | <img src="icons/128x/water_breathing.png" height="48"> |
| <img src="icons/128x/fire_resistance.png" height="48"> | <img src="icons/128x/resistance.png" height="48"> | <img src="icons/128x/strength.png" height="48"> | <img src="icons/128x/weakness.png" height="48"> | <img src="icons/128x/darkness.png" height="48"> |
| <img src="icons/128x/electrified.png" height="48"> | | | | |

### Demonstração dos Efeitos no Jogo

#### Bleeding (Sangramento)
Causa dano contínuo ao longo do tempo e aplica um filtro de sangue na tela do jogador.
<br>
<img src="docs/assets/screenshots/bleeding.png" alt="Efeito Bleeding" width="100%">

---

#### Burn (Queimadura)
Incendeia o alvo, causando dano de fogo e efeito visual de chamas.
<br>
<img src="docs/assets/screenshots/burn.png" alt="Efeito Burn" width="100%">

---

#### Decay (Decomposição)
Corrói a vida da entidade ao longo do tempo com partículas necróticas.
<br>
<img src="docs/assets/screenshots/decay.png" alt="Efeito Decay" width="100%">

---

#### Fire Resistance (Resistência ao Fogo)
Concede imunidade a dano de fogo e protege a entidade em ambientes de lava.
<br>
<img src="docs/assets/screenshots/fire_resistance.png" alt="Efeito Fire Resistance" width="100%">

---

#### Frozen (Congelamento)
Envolve o alvo em gelo, travando o movimento físico e rotação da entidade.
<br>
<img src="docs/assets/screenshots/frozen.png" alt="Efeito Frozen" width="100%">

---

#### Glowing (Brilho)
Emite luz dinâmica ao redor de entidades e jogadores em ambientes escuros.
<br>
<img src="docs/assets/screenshots/glowing_entity.png" alt="Efeito Glowing" width="100%">

---

#### High Jump (Pulo Alto)
Aumenta a velocidade vertical, impulsionando o pulo de jogadores e entidades.
<br>
<img src="docs/assets/screenshots/high_jump.png" alt="Efeito High Jump" width="100%">

---

#### Vida Normal
Status padrão da barra de vida do jogador na HUD.
<br>
<img src="docs/assets/screenshots/full_health.png" alt="Vida Normal" width="100%">

---

#### Instant Damage (Dano Instantâneo)
Subtrai pontos de vida imediatamente ao impacto da poção ou feitiço.
<br>
<img src="docs/assets/screenshots/instant_damage.png" alt="Efeito Instant Damage" width="100%">

---

#### Instant Health (Vida Instantânea)
Restaura pontos de vida imediatamente, recuperando a vida do jogador.
<br>
<img src="docs/assets/screenshots/instant_health.png" alt="Efeito Instant Health" width="100%">

---

## 3. Arquitetura

O RuneCore é dividido em três módulos principais:

* **RuneCore (Núcleo):** Gerencia essências, mana, atributos de combate e progressão do jogador. Expõe a API base.
* **RuneMagic:** Feitiços, runas (efeitos passivos), artefatos e grimórios.
* **RuneAlchemy:** Sistema de criação de poções, reagentes e encantamento de itens.

---

## 4. Tabelas de Loot & Drop

Criaturas possuem chance de dropar essências ao serem derrotadas por jogadores (taxa base: **25%**).

| Ícone | Essência | Mobs Origem |
| :---: | :--- | :--- |
| <img src="icons/essences/Ingredient_Fire_Essence.png" height="32"> | **Fogo** | Emberwulf, Dragão de Fogo, Criaturas de Magma/Chama |
| <img src="icons/essences/Ingredient_Earth_Essence.png" height="32"> | **Terra** | Trork, Golem de Terra, Bisão, Tartaruga, Toupeira |
| <img src="icons/essences/Ingredient_Wind_Essence.png" height="32"> | **Vento** | Pássaros (Falcão, Coruja, Corvo, etc.), Feran Windwalker |
| <img src="icons/essences/Ingredient_Water_Essence.png" height="32"> | **Água** | Peixes (Tubarão, Piranha, etc.), Caranguejo, Sapo, Baleia |
| <img src="icons/essences/Ingredient_Ice_Essence.png" height="32"> | **Gelo** | Urso Polar, Dragão de Gelo, Yeti, Esqueleto de Gelo |
| <img src="icons/essences/Ingredient_Lightning_Essence.png" height="32"> | **Trovão** | Golem de Trovão, Espírito do Trovão, Faísca Viva |
| <img src="icons/essences/Ingredient_Light_Essence.png" height="32"> | **Luz** | Espírito de Raiz, Kweebec de Natal |
| <img src="icons/essences/Ingredient_Shadow_Essence.png" height="32"> | **Sombras** | Cavaleiro das Sombras, Wraith, Skrill |
| <img src="icons/essences/Ingredient_Life_Essence.png" height="32"> | **Vida** | Animais (Vaca, Porco, Ovelha, Cervo), Kweebec |
| <img src="icons/essences/Ingredient_Death_Essence.png" height="32"> | **Morte** | Esqueleto, Zumbi, Ghoul |
| <img src="icons/essences/Ingredient_Mind_Essence.png" height="32"> | **Mente** | Slothian, Feiticeiro Outlander |
| <img src="icons/essences/Ingredient_Blood_Essence.png" height="32"> | **Sangue** | Morcego, Mosquito |
| <img src="icons/essences/Ingredient_Chaos_Essence.png" height="32"> | **Caos** | Berserker Outlander, Chefe Trork |
| <img src="icons/essences/Ingredient_Aether_Essence.png" height="32"> | **Éter** | Espírito de Brasa |
| <img src="icons/essences/Ingredient_Void_Essence.png" height="32"> | **Vazio** | Criaturas corrompidas pelo Vazio |
| <img src="icons/essences/Ingredient_Metal_Essence.png" height="32"> | **Metal** | Golem de Aço, Tanque, Torreta |
| <img src="icons/essences/Ingredient_Crystal_Essence.png" height="32"> | **Cristal** | Golem de Cristal, Scarak |
| <img src="icons/essences/Ingredient_Poison_Essence.png" height="32"> | **Veneno** | Cobra, Aranha, Escorpião |

---

## 5. Recursos Principais

- **20 Elementos:** Divididos em níveis Básico, Avançado, Instável e Químico.
- **API Modular:** Registro direto de essências, feitiços e efeitos de status.
- **Efeitos de Status Persistentes:** Sistema de ticks de buff/debuff com lógica integrada ao mundo.
- **Gerenciamento de Recursos:** Rastreamento de mana, stamina e atributos customizados.

Para detalhes mecânicos de cada elemento, veja [**ELEMENTS-PTBR.md**](docs/ELEMENTS-PTBR.md).

---

## 6. Receitas de Poções

Poções são criadas na **Mesa de Alquimia** usando **Garrafa de Vidro** (`Potion_Empty`) + **Essência Elemental** + **Material Secundário** (tempo base: 4 segundos).

| Poção | Essência | Qtd | Material Secundário | Qtd |
| :--- | :--- | :---: | :--- | :---: |
| **Velocidade** | Essência de Vento | 1 | Penas (Claras) | 2 |
| **Lentidão** | Essência de Terra | 1 | Musgo de Espinhos | 2 |
| **Pressa** | Essência de Trovão | 1 | Cristal (Amarelo) | 1 |
| **Fadiga de Mineração** | Essência de Terra | 1 | Fragmento de Osso | 2 |
| **Impulso de Pulo** | Essência de Vento | 1 | Penas (Azuis) | 2 |
| **Super Pulo** | Essência de Éter | 1 | Penas (Vermelhas) | 2 |
| **Queda Lenta** | Essência de Vento | 1 | Penas (Escuras) | 3 |
| **Levitação** | Essência de Éter | 2 | Cristal (Branco) | 2 |
| **Regeneração** | Essência de Vida | 2 | Flor Rosa | 3 |
| **Veneno** | Essência de Veneno | 1 | Cogumelo Venenoso | 2 |
| **Deterioração** | Essência de Morte | 1 | Fragmento de Osso | 3 |
| **Queimadura** | Essência de Fogo | 1 | Carvão | 2 |
| **Náusea** | Essência de Caos | 1 | Cogumelo Verde | 2 |
| **Sangramento** | Essência de Sangue | 1 | Cristal (Vermelho) | 1 |
| **Congelamento** | Essência de Gelo | 1 | Cristal (Ciano) | 2 |
| **Cura Instantânea** | Essência de Vida (Concentrada) | 1 | Flor Vermelha | 3 |
| **Dano Instantâneo** | Essência do Vazio | 1 | Pó Explosivo | 2 |
| **Invisibilidade** | Essência de Sombras | 2 | Cristal (Branco) | 2 |
| **Brilho** | Essência de Luz | 1 | Cogumelo Brilhante (Laranja) | 2 |
| **Cegueira** | Essência de Sombras | 1 | Cristal (Roxo) | 2 |
| **Visão Noturna** | Essência de Luz | 1 | Cogumelo Brilhante (Azul) | 2 |
| **Respiração Aquática** | Essência de Água | 1 | Coral Azul | 3 |
| **Resistência ao Fogo** | Essência de Fogo | 2 | Cristal (Vermelho) | 2 |
| **Resistência** | Essência de Metal | 2 | Cristal (Azul) | 2 |
| **Força** | Essência de Sangue | 2 | Cristal (Vermelho) | 2 |
| **Fraqueza** | Essência de Morte | 1 | Flor Cinza | 2 |

*Nota: Efeitos avançados exigem 2 essências.*

---

## 7. Atributos de Combate

O RuneCore implementa um pipeline de dano no estilo RPG sobre o cálculo nativo de combate.

Atributos podem ser registrados. Jogadores mantêm blocos persistentes de atributos, enquanto criaturas utilizam mapeamento estático por registry.

### Tipos de Atributos

| Categoria | Atributo | Descrição |
|-----------|----------|-----------|
| **Ofensivo** | Dano Físico | Reduzido pela Armadura do alvo |
| **Ofensivo** | Dano Mágico | Reduzido pela Resistência Mágica do alvo |
| **Ofensivo** | Dano Verdadeiro | Ignora reduções (bloqueado por escudos) |
| **Ofensivo** | Penetração de Armadura | Ignora parte da Armadura do alvo |
| **Ofensivo** | Penetração Mágica | Ignora parte da Resistência Mágica do alvo |
| **Defensivo** | Armadura | Reduz Dano Físico recebido |
| **Defensivo** | Resistência Mágica | Reduz Dano Mágico recebido |
| **Defensivo** | Redução de Dano | Redução % fixa sobre o dano recebido (cap em 90%) |
| **Defensivo** | HP de Escudo | Vida temporária consumida antes da vida base |

### Fórmula de Dano

```text
defesaEfetiva = max(0, defesa - penetração)
danoReduzido = danoBase × 100 / (100 + defesaEfetiva)
danoFinal = (fisicoReduzido + magicoReduzido) × (1 - reducaoDano%) + danoVerdadeiro
```

Hits trafegam pelo `DamagePipeline`, permitindo que outros mods insiram etapas no cálculo (ex: dano crítico, roubo de vida).

### Dano de Criaturas (PvE)

Criaturas utilizam perfis do `CreatureCombatRegistry`. Criaturas não registradas utilizam fallback por `DamageCause`.

| Perfil de Dano | Fórmula | Exemplos |
|----------------|---------|----------|
| **Physical** | Reduzido por Armadura; pode usar Armor Pen | Trork, Skeleton Fighter, Wolf, Bear |
| **Magic** | Reduzido por Res. Mágica; pode usar Magic Pen | Skeleton Mage, Wraith, Necromancer |
| **Hybrid** | Proporção físico/mágico calculada separadamente | Dragão de Fogo, Golem Crystal Flame |
| **True** | Ignora armadura e resistências (absorvido por escudo) | — |

#### Defesa das Criaturas por Família

| Família | Armadura | Res. Mágica | RD | Notas |
| :--- | ---: | ---: | ---: | :--- |
| Wildlife | 2 | 0 | — | Feras e animais base sem proteção |
| Zombies | 4 | 2 | — | Baixa resistência física/mágica |
| Spirits | 4 | 32 | — | Incorpóreos (alta resistência mágica) |
| Goblins | 5 | 3 | — | Armadura leve de couro |
| Trorks | 6 | 0 | — | Foco em combate físico |
| Ferans | 8 | 8 | — | Atributos equilibrados |
| Skeletons | 10 | 4 | — | Proteção física moderada |
| Outlanders | 12 | 6 | — | Saqueadores equipados |
| Void | 12 | 28 | — | Alta resistência mágica |
| Scaraks | 18 | 4 | — | Carapaça dura (alta armadura, baixa res. mágica) |
| Dragons | 32 | 28 | — | Altas resistências base |
| Golems | 34 | 12 | — | Alta armadura base |
| Bosses | 40 | 34 | 15% | Defesas altas + redução % plana |

### Integração com Equipamentos

Armaduras aplicam atributos enquanto equipadas (escalonado por slot: Peitoral 100%, Calças 85%, Capacete 70%, Luvas 50%). Armas aplicam atributos ofensivos no momento do hit.

#### Escalonamento de Armaduras por Tier

| Tier | Materiais | Armadura (Peitoral) | Res. Mágica (Peitoral) |
|------|-----------|:-------------------:|:----------------------:|
| 1 | Tecido, Madeira | 4–8 | 10–20 |
| 2 | Couro Leve, Cobre | 10–14 | 8–10 |
| 3 | Couro Pesado, Bronze, Ferro | 18–25 | 5–8 |
| 4 | Aço, Cobalto | 30–35 | 5–7 |
| 5 | Mithril, Thorium | 42–45 | 12–14 |
| 6 | Adamantita, Onyxium, Prisma | 50–58 | 18–22 |

#### Dano de Armas por Tier (Exemplo Físico)

| Material | Espada | Espada Longa | Machado | Machado de Guerra | Adaga |
|----------|:------:|:------------:|:-------:|:-----------------:|:-----:|
| Madeira | 6–8 | 12 | 10 | 14 | 6 |
| Cobre | 14 | 20 | 16 | 22 | 10 |
| Ferro | 22 | 30 | 24 | 32 | 18 |
| Aço | 28 | — | — | 34 | — |
| Mithril | 38 | 50 | 40 | 52 | 30 |
| Adamantita | 50 | 65 | 52 | 68 | 40 |

### Comandos de Administração

```text
/combatstats view
/combatstats set <atributo> <valor>
/combatstats add <atributo> <valor>
/combatstats reset [all|modifiers|<atributo>]
```

Identificadores suportados: `armor`, `magicresist` (`mr`), `reduction` (`dr`), `physdmg` (`phys`), `magdmg` (`mag`), `truedmg` (`true`), `armorpen` (`apen`), `magicpen` (`mpen`), `shield`.

---

## 8. Efeitos de Status & Testes

Comando de teste in-game:

```text
/rune effect <id>
```

| Ícone | Status | ID do Efeito | Visual Nativo | Descrição |
| :---: | :---: | :--- | :--- | :--- |
| <img src="icons/128x/speed.png" height="32"> | [x] | `speed` | Speed | Aumenta velocidade de movimento. |
| <img src="icons/128x/slowness.png" height="32"> | [x] | `slowness` | Slowness | Reduz velocidade de movimento. |
| <img src="icons/128x/haste.png" height="32"> | [ ] | `haste` | Haste | Modificador de velocidade de ataque/mineração (+50%). |
| <img src="icons/128x/mining_fatigue.png" height="32"> | [ ] | `mining_fatigue`| Mining_Fatigue | Redução de velocidade de ataque/mineração (-70%). |
| <img src="icons/128x/jump_boost.png" height="32"> | [x] | `jump_boost` | Jump_Boost | Aumenta a altura do pulo. |
| <img src="icons/128x/high_jump.png" height="32"> | [x] | `high_jump` | High_Jump | Aumenta significativamente a altura do pulo. |
| <img src="icons/128x/slow_falling.png" height="32"> | [x] | `slow_falling` | Slow_Falling | Reduz a velocidade de queda. |
| <img src="icons/128x/levitation.png" height="32"> | [x] | `levitation` | Levitation | Faz a entidade flutuar para cima. |
| <img src="icons/128x/regeneration.png" height="32"> | [x] | `regeneration` | Regeneration | Recupera 1 HP a cada 50 ticks. |
| <img src="icons/128x/poison.png" height="32"> | [x] | `poison` | Poison | Causa 1 HP de dano a cada 25 ticks. |
| <img src="icons/128x/decay.png" height="32"> | [x] | `decay` | Decay | Causa 1 HP de dano a cada 40 ticks. |
| <img src="icons/128x/darkness.png" height="32"> | [x] | `darkness` | Darkness | Reduz o alcance/brilho da visão. |
| <img src="icons/128x/electrified.png" height="32"> | [x] | `electrified` | Electrified | Dano elétrico periódico e partículas visuais. |
| <img src="icons/128x/burn.png" height="32"> | [x] | `burn` | Burn | Causa 1 HP de dano a cada 20 ticks. |
| <img src="icons/128x/nausea.png" height="32"> | [x] | `nausea` | Nausea | Aplica efeito de rotação na câmera. |
| <img src="icons/128x/bleeding.png" height="32"> | [x] | `bleeding` | Bleeding | Causa 1 HP de dano a cada 20 ticks + partículas de sangue. |
| <img src="icons/128x/frozen.png" height="32"> | [x] | `frozen` | Frozen | Impede movimento durante a duração. |
| | [x] | `instant_health`| (nenhum) | Cura instantânea (`4.0 * power`). |
| | [x] | `instant_damage`| InstantDamage | Dano instantâneo (`6.0 * power`). |
| <img src="icons/128x/invisibility.png" height="32"> | [x] | `invisibility` | Invisibility | Oculta o modelo visual da entidade. |
| <img src="icons/128x/blindness.png" height="32"> | [x] | `blindness` | Blindness | Restringe a visão na câmera. |
| <img src="icons/128x/night_vision.png" height="32"> | [x] | `night_vision` | NightVision | Aplica luz visual de visão noturna. |
| <img src="icons/128x/fire_resistance.png" height="32"> | [x] | `fire_resistance`| FireResistance | Previne dano por fogo. |

### Exemplo de Uso na API

```java
RuneCore core = RuneCore.getInstance();
RuneEffect poison = core.getEffect("poison");

if (poison != null) {
    CastContext ctx = new CastContext(sourceEntity, targetEntity);
    poison.execute(ctx);
}
```

---

## 9. Configuração de Build & Ferramentas

Configure os caminhos locais antes de compilar:

### `local.properties`
```properties
hytale.assets.path=/caminho/para/Hytale/Assets.zip
hytale.mods.dest=/caminho/para/Hytale/Mods/
```

### `gradle.properties`
```properties
org.gradle.java.home=/caminho/para/Hytale/jdk-25
```

### Comandos de Build
- **Compilar Jar:** `./gradlew jar`
- **Versão da Engine:** `./gradlew hytaleVersion`
- **Extrair Schemas:** `./gradlew generateSchemas`
- **Validar Assets:** `./gradlew validateAssets`
- **Validar Prefabs:** `./gradlew validatePrefabs`

---

## 10. Guia do Desenvolvedor & Pontos de Entrada

Principais classes para integração com a API:

| Ponto de Entrada | Uso |
| :--- | :--- |
| `RuneAttributes` | Leitura/escrita de atributos, registro de itens e criaturas |
| `AttributeRegistry` + `RuneAttribute` | Declaração de atributos customizados |
| `DamagePipeline` + `DamageStage` | Etapas customizadas no cálculo de dano |
| `RuneCore` | Acesso a elementos, feitiços e efeitos de status |
| `RuneCoreItemManager` | Registro de itens clicáveis/interativos |

Consulte o [**API_USAGE-PTBR.md**](API_USAGE-PTBR.md) para exemplos de integração.

---

## 11. Licença

Licenciado sob a [Creative Commons Attribution-NonCommercial 4.0 International (CC BY-NC 4.0)](https://creativecommons.org/licenses/by-nc/4.0/deed.pt_BR).
Ícones localizados no diretório `/icons` seguem os mesmos termos de licença.
nes em pixel art** (localizados no diretório `/icons`), está licenciado sob a **Creative Commons Attribution-NonCommercial 4.0 International (CC BY-NC 4.0)**.

- **Atribuição (BY):** Você deve dar o crédito apropriado ao autor original.
- **Não Comercial (NC):** Você não pode usar o material para fins comerciais.
- **Obras derivadas:** Quem modificar pode escolher outra licença para o derivado, desde que respeite as condições acima.

Para mais detalhes, veja o arquivo [LICENSE](LICENSE) ou visite o site da [Creative Commons](https://creativecommons.org/licenses/by-nc/4.0/deed.pt_BR).

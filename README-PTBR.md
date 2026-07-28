# 🔮 RuneCore: Motor de Magia para Hytale

[Read in English](README.md) | [Guia da API](API_USAGE-PTBR.md) | [Referência da API](docs/API_REFERENCE.md) | [Docs Técnicos](docs/ELEMENTS-PTBR.md) | [Manual](RuneCore_Manual-PTBR.md)

<p align="center">
  <img src="icons/logo/runecore-logo.png" alt="Logo do RuneCore" height="180">
  &nbsp;&nbsp;&nbsp;&nbsp;
  <img src="icons/logo/runecore-logo-construction.png" alt="Logo do RuneCore — Camadas e traços" height="180">
</p>

> [!IMPORTANT]
> **Status do Projeto: Em Desenvolvimento**
>
> - 🛠️ **Em Progresso:** Desenvolvimento de novas poções e sistemas de craft para utilizar os novos efeitos de status.
> - ✅ **Funcional:** Comandos principais, gerenciamento de status e sistema de drop de essências.
> - 🧪 **API:** A API ainda está em fase de testes.
> - 🎨 **Visuais:** Logo própria e ícones de essência em alta qualidade. Modelos 3D das essências já implementados (reciclados do jogo).
> - 🚀 **Próximos Passos:** Implementar o sistema completo de Alquimia (**RuneAlchemy**) e criação complexa de poções.

---

## 1. Visão e Origem 🤔

RuneCore nasceu da vontade de trazer um sistema de magia profundo e significativo para o Hytale. Embora o sistema nativo forneça uma base básica, o RuneCore o expande em um motor completo que modders podem usar para criar interações elementais complexas, efeitos de status persistentes e uma progressão mágica rica.

Nosso objetivo não é apenas fornecer um mod, mas uma **API extensível** que sirva como a espinha dorsal para a comunidade de magia de Hytale.

## 2. O que é o RuneCore? 📘

O RuneCore é um sistema de magia modular. Ele é dividido em módulos interdependentes:

*   **🔹 RuneCore (Núcleo):** Gerencia essências, mana e progresso do jogador. Fornece a API para outros modders.
*   **⚔️ RuneMagic:** Focado em feitiços, runas (efeitos passivos), artefatos e grimórios.
*   **⚗️ RuneAlchemy:** Um sistema químico e alquímico para criar poções e encantar itens usando essências.

## 3. Essências Elementais 🔮

O RuneCore apresenta 20 elementos distintos, cada um com sua própria essência utilizada para crafting e conjuração de feitiços. Abaixo estão os ícones das essências em alta qualidade atualmente implementados:

### Nível Básico
| Ícone | Elemento | Nível | Ícone | Elemento | Nível |
| :---: | :--- | :--- | :---: | :--- | :--- |
| <img src="icons/essences/Ingredient_Fire_Essence.png" height="48"> | **Fogo** | Básico | <img src="icons/essences/Ingredient_Water_Essence.png" height="48"> | **Água** | Básico |
| <img src="icons/essences/Ingredient_Earth_Essence.png" height="48"> | **Terra** | Básico | <img src="icons/essences/Ingredient_Wind_Essence.png" height="48"> | **Vento** | Básico |
| <img src="icons/essences/Ingredient_Ice_Essence.png" height="48"> | **Gelo** | Básico | <img src="icons/essences/Ingredient_Lightning_Essence.png" height="48"> | **Trovão** | Básico |

### Nível Avançado
| Ícone | Elemento | Nível | Ícone | Elemento | Nível |
| :---: | :--- | :--- | :---: | :--- | :--- |
| <img src="icons/essences/Ingredient_Life_Essence.png" height="48"> | **Vida** | Avançado | <img src="icons/essences/Ingredient_Death_Essence.png" height="48"> | **Morte** | Avançado |
| <img src="icons/essences/Ingredient_Light_Essence.png" height="48"> | **Luz** | Avançado | <img src="icons/essences/Ingredient_Shadow_Essence.png" height="48"> | **Sombras** | Avançado |
| <img src="icons/essences/Ingredient_Mind_Essence.png" height="48"> | **Mente** | Avançado | <img src="icons/essences/Ingredient_Blood_Essence.png" height="48"> | **Sangue** | Avançado |

### Níveis Instável e Químico
| Ícone | Elemento | Nível | Ícone | Elemento | Nível |
| :---: | :--- | :--- | :---: | :--- | :--- |
| <img src="icons/essences/Ingredient_Chaos_Essence.png" height="48"> | **Caos** | Instável | <img src="icons/essences/Ingredient_Aether_Essence.png" height="48"> | **Éter** | Instável |
| <img src="icons/essences/Ingredient_Void_Essence.png" height="48"> | **Vazio** | Instável | <img src="icons/essences/Ingredient_Time_Essence.png" height="48"> | **Tempo** | Instável |
| <img src="icons/essences/Ingredient_Metal_Essence.png" height="48"> | **Metal** | Químico | <img src="icons/essences/Ingredient_Crystal_Essence.png" height="48"> | **Cristal** | Químico |
| <img src="icons/essences/Ingredient_Poison_Essence.png" height="48"> | **Veneno** | Químico | <img src="icons/essences/Ingredient_Acid_Essence.png" height="48"> | **Ácido** | Químico |

---

## 4. Drops de Mobs & Tabelas de Saque 🦅

Cada criatura em Hytale tem uma chance de dropar essências elementais quando derrotada por um jogador. A taxa de drop base atual é de **25%**.

| Essência | Dropado por (Mobs Comuns) |
| :--- | :--- |
| **Fogo** | Emberwulf, Dragão de Fogo, Criaturas de Magma/Chama |
| **Terra** | Trork, Golem de Terra, Bisão, Tartaruga, Toupeira |
| **Vento** | Pássaros (Falcão, Coruja, Corvo, etc.), Feran Windwalker |
| **Água** | Peixes (Tubarão, Piranha, etc.), Caranguejo, Sapo, Baleia |
| **Gelo** | Urso Polar, Dragão de Gelo, Yeti, Esqueleto de Gelo |
| **Trovão** | Golem de Trovão, Espírito do Trovão, Faísca Viva |
| **Luz** | Espírito de Raiz, Kweebec de Natal |
| **Sombras** | Cavaleiro das Sombras, Wraith, Skrill |
| **Vida** | Animais (Vaca, Porco, Ovelha, Cervo), Kweebec |
| **Morte** | Esqueleto, Zumbi, Ghoul |
| **Mente** | Slothian, Feiticeiro Outlander |
| **Sangue** | Morcego, Mosquito |
| **Caos** | Berserker Outlander, Chefe Trork |
| **Éter** | Espírito de Brasa |
| **Vazio** | Criaturas corrompidas pelo Vazio |
| **Metal** | Golem de Aço, Tanque, Torreta |
| **Cristal** | Golem de Cristal, Scarak |
| **Veneno** | Cobra, Aranha, Escorpião |

---

## 5. Recursos Principais ✨

*   **20 Elementos:** Divididos em níveis Básico, Avançado, Instável e Químico.
*   **API Modular:** Registre facilmente essências, feitiços e efeitos de status personalizados.
*   **Efeitos de Status Persistentes:** Um sistema robusto para buffs/debuffs (ex: Veneno, Regeneração, Congelamento) com lógica ciente do mundo.
*   **Gerenciamento de Recursos:** Rastreio personalizado de mana, stamina e recursos biológicos.

Para um detalhamento completo de todos os 20 elementos e suas mecânicas, veja nossos [**Documentos Técnicos**](docs/ELEMENTS-PTBR.md).

---

## 6. ⚗️ Receitas de Poções

Todas as poções são craftadas na **Mesa de Alquimia** usando uma **Garrafa de Vidro** (Potion_Empty) + uma **Essência Elemental** + um **Material Secundário**. Tempo de craft: **4 segundos**.

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

> **Nota:** Efeitos mais poderosos (Levitação, Invisibilidade, Resistência, Força, Resistência ao Fogo, Regeneração) requerem **2 essências** em vez de 1.

---

## 7. ⚔️ Sistema de Atributos de Combate

O RuneCore inclui um sistema de atributos de combate estilo RPG que funciona em cima do sistema nativo de armadura/dano do Hytale.

Atributos são **registráveis**: os listados abaixo são os que o RuneCore já traz, mas qualquer mod pode adicionar o seu e fazê-lo influenciar o dano. Veja o [Guia da API](API_USAGE-PTBR.md#6-atributos-de-combate).

Jogadores carregam um bloco de atributos persistente. Criaturas não — os valores delas vêm de uma consulta estática ao registry, sem estado por entidade.

### Tipos de Atributos

| Categoria | Atributo | Descrição |
|-----------|----------|-----------|
| **Ofensivo** | Dano Físico | Reduzido pela Armadura do alvo |
| **Ofensivo** | Dano Mágico | Reduzido pela Resistência Mágica do alvo |
| **Ofensivo** | Dano Verdadeiro | Ignora todas as resistências e reduções (apenas bloqueado por escudos) |
| **Ofensivo** | Penetração de Armadura | Ignora parte da Armadura do alvo |
| **Ofensivo** | Penetração Mágica | Ignora parte da Resistência Mágica do alvo |
| **Defensivo** | Armadura | Reduz Dano Físico recebido |
| **Defensivo** | Resistência Mágica | Reduz Dano Mágico recebido |
| **Defensivo** | Redução de Dano | Redução % fixa em todo dano (máx 90%) |
| **Defensivo** | HP de Escudo | HP temporário que absorve dano antes da vida |

### Fórmula de Dano

```
defesaEfetiva = max(0, defesa - penetração)
danoReduzido = danoBase × 100 / (100 + defesaEfetiva)
danoFinal = (fisicoReduzido + magicoReduzido) × (1 - reducaoDano%) + danoVerdadeiro
→ Escudo absorve primeiro, restante atinge HP
```

Todo hit interceptado passa pelo **pipeline de dano**, então mods podem inserir uma etapa própria
antes ou depois desse cálculo — é assim que um atributo customizado como crítico ou roubo de vida
consegue influenciar o resultado. Veja [Pipeline de Dano](API_USAGE-PTBR.md#7-pipeline-de-dano).

**Fontes de ofensiva.** Os atributos ofensivos de um jogador vêm de dois lugares: o bloco
persistente (equipamento) e a **arma empunhada**, resolvida no momento do hit — trocar de slot na
hotbar vale na hora.

### Dano de Criaturas (PvE)

Criaturas possuem **perfis de dano** pré-registrados no `CreatureCombatRegistry`. Quando uma criatura ataca um jogador, o RuneCore consulta o perfil da criatura e aplica a fórmula de defesa correta com a penetração de armadura/magia da criatura. Se a criatura não estiver registrada, o sistema usa a classificação por `DamageCause` como fallback.

Criaturas também são **alvos de dano**: elas têm armadura, resistência mágica e redução de dano,
então atributos de arma e penetração passam a valer em PvE também. Criatura ausente do registry é
deixada intocada — o dano dela continua exatamente como o engine calculou.

| Perfil de Dano | Fórmula | Criaturas Exemplo |
|----------------|---------|-------------------|
| **Physical** | Armadura reduz, criatura pode ter armor pen | Trork, Skeleton Fighter, Wolf, Bear |
| **Magic** | Resistência Mágica reduz, criatura pode ter magic pen | Skeleton Mage, Wraith, Necromancer, Spirits |
| **Hybrid** | Divide phys/magic por ratio, cada um reduzido separadamente | Dragon Fire (60% magic), Golem Crystal Flame, Feran Windwalker |
| **True** | Sem redução (apenas escudo absorve) | — |

Após a redução por tipo, a **Redução de Dano %** é aplicada, e depois o **HP de Escudo** absorve o restante. Se não houver dados da criatura, `DamageCause` é usado como fallback:

| Causa do Dano | Reduzido por |
|---------------|-------------|
| Physical, Projectile, Bludgeoning, Slashing | **Armadura** |
| Elemental, Fire, Ice, Poison, Magic | **Resistência Mágica** |
| True (ou BypassResistances) | **Apenas Escudo** |

#### Criaturas Registradas (~200+)

| Facção | Criaturas | Perfil Típico |
|--------|-----------|---------------|
| Trork | Warrior, Brawler, Guard, Hunter, Mauler, Chieftain, Shaman, Doctor Witch | Physical (guerreiros), Magic (xamã/bruxa) |
| Skeleton | Standard, Burnt, Frost, Sand, Incandescent, Pirate (~35 variantes) | Physical (melee), Hybrid (elementais), Magic (magos) |
| Zombie | Regular, Aberrant, Burnt, Frost, Sand, Werewolf | Physical, Hybrid (variantes elementais) |
| Goblin | Scrapper, Thief, Miner, Lobber, Boss, Duke, Ogre | Physical, Hybrid (lobber/duke) |
| Outlander | Peon, Marauder, Berserker, Brute, Hunter, Cultist, Priest, Sorcerer | Physical (guerreiros), Magic (conjuradores) |
| Scarak | Louse, Seeker, Fighter, Defender, Broodmother | Physical, Hybrid (broodmother) |
| Feran | Burrower, Longtooth, Sharptooth, Windwalker | Physical, Hybrid (windwalker) |
| Dragões | Fire, Frost, Void | Hybrid (60-70% magic, alta pen) |
| Golems | Crystal Earth/Flame/Frost/Sand/Thunder, Firesteel, Guardian Void | Physical (earth/sand), Hybrid (elementais) |
| Void | Crawler, Eye, Larva, Necromancer, Spawn, Spectre, Wraith | Magic (alta pen) |
| Bosses | Shadow Knight, Yeti, Werewolf, Emberwulf | Hybrid/Physical (alta pen) |
| Vida Selvagem | Bears, Wolves, Spiders, Snakes, Sharks, criaturas de caverna | Physical (maioria), Hybrid (cobras, variantes magma) |
| Spirits | Ember, Frost, Root, Thunder, Spark | Magic |

#### Defesa das Criaturas por Família

A defesa é atribuída por família. Estes valores são uma primeira passada e pedem balanceamento
in-game; qualquer criatura pode sobrescrever a família com `withDefense(...)`.

| Família | Armadura | Resist. Mágica | RD | Racional |
| :--- | ---: | ---: | ---: | :--- |
| Wildlife | 2 | 0 | — | bichos e gado, praticamente sem proteção |
| Zombies | 4 | 2 | — | podres e lentos |
| Spirits | 4 | 32 | — | incorpóreos: lâmina atravessa, magia machuca |
| Goblins | 5 | 3 | — | couro improvisado |
| Trorks | 6 | 0 | — | guerreiros tribais, sem proteção mágica |
| Ferans | 8 | 8 | — | feras ágeis, equilibradas |
| Misc | 8 | 8 | — | não classificados |
| Skeletons | 10 | 4 | — | osso apara lâmina melhor que magia |
| Outlanders | 12 | 6 | — | saqueadores equipados |
| Void | 12 | 28 | — | invertido: a magia é o escudo deles |
| Scaraks | 18 | 4 | — | quitina: dura contra aço, fraca contra magia |
| Dragons | 32 | 28 | — | escamados e antigos |
| Golems | 34 | 12 | — | a parede física |
| Bosses | 40 | 34 | 15% | ainda ganham redução plana |

> **Nota:** criaturas continuam não sendo *rastreadas* — o registry é uma consulta estática
> pela chave do modelo, sem estado por entidade.

### Integração com Equipamentos

Itens registrados no `CombatStatsRegistry` aplicam seus atributos de combate de duas formas: **peças de armadura** contribuem enquanto equipadas (recalculado a cada mudança de armadura), e a **arma empunhada** contribui no momento do hit. Todas as armaduras e armas vanilla do Hytale já estão pré-registradas, e mods podem registrar as suas via `RuneAttributes.registerItem(...)`.

#### Stats de Armadura por Tier

Peças escalam por slot: Peitoral 100%, Calças 85%, Capacete 70%, Luvas 50%.

| Tier | Materiais | Armadura (Peitoral) | Res. Mágica (Peitoral) |
|------|-----------|:-------------------:|:----------------------:|
| 1 | Cloth (Cotton, Linen, Wool, Silk, Cindercloth), Wood | 4–8 | 10–20 |
| 2 | Leather Soft/Light, Copper | 10–14 | 8–10 |
| 3 | Leather Medium/Heavy/Raven, Bronze, Iron | 18–25 | 5–8 |
| 4 | Steel, Steel Ancient, Cobalt | 30–35 | 5–7 |
| 5 | Mithril, Thorium | 42–45 | 12–14 |
| 6 | Adamantite, Onyxium, Prisma | 50–58 | 18–22 |

> **Design:** Armaduras de pano/mágicas têm baixa Armadura mas alta Resistência Mágica. Armaduras de metal têm alta Armadura mas baixa Resistência Mágica. Tiers altos (Mithril+) equilibram ambos.

#### Stats de Armas por Tipo

| Tipo | Stat Primário | Stat Secundário | Identidade |
|------|--------------|-----------------|------------|
| Espadas | Dano Físico | — | Melee balanceado |
| Espadas Longas | Dano Físico (alto) | — | Lento, golpes pesados |
| Machados | Dano Físico | Pen. de Armadura | Anti-armadura melee |
| Machados de Guerra | Dano Físico (alto) | Pen. de Armadura (alta) | Anti-armadura pesado |
| Clavas / Maças | Dano Físico | — | Melee contundente |
| Lanças | Dano Físico | Pen. Mágica | Anti-mago melee |
| Adagas | Dano Físico (baixo) | Pen. de Armadura (muito alta) | Assassino / shred |
| Cajados | Dano Mágico | — | Arma básica de mago |
| Grimórios | Dano Mágico (alto) | Pen. Mágica | Burst de mago |
| Varinhas | Dano Mágico (leve) | — | Utilitário de mago |
| Arcos / Bestas | Dano Físico | — | Ranged físico |
| Escudos | Armadura | HP de Escudo | Defensivo |
| Armas de Fogo / Bombas | Dano Físico | Pen. de Armadura | Ranged explosivo |

> **Armas híbridas:** Algumas variantes especiais (Espada de Gelo, Espada Longa do Vazio, Arco Flamejante, etc.) causam tanto Dano Físico quanto Mágico.

#### Dano de Armas por Tier (exemplo armas físicas)

| Material | Espada | Espada Longa | Machado | Machado de Guerra | Adaga |
|----------|:------:|:------------:|:-------:|:-----------------:|:-----:|
| Crude/Wood | 6–8 | 12 | 10 | 14 | 6 |
| Copper | 14 | 20 | 16 | 22 | 10 |
| Bronze | 18 | — | — | — | 14 |
| Iron | 22 | 30 | 24 | 32 | 18 |
| Steel | 28 | — | — | 34 | — |
| Cobalt | 30 | 40 | 32 | 42 | 24 |
| Mithril | 38 | 50 | 40 | 52 | 30 |
| Thorium | 42 | 55 | 44 | 56 | 34 |
| Onyxium | 48 | 62 | 50 | 64 | 38 |
| Adamantite | 50 | 65 | 52 | 68 | 40 |

### Comandos

| Comando | Descrição |
|---------|-----------|
| `/combatstats view` | Ver todos os seus atributos de combate |
| `/combatstats set <atributo> <valor>` | Definir o valor base de um atributo |
| `/combatstats add <atributo> <valor>` | Adicionar um modificador (soma no base) |
| `/combatstats reset all` | Resetar todos os atributos para 0 |
| `/combatstats reset <atributo>` | Resetar um atributo específico para 0 |
| `/combatstats reset modifiers` | Limpar só os modificadores, manter valores base |

**Atributos disponíveis:** `armor`, `magicresist` (mr), `reduction` (dr), `physdmg` (phys), `magdmg` (mag), `truedmg` (true), `armorpen` (apen), `magicpen` (mpen), `shield`

**Exemplos:**
```
/combatstats set armor 50
/combatstats set physdmg 80
/combatstats add mr 20
/combatstats set shield 100
/combatstats set reduction 30     (30% de redução de dano)
/combatstats view
/combatstats reset all
```

---

## 8. 🎮 Como Testar In-Game & Efeitos de Status Atuais

Você pode testar os efeitos de status registrados e o sistema de feitiços usando o comando administrativo integrado:

```text
/rune effect <id>
```

Abaixo está a tabela completa dos efeitos atualmente registrados no motor `RuneCore`, seus estados de desenvolvimento e o comportamento esperado de cada um:

| Ícone | Status | ID do Efeito | Tem Visual Nativo/JSON? | O que deve fazer |
| :---: | :---: | :--- | :--- | :--- |
| <img src="icons/128x/speed.png" height="32"> | [x] | `speed` | Speed | Dá o buff de velocidade. |
| <img src="icons/128x/slowness.png" height="32"> | [x] | `slowness` | Slowness | Lentidão na entidade. |
| <img src="icons/128x/haste.png" height="32"> | [ ] | `haste` | Haste | Modifica Attack Speed e Mining Speed (+50%) e mostra UI. (Attack/Mining Speed pendentes) |
| <img src="icons/128x/mining_fatigue.png" height="32"> | [ ] | `mining_fatigue`| Mining_Fatigue | Modifica Attack Speed e Mining Speed (-70%) e mostra UI. (Attack/Mining Speed pendentes) |
| <img src="icons/128x/jump_boost.png" height="32"> | [x] | `jump_boost` | Jump_Boost | Pulo mais alto. |
| <img src="icons/128x/high_jump.png" height="32"> | [x] | `high_jump` | High_Jump | Pulo muito mais alto. |
| <img src="icons/128x/slow_falling.png" height="32"> | [x] | `slow_falling` | Slow_Falling | Queda lenta. |
| <img src="icons/128x/levitation.png" height="32"> | [x] | `levitation` | Levitation | Faz a entidade flutuar para cima. |
| <img src="icons/128x/regeneration.png" height="32"> | [x] | `regeneration` | Regeneration | Cura +1 de vida a cada 50 ticks. |
| <img src="icons/128x/poison.png" height="32"> | [x] | `poison` | Poison | Dano de 1 de vida a cada 25 ticks. |
| <img src="icons/128x/decay.png" height="32"> | [x] | `decay` | Decay | Dano de 1 de vida a cada 40 ticks. |
| <img src="icons/128x/darkness.png" height="32"> | [x] | `darkness` | Darkness | Reduz significativamente o brilho da visão. |
| <img src="icons/128x/electrified.png" height="32"> | [x] | `electrified` | Electrified | Dano elétrico e faíscas visuais. |
| <img src="icons/128x/burn.png" height="32"> | [x] | `burn` | Burn | Dano de 1 de vida a cada 20 ticks + UI. |
| <img src="icons/128x/nausea.png" height="32"> | [x] | `nausea` | Nausea | Roda a câmera (NauseaTick) + UI. |
| <img src="icons/128x/bleeding.png" height="32"> | [x] | `bleeding` | Bleeding | Dano de 1 vida a cada 20 ticks + UI + Partículas custom de sangue. |
| <img src="icons/128x/frozen.png" height="32"> | [x] | `frozen` | Frozen | Impede movimento temporariamente. |
| | [x] | `instant_health`| (nenhum) | Cura instantânea (4.0 * power). |
| | [x] | `instant_damage`| InstantDamage | Dano instantâneo (6.0 * power). |
| | [ ] | `damage_fire_instant`| DamageFireInstant | Dano de fogo instantâneo (10.0 * power). |
| <img src="icons/128x/invisibility.png" height="32"> | [x] | `invisibility` | Invisibility | Esconde o jogador dos outros. (Ajuste fino de visibilidade próprio pendente) |
| <img src="icons/128x/glowing.png" height="32"> | [ ] | `glowing` | Glowing | Adiciona luz dinâmica (DynamicLight) + UI. (Não persiste no logout/relog) |
| <img src="icons/128x/blindness.png" height="32"> | [x] | `blindness` | Blindness | Modifica a visão (VisualEffectHelper) + UI. |
| <img src="icons/128x/night_vision.png" height="32"> | [x] | `night_vision` | NightVision | Luz dinâmica branca ao redor do jogador + UI. |
| <img src="icons/128x/water_breathing.png" height="32"> | [ ] | `water_breathing`| WaterBreathing | Permite respirar embaixo d'água nativamente. (Simplesmente não funciona) |
| <img src="icons/128x/fire_resistance.png" height="32"> | [x] | `fire_resistance`| FireResistance | Resistência a fogo nativa. |
| <img src="icons/128x/resistance.png" height="32"> | [ ] | `resistance` | Resistance | Resistência nativa. (Não funciona, precisa de melhorias) |
| <img src="icons/128x/strength.png" height="32"> | [ ] | `strength` | Strength | Força nativa. (Não funciona, precisa de melhorias) |
| <img src="icons/128x/weakness.png" height="32"> | [ ] | `weakness` | Weakness | Fraqueza nativa. (Não funciona, precisa de melhorias) |

### Nota de Implementação
Para habilitar todos os sistemas durante o desenvolvimento, certifique-se de que eles estão registrados no seu ponto de entrada:
```java
// Na sua classe de plugin
eventRegistry.registerGlobal(EffectTimerListener.class);
eventRegistry.registerGlobal(CastListener.class);
```

### 🧠 Como e Onde Utilizar os Efeitos (Exemplos)

Modders podem aplicar esses efeitos dinamicamente no mundo utilizando a API do `RuneCore`. Aqui estão alguns exemplos de implementação programática:

```java
// Aplicar um efeito diretamente em uma entidade (ex: jogador ou mob)
RuneCore core = RuneCore.getInstance();
RuneEffect poison = core.getEffect("poison");

if (poison != null) {
    // Cria o contexto com a fonte e o alvo
    CastContext ctx = new CastContext(sourceEntity, targetEntity);
    poison.execute(ctx);
}
```

#### 🛡️ Casos de Uso Recomendados:

*   **🧪 Alquimia e Poções:** Consumir itens que dão buffs como `speed`, `jump_boost`, ou curas como `regeneration` e `instant_health`.
*   **⚔️ Encantamentos de Armas e Flechas:** Adicione veneno (`poison`), sangramento (`bleeding`) ou lentidão (`slowness`) ao atingir alvos com armas específicas.
*   **👹 Mecânicas de Bosses / Mobs:**
    *   Um boss de gelo que congela (`frozen`) o jogador em um ataque carregado.
    *   Um ataque sombrio que inflige cegueira (`blindness`) na área ao redor do boss.
    *   Um monstro de fogo que queima (`burn`) ao toque.
*   **🌍 Armadilhas Ambientais:**
    *   Espinhos no chão que causam `bleeding`.
    *   Cair em pântanos tóxicos que aplicam `decay`.

---

## 9. 🛠️ Guia para Modders

Os pontos de entrada públicos são:

| Ponto de entrada | Para quê |
| :--- | :--- |
| `RuneAttributes` | Ler/escrever atributos, registrar itens e criaturas |
| `AttributeRegistry` + `RuneAttribute` | Declarar um atributo novo |
| `DamagePipeline` + `DamageStage` | Fazer um atributo afetar o dano |
| `RuneCore` | Essências, efeitos, feitiços |
| `EffectHelper`, `StatHelper`, `PlayerStats` | Helpers de atributo e movimento de entidade |

Interessado em construir em cima do RuneCore? Confira nosso [**Guia de Uso da API**](API_USAGE.md) para exemplos de código e passos de integração.

## 10. ⚖️ Licença

Este projeto, incluindo seu código-fonte, documentação e **ícones em pixel art** (localizados no diretório `/icons`), está licenciado sob a **Creative Commons Attribution-NonCommercial 4.0 International (CC BY-NC 4.0)**.

- **Atribuição (BY):** Você deve dar o crédito apropriado ao autor original.
- **Não Comercial (NC):** Você não pode usar o material para fins comerciais.
- **Obras derivadas:** Quem modificar pode escolher outra licença para o derivado, desde que respeite as condições acima.

Para mais detalhes, veja o arquivo [LICENSE](LICENSE) ou visite o site da [Creative Commons](https://creativecommons.org/licenses/by-nc/4.0/deed.pt_BR).

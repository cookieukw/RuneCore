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
> - 🛠️ **Em Progresso:** O sistema de drops e a mecânica de drop de essências estão sendo desenvolvidos no momento.
> - ✅ **Funcional:** Comandos principais e o sistema de gerenciamento de status do jogador estão totalmente operacionais.
> - 🧪 **API:** A API ainda está em fase de testes.
> - 🎨 **Visuais:** O projeto agora tem sua própria logo! As essências utilizam ícones em pixel art.
> - 🚀 **Próximos Passos:** Adicionar modelos 3D para as essências, reciclando modelos já existentes dentro do jogo.
>
> **Nota para testes:** Caso deseje testar o mod totalmente, lembre-se de descomentar o registro dos sistemas no arquivo principal do plugin (`ExamplePlugin.java`).

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

## 6. 🎮 Como Testar In-Game & Efeitos de Status Atuais

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

## 7. 🛠️ Guia para Modders

Interessado em construir em cima do RuneCore? Confira nosso [**Guia de Uso da API**](API_USAGE.md) para exemplos de código e passos de integração.

## 8. ⚖️ Licença

Este projeto, incluindo seu código-fonte, documentação e **ícones em pixel art** (localizados no diretório `/icons`), está licenciado sob a **Creative Commons Attribution-NonCommercial 4.0 International (CC BY-NC 4.0)**.

- **Atribuição (BY):** Você deve dar o crédito apropriado ao autor original.
- **Não Comercial (NC):** Você não pode usar o material para fins comerciais.
- **Obras derivadas:** Quem modificar pode escolher outra licença para o derivado, desde que respeite as condições acima.

Para mais detalhes, veja o arquivo [LICENSE](LICENSE) ou visite o site da [Creative Commons](https://creativecommons.org/licenses/by-nc/4.0/deed.pt_BR).

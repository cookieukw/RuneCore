# 🔮 RuneCore: Motor de Magia para Hytale

[Read in English](README.md) | [Guia da API](API_USAGE-PTBR.md) | [Docs Técnicos](docs/ELEMENTS-PTBR.md) | [Manual](RuneCore_Manual-PTBR.md)

> [!IMPORTANT]
> **Status do Projeto: Em Desenvolvimento**
>
> - 🛠️ **Em Progresso:** O sistema de drops e a mecânica de drop de essências estão sendo desenvolvidos no momento.
> - ✅ **Funcional:** Comandos principais e o sistema de gerenciamento de status do jogador estão totalmente operacionais.
> - 🧪 **API:** A API ainda está em fase de testes.
> - 🎨 **Visuais:** O projeto ainda não possui um ícone próprio. As essências utilizam ícones temporários em pixel art.
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

## 3. Recursos Principais ✨

*   **20 Elementos:** Divididos em níveis Básico, Avançado, Instável e Químico.
*   **API Modular:** Registre facilmente essências, feitiços e efeitos de status personalizados.
*   **Efeitos de Status Persistentes:** Um sistema robusto para buffs/debuffs (ex: Veneno, Regeneração, Congelamento) com lógica ciente do mundo.
*   **Gerenciamento de Recursos:** Rastreio personalizado de mana, stamina e recursos biológicos.

Para um detalhamento completo de todos os 20 elementos e suas mecânicas, veja nossos [**Documentos Técnicos**](docs/ELEMENTS-PTBR.md).

---

## 🎮 Como Testar In-Game & Efeitos de Status Atuais

Você pode testar os efeitos de status registrados e o sistema de feitiços usando o comando administrativo integrado:

```text
/rune effect <id>
```

Abaixo está a tabela completa dos efeitos atualmente registrados no motor `RuneCore`, seus estados de desenvolvimento e o comportamento esperado de cada um:

| Status | ID do Efeito | Tem Visual Nativo/JSON? | O que deve fazer |
| :---: | :--- | :--- | :--- |
| [x] | `speed` | Speed | Dá o buff de velocidade. |
| [x] | `slowness` | Slowness | Lentidão na entidade. |
| [ ] | `haste` | Haste | Modifica Attack Speed e Mining Speed (+50%) e mostra UI. (Attack/Mining Speed pendentes) |
| [ ] | `mining_fatigue`| Mining_Fatigue | Modifica Attack Speed e Mining Speed (-70%) e mostra UI. (Attack/Mining Speed pendentes) |
| [x] | `jump_boost` | Jump_Boost | Pulo mais alto. |
| [x] | `high_jump` | High_Jump | Pulo muito mais alto. |
| [x] | `slow_falling` | Slow_Falling | Queda lenta. |
| [x] | `levitation` | Levitation | Faz a entidade flutuar para cima. |
| [x] | `regeneration` | Regeneration | Cura +1 de vida a cada 50 ticks. |
| [x] | `poison` | Poison | Dano de 1 de vida a cada 25 ticks. |
| [x] | `decay` | Decay | Dano de 1 de vida a cada 40 ticks. |
| [x] | `burn` | Burn | Dano de 1 de vida a cada 20 ticks + UI. |
| [x] | `nausea` | Nausea | Roda a câmera (NauseaTick) + UI. |
| [x] | `bleeding` | Bleeding | Dano de 1 vida a cada 20 ticks + UI + Partículas custom de sangue. |
| [x] | `frozen` | Frozen | Impede movimento temporariamente. |
| [x] | `instant_health`| (nenhum) | Cura instantânea (4.0 * power). |
| [x] | `instant_damage`| InstantDamage | Dano instantâneo (6.0 * power). |
| [ ] | `damage_fire_instant`| DamageFireInstant | Dano de fogo instantâneo (10.0 * power). |
| [x] | `invisibility` | Invisibility | Esconde o jogador dos outros. (Ajuste fino de visibilidade próprio pendente) |
| [ ] | `glowing` | Glowing | Adiciona luz dinâmica (DynamicLight) + UI. (Não persiste no logout/relog) |
| [x] | `blindness` | Blindness | Modifica a visão (VisualEffectHelper) + UI. |
| [x] | `night_vision` | NightVision | Luz dinâmica branca ao redor do jogador + UI. |
| [ ] | `water_breathing`| WaterBreathing | Permite respirar embaixo d'água nativamente. (Simplesmente não funciona) |
| [x] | `fire_resistance`| FireResistance | Resistência a fogo nativa. |
| [ ] | `resistance` | Resistance | Resistência nativa. (Não funciona, precisa de melhorias) |
| [ ] | `strength` | Strength | Força nativa. (Não funciona, precisa de melhorias) |
| [ ] | `weakness` | Weakness | Fraqueza nativa. (Não funciona, precisa de melhorias) |

### Nota de Implementação
Para habilitar todos os sistemas durante o desenvolvimento, certifique-se de que eles estão registrados no seu ponto de entrada:
```java
// Na sua classe de plugin
eventRegistry.registerGlobal(EffectTimerListener.class);
eventRegistry.registerGlobal(CastListener.class);
```

---

## 🛠️ Guia para Modders

Interessado em construir em cima do RuneCore? Confira nosso [**Guia de Uso da API**](API_USAGE.md) para exemplos de código e passos de integração.

# 🔮 RuneCore: Documento de Design

## 1. De onde surgiu? 🤔

Quando o Hytale disse que o jogo teria um sistema de magia, eu logo me interessei pelo sistema, pois nunca vi um jogo do gênero usar isso bem. Contudo, ficamos sabendo que a magia do Hytale estava extremamente básica e que não seria tão explorada assim.

Muita gente se decepcionou porque não teria nada tão legal, só que para mim isso foi mais que uma oportunidade de fazer um sistema tão interessante. Assim como está sendo com o sistema de energia, onde um dos mods mais promissores de automação para o Hytale é o **HyEnergy**, do Seyager. Também quero trazer não só uma promessa, mas também um ótimo exemplo do que a comunidade pode fazer.

---

## 2. O que é o RuneCore? 📘

Primeiramente, precisamos entender que o RuneCore é um sistema de magia para o Hytale. Ele serve para solucionar a falta de profundidade no sistema original.

### 🏗️ Estrutura do Sistema

O projeto é dividido em módulos interdependentes:

#### 🔹 RuneCore (Núcleo)

Um sistema núcleo, ou seja, um sistema principal.

- **Função:** Ele terá um sistema de essências, gerencia o sistema de mana e progresso do jogador.
- **API:** Ele em si sozinho não faz nada por si só. Porém, ele cria uma API (recursos) para que outros modders possam usá-lo como dependência. Todo o sistema gira em torno do RuneCore.

#### ⚔️ RuneMagic

Focado nas habilidades e combate.

- **Magias:** Habilidades, efeitos, etc.
- **Runas:** Efeitos passivos (ou seja, não precisam ser ativados mais de uma vez).
- **Artefatos:** Itens que te dão habilidades extras.
- **Grimórios:** Livros com habilidade única ou conjunto de habilidades.
- **Novos recursos.**

#### ⚗️ RuneQuimic

Um sistema de alquimia.

- **Conteúdo:** Consiste em adicionar alguns elementos químicos, aparelhos para alquimia, poções e livros de encantamentos.
- **Base:** Justamente baseado nas essências.

---

## 3. Sistema de Essências ✨

**O que são essas tais essências?**
Pois bem, essas essências são praticamente almas e, em alguns casos, materiais.

**Formas de obtenção:**

- ⛏️ Coleta de recursos.
- 🏰 Conquistando dungeons e zonas.
- ⚔️ Matando monstros e mobs específicos.
- 🧪 Através da alquimia.

---

## 4. Elementos 🌎

As almas são baseadas em elementos. Normalmente, os mods ou jogos do gênero sempre se baseiam em 4 elementos (água, fogo, vento e terra), sendo que às vezes pode ter éter, luz ou sombra. Só que esse mod não será assim. **Teremos 20 elementos.**

### Classificação dos Elementos

| Nível Básico | Nível Avançado | Elementos Instáveis | Elementos Químicos |
| :----------: | :------------: | :-----------------: | :----------------: |
|   🔥 Fogo    |     ☀️ Luz     |       🌀 Caos       |      ⚙️ Metal      |
|   🪨 Terra   |   🌑 Sombra    |       ✨ Éter       |     💎 Cristal     |
|   💨 Vento   |    🌿 Vida     |      🕳️ Vazio       |     🧪 Veneno      |
|   💧 Água    |    ☠️ Morte    |      ⏳ Tempo       |      🧴 Ácido      |
|   ❄️ Gelo    |    🧠 Mente    |                     |                    |
|   ⚡ Raio    |   🩸 Sangue    |                     |                    |

---

## 5. Detalhes dos Elementos

### 🔥 Fogo

**Conceito:** Elemento de dano contínuo e controle espacial (alteração do ambiente).

> **Definição Funcional:** Ele atua alterando condições do ambiente e dos alvos, reduzindo agressividade direta e preparando reações com outros elementos. (Nota: Texto da definição original parecia ser de Água, ajustado para Fogo conforme contexto ou mantido original se coerente - _Original de Fogo não tinha definição funcional explícita como os outros, assumindo descrição do conceito_)

**Tipo de Dano:**

- Dano contínuo (DoT) aplicado por tempo.
- Dano em área (AoE) com permanência no terreno.
- Parte do dano ignora mitigação física (penetração por queimadura).

**Escalonamento:** Escala melhor com a duração do efeito do que com poder bruto (burst).
**Estilo de Jogo:** Não foca em explosão imediata, mas em acúmulo de dano e negação de área.

#### ✅ Vantagens

- Alta eficiência contra grupos.
- Mantém dano mesmo sem ação contínua do jogador.
- Reduz mobilidade inimiga ao alterar terreno.
- Sinergia direta com efeitos inflamáveis (óleo, madeira, gases).
- Força o inimigo a reposicionar-se.

#### ❌ Desvantagens

- Baixa eficiência contra alvos únicos de alta resistência.
- Fortemente reduzido por água, gelo e resistência térmica.
- Alto consumo de recurso ao manter efeitos ativos.
- Pode causar efeitos colaterais no ambiente (NPCs, estruturas, drops).
- Difícil controle em áreas fechadas.

#### 🪄 Formas de Uso

- **Aplicação Direta:** Projéteis incendiários, Feixes contínuos de fogo.
- **Área Persistente:** Superfícies em combustão, Zonas de calor intenso, Campos que aplicam queimadura por permanência.
- **Interação Ambiental:** Incêndio de estruturas inflamáveis, Reação com líquidos e materiais orgânicos, Alteração permanente ou temporária do terreno.
- **Uso Alquímico:** Bombas incendiárias, Reagente para fusões elementais, Fonte de energia para máquinas alquímicas.

#### 😵 Efeitos de Status Associados

- **Queimadura:** Dano periódico acumulável.
- **Exaustão Térmica:** Redução de stamina ou velocidade.
- **Desorientação:** Em criaturas frágeis.
- **Resíduo Térmico:** No solo, afetando futuras interações.

#### 🔥 Essência de Fogo

- **Métodos de Obtenção:**
  - **Ambiental:** Biomas de alta temperatura, Lava solidificada, Fendas térmicas, Eventos climáticos extremos.
  - **Criaturas:** Entidades elementais, Mobs expostos a calor extremo por longo período, Chefes ígneos.
  - **Estruturas:** Fornalhas antigas, Complexos industriais abandonados, Ruínas queimadas.
  - **Alquimia:** Destilação de compostos inflamáveis, Processos de combustão controlada, Fermentação térmica com risco de falha.
- **Classificação da Essência:**
  - **Instável:** Fácil obtenção, efeitos imprevisíveis.
  - **Refinada:** Uso padrão em feitiços.
  - **Viva:** Reage com o ambiente e outros elementos.
- **Comportamento Sistêmico:**
  - Uso excessivo aumenta temperatura local.
  - Temperatura elevada altera spawn, clima e bioma.
  - Regiões afetadas tendem a gerar entidades ígneas.

---

### 🪨 Terra

**Conceito:** Elemento de controle estrutural, mitigação de dano e modificação física do espaço.

> **Definição Funcional:** Ele atua criando, deslocando ou reforçando matéria sólida para influenciar o fluxo do combate e a progressão do mundo.

**Tipo de Efeito:**

- Mitigação de dano físico e elemental.
- Controle de área por obstrução.
- Alteração direta de terreno e estruturas.
- Dano baseado em impacto e pressão (não contínuo).

#### ✅ Vantagens

- Alta eficiência defensiva.
- Escala bem com preparação e posicionamento.
- Forte contra inimigos de contato direto.
- Cria cobertura real e permanente.
- Sinergia com sistemas de construção e worldgen.

#### ❌ Desvantagens

- Baixa mobilidade.
- Execução lenta.
- Pouca eficiência contra alvos aéreos.
- Fraco contra água (erosão) e explosões.
- Consome espaço e pode atrapalhar aliados.

#### 🪄 Formas de Uso

- **Criação Estrutural:** Paredes, Colunas, Cúpulas, Barreiras direcionais.
- **Controle de Terreno:** Elevação ou depressão do solo, Criação de obstáculos, Fechamento de passagens, Canalização de movimento inimigo.
- **Ataque Físico:** Espinhos de pedra, Projéteis maciços, Ondas sísmicas, Quedas de blocos.
- **Uso Alquímico:** Reforço de equipamentos, Encantamentos defensivos, Componentes de rituais de contenção, Estruturas alquímicas permanentes.

#### 🪨 Essência de Terra

- **Métodos de Obtenção:**
  - **Ambiental:** Veios minerais, Cavernas profundas, Biomas rochosos, Áreas tectonicamente instáveis.
  - **Criaturas:** Elementais de pedra, Criaturas subterrâneas, Mobs fossilizados ou cristalizados.
  - **Estruturas:** Minas abandonadas, Fortificações antigas, Ruínas soterradas.
  - **Alquimia:** Compactação de minerais, Destilação de sedimentos, Cristalização por pressão. (Risco: Falha gera colapso ou soterramento).
- **Classificação da Essência:**
  - **Fragmentada:** Fraca, fácil de obter.
  - **Condensada:** Uso padrão.
  - **Núcleo Geológico:** Rara, alta estabilidade.
- **Comportamento Sistêmico:**
  - Uso contínuo altera topografia local.
  - Excesso gera deslizamentos, colapsos ou cavernas.
  - Estruturas criadas podem persistir no mundo.
  - Regiões saturadas atraem criaturas subterrâneas.

---

### 💨 Vento

**Conceito:** Elemento de força vetorial, controle cinético e manipulação de trajetória.

> **Definição Funcional:** Ele atua aplicando aceleração, desaceleração e redirecionamento a entidades, projéteis e efeitos ambientais.

**Tipo de Efeito:**

- Deslocamento forçado (knockback / pull / lift).
- Alteração de velocidade e direção.
- Controle de projéteis.
- Interferência em conjurações e movimentos.

#### ✅ Vantagens

- Alto controle sem depender de dano direto.
- Efetivo contra múltiplos alvos.
- Neutraliza projéteis físicos e mágicos.
- Forte sinergia com outros elementos.
- Escala com posicionamento e timing.

#### ❌ Desvantagens

- Baixo dano base.
- Pouco efeito em alvos muito pesados.
- Exige leitura do espaço.
- Menor impacto em áreas fechadas.
- Pode atrapalhar aliados e setups.

#### 🪄 Formas de Uso

- **Manipulação de Movimento:** Empurrar ou puxar inimigos, Lançar alvos ao ar, Redirecionar quedas, Suspender brevemente entidades.
- **Controle de Projéteis:** Desviar flechas e magias, Acelerar projéteis aliados, Criar zonas de turbulência, Curvar trajetórias.
- **Mobilidade:** Saltos assistidos, Deslocamento rápido, Planagem, Cancelamento de queda.
- **Interação Ambiental:** Espalhar fogo, venenos e gases, Dissipar névoa e fumaça, Ativar mecanismos baseados em pressão, Alterar clima local temporariamente.

#### 😵 Efeitos de Status Associados

- **Desequilíbrio:** Reduz precisão e controle.
- **Exposição:** Remove cobertura.
- **Desarme:** Chance de soltar itens leves.
- **Queda Forçada.**

#### 💨 Essência de Vento

- **Métodos de Obtenção:**
  - **Ambiental:** Picos elevados, Desfiladeiros, Planícies abertas, Tempestades e frentes frias.
  - **Criaturas:** Elementais aéreos, Criaturas voadoras, Entidades de clima extremo.
  - **Estruturas:** Torres antigas, Moinhos, Observatórios climáticos, Ruínas em regiões altas.
  - **Alquimia:** Compressão de ar em câmaras seladas, Destilação de correntes atmosféricas. (Risco: Uso falho gera explosão de pressão).
- **Classificação da Essência:**
  - **Dispersa:** Fraca, difícil de armazenar.
  - **Condensada:** Uso padrão.
  - **Ciclônica:** Rara, altamente reativa.
- **Comportamento Sistêmico:**
  - Uso contínuo altera padrões climáticos.
  - Pode aumentar spawn de criaturas aéreas.
  - Zonas saturadas geram rajadas aleatórias.
  - Interfere em outros elementos ativos.

---

### 💧 Água

**Conceito:** Elemento de controle de estado, mitigação térmica e interação química.

> **Definição Funcional:** Ela atua alterando condições do ambiente e dos alvos, reduzindo agressividade direta e preparando reações com outros elementos.

**Tipo de Efeito:**

- Controle de área por fluido.
- Alteração de estados físicos (molhado, encharcado).
- Redução e anulação de efeitos térmicos.
- Dano indireto por afogamento, pressão ou reação.

#### ✅ Vantagens

- Forte contra fogo e calor.
- Excelente em controle prolongado.
- Facilita reações elementais.
- Afeta múltiplos alvos de forma consistente.
- Baixo risco ambiental comparado a fogo.

#### ❌ Desvantagens

- Dano direto baixo.
- Dependente de terreno e volume.
- Pode favorecer inimigos aquáticos.
- Execução lenta em áreas secas.
- Pouco efeito imediato sem combinação.

#### 🪄 Formas de Uso

- **Controle de Área:** Superfícies escorregadias, Campos de inundação, Correntes direcionais, Zonas de pressão hidráulica.
- **Alteração de Estado:** Aplicação de “Molhado”, Supressão de queimadura, Aumento de condução elétrica, Redução de atrito.
- **Ataque Indireto:** Jatos de alta pressão, Esmagamento hidráulico, Afogamento progressivo, Queda induzida por instabilidade.
- **Uso Alquímico:** Base para poções e solventes, Extração e diluição de essências, Reações químicas controladas, Estabilização de compostos voláteis.

#### 😵 Efeitos de Status Associados

- **Molhado:** Reduz resistência ao raio, aumenta peso efetivo.
- **Arrasto:** Redução de velocidade.
- **Desorientação:** Em criaturas terrestres.
- **Asfixia:** Em submersão prolongada.

---

### ❄️ Gelo

**Conceito:** Elemento de redução térmica, restrição de movimento e fragilização estrutural.

> **Definição Funcional:** Ele atua diminuindo energia cinética dos alvos e transformando água e superfícies em estados sólidos instáveis.

**Tipo de Efeito:**

- Redução progressiva de velocidade.
- Imobilização temporária.
- Dano por fratura térmica.
- Modificação física do terreno.

#### ✅ Vantagens

- Forte controle de multidão.
- Sinergia direta com água.
- Neutraliza fogo e efeitos térmicos.
- Cria superfícies defensivas e ofensivas.
- Excelente setup para burst de outros elementos.

#### ❌ Desvantagens

- Dano direto baixo.
- Alvos resistentes a frio reduzem muito o impacto.
- Efeitos quebráveis.
- Dependente de posicionamento.
- Perde eficiência em ambientes quentes.

#### 🪄 Formas de Uso

- **Controle de Movimento:** Congelamento gradual, Enraizamento por gelo, Prisões cristalinas, Superfícies escorregadias.
- **Alteração do Ambiente:** Congelar água e lama, Criar pontes temporárias, Selar passagens, Reforçar estruturas por curto período.
- **Ataque Estrutural:** Lâminas de gelo, Espinhos emergentes, Explosão por expansão térmica, Fratura de alvos congelados.
- **Uso Alquímico:** Preservação de reagentes, Estabilização de poções, Reações de choque térmico, Contenção de essências instáveis.

#### 😵 Efeitos de Status Associados

- **Frio:** Redução de velocidade e ataque.
- **Congelado:** Imobilização total.
- **Fragilidade:** Aumento de dano recebido.
- **Hipotermia:** Perda progressiva de recursos.

#### ❄️ Essência de Gelo

- **Métodos de Obtenção:**
  - **Ambiental:** Biomas glaciais, Picos congelados, Cavernas de gelo, Tempestades de neve.
  - **Criaturas:** Elementais de gelo, Predadores árticos, Entidades cristalizadas.
  - **Estruturas:** Ruínas soterradas em gelo, Templos antigos congelados, Laboratórios de preservação.
  - **Alquimia:** Cristalização controlada, Extração de frio residual, Congelamento sob pressão. (Risco: Falha gera estilhaçamento violento).
- **Classificação da Essência:**
  - **Frágil:** Curta duração.
  - **Estável:** Uso padrão.
  - **Eterna:** Rara, não derrete facilmente.
- **Comportamento Sistêmico:**
  - Uso prolongado reduz temperatura local.
  - Pode congelar corpos d’água inteiros.
  - Aumenta spawn de criaturas frias.
  - Cria risco de fraturas ambientais.

---

### ⚡ Raio

**Conceito:** Elemento de descarga elétrica, propagação em cadeia e interferência energética.

> **Definição Funcional:** Ele atua aplicando dano instantâneo, saltando entre condutores e afetando sistemas ativos e estados alterados.

**Tipo de Efeito:**

- Dano instantâneo (burst).
- Propagação entre alvos próximos.
- Interrupção de ações e conjurações.
- Ativação ou sobrecarga de sistemas.

#### ✅ Vantagens

- Alto dano imediato.
- Escala muito bem com alvos agrupados.
- Forte contra inimigos molhados ou metálicos.
- Pode interromper ataques e feitiços.
- Pouco dependente de terreno.

#### ❌ Desvantagens

- Alcance limitado sem condutores.
- Ineficiente contra alvos isolados.
- Consumo alto de recurso por uso.
- Pouco efeito prolongado.
- Risco de retorno elétrico ao usuário.

#### 🪄 Formas de Uso

- **Descarga Direta:** Raios instantâneos, Projéteis elétricos, Pulsos de curto alcance.
- **Propagação:** Correntes elétricas em cadeia, Campos de condução, Saltos entre alvos próximos.
- **Interferência:** Interromper conjuração, Desativar mecanismos, Causar falhas em criaturas artificiais, Sobrecarregar equipamentos.
- **Uso Alquímico:** Fonte de energia, Ativação de reatores, Catalisador de reações rápidas, Carregamento de itens.

#### 😵 Efeitos de Status Associados

- **Eletrocutado:** Dano imediato adicional.
- **Atordoamento:** Perda temporária de ação.
- **Sobrecarga:** Efeitos ativos são interrompidos.
- **Condução:** Aumenta chance de salto elétrico.

#### ⚡ Essência de Raio

- **Métodos de Obtenção:**
  - **Ambiental:** Tempestades elétricas, Picos elevados, Torres naturais, Regiões de clima instável.
  - **Criaturas:** Elementais elétricos, Criaturas energizadas, Entidades artificiais.
  - **Estruturas:** Para-raios antigos, Geradores abandonados, Laboratórios arcanos.
  - **Alquimia:** Captura de descargas, Armazenamento em cristais condutores, Compressão energética. (Risco: Falha gera explosão elétrica).
- **Classificação da Essência:**
  - **Instável:** Difícil de conter.
  - **Condensada:** Uso padrão.
  - **Ionizada:** Rara, alta propagação.
- **Comportamento Sistêmico:**
  - Uso frequente ioniza a área.
  - Aumenta chance de eventos elétricos.
  - Potencializa efeitos de água e metal.
  - Pode ativar entidades dormentes.

---

### ☀️ Luz

**Conceito:** Elemento de radiação energética, revelação de informação e neutralização de estados anômalos.

> **Definição Funcional:** Ela atua reduzindo entropia local, expondo entidades ocultas e interferindo em efeitos baseados em sombra, corrupção ou ilusão.

**Tipo de Efeito:**

- Dano energético direto.
- Revelação e detecção.
- Supressão de efeitos negativos.
- Interferência em estados ocultos.

#### ✅ Vantagens

- Forte contra sombra, ilusão e corrupção.
- Revela inimigos invisíveis ou camuflados.
- Limpa ou reduz debuffs.
- Atua à distância com alta precisão.
- Baixo impacto ambiental destrutivo.

#### ❌ Desvantagens

- Eficiência reduzida contra alvos neutros.
- Pouca alteração de terreno.
- Consumo alto de essência refinada.
- Pouca sinergia com efeitos físicos.
- Fraca em ambientes saturados de luz.

#### 🪄 Formas de Uso

- **Dano Energético:** Feixes concentrados, Pulsos radiantes, Projéteis fotônicos.
- **Revelação:** Exposição de entidades ocultas, Marcação de alvos, Iluminação de áreas corrompidas, Detecção de armadilhas e ilusões.
- **Supressão:** Remoção de debuffs, Redução de corrupção, Enfraquecimento de entidades sombrias, Estabilização de áreas instáveis.
- **Uso Alquímico:** Purificação de reagentes, Catalisador de rituais de limpeza, Criação de cristais radiantes, Estabilização de essências perigosas.

#### 😵 Efeitos de Status Associados

- **Iluminado:** Visível, sem bônus de ocultação.
- **Purificado:** Redução de efeitos negativos.
- **Exposto:** Perde bônus defensivos temporariamente.
- **Radiação:** Dano leve contínuo em entidades sensíveis.

#### ☀️ Essência de Luz

- **Métodos de Obtenção:**
  - **Ambiental:** Regiões de alta exposição solar, Fenômenos luminosos raros, Picos elevados ao amanhecer.
  - **Criaturas:** Entidades radiantes, Guardiões antigos, Criaturas de planos luminosos.
  - **Estruturas:** Templos solares, Observatórios antigos, Artefatos de foco luminoso.
  - **Alquimia:** Concentração de luz em cristais, Purificação por refração, Destilação de energia radiante. (Risco: Falha gera cegueira ou sobrecarga).
- **Classificação da Essência:**
  - **Difusa:** Comum, baixa potência.
  - **Focada:** Uso padrão.
  - **Radiante:** Rara, alta pureza.
- **Comportamento Sistêmico:**
  - Uso contínuo reduz corrupção local.
  - Pode estabilizar regiões caóticas.
  - Diminui spawn de entidades sombrias.
  - Interfere em efeitos de sombra ativos.

---

### 🌑 Sombra

**Conceito:** Elemento de ocultação, drenagem entrópica e manipulação de percepção.

> **Definição Funcional:** Ela atua reduzindo informação disponível, degradando energia e explorando estados de medo, ilusão e vulnerabilidade.

**Tipo de Efeito:**

- Ocultação e invisibilidade parcial.
- Dano por drenagem.
- Interferência sensorial.
- Debuffs psicológicos e energéticos.

#### ✅ Vantagens

- Forte em controle indireto.
- Efetiva contra alvos isolados.
- Sinergia com ambientes escuros.
- Permite abordagem furtiva real.
- Escala com duração e posicionamento.

#### ❌ Desvantagens

- Fraca sob luz intensa.
- Pouco impacto estrutural.
- Dano direto limitado.
- Alto custo de manutenção.
- Requer preparo e ambiente favorável.

#### 🪄 Formas de Uso

- **Ocultação:** Invisibilidade condicional, Camuflagem dinâmica, Redução de detecção inimiga, Supressão de iluminação.
- **Drenagem:** Roubo de vida ou energia, Enfraquecimento progressivo, Erosão de buffs ativos, Exaustão mental.
- **Manipulação Sensorial:** Ilusões de movimento, Distorção de som, Confusão de alvo, Indução de medo.
- **Uso Alquímico:** Catalisador de rituais proibidos, Criação de venenos entrópicos, Armazenamento de corrupção, Estabilização de elementos caóticos.

#### 😵 Efeitos de Status Associados

- **Oculto:** Reduz chance de detecção.
- **Drenado:** Perda contínua de recursos.
- **Aterrorizado:** Redução de precisão e controle.
- **Corrompido:** Aumento de dano recebido de luz.

#### 🌑 Essência de Sombra

- **Métodos de Obtenção:**
  - **Ambiental:** Cavernas profundas, Ruínas sem iluminação, Biomas de entropia elevada, Eventos de eclipse.
  - **Criaturas:** Entidades sombrias, Criaturas noturnas, Mobs corrompidos.
  - **Estruturas:** Santuários esquecidos, Prisões antigas, Templos invertidos.
  - **Alquimia:** Condensação de ausência luminosa, Extração entrópica, Sacrifício energético. (Risco: Falha gera corrupção descontrolada).
- **Classificação da Essência:**
  - **Rala:** Fraca, instável.
  - **Densa:** Uso padrão.
  - **Abissal:** Rara, altamente corrosiva.
- **Comportamento Sistêmico:**
  - Uso prolongado escurece a região.
  - Reduz iluminação global local.
  - Aumenta spawn de criaturas sombrias.
  - Enfraquece efeitos de luz ativos.

---

### 🌿 Vida

**Conceito:** Elemento de regeneração biológica, adaptação orgânica e crescimento dirigido.

> **Definição Funcional:** Ela atua restaurando, modificando e ampliando estruturas vivas, com custo e risco de crescimento descontrolado.

**Tipo de Efeito:**

- Regeneração progressiva.
- Aumento e modificação de atributos.
- Criação e controle de matéria orgânica.
- Transferência de vitalidade.

#### ✅ Vantagens

- Sustentação prolongada.
- Sinergia forte com aliados e invocações.
- Pode alterar permanentemente entidades.
- Escala com tempo e investimento.
- Forte fora de combate imediato.

#### ❌ Desvantagens

- Resposta lenta em combate intenso.
- Pode gerar mutações indesejadas.
- Ineficiente contra dano massivo.
- Forte counter por morte e corrupção.
- Requer gerenciamento constante.

#### 🪄 Formas de Uso

- **Regeneração:** Cura gradual, Regeneração de membros, Recuperação de stamina, Retirada de efeitos degenerativos.
- **Crescimento Orgânico:** Vinhas, raízes, carapaças; Barreiras vivas; Pontes e estruturas orgânicas; Armaduras biológicas temporárias.
- **Adaptação:** Resistência progressiva a dano recebido, Ajuste a biomas hostis, Tolerância a venenos e doenças, Evolução de invocações.
- **Uso Alquímico:** Criação de elixires orgânicos, Fermentação viva, Catalisador de mutações, Rituais de revitalização.

#### 😵 Efeitos de Status Associados

- **Regenerando:** Recuperação contínua.
- **Enraizado:** Mobilidade reduzida, defesa maior.
- **Mutação:** Ganho com efeito colateral.
- **Excesso Vital:** Crescimento prejudicial.

#### 🌿 Essência de Vida

- **Métodos de Obtenção:**
  - **Ambiental:** Florestas densas, Regiões intocadas, Árvores antigas, Biomas férteis.
  - **Criaturas:** Entidades naturais, Guardiões da floresta, Criaturas regenerativas.
  - **Estruturas:** Santuários naturais, Altares vivos, Jardins antigos.
  - **Alquimia:** Extração de seiva vital, Cultivo controlado, Simbiose alquímica. (Risco: Falha gera proliferação hostil).
- **Classificação da Essência:**
  - **Latente:** Fraca, comum.
  - **Vigorosa:** Uso padrão.
  - **Primordial:** Rara, altamente reativa.
- **Comportamento Sistêmico:**
  - Uso contínuo aumenta fertilidade local.
  - Pode gerar crescimento excessivo.
  - Atrai criaturas orgânicas.
  - Pode conflitar com estruturas artificiais.

---

### ☠️ Morte

**Conceito:** Elemento de degradação vital, interrupção de regeneração e reaproveitamento de resíduos orgânicos.

> **Definição Funcional:** Ela atua encerrando processos de vida, acelerando decomposição e convertendo vitalidade em energia entrópica.

**Tipo de Efeito:**

- Dano degenerativo.
- Supressão de cura e regeneração.
- Decomposição progressiva.
- Conversão de vida em recurso.

#### ✅ Vantagens

- Forte contra alvos com alta regeneração.
- Neutraliza efeitos de Vida.
- Dano consistente ao longo do tempo.
- Sinergia com cadáveres e áreas de morte.
- Escala bem em combates longos.

#### ❌ Desvantagens

- Pouco burst.
- Baixa eficiência contra construtos e máquinas.
- Requer manutenção de efeitos.
- Pode gerar consequências ambientais.
- Hostilidade de NPCs e facções.

#### 🪄 Formas de Uso

- **Degeneração:** Apodrecimento progressivo, Necrose localizada, Drenagem vital, Erosão de buffs biológicos.
- **Supressão:** Bloqueio de cura, Redução de regeneração, Cancelamento de efeitos de Vida, Enfraquecimento prolongado.
- **Reaproveitamento:** Animação de cadáveres, Extração de energia vital residual, Criação de zonas de morte, Alimentação de rituais.
- **Uso Alquímico:** Veneno necrótico, Conservação de cadáveres, Catalisador de rituais fúnebres, Estabilização de corrupção orgânica.

#### 😵 Efeitos de Status Associados

- **Necrose:** Dano contínuo + redução de cura.
- **Exaurido:** Perda de stamina máxima.
- **Marcado para Morte:** Aumenta dano recebido.
- **Putrefação:** Efeitos se espalham após a morte.

#### ☠️ Essência de Morte

- **Métodos de Obtenção:**
  - **Ambiental:** Campos de batalha antigos, Cemitérios e criptas, Biomas estéreis, Zonas de extinção.
  - **Criaturas:** Mortos-vivos, Entidades necromânticas, Criaturas corrompidas.
  - **Estruturas:** Catacumbas, Mausoléus, Altares fúnebres.
  - **Alquimia:** Destilação de resíduos vitais, Decomposição controlada, Sacrifício orgânico. (Risco: Falha gera praga ou contaminação).
- **Classificação da Essência:**
  - **Residual:** Fraca, instável.
  - **Condensada:** Uso padrão.
  - **Terminal:** Rara, extremamente corrosiva.
- **Comportamento Sistêmico:**
  - Uso contínuo esteriliza o ambiente.
  - Reduz crescimento orgânico.
  - Aumenta spawn de mortos-vivos.
  - Conflita diretamente com Vida e Luz.

---

### 🧠 Mente

**Conceito:** Elemento de processamento cognitivo, manipulação de percepção e interferência comportamental.

> **Definição Funcional:** Ele atua diretamente sobre tomada de decisão, foco, memória e controle de ações.

**Tipo de Efeito:**

- Controle parcial ou total de entidades.
- Alteração de percepção e prioridade.
- Interrupção cognitiva.
- Dano psíquico (não físico).

#### ✅ Vantagens

- Ignora armadura física.
- Extremamente forte contra alvos inteligentes.
- Permite controle sem dano direto.
- Sinergia com furtividade e preparo.
- Pode encerrar combate sem matar.

#### ❌ Desvantagens

- Fraca contra criaturas simples ou autômatos.
- Alto custo de essência.
- Duração limitada.
- Forte counter por resistência mental.
- Pode gerar consequências narrativas severas.

#### 🪄 Formas de Uso

- **Controle Comportamental:** Encantar (charm), Provocar (taunt), Forçar fuga ou hesitação, Redirecionar agressividade.
- **Manipulação Perceptiva:** Ilusões visuais e sonoras, Invisibilidade cognitiva, Distorção de espaço percebido, Falsos alvos.
- **Interferência Mental:** Interrupção de conjuração, Confusão de comandos, Sobrecarga sensorial, Dano psíquico direto.
- **Uso Alquímico:** Elixires de foco ou delírio, Drogas psíquicas, Catalisadores de rituais mentais, Armazenamento de pensamentos residuais.

#### 😵 Efeitos de Status Associados

- **Confuso:** Comandos erráticos.
- **Dominado:** Controle temporário.
- **Amedrontado:** Fuga ou paralisia.
- **Esgotamento Mental:** Aumento de custo de ações.

#### 🧠 Essência de Mente

- **Métodos de Obtenção:**
  - **Ambiental:** Locais de alta atividade psíquica, Ruínas de conhecimento, Zonas de sonho ou delírio.
  - **Criaturas:** Entidades inteligentes, Psíquicos naturais, Criaturas oníricas.
  - **Estruturas:** Bibliotecas antigas, Observatórios mentais, Laboratórios de controle.
  - **Alquimia:** Destilação de memórias, Extração de pensamentos residuais, Condensação de foco. (Risco: Falha gera loucura temporária).
- **Classificação da Essência:**
  - **Difusa:** Fraca, instável.
  - **Clara:** Uso padrão.
  - **Sináptica:** Rara, alta precisão.
- **Comportamento Sistêmico:**
  - Uso excessivo afeta NPCs próximos.
  - Pode alterar comportamento de facções.
  - Gera resistência mental acumulativa.
  - Abuso resulta em instabilidade psíquica.

---

### 🩸 Sangue

**Conceito:** Elemento de sacrifício, vínculo vital e amplificação direta.

> **Definição Funcional:** Ele converte vida atual ou potencial em poder imediato, criando efeitos fortes com custo mensurável e irreversível no curto prazo.

**Tipo de Efeito:**

- Amplificação de dano e efeitos.
- Ativação de rituais.
- Vínculo entre entidades.
- Transferência direta de vitalidade.

#### ✅ Vantagens

- Escala direto com risco assumido.
- Alto impacto imediato.
- Ignora limitações tradicionais de mana.
- Forte sinergia com Vida e Morte.
- Permite efeitos acima do “normal” do sistema.

#### ❌ Desvantagens

- Custo direto em HP ou recursos vitais.
- Risco real de morte do usuário.
- Penalidades acumulativas.
- Pouca sustentabilidade.
- Hostilidade social e narrativa.

#### 🪄 Formas de Uso

- **Sacrifício:** Converter HP em poder, Ativar feitiços acima do limite, Reduzir cooldowns, Forçar conjuração instantânea.
- **Vínculo:** Compartilhar dano entre entidades, Drenar vida de alvos marcados, Criar pactos temporários, Controlar invocações por sangue.
- **Amplificação:** Aumentar dano elemental, Estender duração de efeitos, Ignorar resistências, Reforçar rituais complexos.
- **Uso Alquímico:** Catalisador de rituais, Poções de sacrifício, Selos de sangue, Armazenamento vital instável.

#### 😵 Efeitos de Status Associados

- **Sangramento:** Perda contínua de HP.
- **Exaurido:** Redução de vida máxima temporária.
- **Marcado:** Alvo rastreável.
- **Vínculo de Sangue:** Dano compartilhado.

#### 🩸 Essência de Sangue

- **Métodos de Obtenção:**
  - **Ambiental:** Campos de batalha recentes, Altares sacrificiais, Eventos de massacre.
  - **Criaturas:** Inimigos orgânicos, Chefes vivos, Criaturas raras (maior pureza).
  - **Estruturas:** Templos profanos, Arenas rituais, Laboratórios proibidos.
  - **Alquimia:** Destilação de sangue fresco, Coagulação controlada, Mistura com catalisadores vitais. (Risco: Falha gera hemorragia ou corrupção).
- **Classificação da Essência:**
  - **Diluído:** Comum, fraco.
  - **Vital:** Uso padrão.
  - **Ancestral:** Raro, poder extremo.
- **Comportamento Sistêmico:**
  - Uso frequente reduz vida máxima.
  - Atrai entidades predatórias.
  - Pode gerar maldições persistentes.
  - Sinergia perigosa com Morte e Caos.

---

## 6. Elementos Instáveis 🌀

### 🌀 Caos

**Conceito:** Elemento de entropia ativa, instabilidade sistêmica e quebra de regras locais.

> **Definição Funcional:** Ele não cria efeitos específicos; ele altera como os efeitos funcionam, introduzindo variação, erro e mutação.

**Tipo de Efeito:**

- Modificação aleatória de resultados.
- Amplificação ou colapso de efeitos.
- Mutação de entidades e ambiente.
- Violação controlada de limites do sistema.

#### ✅ Vantagens

- Potencial de poder acima do normal.
- Quebra padrões previsíveis de combate.
- Sinergia com qualquer elemento.
- Permite soluções emergentes.
- Alto impacto narrativo.

#### ❌ Desvantagens

- Resultado imprevisível.
- Risco elevado ao usuário.
- Difícil de balancear.
- Efeitos colaterais frequentes.
- Pode tornar áreas inutilizáveis.

#### 🪄 Formas de Uso

- **Modificação de Feitiços:** Alterar alcance, forma ou custo; Inverter efeitos; Duplicar ou cancelar resultados; Introduzir efeitos colaterais.
- **Mutação:** Alterar atributos permanentemente, Criar criaturas instáveis, Fundir entidades, Corromper estruturas.
- **Ruptura Sistêmica:** Ignorar resistências, Quebrar cooldowns, Distorcer tempo curto, Reescrever interações locais.
- **Uso Alquímico:** Catalisador universal, Instabilizador de poções, Rituais imprevisíveis, Combustível de artefatos proibidos.

#### 😵 Efeitos de Status Associados

- **Instável:** Efeitos variáveis.
- **Mutado:** Ganho com penalidade.
- **Distorcido:** Controles alterados.
- **Colapso:** Falha crítica iminente.

#### 🌀 Essência de Caos

- **Métodos de Obtenção:**
  - **Ambiental:** Fendas dimensionais, Regiões corrompidas, Eventos anômalos, Biomas quebrados.
  - **Criaturas:** Entidades instáveis, Aberrações, Chefes corrompidos.
  - **Estruturas:** Laboratórios destruídos, Altares caóticos, Núcleos colapsados.
  - **Alquimia:** Reações falhas, Misturas incompatíveis, Sobrecarga energética. (Risco: Falha gera colapso local).
- **Classificação da Essência:**
  - **Volátil:** Quase inútil.
  - **Caótica:** Uso padrão.
  - **Primordial:** Extremamente rara.
- **Comportamento Sistêmico:**
  - Uso contínuo degrada regras locais.
  - Aumenta chance de eventos aleatórios.
  - Pode criar zonas permanentemente instáveis.
  - Interfere em todos os outros elementos.

---

### ✨ Éter

**Conceito:** Elemento de transição espacial, deslocamento dimensional e mediação entre planos.

> **Definição Funcional:** Ele não causa dano direto por padrão; ele altera onde e como entidades e efeitos existem.

**Tipo de Efeito:**

- Teleporte e reposicionamento.
- Distorção de espaço.
- Ancoragem e desancoragem dimensional.
- Interferência em colisão e alcance.

#### ✅ Vantagens

- Controle absoluto de posicionamento.
- Ignora obstáculos físicos.
- Sinergia com qualquer elemento.
- Permite fuga, acesso e manipulação avançada.
- Alto valor tático e utilitário.

#### ❌ Desvantagens

- Alto custo de essência.
- Erros causam falhas graves.
- Pouco impacto direto em dano.
- Exige leitura precisa do espaço.
- Forte counter por ancoragem dimensional.

#### 🪄 Formas de Uso

- **Deslocamento:** Teleporte curto ou médio, Troca de posição entre entidades, Retorno a ponto marcado, Deslocamento em cadeia.
- **Distorção Espacial:** Alongar ou comprimir distância, Curvar trajetórias, Criar dobras temporárias, Alterar alcance efetivo.
- **Ancoragem:** Impedir teleporte inimigo, Fixar entidades no espaço, Estabilizar estruturas instáveis, Selar fendas.
- **Uso Alquímico:** Núcleos de teleporte, Rituais de transição, Armazenamento extradimensional, Catalisador de fusões avançadas.

#### 😵 Efeitos de Status Associados

- **Desancorado:** Colisão instável.
- **Deslocado:** Posição alterada forçadamente.
- **Faseado:** Ignora colisão parcial.
- **Ruptura:** Falha espacial iminente.

#### ✨ Essência de Éter

- **Métodos de Obtenção:**
  - **Ambiental:** Fendas espaciais, Regiões liminares, Pontos de transição entre biomas, Eventos dimensionais.
  - **Criaturas:** Entidades interplanares, Criaturas faseadas, Guardiões de portais.
  - **Estruturas:** Portais antigos, Observatórios dimensionais, Ruínas deslocadas.
  - **Alquimia:** Condensação de espaço, Extração de energia de transição, Estabilização dimensional. (Risco: Falha gera deslocamento aleatório).
- **Classificação da Essência:**
  - **Difusa:** Instável, imprecisa.
  - **Estável:** Uso padrão.
  - **Transcendente:** Rara, altamente precisa.
- **Comportamento Sistêmico:**
  - Uso contínuo enfraquece limites espaciais.
  - Pode gerar fendas espontâneas.
  - Aumenta spawn de entidades interplanares.
  - Interfere com teleporte e colisão locais.

---

### 🕳️ Vazio

**Conceito:** Elemento de anulação, remoção de existência e silêncio sistêmico.

> **Definição Funcional:** Ele não causa dano tradicional; ele interrompe, apaga ou invalida entidades, efeitos e regras locais.

**Tipo de Efeito:**

- Anulação de entidades e projéteis.
- Supressão total de magia.
- Drenagem de existência.
- Criação de zonas neutras.

#### ✅ Vantagens

- Ignora resistências e armadura.
- Forte contra buffs, invocações e magia.
- Remove efeitos em vez de competir com eles.
- Alto controle em situações críticas.
- Escala bem contra builds complexas.

#### ❌ Desvantagens

- Custo extremamente alto.
- Uso limitado.
- Impacto ambiental permanente.
- Pode afetar o próprio usuário.
- Forte penalidade narrativa e sistêmica.

#### 🪄 Formas de Uso

- **Anulação:** Cancelar feitiços ativos, Apagar projéteis, Dissolver invocações, Remover buffs e debuffs.
- **Drenagem de Existência:** Redução de vida máxima, Erosão de atributos, Consumo de entidades fracas, Enfraquecimento progressivo.
- **Zonas de Vazio:** Áreas sem magia, Supressão elemental total, Silêncio absoluto, Colapso de efeitos persistentes.
- **Uso Alquímico:** Núcleos de anulação, Rituais de apagamento, Selos de silêncio, Contenção de entidades proibidas.

#### 😵 Efeitos de Status Associados

- **Anulado:** Efeitos removidos.
- **Esvaziado:** Redução de recursos máximos.
- **Silenciado:** Impossibilidade de conjurar.
- **Desexistência:** Dano direto à essência.

#### 🕳️ Essência de Vazio

- **Métodos de Obtenção:**
  - **Ambiental:** Fendas de inexistência, Regiões mortas, Bordas do mundo, Eventos de colapso.
  - **Criaturas:** Entidades do vazio, Aberrações não-existentes, Chefes apocalípticos.
  - **Estruturas:** Prisões dimensionais, Núcleos de contenção, Ruínas apagadas.
  - **Alquimia:** Remoção total de reagentes, Isolamento absoluto, Reações zeradas. (Risco: Falha apaga área ou usuário).
- **Classificação da Essência:**
  - **Eco:** Quase inútil.
  - **Nula:** Uso padrão.
  - **Abissal:** Extremamente rara.
- **Comportamento Sistêmico:**
  - Uso contínuo cria áreas irrecuperáveis.
  - Reduz spawn global.
  - Interrompe sistemas mágicos.
  - Pode gerar entidades paradoxais.

---

### ⏳ Tempo

**Conceito:** Elemento de controle de fluxo, ordenação de eventos e restrição causal.

> **Definição Funcional:** Ele não cria estados novos; ele altera a velocidade, a ordem ou a persistência do que já existe.

**Tipo de Efeito:**

- Aceleração e desaceleração.
- Pausa parcial.
- Reversão limitada.
- Extensão ou encurtamento de estados.

#### ✅ Vantagens

- Controle extremo de combate.
- Sinergia com todos os elementos.
- Permite correção de erro.
- Muito forte em execução precisa.
- Impacto alto mesmo sem dano.

#### ❌ Desvantagens

- Custo altíssimo.
- Janelas curtas.
- Forte restrição de uso.
- Risco de paradoxos.
- Penalidades acumulativas.

#### 🪄 Formas de Uso

- **Alteração de Fluxo:** Slow seletivo, Haste localizada, Acelerar cooldowns, Desacelerar projéteis.
- **Pausa Parcial:** Congelar entidades, Suspender efeitos, Travar áreas pequenas, Interromper eventos críticos.
- **Reversão Limitada:** Reverter posição recente, Cancelar último dano, Restaurar estado anterior curto, Voltar projéteis ao ponto inicial.
- **Manipulação de Duração:** Estender buffs/debuffs, Antecipar colapsos, Encurtar efeitos perigosos, Forçar expiração imediata.

#### 😵 Efeitos de Status Associados

- **Lento:** Redução de ações.
- **Acelerado:** Aumento de ações.
- **Congelado no Tempo:** Sem interação.
- **Eco Temporal:** Ação se repete.

#### ⏳ Essência de Tempo

- **Métodos de Obtenção:**
  - **Ambiental:** Fendas temporais, Ruínas fora de época, Eventos de descontinuidade, Zonas instáveis.
  - **Criaturas:** Entidades cronoafetadas, Guardiões temporais, Chefes paradoxais.
  - **Estruturas:** Relógios antigos, Observatórios temporais, Prisões de loop.
  - **Alquimia:** Destilação de eventos, Condensação de duração, Isolamento causal. (Risco: Falha cria loop ou aging forçado).
- **Classificação da Essência:**
  - **Residual:** Fraca, curta duração.
  - **Estável:** Uso padrão.
  - **Paradoxal:** Rara, alto risco.
- **Comportamento Sistêmico:**
  - Uso repetido gera resistência temporal.
  - Pode causar aging em entidades.
  - Cria ecos de eventos passados.
  - Interfere com éter e vazio.

---

## 7. Elementos Químicos 🧪

### ⚙️ Metal

**Conceito:** Elemento de condutividade, amplificação estrutural e resposta física.

> **Definição Funcional:** Ele não cria efeitos por si só; ele modifica como outros efeitos se propagam, resistem ou retornam.

**Tipo de Efeito:**

- Condução de energia (raio, calor, éter).
- Amplificação ou bloqueio de impacto.
- Reflexão e retorno de força.
- Interação direta com equipamentos.

#### ✅ Vantagens

- Sinergia forte com Raio, Fogo e Éter.
- Aumenta eficiência de equipamentos.
- Alta resistência física.
- Permite builds reativos (counter).
- Estável e previsível.

#### ❌ Desvantagens

- Peso e lentidão.
- Vulnerável à corrosão (água, ácido).
- Pode amplificar dano recebido errado.
- Baixa flexibilidade.
- Dependente de gear (equipamento).

#### 🪄 Formas de Uso

- **Condução:** Canalizar raio, Propagar calor, Redirecionar energia, Criar redes condutoras.
- **Amplificação:** Aumentar dano elemental, Estender alcance por condução, Intensificar efeitos contínuos, Focar energia em pontos específicos.
- **Defesa Reativa:** Placas refletivas, Retorno de dano, Dissipação controlada, Blindagem direcional.
- **Uso Alquímico:** Ligas elementais, Núcleos condutores, Catalisadores físicos, Selos de estabilidade.

#### 😵 Efeitos de Status Associados

- **Condutor:** Aumenta propagação de raio.
- **Aquecido:** Dano contínuo por calor.
- **Magnetizado:** Atração/repulsão.
- **Sobrecarregado:** Falha iminente.

#### ⚙️ Essência de Metal

- **Métodos de Obtenção:**
  - **Ambiental:** Veios minerais, Meteoritos, Regiões ricas em minério.
  - **Criaturas:** Construtos, Entidades metálicas, Mobs mecanizados.
  - **Estruturas:** Forjas antigas, Cidades industriais, Ruínas mecânicas.
  - **Alquimia:** Fusão e liga, Refino de impurezas, Condensação de metal vivo. (Risco: Falha gera colapso estrutural).
- **Classificação da Essência:**
  - **Bruta:** Comum.
  - **Refinada:** Uso padrão.
  - **Viva:** Rara, reage a energia.
- **Comportamento Sistêmico:**
  - Áreas saturadas atraem raio.
  - Interfere com campos magnéticos.
  - Amplifica efeitos ativos.
  - Pode causar sobrecarga em cadeia.

---

### 💎 Cristal

**Conceito:** Elemento de foco, estabilização e armazenamento.

> **Definição Funcional:** Ele não gera energia; ele organiza, mantém e direciona energia elemental sem perda.

**Tipo de Efeito:**

- Focalização de magia.
- Estabilidade de efeitos.
- Armazenamento de energia.
- Precisão e repetibilidade.

#### ✅ Vantagens

- Reduz custo de magia.
- Aumenta precisão.
- Mantém efeitos ativos por mais tempo.
- Permite pré-carregamento.
- Essencial para builds avançadas.

#### ❌ Desvantagens

- Frágil.
- Overload (sobrecarga) causa explosão.
- Dependente de pureza.
- Pouca utilidade isolada.
- Requer preparação.

#### 🪄 Formas de Uso

- **Foco:** Reduz dispersão, Aumenta alcance efetivo, Direciona efeitos complexos, Elimina ruído mágico.
- **Estabilização:** Evita falha alquímica, Mantém efeitos contínuos, Reduz variância, Previne colapso.
- **Armazenamento:** Spell (feitiço) pré-carregado, Essência encapsulada, Cargas reutilizáveis, Liberação sob gatilho.
- **Amplificação Controlada:** Boost (reforço) previsível, Sinergia com Luz, Éter e Tempo, Multiplicação sem caos, Escalonamento limpo.

#### 😵 Efeitos de Status Associados

- **Sintonizado:** Maior eficiência.
- **Ressonante:** Efeito repetido.
- **Instável:** Quebra iminente.
- **Saturado:** Não aceita mais carga.

#### 💎 Essência de Cristal

- **Métodos de Obtenção:**
  - **Ambiental:** Geodos, Cavernas cristalinas, Linhas de energia.
  - **Criaturas:** Entidades cristalizadas, Guardiões de foco, Mobs de ressonância.
  - **Estruturas:** Torres arcanas, Câmaras de foco, Altares de estabilização.
  - **Alquimia:** Cristalização de essência, Resfriamento controlado. (Risco: Falha gera fragmentação caótica).
- **Classificação da Essência:**
  - **Impura:** Baixa eficiência.
  - **Clara:** Padrão.
  - **Perfeita:** Alta estabilidade.
- **Comportamento Sistêmico:**
  - Amplifica o que toca.
  - Mantém estados ativos.
  - Reage a sobrecarga.
  - Pode entrar em ressonância em cadeia.

---

### 🧪 Veneno

**Conceito:** Elemento de degradação progressiva e interferência biológica.

> **Definição Funcional:** Ele não causa burst (explosão de dano); ele quebra o alvo ao longo do tempo, ignorando defesa bruta.

**Tipo de Efeito:**

- Dano contínuo (DoT).
- Acúmulo por stacks (camadas).
- Enfraquecimento gradual.
- Persistência após combate.

#### ✅ Vantagens

- Escala com duração.
- Ignora parte da armadura.
- Forte contra alvos grandes.
- Mantém pressão fora do combate direto.
- Sinergia com controle.

#### ❌ Desvantagens

- Resultado lento.
- Fraco contra mortos-vivos e máquinas.
- Pode ser purificado.
- Requer aplicação contínua.
- Baixo impacto imediato.

#### 🪄 Formas de Uso

- **Aplicação Direta:** Golpes, Projéteis, Nuvens, Contato com superfície.
- **Acúmulo:** Stacks aumentam dano, Stacks destravam efeitos, Limite máximo controlado, Decaimento gradual.
- **Interferência:** Redução de cura, Redução de stamina, Atraso de ações, Penalidade progressiva.
- **Persistência Ambiental:** Áreas contaminadas, Água envenenada, Terreno tóxico, Armadilhas.

#### 😵 Efeitos de Status Associados

- **Envenenado:** Dano por tempo.
- **Contaminado:** Debuff ampliado.
- **Colapso Orgânico:** Falha de cura.
- **Saturado:** Limite de stacks atingido.

#### 🧪 Essência de Veneno

- **Métodos de Obtenção:**
  - **Ambiental:** Plantas tóxicas, Fungos, Pântanos, Carcaças.
  - **Criaturas:** Aranhas, Cobras, Entidades tóxicas.
  - **Estruturas:** Laboratórios, Covis, Ruínas contaminadas.
  - **Alquimia:** Destilação orgânica, Concentração química. (Risco: Falha gera mutação hostil).
- **Classificação da Essência:**
  - **Fraca:** Curta duração.
  - **Concentrada:** Padrão.
  - **Virulenta:** Efeito colateral.
- **Comportamento Sistêmico:**
  - Stacks interagem com Tempo.
  - Água pode diluir.
  - Fogo pode neutralizar ou volatilizar.
  - Cristal estabiliza.

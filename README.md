# LOLChampions App

Este é um aplicativo Android desenvolvido usando Jetpack Compose. O objetivo deste app é fornecer uma interface simples com opções para carregar personagens do jogo **League of Legends** e uma funcionalidade fictícia para partidas 5x5.

## Funcionalidades

- **Carregar Personagens**: Um botão que, ao ser clicado, executa a ação de carregar os personagens do jogo.
- **5 x 5**: Um botão que simula a navegação ou ação de iniciar uma partida fictícia de 5 contra 5.

## Estrutura do Projeto

### `MainActivity`

O ponto de entrada da aplicação, onde:

- Habilitamos o modo **Edge-to-Edge** para a interface do usuário.
- Chamamos o tema `LOLChampionsTheme` para aplicar o estilo visual da aplicação.
- Inicializamos o conteúdo da tela por meio do método `setContent {}`.

### `MainScreen`

Essa função é o núcleo da interface do usuário e inclui:

- **Imagem de Fundo**: Uma imagem de fundo carregada de forma assíncrona via **Coil** (`rememberAsyncImagePainter`).
- **Sobreposição Semi-transparente**: Uma camada preta semi-transparente sobre a imagem de fundo para melhorar a visibilidade dos elementos.
- **Botões Interativos**:
    - `Carregar Personagens`: Executa a ação associada à função `onLoadCharactersClick`.
    - `5 X 5`: Executa a ação associada à função `onFiveXFiveClick`.

### Prévia

O método `@Preview` é utilizado para exibir uma prévia da interface diretamente no editor.

## Tecnologias Utilizadas

- **Jetpack Compose**: Framework moderno da Google para a construção de interfaces nativas no Android.
- **Material 3**: Utilizado para estilização de botões e textos.
- **Coil**: Biblioteca para carregamento de imagens assíncronas.
- **Kotlin**: Linguagem de programação utilizada para todo o projeto.

## Como Executar o Projeto

1. Clone o repositório:
   ```bash
   git clone https://github.com/username/lolchampions.git

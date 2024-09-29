# LOL Champions

Este aplicativo Android é um catálogo interativo que permite aos usuários visualizar informações detalhadas sobre os campeões do jogo League of Legends. Usando uma combinação de Jetpack Compose para a UI e uma arquitetura de navegação simples, o aplicativo consulta uma API para buscar dados dos campeões e apresenta-os de maneira limpa e funcional.

## Características

- **Interface moderna**: Construído com Jetpack Compose, oferecendo uma experiência fluída e responsiva.
- **Navegação integrada**: Utiliza a biblioteca de navegação do Compose para transições entre as telas.
- **Consulta a API**: Busca informações dos campeões de uma API externa e as exibe de forma organizada.

## Instalação

Para executar o projeto, siga estes passos:

1. Clone o repositório para sua máquina local usando:
2. Abra o projeto no Android Studio.
3. Sincronize o Gradle e aguarde a conclusão da configuração.
4. Execute o aplicativo em um emulador ou dispositivo Android.

## Uso

Ao iniciar o aplicativo, você será apresentado a uma tela principal com um botão "Carregar Personagens". Ao clicar neste botão:

- Os dados dos campeões são buscados da API.
- Uma nova tela com a lista de campeões é exibida.
- Clicar em um campeão na lista abre uma tela de detalhes, onde mais informações sobre o campeão selecionado são mostradas.

## Estrutura do Projeto

A estrutura do código é organizada como segue:

- `MainActivity`: Configuração inicial do tema e navegação.
- `Character`: Modelo de dados para os campeões.
- `fetchCharacters`: Função que realiza a chamada à API e processa os dados recebidos.
- `MainScreen`, `CharacterListScreen`, `CharacterDetailScreen`: Composables que definem as telas do aplicativo.
- `AppNavigation`: Gerenciamento da navegação entre as telas do app.

## Contribuições

Contribuições são bem-vindas! Para contribuir, por favor abra um pull request com suas sugestões de melhorias ou correções.

## Licença

Este projeto é distribuído sob a licença MIT. Veja o arquivo `LICENSE` para mais detalhes.
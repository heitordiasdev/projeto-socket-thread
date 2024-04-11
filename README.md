# Projeto Cliente-Servidor em Java e JavaFX

Este projeto é uma aplicação cliente-servidor simples implementada em Java e JavaFX, utilizando sockets para comunicação.

## Requisitos

- Java 8 ou superior
- JavaFX

## Como executar

1. Clone o repositório para sua máquina local.
2. Navegue até a pasta do projeto via terminal.
3. Compile e execute o servidor:
    ```bash
    javac Servidor.java
    java Servidor
    ```
4. Em um novo terminal, compile e execute o cliente:
    ```bash
    javac Cliente.java
    java Cliente
    ```

## Funcionalidades

- O servidor aceita múltiplas conexões de clientes simultaneamente.
- Os clientes podem fazer upload, fazer download e deletar imagens do servidor.
- O servidor recebe e processa todas as acoes que o cliente solicitar.

## Estrutura do projeto

- `Servidor.java`: Este é o arquivo principal do servidor. Ele lida com a criação do socket do servidor e a aceitação de conexões de clientes.
- `Cliente.java`: Este é o arquivo principal do cliente. Ele lida com a criação do socket do cliente e a comunicação com o servidor.
- `imagens`: Esta é a pasta onde é encontrado todas as imagens.
## Contribuindo

Contribuições são bem-vindas! Por favor, leia as diretrizes de contribuição antes de começar.

## Licença

Este projeto ainda não está licenciado sob a licença MIT.

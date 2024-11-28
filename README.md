---
autor: Analice Leite
data: 2024-10-2024
tags:
- Documentação
- API
- Java
- Spring-Boot
versão: 1.0
---

\#Autor/AnaliceLeite \#Versão/1 \#Data/2024-10-24

## Descrição da API

A API de Jogos, desenvolvida com **Spring Boot**, **JPA** e **Docker** (usando PostgreSQL), permite realizar operações **CRUD** em uma base de dados de jogos. Ela organiza informações como nome, descrição, preços e categorias, retornando dados em formato JSON, ideal para integrar jogos em aplicações e facilitar a comunicação entre front-end e back-end.

## Docker Setup

A aplicação utiliza o **PostgreSQL** como banco de dados, e para facilitar o desenvolvimento, utilizei o Docker para configurar o ambiente. Abaixo estão os detalhes sobre como configurar o banco de dados PostgreSQL usando uma imagem Docker.

### Docker Compose

O arquivo `docker-compose.yml` inclui a configuração do serviço de banco de dados PostgreSQL. A configuração mapeia a porta do banco de dados para a porta local e define as variáveis de ambiente necessárias para o PostgreSQL, como o usuário, senha e o nome do banco de dados.

Aqui está o arquivo `docker-compose.yml` utilizado para configurar o serviço de banco de dados:

``` yml
services:
  db:
    image: postgres:13.0-alpine
    ports:
      - "5433:5432"  # Mapeia a porta 5432 do contêiner para a porta 5433 no localhost
    volumes:
      - postgres_data:/var/lib/postgresql/data  # Armazena os dados no volume local para persistência
    environment:
      POSTGRES_USER: "api_games"  # Usuário do banco de dados
      POSTGRES_PASSWORD: "api_games"  # Senha do banco de dados
      POSTGRES_DB: "db_api_games"  # Nome do banco de dados
    networks:
      - backend  # Rede para comunicação entre containers
    deploy:
      resources:
        limits:
          memory: 500M  # Limitação de memória do container
        reservations:
          memory: 50M  # Reserva mínima de memória para o container

volumes:
  postgres_data:  # Volume para persistência dos dados do banco de dados

networks:
  backend:
    driver: bridge  # Rede de comunicação entre containers usando o driver bridge
```

### Passos para Executar o Docker Compose

1.  **Criar o arquivo** `docker-compose.yml` na raiz do projeto, com o conteúdo acima;
2.  **Executar o Docker Compose**:

``` bash
docker-compose up --build
```

Esse comando irá iniciar o contêiner do PostgreSQL em segundo plano.

3.  **Verificar se o PostgreSQL está rodando corretamente**: Após executar o Docker Compose, você pode verificar se o banco de dados está em execução com o seguinte comando:

``` bash
docker ps
```

### Conectando ao Banco de Dados com o DBeaver

Uma vez que o banco de dados PostgreSQL está em execução no Docker, siga as instruções abaixo:

1.  **Abrir o DBeaver** e clicar em "Nova Conexão".
2.  Escolher **PostgreSQL** como o tipo de banco de dados.
3.  Preencher as informações de conexão:
    - **Host**: `localhost` (ou `127.0.0.1`)
    - **Porta**: `5433` (a porta do PostgreSQL mapeada no Docker)
    - **Banco de Dados**: `db_api_games`
    - **Usuário**: `api_games`
    - **Senha**: `api_games`
4.  **Testar a Conexão** clicando em "Testar Conexão". Se tudo estiver configurado corretamente, o DBeaver exibirá uma mensagem de sucesso.
5.  **Conectar** ao banco de dados para começar a explorar e gerenciar os dados da API.

### Como Funciona a Comunicação com o PostgreSQL

Com o Docker configurado e o DBeaver conectado ao banco de dados, a aplicação da API de Jogos pode se comunicar diretamente com o PostgreSQL. As variáveis de ambiente definidas no Docker Compose (`POSTGRES_USER`, `POSTGRES_PASSWORD`, `POSTGRES_DB`) são usadas pela aplicação para conectar-se ao banco de dados.

A configuração do arquivo `application.properties` da aplicação ficou da seguinte forma:

``` properties
spring.datasource.url=jdbc:postgresql://localhost:5433/db_api_games spring.datasource.username=api_games 
spring.datasource.password=api_games
```

### Notas Importantes

- **Persistência de Dados**: O volume `postgres_data` é usado para garantir que os dados do banco de dados não sejam perdidos quando o contêiner for reiniciado.
- **Segurança**: Em um ambiente de produção, é recomendável não usar senhas fracas ou públicas, como feito no exemplo. Considere usar variáveis de ambiente seguras ou ferramentas de gerenciamento de segredos.

## Rodando a API localmente

Você pode utilizar qualquer SGBD da sua preferência, eu realizei os testes com DBeaver e Postgres.

Depois que o contêiner com o banco de dados estiver em execução, você pode configurar e iniciar a aplicação com os seguintes passos:

1.  Na raiz do projeto, compile e instale as dependências executando:

``` bash
mavn clean install
```

2.  Em seguida, inicie a aplicação com o comando:

``` bash
mvn spring-boot:run
```

3.  Após o início da aplicação, as requisições podem ser feitas utilizando o endereço base:

``` bash
http://localhost:8080/api/games
```

## 1. Criar um Jogo

- **Método:** `POST`
- **Endpoint:** `/api/games`
- **Descrição:** Cria um novo jogo no banco de dados.
- **Corpo da Requisição:**

``` java
{     
    "name": "FIFA 23",
    "description": "O FIFA 23 é o mais recente lançamento da renomada série de jogos de futebol da EA Sports...",
    "releaseDate": "11/23/2022",
    "discount": 10.00,
    "oldPrice": 100.00,
    "category": "Esporte",
    "plataform": "PS5"
}
```

- **Resposta de Sucesso:**

``` java
{     
    "status": 201,     
    "message": "Jogo criado com sucesso!",    
    "data": {
        "id": 1,
        "name" "FIFA 23"
        ...
    }
}
```

## 2. Listar Todos os Jogos

- **Método:** `GET`
- **Endpoint:** `/api/games`
- **Descrição:** Retorna uma lista de todos os jogos no banco de dados.
- **Resposta de Sucesso:**

``` java
{
    "status": 200,
    "message": "Jogos listados com sucesso.",
    "data": [
        {
            "id": 1,
            "name": "FIFA 18",
            "description": "Jogo de futebol",
            "releaseDate": "2022-11-23",
            "discount": 10.0,
            "oldPrice": 100.0,
            "currentPrice": 90.0,
            "category": "Esporte",
            "platform": "PS5"

        },
        {
            "id": 2,
            "name": "Call of Duty: Modern Warfare",
            ...
        }
    ]
}
       
```

## 3. Obter um Jogo por ID

- **Método:** `GET`
- **Endpoint:** `/api/games/{id}`
- **Descrição:** Retorna os detalhes de um jogo específico pelo ID.
- **Parâmetros:**
  - `id`: ID do jogo (ex: 1)
- **Resposta de Sucesso:**

``` java
{     
    "status": 200,     
    "message": "Jogo encontrado.",    
    "data": {
        "id": 1,
        "name" "FIFA 23"
        ...
    }
}
```

- **Resposta de Erro (Jogo Não Encontrado):**

``` java
{ 
    "status": 404, 
    "message": "Jogo não encontrado!", 
    "data": null
}
```

## 4. Atualizar um Jogo

- **Método:** `PUT`
- **Endpoint:** `/api/games/{id}`
- **Descrição:** Atualiza os detalhes de um jogo específico pelo ID.
- **Parâmetros:**
  - `id`: ID do jogo (ex: 1)

**Corpo da Requisição:**

``` java
{ 
    "name": "FIFA 23", 
    "description": "Descrição atualizada...", 
    "releaseDate": "2024-10-01", 
    "discount": 10.0, "old_price": 219.9, 
    "currentPrice": 199.9, 
    "category": "Esporte", 
    "plataform": "PS5"
}
```

- **Resposta de Sucesso:**

``` java
{     
    "status": 200,     
    "message": "Jogo atualizado com sucesso!",    
    "data": {
        "id": 1,
        "name" "FIFA 23"
        "description": "Descrição atualizada..."
        ...
    }
}
```

- **Resposta de Erro (Jogo Não Encontrado):**

``` java
{ 
    "status": 404, 
    "message": "Jogo não encontrado!", 
    "data": null
}
```

## 5. Excluir um Jogo

- **Método:** `DELETE`
- **Endpoint:** `/api/games/{id}`
- **Descrição:** Exclui um jogo específico pelo ID.
- **Parâmetros:**
  - `id`: ID do jogo (ex: 1)
- **Resposta de Sucesso:**

``` java
{ 
    "status": 204, 
    "message": "Sem conteúdo, exclusão realizada.", 
    "data": null
}
```

- **Resposta de Erro (Jogo Não Encontrado):**

``` java
{ 
    "status": 404, 
    "message": "Jogo não encontrado!", 
    "data": null
}
```
## Swagger

Para visualizar a documentação da API utilizando o Swagger, basta acessar esse endpoint:

``` bash
http://localhost:8080/swagger-ui/index.html
```

## Implementações Futuras

- **Autenticação:** Autenticação para proteger os endpoints, especialmente os métodos `POST`, `PUT` e `DELETE`.
- **Paginação:** Paginação para a listagem de jogos, tendo em vista aumento no volume de dados.

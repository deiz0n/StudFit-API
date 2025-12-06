# StudFit API

API back-end para gerenciamento da academia do IFCE campus Cedro, contemplando cadastro de alunos, autenticação via JWT, controle de presenças, horários e integração com serviços da AWS. A aplicação permite que usuários gerenciem alunos, realizem upload de atestados, controlem frequências e exportem dados, garantindo segurança, rastreabilidade e escalabilidade.

## Índice

1. [Tecnologias Utilizadas](#tecnologias-utilizadas)
2. [Pré-requisitos](#pré-requisitos)
3. [Configurações Iniciais](#configurações-iniciais)
4. [Executando a API](#executando-a-api)
5. [Documentação](#documentação)

## Tecnologias Utilizadas

<div>
  <img
    align="left"
    alt="Java"
    title="Java"
    width="40px"
    style="padding-right: 10px;"
    src="https://github.com/devicons/devicon/blob/v2.16.0/icons/java/java-original.svg"
/>
<img
    align="left"
    alt="Spring"
    title="Spring"
    width="40px"
    style="padding-right: 10px;"
    src="https://github.com/devicons/devicon/blob/v2.16.0/icons/spring/spring-original.svg"
/>
<img
    align="left"
    alt="PostgreSQL"
    title="PostgreSQL"
    width="40px"
    style="padding-right: 10px;"
    src="https://github.com/devicons/devicon/blob/v2.16.0/icons/postgresql/postgresql-original.svg"
/>
    <img
    align="left"
    alt="Redis"
    title="Redis"
    width="40px"
    style="padding-right: 10px;"
    src="https://github.com/devicons/devicon/blob/v2.16.0/icons/redis/redis-original.svg"
/>
<img
    align="left"
    alt="Docker"
    title="Docker"
    width="40px"
    style="padding-right: 10px;"
    src="https://github.com/devicons/devicon/blob/v2.16.0/icons/docker/docker-original.svg"
/>
<img
    align="left"
    alt="GitHub Actions"
    title="GitHub Actions"
    width="40px"
    style="padding-right: 10px;"
    src="https://github.com/devicons/devicon/blob/v2.16.0/icons/githubactions/githubactions-original.svg"
/>
<img
    align="left"
    alt="AWS"
    title="AWS"
    width="40px"
    style="padding-right: 10px;"
    src="https://github.com/devicons/devicon/blob/v2.16.0/icons/amazonwebservices/amazonwebservices-original-wordmark.svg"
/>
<img
    align="left"
    alt="Maven"
    title="Maven"
    width="40px"
    style="padding-right: 10px;"
    src="https://github.com/devicons/devicon/blob/v2.16.0/icons/maven/maven-original-wordmark.svg"
/>
<img
    align="left"
    alt="JUnit"
    title="JUnit"
    width="40px"
    style="padding-right: 10px;"
    src="https://github.com/devicons/devicon/blob/v2.16.0/icons/junit/junit-original-wordmark.svg"
/>
<img
    align="left"
    alt="Swagger"
    title="Swagger"
    width="40px"
    style="padding-right: 10px;"
    src="https://github.com/devicons/devicon/blob/v2.16.0/icons/swagger/swagger-original.svg"
/>
<img
    align="left"
    alt="Postman"
    title="Postman"
    width="40px"
    style="padding-right: 10px;"
    src="https://github.com/devicons/devicon/blob/v2.16.0/icons/postman/postman-original.svg"
/>
</div>

<br></br>

## Pré-requisitos

Para executar este projeto, você precisará ter instalado:

- **Git** - Para clonar o repositório
- **Java 17** ou superior
- **Maven 3.6+** - Para gerenciar dependências
- **Docker e Docker Compose** (Opcional, mas recomendado)
- **PostgreSQL** (caso não use Docker)
- **Redis** (caso não use Docker)

## Configurações Iniciais

### 1. Clonando o Repositório

Clone o repositório executando o seguinte comando no terminal:

```bash
git clone https://github.com/deiz0n/StudFit-API
cd StudFit-API
```

### 2. Configurando as Variáveis de Ambiente

Crie um arquivo `.env` com base no arquivo `.env.example` e preencha com suas credenciais



**⚠️ IMPORTANTE:**
- As portas `5432` para o PostgreSQL e `587` para servidor SMTP devem está disponíveis
- Todas as credenciais informadas são confidenciais e serão utilizadas apenas para o funcionamento da aplicação
- Certifique-se de preencher todos os campos obrigatórios para garantir o correto funcionamento da API

### 3. Instalando as Dependências

Se você não estiver usando Docker, instale as dependências do projeto executando:

```bash
mvn clean install
```

## Executando a API

### Opção 1: Executando com Docker Compose (Recomendado)

Esta opção iniciará automaticamente o banco de dados PostgreSQL, Redis e a aplicação:

```bash
docker-compose up --build
```

A API estará disponível em: `http://localhost:80`

Para parar os containers:

```bash
docker-compose down
```

### Opção 2: Executando Localmente

Certifique-se de que o PostgreSQL e Redis estão rodando localmente, então execute:

```bash
mvn spring-boot:run
```

Ou, após buildar o projeto:

```bash
java -jar target/studfit-0.0.1-SNAPSHOT.jar
```

A API estará disponível em: `http://localhost:{API_PORT}`

### Opção 3: Executando apenas com Docker

Construa a imagem Docker:

```bash
docker build -t studfit-api .
```

Execute o container:

```bash
docker run --env-file .env -p 8080:8080 studfit-api
```

## Documentação

A API possui documentação interativa gerada automaticamente com Swagger/OpenAPI.

Após iniciar a aplicação, acesse:

```
http://localhost:{API_PORT}/swagger-studfit.html
```

Ou para acessar a especificação JSON da API:

```
http://localhost:{API_PORT}/v3/api-docs
```

## Estrutura do Projeto

```
src/
├── main/
│   ├── java/com/deiz0n/studfit/
│   │   ├── controllers/          # Controladores REST
│   │   ├── domain/                # Entidades, DTOs, Enums
│   │   ├── infrastructure/        # Configurações, Segurança, Repositórios
│   │   ├── services/              # Lógica de negócio
│   │   └── StudfitApplication.java
│   └── resources/
│       ├── application.properties
│       ├── db/migration/          # Scripts Flyway
│       └── templates/             # Templates de e-mail
└── test/
    └── java/com/deiz0n/studfit/
        ├── integration/           # Testes de integração
        └── unit/                  # Testes unitários
```

## Suporte

Para problemas, dúvidas ou sugestões, abra uma issue no repositório do projeto.

---

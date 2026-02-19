# User API

API REST simples feita com Spring Boot para cadastro e consulta de usuários.

## Tecnologias usadas
- Java
- Spring Boot
- Spring Data JPA
- Spring Validation
- Spring Security (Basic Auth)
- MySQL
- Maven
- JUnit e Mockito

## Como rodar o projeto

1. Criar o banco:

```
CREATE DATABASE userapi;
```

2. Configurar o application.properties com seu usuário e senha do MySQL.

3. Rodar a aplicação:

```
mvn spring-boot:run
```

## Autenticação
A API usa Basic Auth em memória:

- usuário: admin
- senha: admin123

## Endpoints principais

### Criar usuário
POST `/users`

### Listar usuários
GET `/users`

Pode filtrar por nome:
GET `/users?name=pedro`

### Buscar por id
GET `/users/{id}`

## Observação
O envio de e-mail é apenas simulado (aparece no console). Foi feito assim
somente para demonstrar a regra de negócio e a transação.

## Testes
Para rodar os testes:

```
mvn test
```

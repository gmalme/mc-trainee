# MCT Back-end (Quarkus)

Este projeto é um simulador de CRUD de Usuários e Tarefas, desenvolvido com Quarkus (Java 21).

## Stack
- Quarkus 3.8.3 (Java 21)
- RESTEasy Reactive (JAX-RS)
- Hibernate ORM + Panache
- Jakarta Bean Validation
- Flyway (Migrações)
- SmallRye OpenAPI (Swagger)
- PostgreSQL

## Como rodar

### Usando Docker Compose
Para subir o banco de dados e a aplicação:
```bash
docker-compose up --build
```

### Modo Desenvolvimento (Quarkus Dev)
1. Suba apenas o banco:
```bash
docker-compose up db
```
2. Rode a aplicação:
```bash
./mvnw quarkus:dev
```

Acesse o Swagger em: `http://localhost:8080/q/swagger-ui`

## Principais Rotas

### Usuários
- `POST /users`: Cria um novo usuário
- `GET /users`: Lista todos os usuários
- `GET /users/{userId}`: Detalhes de um usuário

### Tarefas (Vinculadas ao Usuário)
- `POST /users/{userId}/tasks`: Cria uma tarefa para o usuário
- `GET /users/{userId}/tasks`: Lista tarefas do usuário (suporta query param `?status=OPEN`)
- `GET /users/{userId}/tasks/{taskId}`: Detalhes de uma tarefa
- `PUT /users/{userId}/tasks/{taskId}`: Atualiza uma tarefa
- `DELETE /users/{userId}/tasks/{taskId}`: Remove uma tarefa

## Exemplos de CURL

### 1. Criar um Usuário
```bash
curl -X POST http://localhost:8080/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "João Silva",
    "email": "joao@email.com",
    "username": "joaosilva",
    "password": "123",
    "role": "USER"
  }'
```

### 2. Criar uma Tarefa para o Usuário
(Substitua `{userId}` pelo ID retornado na criação do usuário)
```bash
curl -X POST http://localhost:8080/users/{userId}/tasks \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Minha primeira tarefa",
    "description": "Descrição detalhada da tarefa",
    "status": "OPEN"
  }'
```

### 3. Atualizar uma Tarefa
(Substitua `{userId}` e `{taskId}`)
```bash
curl -X PUT http://localhost:8080/users/{userId}/tasks/{taskId} \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Título Atualizado",
    "description": "Descrição atualizada",
    "status": "DONE"
  }'
```

### 4. Listar Tarefas com Filtro
```bash
curl http://localhost:8080/users/{userId}/tasks?status=OPEN
```

## Regras de Negócio Implementadas
- Validação de entrada com Bean Validation (400 Bad Request).
- Não é permitido criar tarefas com o mesmo título para o mesmo usuário (409 Conflict).
- Tratamento global de erros com payload padronizado.
- Migrações automáticas de banco de dados com Flyway.

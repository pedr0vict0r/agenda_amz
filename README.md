# Agenda AMZ

Aplicação web de agenda de contatos construída com Spring Boot + Thymeleaf.

## Propósito

Gerenciar contatos com operações básicas de CRUD:

- cadastrar contato
- listar contatos
- visualizar detalhes
- editar contato
- excluir contato

## Stack

- Java 11
- Spring Boot 2.7.x
- Spring MVC + Spring Data JPA + Bean Validation
- Thymeleaf
- Banco:
  - `dev` (padrão): H2 em memória
  - `mysql`: MySQL via variáveis de ambiente
- Maven

## Escopo MVP (done)

- CRUD completo funcional com validação de campos obrigatórios
- Persistência via JPA
- Feedback de sucesso/erro em ações principais
- Perfis de configuração para execução local segura e para MySQL

## Configuração de banco

### Perfil padrão (`dev`)

Não requer banco externo. Usa H2 em memória automaticamente.

### Perfil MySQL (`mysql`)

Defina variáveis de ambiente:

- `DB_URL` (ex.: `jdbc:mysql://localhost:3306/agenda_amz?useSSL=false&serverTimezone=UTC`)
- `DB_USERNAME`
- `DB_PASSWORD`

Execute com:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=mysql
```

## Como executar

```bash
mvn spring-boot:run
```

Aplicação: `http://localhost:8080`

## Build e testes

```bash
mvn clean verify
```

```bash
mvn test
```

## Roadmap (não-MVP)

- busca/filtro de contatos
- paginação da listagem
- autenticação/autorização
- API REST para integração externa

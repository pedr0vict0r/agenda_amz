# Agenda AMZ

![CI](https://github.com/pedr0vict0r/agenda_amz/actions/workflows/ci.yml/badge.svg?branch=main)

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

## Novos recursos implementados

- busca/filtro de contatos na listagem
- paginação da listagem de contatos
- API REST em `/api/contatos`
- autenticação/autorização para operações de escrita
- logs de ações críticas (criação, edição, exclusão e tentativas inválidas)

## Configuração de segurança (escrita)

Operações de escrita (UI e API) exigem autenticação com perfil `EDITOR`.

Variáveis opcionais:

- `APP_SECURITY_USERNAME` (padrão: `admin`)
- `APP_SECURITY_PASSWORD` (padrão: `admin123`)

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

## API REST

Base: `http://localhost:8080/api/contatos`

- `GET /api/contatos?nome=&page=0&size=10`
- `GET /api/contatos/{codigo}`
- `POST /api/contatos` *(autenticado)*
- `PUT /api/contatos/{codigo}` *(autenticado)*
- `DELETE /api/contatos/{codigo}` *(autenticado)*

Exemplo de criação:

```bash
curl -u admin:admin123 -X POST http://localhost:8080/api/contatos \
  -H 'Content-Type: application/json' \
  -d '{"nome":"Maria","numero":"(11) 99999-9999"}'
```

## Build e testes

```bash
mvn clean verify
```

```bash
mvn test
```

## CI/CD e branch principal

- CI executa em push e PR para `main`
- workflow de release gera notas automáticas para tags `v*.*.*`
- branch padrão do projeto: `main`

## Troubleshooting

### `dev` (H2)

- erro de porta ocupada: execute em outra porta (`SERVER_PORT=8081`)
- limpar estado local: reinicie a aplicação (H2 é em memória)
- acessar console H2: `/h2-console`

### `mysql`

- falha de conexão: validar `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`
- timezone/SSL: manter parâmetros `useSSL=false&serverTimezone=UTC`
- erro de dialeto: confirmar driver `com.mysql.cj.jdbc.Driver`

## Governança de contribuição

Consulte [CONTRIBUTING.md](CONTRIBUTING.md) para:

- regras de PR com base em `main`
- proteções recomendadas de branch
- política de versionamento/release

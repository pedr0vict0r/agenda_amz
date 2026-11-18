# Contribuindo para Agenda AMZ

## Branch padrão

A branch padrão do projeto é `main`.

## Estratégia de branches

- Branches de trabalho devem sair de `main`.
- Pull requests devem ter `main` como base.
- Não usar `master` em links, scripts ou workflows.

## Proteções recomendadas para `main`

Configurar no GitHub (Settings > Branches):

- exigir pull request para merge
- bloquear push direto em `main`
- exigir aprovação de revisão
- exigir status checks de CI (`CI / test`)

## Critérios de validação local

Antes de abrir PR:

```bash
mvn test
mvn clean verify
```

## Versionamento e release

- Use versionamento semântico em tags: `vMAJOR.MINOR.PATCH`
- Releases são publicadas automaticamente ao criar tag `v*.*.*`
- As release notes são geradas automaticamente pelo GitHub Actions

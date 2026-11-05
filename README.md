# Sistema de Aluguel de Carros

Laboratório 02 da disciplina DIAW (Desenvolvimento e Integração de Aplicações Web), curso de Engenharia de Software, PUC Minas. Professor: João Paulo Carneiro Aramuni.

Aluno(a): Carolina Almeida Mendes de Souza

## Descrição

Sistema web de apoio à gestão de aluguéis de automóveis. Clientes cadastram-se, fazem login e introduzem, modificam, consultam e cancelam pedidos de aluguel. Agentes (empresas e bancos) analisam os pedidos, dão parecer financeiro e executam o contrato, que pode ter um contrato de crédito concedido por um banco.

## Tecnologias

| Camada | Tecnologia |
|---|---|
| Linguagem | Java (JDK 25) |
| Back-end | Spring Boot 4.0.x, arquitetura MVC, Spring Security, Spring Data JPA |
| Build | Apache Maven |
| Front-end | React + Vite |
| Banco de dados | Supabase (PostgreSQL) |
| Autenticação | login obrigatório com usuário e senha (HTTP Basic, senha com hash BCrypt) |

## Modelagem

Os diagramas estão em Mermaid (o GitHub desenha automaticamente):

- [Casos de uso](docs/01-casos-de-uso.md)
- [Histórias de usuário](docs/02-historias-de-usuario.md)
- [Diagrama de classes](docs/03-diagrama-de-classes.md)
- [Diagrama de pacotes](docs/04-diagrama-de-pacotes.md)

Quando o código ou o feedback em aula mudarem o modelo, os arquivos de `docs` devem ser atualizados e versionados junto.

## Funcionalidades

- cadastro e login de clientes; agentes têm cadastro prévio (feito pelos dados iniciais)
- CRUD de cliente com até 3 rendimentos
- cliente: criar, consultar (com status), modificar e cancelar pedidos
- agente: ver todos os pedidos, modificar, avaliar (parecer positivo ou negativo) e executar contrato
- banco: associar contrato de crédito ao contrato de aluguel
- agente: cadastrar automóveis

Status de um pedido: `EM_ANALISE` -> `APROVADO` ou `REPROVADO`; `APROVADO` -> `CONTRATADO`; `EM_ANALISE` ou `APROVADO` -> `CANCELADO`.

## Como rodar

### 1. Banco de dados (Supabase)

1. crie um projeto no [Supabase](https://supabase.com)
2. abra o SQL Editor, cole o conteúdo de `database/schema.sql` e execute
3. em Project Settings > Database > Connection string, copie os dados de conexão

### 2. Back-end

Precisa de JDK 25 e Maven. Defina as variáveis de ambiente (exemplo para a conexão do Supabase):

```bash
export SUPABASE_DB_URL="jdbc:postgresql://<host>:5432/postgres"
export SUPABASE_DB_USER="postgres"
export SUPABASE_DB_PASSWORD="<sua senha>"
```

Depois:

```bash
cd backend
mvn spring-boot:run
```

O servidor sobe em http://localhost:8080. Na primeira execução o sistema cria agentes e automóveis de exemplo (desligue com `DADOS_INICIAIS=false`).

Outras variáveis: `CORS_ORIGENS` (padrão `http://localhost:5173`).

### 3. Front-end

```bash
cd frontend
cp .env.example .env
npm install
npm run dev
```

Abra http://localhost:5173.

### Usuários para teste

| Perfil | Usuário | Senha |
|---|---|---|
| Empresa (agente) | empresa | empresa123 |
| Banco (agente) | banco | banco123 |
| Cliente | crie pela tela de cadastro | |

Essas senhas são só para desenvolvimento. Em produção, desligue os dados iniciais e troque as senhas.

## API

Todas as rotas exigem login (header `Authorization: Basic ...`), exceto o cadastro de cliente.

| Método | Rota | Quem | O que faz |
|---|---|---|---|
| GET | /api/auth/eu | logado | dados de quem está logado |
| POST | /api/clientes | público | cadastra cliente |
| GET | /api/clientes | agente | lista clientes |
| GET, PUT, DELETE | /api/clientes/{id} | agente ou o próprio cliente | consulta, altera, exclui |
| GET | /api/automoveis | logado | lista automóveis |
| POST | /api/automoveis | agente | cadastra automóvel |
| POST | /api/pedidos | cliente | cria pedido |
| GET | /api/pedidos | logado | cliente vê os seus, agente vê todos |
| GET, PUT | /api/pedidos/{id} | dono ou agente | consulta e modifica (só EM_ANALISE) |
| PATCH | /api/pedidos/{id}/cancelamento | cliente dono | cancela |
| PATCH | /api/pedidos/{id}/avaliacao | agente | parecer positivo ou negativo |
| POST | /api/pedidos/{id}/contrato | agente | executa contrato (e crédito, se for banco) |

## Estrutura do projeto

```
backend/    Spring Boot (controller, service, repository, model, dto, config, excecao)
frontend/   React + Vite (paginas, componentes, contexto, servicos)
database/   schema.sql do Supabase
docs/       modelagem UML e histórias de usuário
```

## Alinhamento entre modelo e código

| Modelo | Código |
|---|---|
| Classes do diagrama de classes | `backend/src/main/java/br/pucminas/aluguel/model` |
| Pacotes do diagrama de pacotes | pastas dentro de `backend/src/main/java/br/pucminas/aluguel` e `frontend/src` |
| Tabelas | `database/schema.sql` |

# 📚 Sistema de Gestão de Biblioteca - Full Stack

Este projeto é uma aplicação Full Stack completa desenvolvida para a disciplina de Programação Orientada a Objetos (POO). O sistema permite o gerenciamento de acervo, clientes, controle de empréstimos em tempo real e um avançado sistema de suporte via tickets integrado com notificações por e-mail, utilizando persistência de dados em banco relacional.

## 🚀 Funcionalidades Principais

- **Gestão de Acervo**: CRUD completo de livros com controle de disponibilidade automática.
- **Gestão de Clientes**: Cadastro, edição, remoção e manutenção de usuários da biblioteca.
- **Controle de Empréstimos**: Registro de saídas com vínculo entre o cliente e o livro, alterando o status do acervo instantaneamente em tempo real.
- **Módulo de Suporte (Tickets)**:
  - Criação e acompanhamento de tickets (Kanban interativo).
  - Atualização automática de status (`ABERTO`, `EM_ANALISE`, `CONCLUIDO`) e níveis de prioridade.
  - Sistema de interações/respostas dentro dos cards de cada ticket.
  - **Relatórios por E-mail**: Geração de resumos de tickets pendentes/abertos, enviados automaticamente via e-mail utilizando a API Rest do Resend.
- **Interface Moderna e Intuitiva**: Dashboard amplamente responsivo com suporte a Dark Mode integrado, construído em cima de Shadcn/UI.

## 🛠️ Tecnologias e Dependências Utilizadas

### Front-end
- **Framework Core**: Next.js (App Router)
- **Estilização**: Tailwind CSS
- **Biblioteca de Componentes UI**: Shadcn/UI (Radix UI)
- **Ícones**: Lucide React
- **Estado e Integração HTTP**: React Context API e chamadas Fetch nativas.

### Back-end (API REST)
- **Linguagem Base**: Java 21
- **Framework Principal**: Spring Boot 3.4.x
- **Persistência e ORM**: Spring Data JPA (Hibernate)
- **Driver de Banco**: MySQL Connector-J (MySQL 8.0+)
- **Utilitários**: Lombok (redução de código verboso/boilerplate)
- **Módulo de Comunicação**: Spring Boot Starter Mail & Integração com Resend HTTP API.
- **Segurança**: Políticas rigorosas de CORS aplicadas para as requisições oriundas do Next.js.

## 🔧 Pré-requisitos e Instalação

### 1. Preparando o Banco de Dados
- Certifique-se de que o gerenciador de banco de dados **MySQL 8.0+** esteja executando em sua máquina (na porta 3306 padrão).
- Crie um banco (schema) vazio correspondente ao configurado no back-end.

### 2. Configurando o Back-end
1. Navegue até a raiz do projeto Spring (`api-biblioteca`).
2. Abra e edite o arquivo `src/main/resources/application.properties` para alinhar suas credenciais do banco `spring.datasource.username` / `password` e, também, variáveis relativas aos e-mails.
3. Para iniciar a API (geralmente executando na porta `8080`), utilize sua IDE favorita (como IntelliJ ou VS Code) na classe `BibliotecaApplication`, ou via terminal embutido com Maven:
   ```bash
   ./mvnw spring-boot:run
   ```

### 3. Configurando o Front-end
1. Acesse o diretório do front-end (`/biblioteca`).
2. Realize a instalação de todos os pacotes das dependências do Node listados em `package.json`:
   ```bash
   npm install
   ```
3. Inicie o servidor Node de desenvolvimento:
   ```bash
   npm run dev
   ```
4. A aplicação agora estará disponível no seu navegador em `http://localhost:3000`.

## 👨‍💻 Autores

- **Bruno Lima Amaral** - Estudante de Engenharia/TI - 4º Semestre
- **Pedro Henrique Rodrigues dos Santos** - Estudante de Engenharia/TI - 4º Semestre

> *"Desenvolvido em uma maratona épica de código, superando desafios de integração e infraestrutura."*
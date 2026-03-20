📚 Sistema de Gestão de Biblioteca - Full Stack
Este projeto é uma aplicação Full Stack completa desenvolvida para a disciplina de Programação Orientada a Objetos (POO). O sistema permite o gerenciamento de acervo, clientes e o controle de empréstimos em tempo real, com persistência de dados em banco relacional.

🚀 Funcionalidades Principais
Gestão de Acervo: CRUD completo de livros com controle de disponibilidade automática.

Gestão de Clientes: Cadastro e manutenção de usuários da biblioteca.

Controle de Empréstimos: Registro de saídas com vínculo entre cliente e livro, alterando o status do acervo instantaneamente.

Interface Moderna: Dashboard responsivo com tema escuro (Dark Mode) utilizando Shadcn/UI.

🛠️ Tecnologias Utilizadas
Front-end
Framework: Next.js 16 (App Router)

Estilização: Tailwind CSS

Componentes: Shadcn/UI & Lucide React

Estado/Comunicação: React Context API & Fetch API

Back-end (API REST)
Linguagem: Java 21

Framework: Spring Boot 3.4

ORM: Spring Data JPA (Hibernate)

Segurança: Configuração de CORS para integração segura com o Front-end.

Banco de Dados
Tipo: Relacional (SQL)

Engine: MySQL 8.0

Modelagem: Relacionamentos @ManyToOne para controle de empréstimos.

🔧 Como rodar o projeto localmente
1. Configurar o Back-end
   Certifique-se de ter o MySQL rodando.

No arquivo src/main/resources/application.properties, ajuste as credenciais do seu banco.

Execute a classe BibliotecaApplication via IntelliJ ou Maven.

2. Configurar o Front-end
   Acesse a pasta biblioteca.

Instale as dependências: npm install.

Inicie o servidor de desenvolvimento: npm run dev.

Acesse http://localhost:3000.

👨‍💻 Autores:

Bruno Lima Amaral - Estudante de Engenharia/TI - 4º Semestre
Pedro Henrique Rodrigues dos Santos - Estudante de Engenharia/TI - 4º Semestre

"Desenvolvido em uma maratona épica de código, superando desafios de integração e infraestrutura."
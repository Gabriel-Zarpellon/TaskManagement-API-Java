# Projeto

Neste projeto é desenvolvida uma API que permite o registro, login e listagem de usuários, que podem criar, listar, atualizar e excluir tarefas.

Todas as rotas da aplicação, com excessão de registro e login de usuários, são protegidas e necessitam de um token de acesso para serem utilizadas.

## Objetivos da Aplicação

    1. Criação de usuário: Permite que seja cadastrado um novo usuário no sistema, contendo um identificador único, username, senha e nome.

    2. Login de Usuário: Gera um token de acesso a partir do username e senha de um usuário existente.

    3. Listagem de usuários: Visualização de todos os usuários cadastrados (Só pode ser acessada por usuários com permissão de administrador).

    4. Criação de Tarefa: Permite adição de novas tarefas ao sistema, com cada tarefa tendo um identificador único, titulo, status, descrição e usuário (Quem criou a tarefa).
    
    5. Listagem de Tarefas: Permite que administradores tenham acesso a todas as tarefas criadas e usuários comuns visualizem as tarefas das quais são donos. Além disso, permite filtragem de tarefas pelo status.
    
    6. Busca de Tarefa: Oferece uma funcionalidade de busca para localizar rapidamente tarefas pelo ID, permitida ao dono da tarefa ou administrador.

    8. Atualização de Tarefa: Permite atualização de uma tarefa partir do ID (dono ou administrador).
    
    8. Deleção de Tarefa: Oferece uma funcionalidade de deleção de tarefa por ID (dono ou adminstrador).

## Estrutura

A aplicação é estruturada em várias partes, cada uma com sua responsabilidade específica:

Tarefas

    - Entidade de Tarefa (TaskEntity): Define a estrutura de dados para as tarefas.
    
    - Controlador de Tarefas (TaskController): Gerencia o fluxo de requisição do usuário, entrada e saída de dados.
    
    - Serviço de Tarefas (TaskService): Gerencia a lógica da aplicação com as operações de criação, leitura, deleção e busca de tarefa.
    
    - Repositório de Tarefas (TaskRepository): Gerencia os métodos do ORM do JPA.
    
    - DTO para criação de Tarefas (TaskCreateDTO): Gerencia como os dados devem ser recebidos pela aplicação.
    
    - Exceções Personalizadas: Inclui exceções customizadas para tratar situações específicas como conflitos de nome e produtos não encontrados.

Usuários

    - Entidade de Usuário (UserEntity): Define a estrutura de dados para os usuários.
    
    - Controlador de Usuários (UserController): Gerencia o fluxo de requisição do usuário, entrada e saída de dados.
    
    - Serviço de Usuários (UserService): Gerencia a lógica da aplicação com as operações de registro e login de usuário.
    
    - Repositório de Usuários (UserRepository): Gerencia os métodos do ORM do JPA.
    
    - DTO para criação de Usuários (UserCreateDTO): Gerencia como os dados devem ser recebidos pela aplicação.

    - DTO de retorno de Usuários (UserReturnDTO): Gerencia como os dados devem ser retornados pela aplicação.
    
    - Exceções Personalizadas: Inclui exceções customizadas para tratar situações específicas como conflitos de username e senha, bem como propriedade de tarefas.

Segurança

    - Serviço de JWT (jwtTokenService): Valida o token e retorna as credenciais do usuário.
    
    - Filtro de autenticação JWT (jwtTokenAuthenticationFilter): Valida o token recebido e define se o usuário está autenticado ou não.

Configurações

    - Configurações de Segurança (securityConfig): Define filtros de segurança e configura autorização de requisições HTTP.
    
    - Configurações de JWT (jwtConfig): Declara as configurações iniciais e obrigatórias para a geração de um token jwt.

    - Configurações de Sessão (sessionConfig): Define como o serviço de detalhes do usuário deve ser criado e como o gerenciamento de autenticação deve ser realizado.


# API Tarefas e Usuários

## Rotas de usuário

### Registro de usuário POST /api/users

Padrão de corpo

```json
{
	"username": "example@mail.com",
	"password": "123456",
	"name": "Exemplo"
}
```

Padrão de resposta (STATUS 201)

```json
{
	"id": "103",
	"username": "example@mail.com",
	"name": "Exemplo",
	"tasks": null,
	"admin": false
}
```

Possíveis erros 

409 CONFLICT

```json
{
  "error": "user already registered."
}
```

400 BAD REQUEST

```json
[
	{
		"error": "username is required."
	},
	{
		"error": "password is required."
	},
	{
		"error": "name is required."
	}
]
```

### Login de usuário POST /api/users

Padrão de corpo

```json
{
	"username": "example@mail.com",
	"password": "123456"
}
```
Padrão de resposta (STATUS 200)

```json
{
	"token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbjY5QG1haWwuY29tIiwicm9sZXMiOiJST0xFX0FETUlOIiwiaWF0IjoxNzM0NzEzODYxLCJ"
}
```

Possíveis Erros




### Registro de tarefa POST /api/tasks

Padrão de corpo

```json
{
	"title": "Tarefa 1",
	"status": "Pendente",
	"description": "Descrição da tarefa"
}
```

Padrão de resposta (STATUS 201)

```json
{
	"id": "612d78fe-6848-43a0-a458-0452ef08b247",
	"title": "Tarefa 1",
	"status": "Pendente",
	"description": "Descrição da tarefa"
}
```

Possíveis erros 

400 BAD REQUEST

```json
[
	{
		"error": "Title is required."
	},
	{
		"error": "Status is required."
	},
	{
		"error": "Description is required."
	}
]
```

### Leitura de tarefas GET /api/tasks

Padrão de resposta (STATUS 200)

```json
[
	{
		"id": "612d78fe-6848-43a0-a458-0452ef08b247",
		"title": "Tarefa 1",
		"status": "Pendente",
		"description": "Descrição da tarefa"
	}
]
```

URL Search Params

| Parâmetro | Exemplo de uso            | Descrição                                                                         |
| --------- | ------------------------- | --------------------------------------------------------------------------------- |
|  status   | /api/tasks?status=exemplo | Forneça o "status" da tarefa para trazer somente tarefas com o status determinado |


### Leitura de tarefas por id GET /api/tasks/id


Padrão de resposta (STATUS 200)

```json
{
	"id": "612d78fe-6848-43a0-a458-0452ef08b247",
	"title": "Tarefa 1",
	"status": "Pendente",
	"description": "Descrição da tarefa"
}
```

Possíveis erros 

STATUS 404 NOT FOUND

```json
{
	"error": "Task not found."
}
```

### Exclusão de tarefas por id DELETE /api/tasks/id


Padrão de resposta (STATUS 204)

Nenhum corpo de resposta 


Possíveis erros 

STATUS 404 NOT FOUND

```json
{
	"error": "Task not found."
}
```



# Projeto School

## Sobre esse projeto

Esta é uma API simples que tem o objetivo de gerenciar usuários, cursos e matrículas.

São utilizadas as tecnologias:

- Java 11
- Maven 3+  
- Spring Boot
- Spring Web
- Bean Validation
- Spring Data JPA
- H2, o BD relacional em memória

Abra o projeto na IDE da sua preferência.

Execute os testes automatizados. 

Suba a aplicação e explore a API com uma ferramenta como [cURL](https://curl.se/), [Insomnia](https://insomnia.rest/), [Postman](https://www.postman.com/).

Alguns exemplos de chamadas usando cURL estão em [exemplos-curl.md](exemplos-curl.md).

### O que já estava implementado?

Os seguintes endpoints estavam implementados:

- `GET /users/{username}` obtém os detalhes de um usuário
- `POST /users` adiciona um novo usuário
- `GET /courses` lista os cursos já cadastrado
- `POST /courses` adiciona um novo curso
- `GET /courses/{code}` obtém os detalhes de um curso

## O que eu tive que fazer

Implementei as seguintes tarefas.

### 1: Correção dos testes automatizados

Nem todos os testes automatizados que já existem no projeto estavam passando.

Alterei o código de produção para que todos os testes automatizados passem.

Observação: **NÃO** modifiquei nenhum teste ja existente do projeto!

### 2: Implementei a Matrícula de usuário

Uma Matrícula associa um usuário a um curso, em uma data específica.

Um usuário não pode matricular-se mais de uma vez em um curso.

Implementei um endpoint no projeto que recebe um `POST` em `/courses/{courseCode}/enroll`.

O `username` do usuário a ser matriculado e passado em um JSON no corpo da requisição.

Por exemplo, para matricular o usuário `alex` no curso de código `java-1`, deve ser feito o seguinte:

```
POST /courses/java-1/enroll
Content-Type: application/json

{"username": "alex"}
```

O _status code_ de retorno deve ser [201 Created](https://httpstatusdogs.com/201-created).

Caso o usuário já esteja matriculado no curso, é retornado um _status code_ [400 Bad Request](https://httpstatusdogs.com/400-bad-request).

### 3: Implementei relatório de matrículas

O relátorio retorna os usuários que tenha pelo menos uma matrícula, ordenada pela quantidade.

Implementei um endpoint `GET` no projeto que retorna os dados em `/courses/enroll/report`.

Exemplo, ao chamar o recurso, o retorno esta no seguinte formato:

```
GET /courses/enroll/report
[
    {
        "email": "alex@alura.com.br",
        "quantidade_matriculas": 10
    },
    {
        "email": "caio@alura.com.br",
        "quantidade_matriculas": 5
    }
]
```

Caso não tenha nenhum usuário com matrícula, é retornado um _status code_ [204 No Content](https://httpstatusdogs.com/204-no-content).

# book-api

 Book-API é uma api que disponibiliza informações sobre livros, cujas informações foram obtidas por meio de dados publicos disponibilizados no [Kaggle](https://www.kaggle.com/datasets/chhavidhankhar11/amazon-books-dataset/).


## I. Arquitetura de Solução e Arquitetura Técnica: 

 Esta aplicação foi desenvolvida utilizando Spring Boot na vesão 3.3.3 e com Java 17, fazendo uso de uma arquitetura em camadas visando a segregação das responsabilidades e torná-la facilmente escalável. A mesma se utiliza do banco de dados relacional MySql, assim como do mecanimos de cache Memcached para melhoria de performance para algumas consultas repetidas. A estruta do banco de dados é criada automaticamente por meio da utilização de migrations (Flyway). A applicação também faz uso do Model Mapper e contém testes de integração e testes unitários, cujos testes são realizados utilizando-se do testcontainers e JUnit.
 A documentação desta aplicação está disponível por meio do [Swagger](http://localhost/swagger-ui/index.html), ou via documentação do [Postman](https://documenter.getpostman.com/view/9015507/2sAXqng5HW).


## II. Explicação sobre o Case Desenvolvido (Plano de Implementação): 

 A api conta com uma arquitetura de camadas, cujas responsabilidade estão segregadas nas seguintes camadas lógicas:
- [] Controller: Responsável por receber as requisições HTTP, processar a entrada e delegar para os serviços.
- [] Service: Contém a lógica de negócios da aplicação.
- [] Repository: Contém a lógica de persistência e acesso ao banco de dados, extendendo JpaRepository.
- [] Model: Representa as entidades do banco de dados.
- [] Data/VO/V1: - Representa os objetos de transferência de dados na Versão 1 desta aplicação.
- [] Mapper: Contém o mapeamento entre modelos e objetos de transferência.
- [] Config: Contém configurações específicas do Spring, como CORS, Swagger, Memcached, etc.
- [] Exception: Contém as classes de exceção personalizadas que lidam com erros específicos da aplicação.
- [] Util: Classe de utilidade que contém funções auxiliares que podem ser reutilizadas em várias partes da aplicação.
- [] Infrastructure: Contém as classes que auxiliam na configuração da aplicação.
 Atualmente, as informações disponibilizadas são inseridas automaticamente na aplicação através de um DataLoader que faz a leitura de um arquivo CSV com os dados.

## III. Melhorias e Considerações Finais: Discussão sobre possíveis 

  Em termos de melhoria para a aplicação, as próximas etapas são:
- [] Criar os endpoints para a completa gestão da informações para cada registros no banco de dados (Create, Update e Delete).
- [] Criar endpoints para upload de informações em arquivo CVS.
- [] Implementar autenticação na aplicação, limitando o acesso aos endpoints para gestão dos registros relacionados.
- [] Preparar a aplicação para deploy em uma Cloud.


## Setup da Aplicação

A aplicação pode ser executada por meio do docker compose disponibilizado nesse repositório, bastando localizá-lo no diretório e executar o seguinte comando:

```
docker compose up -d --build
```

 Para finalizar, basta executar:
 
```
docker compose down
```

 Garanta que o arquivo .JAR tenha sido gerado antes de executar o docker compose.
 

### Primeira execução

 A imagem do MySql demora alguns segundos para estabilizar e comunicar normalmente com a aplicação. Faz-se necessário conectar-se à instência do MySql e criar a database "books_db". A referida database pode ser criada por meio do seguinte comando: 
 
```
Create database books_db;
```

 Para se conectar na instância, deve-se utilizar as credenciais de usuário e senha que constam no arquivo do docker compose.


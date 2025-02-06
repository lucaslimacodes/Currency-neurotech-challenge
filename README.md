# Currency API challenge
This repository was made to publish the neurotech api test proposed.<br /> <br />
The main idea is to implement a currency API that checks the currency rate between BRL and USD. <br /> <br />
The framework chosen to perform this task was [Spring Boot](https://spring.io/projects/spring-boot). It allows fast and robust web service development, and it fits perfectly with the requirements of this project.

## Domain
First things first, we have to properly setup the domain of the problem.<br />
To do so, i have made an [ER-Model](https://en.wikipedia.org/wiki/Entity%E2%80%93relationship_model) to describe entities and relationships:
<br /> <br />
![alt text](https://github.com/lucaslimacodes/Currency-neurotech-challenge/blob/main/Resources/Images/entityRelationshipDiagram.png)
<br /> <br />
- The `Cambio` entity is mapped into a table.
- `dataCambio`, `cotacaoCompra` and `cotacaoVenda` are columns of the table, and since the currency rate is daily, `dataCambio` is a nice candidate to be the primary key.
- Since there is only one entity with only a few columns and no relationships, the [H2 Database](https://en.wikipedia.org/wiki/H2_(database)) was chosen to store all data because of its simplicity and usability.

## API
The API architecture is shown below:
<br /> <br />
![alt text](https://github.com/lucaslimacodes/Currency-neurotech-challenge/blob/main/Resources/Images/ApiArchitecture.png)
<br /> <br />
- `Controller`: It is responsible for handling the endpoints and calling the service layer when needed.
- `Service`: Responsible for business logic, it provides service to Controller and fetches data from Repository layer.
- `Repository`: Responsible for communicating with database.

### Endpoints
- `GET /latest`: returns a `Cambio` object with the latest instance of Cambio in Database. It there are none, returns an Error object.
- `GET /interval`: returns a list of `Cambio` objects whose Date are between the interval given through query parameters. It may return an error objects if there are no instances in the interval or if the Date format is wrong

### DBPopulator
Another important module is the `DBPopulator`.<br /> <br />
It has a script that runs everytime the api starts, and it is responsible for fetching data from [Banco Central do Brasil](https://dadosabertos.bcb.gov.br/dataset/dolar-americano-usd-todos-os-boletins-diarios/resource/ada8e867-7e1f-498b-aa54-97c6a523e8d5?inner_span=True). This way, it fills the database with data, so it can be retreived through the endpoints.
<br /> <br />
It fetches all available data from 01/01/2010 up to today's date, but it is easily configurable by changing the `startDate` variable inside [DBPopulator class](https://github.com/lucaslimacodes/Currency-neurotech-challenge/blob/main/currencyAPI/src/main/java/com/neurotech/currencyAPI/DBPopulator/DBPopulator.java).

## Frontend
A simple frontend was developed using `Node.js`, `http-server` and `charts.js`<br /> <br />
![alt text](https://github.com/lucaslimacodes/Currency-neurotech-challenge/blob/main/Resources/Images/frontEndSample.png)

## How to run
Run the following script to build and run both frontend and api images:<br />

```bash
docker-compose up
```
To access frontend, just go to `localhost:8081`<br /> <br />
To access back end swagger documentation, go to `localhost:8080/swagger-ui/index.html`


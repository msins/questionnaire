## Questionnaire

- Web app used for managing game surveys. Can be controlled through the dashboard or the REST API.
- Made with Spring Boot, Vaadin, Hibernate and uses MVP for user interface.
- This was my part of a 2019 student group project. Other parts of the project were Unity VR library, which renders
  these surveys in-game, and a simple game(Whack-a-mole) to test it on.

### Testing

`port 8080`\
`Username: admin`\
`Password: admin`

1) Create database:

```sh
$ docker container run -d --name=questionnaire -p 5432:5432 -e POSTGRES_PASSWORD=password -v ${HOME}/postgres/questionnaire:/var/lib/postgresql/data postgres:13.3-alpine
```

2) Open repo as maven project in any IDE and click run.

or use `docker-compose.yml`:

```sh
# build jar
$ mvn package -Pproduction -DskipTests
# run webapp and database containers
$ docker-compose up -d
```

## API

#### Game

`[GET] /api/v1/game` - get all games\
`[GET] /api/v1/game/{gameId}` - get game with an id `gameId`\
`[POST] /api/v1/game/{gameName}` - add game with a name `gameName`\
`[DELETE] /api/v1/game/{gameId}` - delete game with an id `gameId`

#### Scenario

`[POST] /api/v1/scenario/{gameId}/{scenarioText}` - add scenario with text `scenarioText` to a game with an id `gameId`\
`[DELETE] /api/v1/scenario/{scenarioId}` - delete scenario with id `scenarioId`

#### Question

`[POST] /api/v1/question/{scenarioId}` [see request body](src/main/java/edu/fer/project/questionnaire/dtos/requests/AddQuestionRequest.java)\
`[DELETE] /api/v1/question/{questionId}`

#### Answer

- Optional queries: `?offset=10&limit=10&sort=time,desc`

`[GET] /api/v1/answer` - get all answers\
`[GET] /api/v1/answer/{gameId}` - get answers for game with an id equal to `gameId`\
`[GET] /api/v1/answer/{scenarioId}` - get answers for scenario with an id equal to `scenarioId`\
`[GET] /api/v1/answer/{questionId}` - get answers for question with an id equal to `questionId`\
`[POST] /api/v1/answer/` [see request body](src/main/java/edu/fer/project/questionnaire/dtos/requests/AddAnswerRequest.java)

#### Error template:

```json
{
  "error": "Not Found",
  "status": 404,
  "message": "Error message..."
}
```

## Pics

![plot](./pics/questionnaire_1.png?raw=true "function")
![plot](./pics/questionnaire_2.png?raw=true "function")

package edu.fer.project.questionnaire

import edu.fer.project.questionnaire.controllers.error.ApiError
import edu.fer.project.questionnaire.dtos.requests.AddAnswerRequest
import edu.fer.project.questionnaire.dtos.requests.AddChoiceRequest
import edu.fer.project.questionnaire.dtos.requests.AddQuestionRequest
import edu.fer.project.questionnaire.dtos.responses.*
import edu.fer.project.questionnaire.model.Question
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.*
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.context.annotation.PropertySource
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import java.net.URI
import java.sql.Timestamp
import java.time.Instant
import kotlin.test.assertEquals

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
class ApiTest(@Autowired val client: TestRestTemplate) {

    @LocalServerPort
    var port: Int = -1

    val base: String
        get() = "http://localhost:$port/api/v1/"

    val gameBase: String
        get() = "$base/game/"

    val scenarioBase: String
        get() = "$base/scenario/"

    val questionBase: String
        get() = "$base/question/"

    val answerBase: String
        get() = "$base/answer/"

    private fun addGame(name: String): ResponseEntity<GameResponse> {
        return client.postForEntity(URI(gameBase + name), null);
    }

    private fun addScenario(text: String, gameId: Long): ResponseEntity<ScenarioResponse> {
        return client.postForEntity(URI("$scenarioBase$gameId/$text"))
    }

    private fun addQuestion(
        request: AddQuestionRequest,
        scenarioId: Long
    ): ResponseEntity<QuestionResponse> {
        return client.postForEntity(URI("$questionBase$scenarioId"), request)
    }

    private fun addAnswer(request: AddAnswerRequest): ResponseEntity<AnswerResponse> {
        return client.postForEntity(URI(answerBase), request)
    }

    @Test
    fun `add game`() {
        val entity = addGame("League%20of%20Legends")
        assertEquals(HttpStatus.OK, entity.statusCode)
        assertEquals(1, entity.body.id())
        assertEquals("League of Legends", entity.body.name())
    }

    @Test
    fun `delete game`() {
        addGame("League%20of%20Legends")
        val response = client.exchange<Void>(URI("$gameBase/1"), HttpMethod.DELETE, null)
        assertEquals(HttpStatus.NO_CONTENT, response.statusCode)
    }

    @Test
    fun `delete non existing game`() {
        val response = client.exchange<Void>(URI("$gameBase/1"), HttpMethod.DELETE, null)
        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
    }

    @Test
    fun `list games empty`() {
        val response = client.getForEntity<List<GameResponse>>(URI(gameBase))
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(0, response.body.size)
    }

    @Test
    fun `list games`() {
        val range = 1..5
        for (i in range) {
            addGame("game$i")

            val response = client.getForEntity<Array<GameResponse>>(URI(gameBase))
            assertEquals(HttpStatus.OK, response.statusCode)
            assertEquals(i, response.body.size)
            assertEquals(i.toLong(), response.body[i - 1].id())
            assertEquals("game$i", response.body[i - 1].name())
        }
    }

    @Test
    fun `get game`() {
        addGame("Factorio")
        val response = client.getForEntity<GameResponse>(URI("$gameBase/1"))
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals("Factorio", response.body.name())
        assertEquals(1, response.body.id())
    }

    @Test
    fun `get non existing game`() {
        val response = client.getForEntity<ApiError>(URI("$gameBase/5"))
        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
        assertEquals(404, response.body.status())
        assertEquals("Not Found", response.body.error())
        assertEquals("Game with id '5' doesn't exist.", response.body.message())
    }

    @Test
    fun `add scenario`() {
        val gameResponse = addGame("Factorio")
        val response = addScenario("Scenario", gameResponse.body.id())
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(2, response.body.id())
        assertEquals("Scenario", response.body.text())
        assertEquals(emptyList(), response.body.questions())
    }

    @Test
    fun `delete scenario`() {
        val gameResponse = addGame("Factorio")
        val scenarioResponse = addScenario("Scenario", gameResponse.body.id())
        assertEquals(HttpStatus.OK, scenarioResponse.statusCode)

        val deleteResponse = client.exchange<Void>(
            URI("$scenarioBase/${scenarioResponse.body.id()}"),
            HttpMethod.DELETE,
            null
        )
        assertEquals(HttpStatus.NO_CONTENT, deleteResponse.statusCode)
    }

    @Test
    fun `delete non existing scenario`() {
        val deleteResponse = client.exchange<Void>(
            URI("$scenarioBase/5"),
            HttpMethod.DELETE,
            null
        )

        assertEquals(HttpStatus.NOT_FOUND, deleteResponse.statusCode)
    }

    @Test
    fun `add question`() {
        val gameResponse = addGame("Factorio")
        val scenarioResponse = addScenario("Scenario", gameResponse.body.id())
        val request = AddQuestionRequest.builder()
            .text("Question")
            .type(Question.Type.MULTIPLE_CHOICE)
            .choices(
                mutableListOf(
                    AddChoiceRequest.builder().text("a").ordering(1).build(),
                    AddChoiceRequest.builder().text("b").ordering(2).build(),
                    AddChoiceRequest.builder().text("c").ordering(3).build()
                )
            ).build()
        val questionResponse = addQuestion(request, scenarioResponse.body.id())

        assertEquals(HttpStatus.OK, questionResponse.statusCode)
        assertEquals(Question.Type.MULTIPLE_CHOICE, questionResponse.body.type())
        assertEquals(listOf("a", "b", "c"), questionResponse.body.choices().map { it.text() })
        assertEquals(listOf(1, 2, 3), questionResponse.body.choices().map { it.ordering() })
    }

    @Test
    fun `delete question`() {
        val gameResponse = addGame("Factorio")
        val scenarioResponse = addScenario("Scenario", gameResponse.body.id())
        val questionResponse = addQuestion(
            AddQuestionRequest.builder()
                .text("Question")
                .type(Question.Type.SCALING)
                .choices(listOf())
                .build(),
            scenarioResponse.body.id()
        )

        val deleteResponse = client.exchange<Void>(
            URI("$questionBase/${questionResponse.body.id()}"),
            HttpMethod.DELETE,
            null
        )

        assertEquals(HttpStatus.NO_CONTENT, deleteResponse.statusCode)
    }

    @Test
    fun `delete non existing question`() {
        val deleteResponse = client.exchange<Void>(
            URI("$questionBase/5"),
            HttpMethod.DELETE,
            null
        )

        assertEquals(HttpStatus.NOT_FOUND, deleteResponse.statusCode)
    }

    @Test
    fun `add answer`() {
        val gameResponse = addGame("Factorio")
        val scenarioResponse = addScenario("Ping%20under%2060ms", gameResponse.body.id())
        val questionResponse = addQuestion(
            AddQuestionRequest.builder()
                .text("Did you experience lag?")
                .type(Question.Type.MULTIPLE_CHOICE)
                .choices(
                    listOf(
                        AddChoiceRequest.builder().text("Yes").ordering(1).build(),
                        AddChoiceRequest.builder().text("No").ordering(2).build()
                    )
                )
                .build(),
            scenarioResponse.body.id()
        )

        val answerResponse = addAnswer(
            AddAnswerRequest.builder()
                .gameId(gameResponse.body.id())
                .scenarioId(scenarioResponse.body.id())
                .questionId(questionResponse.body.id())
                .choiceId(questionResponse.body.choices().first().id())
                .user(
                    VoterInformationRequest.builder()
                        .age(20)
                        .name("Tobi")
                        .email("test@gmail.com")
                        .gender("Male")
                        .ip("188.252.197.44")
                        .build()
                )
                .time(Timestamp.from(Instant.now()))
                .build()
        )

        println(answerResponse.body.time())

        assertEquals(HttpStatus.OK, answerResponse.statusCode)
        assertEquals(gameResponse.body.id(), answerResponse.body.gameId())
        assertEquals(scenarioResponse.body.id(), answerResponse.body.scenarioId())
        assertEquals(questionResponse.body.id(), answerResponse.body.questionId())
        assertEquals(questionResponse.body.choices().first().id(), answerResponse.body.choiceId())
    }
}
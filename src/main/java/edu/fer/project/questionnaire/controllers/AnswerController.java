package edu.fer.project.questionnaire.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.fer.project.questionnaire.converters.implementation.AnswerConverter;
import edu.fer.project.questionnaire.dtos.requests.AddAnswerRequest;
import edu.fer.project.questionnaire.dtos.responses.AnswerResponse;
import edu.fer.project.questionnaire.dtos.responses.PagingResponse;
import edu.fer.project.questionnaire.services.AnswerService;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import java.io.StringWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/answer")
public class AnswerController {

  private static final String DEFAULT_LIMIT = "20";

  private final AnswerService answerService;
  private final AnswerConverter answerConverter;

  @Autowired
  public AnswerController(AnswerService answerService, AnswerConverter answerConverter) {
    this.answerService = answerService;
    this.answerConverter = answerConverter;
  }

  @GetMapping
  public Single<ResponseEntity<PagingResponse<AnswerResponse>>> getAnswers(
      @RequestParam(value = "limit", defaultValue = DEFAULT_LIMIT) int limit,
      @RequestParam(value = "offset", defaultValue = "0") int offset,
      @SortDefault(sort = "time", direction = Direction.DESC) Sort sort
  ) {
    return answerService.fetchAnswers(offset, limit, sort)
        .subscribeOn(Schedulers.io())
        .map(page -> ResponseEntity.ok(
            PagingResponse.create(page.map(answerConverter::localToRemote))
        ));
  }

  @GetMapping("{gameId}")
  public Single<ResponseEntity<PagingResponse<AnswerResponse>>> getAnswersForGame(
      @PathVariable long gameId,
      @RequestParam(value = "limit", defaultValue = DEFAULT_LIMIT) int limit,
      @RequestParam(value = "offset", defaultValue = "0") int offset,
      @SortDefault(sort = "time", direction = Direction.DESC) Sort sort
  ) {
    return answerService.fetchAnswersForGame(gameId, offset, limit, sort)
        .subscribeOn(Schedulers.io())
        .map(page -> ResponseEntity.ok(
            PagingResponse.create(page.map(answerConverter::localToRemote))
        ));
  }

  @GetMapping("{scenarioId}")
  public Single<ResponseEntity<PagingResponse<AnswerResponse>>> getAnswersForScenario(
      @PathVariable long scenarioId,
      @RequestParam(value = "limit", defaultValue = DEFAULT_LIMIT) int limit,
      @RequestParam(value = "offset", defaultValue = "0") int offset,
      @SortDefault(sort = "time", direction = Direction.DESC) Sort sort
  ) {
    return answerService.fetchAnswersForScenario(scenarioId, offset, limit, sort)
        .subscribeOn(Schedulers.io())
        .map(page -> ResponseEntity.ok(
            PagingResponse.create(page.map(answerConverter::localToRemote))
        ));
  }

  @PostMapping
  public Single<ResponseEntity<AnswerResponse>> addAnswer(@RequestBody AddAnswerRequest body) {
    return answerService.saveAnswer(body)
        .subscribeOn(Schedulers.io())
        .map(answer -> ResponseEntity.ok(
            answerConverter.localToRemote(answer)
        ));
  }
}

package edu.fer.project.questionnaire.controllers;

import edu.fer.project.questionnaire.converters.implementation.QuestionConverter;
import edu.fer.project.questionnaire.dtos.requests.AddQuestionRequest;
import edu.fer.project.questionnaire.dtos.responses.QuestionResponse;
import edu.fer.project.questionnaire.services.QuestionService;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/question")
public class QuestionController {

  private final QuestionService questionService;
  private final QuestionConverter questionConverter;

  @Autowired
  public QuestionController(QuestionService questionService, QuestionConverter questionConverter) {
    this.questionService = questionService;
    this.questionConverter = questionConverter;
  }

  @PostMapping("{scenarioId}")
  public Single<ResponseEntity<QuestionResponse>> addQuestion(
      @PathVariable long scenarioId,
      @RequestBody AddQuestionRequest body
  ) {
    return questionService.addQuestion(scenarioId, body)
        .subscribeOn(Schedulers.io())
        .map(question -> ResponseEntity.ok().body(
            questionConverter.localToRemote(question)
        ));
  }

  @DeleteMapping("{questionId}")
  public Single<ResponseEntity<Void>> deleteQuestion(@PathVariable long questionId) {
    return questionService.removeQuestion(questionId)
        .subscribeOn(Schedulers.io())
        .map(question -> ResponseEntity.noContent().build());
  }
}

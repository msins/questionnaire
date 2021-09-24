package edu.fer.project.questionnaire.services;

import edu.fer.project.questionnaire.dtos.requests.AddQuestionRequest;
import edu.fer.project.questionnaire.model.Choice;
import edu.fer.project.questionnaire.model.Question;
import edu.fer.project.questionnaire.model.Scenario;
import edu.fer.project.questionnaire.repositories.QuestionRepository;
import edu.fer.project.questionnaire.repositories.ScenarioRepository;
import edu.fer.project.questionnaire.services.exceptions.EntityWithIdNotFound;
import edu.fer.project.questionnaire.services.exceptions.EntityWithNaturalIdExists;
import io.reactivex.Single;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
public class QuestionService {

  private final QuestionRepository questionRepository;
  private final ScenarioRepository scenarioRepository;

  @Autowired
  public QuestionService(
      QuestionRepository questionRepository,
      ScenarioRepository scenarioRepository
  ) {
    this.questionRepository = questionRepository;
    this.scenarioRepository = scenarioRepository;
  }

  @Transactional
  public Single<Question> addQuestion(long scenarioId, AddQuestionRequest body) {
    return Single.create(subscriber -> {
      Optional<Scenario> scenarioOptional = scenarioRepository.findByIdWithQuestions(scenarioId);
      if (scenarioOptional.isEmpty()) {
        subscriber.onError(new EntityWithIdNotFound(Scenario.class, scenarioId));
        return;
      }

      Scenario scenario = scenarioOptional.get();
      Question newQuestion = new Question();
      newQuestion.setText(body.text().trim());
      newQuestion.setType(body.type());
      newQuestion.setScenario(scenario);

      for (var question : scenario.getQuestions()) {
        if (question.getText().equals(newQuestion.getText())) {
          subscriber.onError(new EntityWithNaturalIdExists(Question.class, body.text()));
          return;
        }
      }

      scenario.addQuestion(newQuestion);

      for (var choiceRequest : body.choices()) {
        Choice newChoice = Choice.fromTextAndOrdering(choiceRequest.text(), choiceRequest.ordering());
        newQuestion.addChoice(newChoice);
      }

      questionRepository.save(newQuestion);

      subscriber.onSuccess(newQuestion);
    });
  }

  @Transactional
  public Single<Boolean> removeQuestion(long questionId) {
    return Single.create(subscriber -> {
      try {
        questionRepository.deleteById(questionId);
      } catch (EmptyResultDataAccessException questionNotFound) {
        subscriber.onError(new EntityWithIdNotFound(Question.class, questionId));
        return;
      }
      subscriber.onSuccess(true);
    });
  }

  @Transactional
  public Single<Question> updateQuestion(Question question) {
    return Single.create(subscriber -> subscriber.onSuccess(questionRepository.save(question)));
  }

  @Transactional
  public Single<List<Question>> fetchQuestionsForScenario(long scenarioId) {
    return Single.create(subscriber -> {
      if (!scenarioRepository.existsById(scenarioId)) {
        subscriber.onError(new EntityWithIdNotFound(Scenario.class, scenarioId));
        return;
      }

      subscriber.onSuccess(questionRepository.findByScenarioIdWithChoices(scenarioId));
    });
  }

  @Transactional
  public Single<Integer> countForScenario(long scenarioId) {
    return Single.create(subscriber -> {
      if (!scenarioRepository.existsById(scenarioId)) {
        subscriber.onError(new EntityWithIdNotFound(Scenario.class, scenarioId));
        return;
      }

      subscriber.onSuccess(questionRepository.countQuestionsByScenarioId(scenarioId));
    });
  }
}
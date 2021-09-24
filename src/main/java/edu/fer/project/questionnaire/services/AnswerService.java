package edu.fer.project.questionnaire.services;

import edu.fer.project.questionnaire.dtos.requests.AddAnswerRequest;
import edu.fer.project.questionnaire.model.Answer;
import edu.fer.project.questionnaire.model.Choice;
import edu.fer.project.questionnaire.model.Game;
import edu.fer.project.questionnaire.model.Question;
import edu.fer.project.questionnaire.model.Scenario;
import edu.fer.project.questionnaire.model.VoterInformation;
import edu.fer.project.questionnaire.repositories.AnswerRepository;
import edu.fer.project.questionnaire.repositories.ChoiceRepository;
import edu.fer.project.questionnaire.repositories.GameRepository;
import edu.fer.project.questionnaire.repositories.QuestionRepository;
import edu.fer.project.questionnaire.repositories.ScenarioRepository;
import edu.fer.project.questionnaire.services.exceptions.EntityWithIdNotFound;
import edu.fer.project.questionnaire.services.exceptions.InvalidSortQueryException;
import edu.fer.project.questionnaire.services.utils.OffsetBasedPageRequest;
import io.reactivex.Single;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class AnswerService {

  private final GameRepository gameRepository;
  private final ScenarioRepository scenarioRepository;
  private final QuestionRepository questionRepository;
  private final ChoiceRepository choiceRepository;
  private final AnswerRepository answerRepository;

  private static final Set<String> ALLOWED_SORT_PROPERTIES;

  static {
    ALLOWED_SORT_PROPERTIES = Set.of("time", "game");
  }

  @Autowired
  public AnswerService(
      GameRepository gameRepository,
      ScenarioRepository scenarioRepository,
      QuestionRepository questionRepository,
      ChoiceRepository choiceRepository,
      AnswerRepository answerRepository
  ) {
    this.gameRepository = gameRepository;
    this.scenarioRepository = scenarioRepository;
    this.questionRepository = questionRepository;
    this.choiceRepository = choiceRepository;
    this.answerRepository = answerRepository;
  }

  @Transactional
  public Single<Answer> saveAnswer(AddAnswerRequest body) {
    return Single.create(subscriber -> {

      long gameId = body.gameId();
      long scenarioId = body.scenarioId();
      long questionId = body.questionId();
      long choiceId = body.choiceId();

      Optional<Question> questionOptional = questionRepository.findById(questionId);
      if (questionOptional.isEmpty()) {
        subscriber.onError(new EntityWithIdNotFound(Question.class, questionId));
        return;
      }

      Optional<Game> gameOptional = gameRepository.findById(gameId);
      if (gameOptional.isEmpty()) {
        subscriber.onError(new EntityWithIdNotFound(Game.class, gameId));
        return;
      }

      Optional<Scenario> scenarioOptional = scenarioRepository.findById(scenarioId);
      if (scenarioOptional.isEmpty()) {
        subscriber.onError(new EntityWithIdNotFound(Scenario.class, scenarioId));
        return;
      }

      Optional<Choice> choiceOptional = choiceRepository.findById(choiceId);
      if (choiceOptional.isEmpty()) {
        subscriber.onError(new EntityWithIdNotFound(Choice.class, choiceId));
        return;
      }

      Answer newAnswer = new Answer();
      newAnswer.setTime(body.time());
      newAnswer.setGame(gameOptional.get());
      newAnswer.setScenario(scenarioOptional.get());
      newAnswer.setQuestion(questionOptional.get());
      newAnswer.setChoice(choiceOptional.get());

      VoterInformation voter = new VoterInformation();
      voter.setName(body.user().name());
      voter.setEmail(body.user().email());
      voter.setGender(body.user().gender());
      voter.setAge(body.user().age());
      voter.setIp(body.user().ip());
      newAnswer.setVoterInformation(voter);

      subscriber.onSuccess(answerRepository.save(newAnswer));
    });
  }

  @Transactional
  public Single<Page<Answer>> fetchAnswers(int offset, int limit, Sort sort) {
    return Single.create(subscriber -> {
      if (!isSortValid(sort)) {
        subscriber.onError(new InvalidSortQueryException(filterInvalidProperties(sort)));
        return;
      }

      var request = OffsetBasedPageRequest.of(offset, limit, sort);

      subscriber.onSuccess(answerRepository.findAll(request));
    });
  }

  @Transactional
  public Single<Page<Answer>> fetchAnswersForGame(long gameId, int offset, int limit, Sort sort) {
    return Single.create(subscriber -> {
      if (!isSortValid(sort)) {
        subscriber.onError(new InvalidSortQueryException(filterInvalidProperties(sort)));
        return;
      }

      boolean gameExists = gameRepository.existsById(gameId);
      if (!gameExists) {
        subscriber.onError(new EntityWithIdNotFound(Game.class, gameId));
        return;
      }

      var request = OffsetBasedPageRequest.of(offset, limit, sort);

      subscriber.onSuccess(answerRepository.findAllByGameId(gameId, request));
    });
  }

  @Transactional
  public Single<Page<Answer>> fetchAnswersForScenario(long scenarioId, int offset, int limit, Sort sort) {
    return Single.create(subscriber -> {
      if (!isSortValid(sort)) {
        subscriber.onError(new InvalidSortQueryException(filterInvalidProperties(sort)));
        return;
      }

      boolean scenarioExists = scenarioRepository.existsById(scenarioId);
      if (!scenarioExists) {
        subscriber.onError(new EntityWithIdNotFound(Scenario.class, scenarioId));
        return;
      }

      var request = OffsetBasedPageRequest.of(offset, limit, sort);

      subscriber.onSuccess(answerRepository.findAllByScenarioId(scenarioId, request));
    });
  }

  @Transactional
  public Single<Page<Answer>> fetchAnswersForQuestion(long questionId, int offset, int limit, Sort sort) {
    return Single.create(subscriber -> {
      if (!isSortValid(sort)) {
        subscriber.onError(new InvalidSortQueryException(filterInvalidProperties(sort)));
        return;
      }

      boolean questionExists = questionRepository.existsById(questionId);
      if (!questionExists) {
        subscriber.onError(new EntityWithIdNotFound(Question.class, questionId));
        return;
      }

      var request = OffsetBasedPageRequest.of(offset, limit, sort);

      subscriber.onSuccess(answerRepository.findAllByQuestionId(questionId, request));
    });
  }

  @Transactional
  public int countForGame(long gameId) {
    return answerRepository.countAnswerByGameId(gameId);
  }

  @Transactional
  public int countForScenario(long scenarioId) {
    return answerRepository.countAnswerByScenarioId(scenarioId);
  }

  @Transactional
  public int count() {
    return (int) answerRepository.count();
  }

  private static boolean isSortValid(Sort sort) {
    for (var order : sort) {
      if (!isPropertyValid(order.getProperty())) {
        return false;
      }
    }

    return true;
  }

  private static List<String> filterInvalidProperties(Sort sort) {
    List<String> invalidProperties = new ArrayList<>();
    for (var order : sort) {
      if (!isPropertyValid(order.getProperty())) {
        invalidProperties.add(order.getProperty());
      }
    }
    return invalidProperties;
  }

  private static boolean isPropertyValid(String property) {
    return ALLOWED_SORT_PROPERTIES.contains(property);
  }
}

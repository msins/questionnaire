package edu.fer.project.questionnaire.services;

import edu.fer.project.questionnaire.model.Game;
import edu.fer.project.questionnaire.model.Scenario;
import edu.fer.project.questionnaire.repositories.GameRepository;
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
public class ScenarioService {

  private final GameRepository gameRepository;
  private final ScenarioRepository scenarioRepository;

  @Autowired
  public ScenarioService(GameRepository gameRepository, ScenarioRepository scenarioRepository) {
    this.gameRepository = gameRepository;
    this.scenarioRepository = scenarioRepository;
  }

  @Transactional
  public Single<List<Scenario>> fetchScenariosForGame(long gameId) {
    return Single.create(subscriber -> {
      Optional<Game> gameOptional = gameRepository.findByIdWithScenarios(gameId);
      if (gameOptional.isEmpty()) {
        subscriber.onError(new EntityWithIdNotFound(Game.class, gameId));
        return;
      }

      Game game = gameOptional.get();
      subscriber.onSuccess(game.getScenarios());
    });
  }

  @Transactional
  public Single<Scenario> addScenarioToGame(long gameId, String scenarioText) {
    return Single.create(subscriber -> {
      Optional<Game> gameOptional = gameRepository.findByIdWithScenarios(gameId);
      if (gameOptional.isEmpty()) {
        subscriber.onError(new EntityWithIdNotFound(Game.class, gameId));
        return;
      }
      Game game = gameOptional.get();
      Scenario newScenario = Scenario.fromText(scenarioText.trim());

      for (var scenario : game.getScenarios()) {
        if (scenario.getText().equals(newScenario.getText())) {
          subscriber.onError(new EntityWithNaturalIdExists(Scenario.class, scenarioText));
          return;
        }
      }

      game.addScenario(newScenario);
      scenarioRepository.save(newScenario);
      subscriber.onSuccess(newScenario);
    });
  }

  @Transactional
  public Single<Boolean> deleteScenario(long scenarioId) {
    return Single.create(subscriber -> {
      try {
        scenarioRepository.deleteById(scenarioId);
      } catch (EmptyResultDataAccessException scenarioNotFound) {
        subscriber.onError(new EntityWithIdNotFound(Scenario.class, scenarioId));
        return;
      }
      subscriber.onSuccess(true);
    });
  }

  @Transactional
  public Single<Integer> countForGame(long gameId) {
    return Single.create(subscriber -> {
      Optional<Game> gameOptional = gameRepository.findByIdWithScenarios(gameId);
      if (gameOptional.isEmpty()) {
        subscriber.onError(new EntityWithIdNotFound(Game.class, gameId));
        return;
      }

      Game game = gameOptional.get();
      subscriber.onSuccess(game.getScenarios().size());
    });
  }
}

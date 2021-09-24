package edu.fer.project.questionnaire.services;

import edu.fer.project.questionnaire.model.Game;
import edu.fer.project.questionnaire.repositories.GameRepository;
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
public class GameService {

  private final GameRepository gameRepository;

  @Autowired
  public GameService(GameRepository gameRepository) {
    this.gameRepository = gameRepository;
  }

  @Transactional
  public Single<Game> addGame(String name) {
    return Single.create(subscriber -> {
      boolean gameExists = gameRepository.existsByName(name);
      if (gameExists) {
        subscriber.onError(new EntityWithNaturalIdExists(Game.class, name));
        return;
      }
      subscriber.onSuccess(gameRepository.save(Game.fromName(name)));
    });
  }

  @Transactional
  public Single<Game> fetchGame(long gameId) {
    return Single.create(subscriber -> {
          Optional<Game> gameOptional = gameRepository.findByIdEager(gameId);
          if (gameOptional.isEmpty()) {
            subscriber.onError(new EntityWithIdNotFound(Game.class, gameId));
          } else {
            subscriber.onSuccess(gameOptional.get());
          }
        }
    );
  }

  @Transactional
  public Single<Boolean> deleteGame(long gameId) {
    return Single.create(subscriber -> {
      try {
        gameRepository.deleteById(gameId);
      } catch (EmptyResultDataAccessException gameNotFound) {
        subscriber.onError(new EntityWithIdNotFound(Game.class, gameId));
        return;
      }
      subscriber.onSuccess(true);
    });
  }

  @Transactional
  public Single<List<Game>> fetchGames() {
    return Single.just(gameRepository.findAll());
  }

  @Transactional
  public int count() {
    return (int) gameRepository.count();
  }
}

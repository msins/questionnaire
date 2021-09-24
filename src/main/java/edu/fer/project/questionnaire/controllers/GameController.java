package edu.fer.project.questionnaire.controllers;

import edu.fer.project.questionnaire.converters.implementation.GameConverter;
import edu.fer.project.questionnaire.dtos.responses.GameResponse;
import edu.fer.project.questionnaire.services.GameService;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/game")
public class GameController {

  private final GameService gameService;
  private final GameConverter gameConverter;

  @Autowired
  public GameController(GameService gameService, GameConverter gameConverter) {
    this.gameService = gameService;
    this.gameConverter = gameConverter;
  }

  @GetMapping("{gameId}")
  public Single<ResponseEntity<GameResponse>> getGame(@PathVariable long gameId) {
    return gameService.fetchGame(gameId)
        .subscribeOn(Schedulers.io())
        .map(game -> ResponseEntity.ok().body(
            gameConverter.localToRemote(game)
        ));
  }

  @GetMapping("")
  public Single<ResponseEntity<List<GameResponse>>> getGames() {
    return gameService.fetchGames()
        .subscribeOn(Schedulers.io())
        .map(games -> ResponseEntity.ok(
            games.stream().map(gameConverter::localToRemote).collect(Collectors.toList())
        ));
  }

  @PostMapping("{gameName}")
  public Single<ResponseEntity<GameResponse>> addGame(@PathVariable String gameName) {
    return gameService.addGame(gameName)
        .subscribeOn(Schedulers.io())
        .map(game -> ResponseEntity.ok().body(
            gameConverter.localToRemote(game)
        ));
  }

  @DeleteMapping("{gameId}")
  public Single<ResponseEntity<Void>> deleteGame(@PathVariable long gameId) {
    return gameService.deleteGame(gameId)
        .subscribeOn(Schedulers.io())
        .map(isDeleted -> ResponseEntity.noContent().build());
  }
}

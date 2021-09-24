package edu.fer.project.questionnaire.converters.implementation;

import edu.fer.project.questionnaire.converters.LocalToRemote;
import edu.fer.project.questionnaire.converters.RemoteToLocal;
import edu.fer.project.questionnaire.dtos.responses.GameResponse;
import edu.fer.project.questionnaire.model.Game;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public final class GameConverter implements LocalToRemote<Game, GameResponse>, RemoteToLocal<GameResponse, Game> {

  private final ScenarioConverter scenarioConverter;

  @Autowired
  public GameConverter(ScenarioConverter scenarioConverter) {
    this.scenarioConverter = scenarioConverter;
  }

  @Override
  public List<GameResponse> localToRemote(List<Game> items) {
    return items.stream()
        .map(this::localToRemote)
        .collect(Collectors.toList());
  }

  @Override
  public GameResponse localToRemote(Game game) {
    return GameResponse.builder()
        .id(game.getId())
        .name(game.getName())
        .scenarios(scenarioConverter.localToRemote(game.getScenarios()))
        .build();
  }

  @Override
  public Game remoteToLocal(GameResponse dto) {
    return null;
  }
}

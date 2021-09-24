package edu.fer.project.questionnaire.controllers;

import edu.fer.project.questionnaire.converters.implementation.ScenarioConverter;
import edu.fer.project.questionnaire.dtos.responses.ScenarioResponse;
import edu.fer.project.questionnaire.services.ScenarioService;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/scenario")
public class ScenarioController {

  private final ScenarioService scenarioService;
  private final ScenarioConverter scenarioConverter;

  @Autowired
  public ScenarioController(ScenarioService scenarioService, ScenarioConverter scenarioConverter) {
    this.scenarioService = scenarioService;
    this.scenarioConverter = scenarioConverter;
  }

  @PostMapping("{gameId}/{scenarioText}")
  public Single<ResponseEntity<ScenarioResponse>> addScenarioToGame(
      @PathVariable long gameId,
      @PathVariable String scenarioText
  ) {
    return scenarioService.addScenarioToGame(gameId, scenarioText)
        .subscribeOn(Schedulers.io())
        .map(scenario -> ResponseEntity.ok().body(
            scenarioConverter.localToRemote(scenario)
        ));
  }

  @DeleteMapping({"{scenarioId}"})
  public Single<ResponseEntity<Void>> deleteScenario(@PathVariable long scenarioId) {
    return scenarioService.deleteScenario(scenarioId)
        .subscribeOn(Schedulers.io())
        .map(isDeleted -> ResponseEntity.noContent().build());
  }
}

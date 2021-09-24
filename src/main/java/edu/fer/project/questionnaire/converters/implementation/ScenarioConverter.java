package edu.fer.project.questionnaire.converters.implementation;

import edu.fer.project.questionnaire.converters.LocalToRemote;
import edu.fer.project.questionnaire.converters.RemoteToLocal;
import edu.fer.project.questionnaire.dtos.responses.ScenarioResponse;
import edu.fer.project.questionnaire.model.Scenario;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public final class ScenarioConverter implements LocalToRemote<Scenario, ScenarioResponse>, RemoteToLocal<ScenarioResponse, Scenario> {

  private final QuestionConverter questionConverter;

  @Autowired
  public ScenarioConverter(QuestionConverter questionConverter) {
    this.questionConverter = questionConverter;
  }

  @Override
  public List<ScenarioResponse> localToRemote(List<Scenario> items) {
    return items.stream()
        .map(this::localToRemote)
        .collect(Collectors.toList());
  }

  @Override
  public ScenarioResponse localToRemote(Scenario item) {
    return ScenarioResponse.builder()
        .id(item.getId())
        .text(item.getText())
        .questions(
            item.getQuestions().stream().map(questionConverter::localToRemote).collect(Collectors.toList())
        )
        .build();
  }

  @Override
  public Scenario remoteToLocal(ScenarioResponse item) {
    throw new UnsupportedOperationException();
  }
}

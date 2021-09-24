package edu.fer.project.questionnaire.converters.implementation;

import edu.fer.project.questionnaire.converters.LocalToRemote;
import edu.fer.project.questionnaire.converters.RemoteToLocal;
import edu.fer.project.questionnaire.dtos.responses.ChoiceResponse;
import edu.fer.project.questionnaire.model.Choice;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public final class ChoiceConverter implements LocalToRemote<Choice, ChoiceResponse>, RemoteToLocal<ChoiceResponse, Choice> {

  @Override
  public List<ChoiceResponse> localToRemote(List<Choice> items) {
    return items.stream()
        .map(this::localToRemote)
        .collect(Collectors.toList());
  }

  @Override
  public ChoiceResponse localToRemote(Choice item) {
    return ChoiceResponse.builder()
        .id(item.getId())
        .text(item.getText())
        .ordering(item.getOrdering())
        .build();
  }

  @Override
  public Choice remoteToLocal(ChoiceResponse item) {
    throw new UnsupportedOperationException();
  }
}

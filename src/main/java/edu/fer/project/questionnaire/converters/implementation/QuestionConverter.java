package edu.fer.project.questionnaire.converters.implementation;

import edu.fer.project.questionnaire.converters.LocalToRemote;
import edu.fer.project.questionnaire.converters.RemoteToLocal;
import edu.fer.project.questionnaire.dtos.responses.QuestionResponse;
import edu.fer.project.questionnaire.model.Question;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public final class QuestionConverter implements LocalToRemote<Question, QuestionResponse>,
    RemoteToLocal<QuestionResponse, Question> {

  private final ChoiceConverter choiceConverter;

  @Autowired
  public QuestionConverter(ChoiceConverter choiceConverter) {
    this.choiceConverter = choiceConverter;
  }

  @Override
  public List<QuestionResponse> localToRemote(List<Question> items) {
    return items.stream()
        .map(this::localToRemote)
        .collect(Collectors.toList());
  }

  @Override
  public QuestionResponse localToRemote(Question item) {
    return QuestionResponse.builder()
        .id(item.getId())
        .text(item.getText())
        .type(item.getType())
        .choices(choiceConverter.localToRemote(new ArrayList<>(item.getChoices())))
        .build();
  }

  @Override
  public Question remoteToLocal(QuestionResponse item) {
    throw new UnsupportedOperationException();
  }
}

package edu.fer.project.questionnaire.converters.implementation;

import edu.fer.project.questionnaire.converters.LocalToRemote;
import edu.fer.project.questionnaire.converters.RemoteToLocal;
import edu.fer.project.questionnaire.dtos.responses.AnswerResponse;
import edu.fer.project.questionnaire.model.Answer;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public final class AnswerConverter implements LocalToRemote<Answer, AnswerResponse>,
    RemoteToLocal<AnswerResponse, Answer> {

  private final VoterInformationConverter voterConverter;

  @Autowired
  public AnswerConverter(VoterInformationConverter voterConverter) {
    this.voterConverter = voterConverter;
  }

  @Override
  public List<AnswerResponse> localToRemote(List<Answer> items) {
    return items.stream()
        .map(this::localToRemote)
        .collect(Collectors.toList());
  }

  @Override
  public AnswerResponse localToRemote(Answer item) {
    return AnswerResponse.builder()
        .id(item.getId())
        .time(item.getTime())
        .voter(voterConverter.localToRemote(item.getVoterInformation()))
        .gameId(item.getGame().getId())
        .scenarioId(item.getScenario().getId())
        .questionId(item.getQuestion().getId())
        .choiceId(item.getChoice().getId())
        .build();
  }

  @Override
  public Answer remoteToLocal(AnswerResponse item) {
    throw new UnsupportedOperationException();
  }
}

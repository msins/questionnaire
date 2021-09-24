package edu.fer.project.questionnaire.converters.implementation;

import edu.fer.project.questionnaire.converters.LocalToRemote;
import edu.fer.project.questionnaire.converters.RemoteToLocal;
import edu.fer.project.questionnaire.dtos.responses.VoterInformationRequest;
import edu.fer.project.questionnaire.model.VoterInformation;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public final class VoterInformationConverter implements LocalToRemote<VoterInformation, VoterInformationRequest>,
    RemoteToLocal<VoterInformationRequest, VoterInformation> {

  @Override
  public List<VoterInformationRequest> localToRemote(List<VoterInformation> items) {
    return items.stream()
        .map(this::localToRemote)
        .collect(Collectors.toList());
  }

  @Override
  public VoterInformationRequest localToRemote(VoterInformation item) {
    return VoterInformationRequest.builder()
        .name(item.getName())
        .email(item.getEmail())
        .age(item.getAge())
        .gender(item.getGender())
        .ip(item.getIp())
        .build();
  }

  @Override
  public VoterInformation remoteToLocal(VoterInformationRequest item) {
    throw new UnsupportedOperationException();
  }
}

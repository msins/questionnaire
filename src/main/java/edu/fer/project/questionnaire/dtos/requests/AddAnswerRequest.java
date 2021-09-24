package edu.fer.project.questionnaire.dtos.requests;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.auto.value.AutoValue;
import edu.fer.project.questionnaire.dtos.responses.VoterInformationRequest;
import java.sql.Timestamp;

@AutoValue
@JsonSerialize(as = AddAnswerRequest.class)
@JsonDeserialize(builder = AutoValue_AddAnswerRequest.Builder.class)
public abstract class AddAnswerRequest {

  @JsonProperty("time")
  public abstract Timestamp time();

  @JsonProperty("user")
  public abstract VoterInformationRequest user();

  @JsonProperty("gameId")
  public abstract long gameId();

  @JsonProperty("scenarioId")
  public abstract long scenarioId();

  @JsonProperty("questionId")
  public abstract long questionId();

  @JsonProperty("choiceId")
  public abstract long choiceId();

  @JsonCreator
  public static Builder builder() {
    return new AutoValue_AddAnswerRequest.Builder();
  }

  @AutoValue.Builder
  public abstract static class Builder {

    @JsonProperty("time")
    public abstract Builder time(Timestamp time);

    @JsonProperty("user")
    public abstract Builder user(VoterInformationRequest user);

    @JsonProperty("gameId")
    public abstract Builder gameId(long gameId);

    @JsonProperty("scenarioId")
    public abstract Builder scenarioId(long scenarioId);

    @JsonProperty("questionId")
    public abstract Builder questionId(long questionId);

    @JsonProperty("choiceId")
    public abstract Builder choiceId(long choiceId);

    public abstract AddAnswerRequest build();
  }
}

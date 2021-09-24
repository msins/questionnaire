package edu.fer.project.questionnaire.dtos.responses;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.auto.value.AutoValue;

import java.sql.Timestamp;

@AutoValue
@JsonSerialize(as = AnswerResponse.class)
@JsonDeserialize(builder = AutoValue_AnswerResponse.Builder.class)
public abstract class AnswerResponse {

  @JsonProperty("id")
  public abstract long id();

  @JsonProperty("time")
  public abstract Timestamp time();

  @JsonProperty("voter")
  public abstract VoterInformationRequest voter();

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
    return new AutoValue_AnswerResponse.Builder();
  }

  @AutoValue.Builder
  public abstract static class Builder {

    @JsonProperty("id")
    public abstract Builder id(final long id);

    @JsonProperty("time")
    public abstract Builder time(Timestamp time);

    @JsonProperty("voter")
    public abstract Builder voter(VoterInformationRequest voter);

    @JsonProperty("gameId")
    public abstract Builder gameId(long gameId);

    @JsonProperty("scenarioId")
    public abstract Builder scenarioId(long scenarioId);

    @JsonProperty("questionId")
    public abstract Builder questionId(long questionId);

    @JsonProperty("choiceId")
    public abstract Builder choiceId(long choiceId);

    public abstract AnswerResponse build();
  }
}

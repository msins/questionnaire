package edu.fer.project.questionnaire.dtos.responses;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.auto.value.AutoValue;
import java.util.List;

@AutoValue
@JsonSerialize(as = ScenarioResponse.class)
@JsonDeserialize(builder = AutoValue_ScenarioResponse.Builder.class)
public abstract class ScenarioResponse {

  @JsonProperty("id")
  public abstract long id();

  @JsonProperty("text")
  public abstract String text();

  @JsonProperty("questions")
  public abstract List<QuestionResponse> questions();

  @JsonCreator
  public static Builder builder() {
    return new AutoValue_ScenarioResponse.Builder();
  }

  @AutoValue.Builder
  public abstract static class Builder {

    @JsonProperty("id")
    public abstract Builder id(long id);

    @JsonProperty("text")
    public abstract Builder text(String text);

    @JsonProperty("questions")
    public abstract Builder questions(List<QuestionResponse> questions);

    public abstract ScenarioResponse build();
  }
}

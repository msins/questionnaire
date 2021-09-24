package edu.fer.project.questionnaire.dtos.responses;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.auto.value.AutoValue;

@AutoValue
@JsonSerialize(as = ChoiceResponse.class)
@JsonDeserialize(builder = AutoValue_ChoiceResponse.Builder.class)
public abstract class ChoiceResponse {

  @JsonProperty("id")
  public abstract long id();

  @JsonProperty("text")
  public abstract String text();

  @JsonProperty("ordering")
  public abstract int ordering();

  @JsonCreator
  public static Builder builder() {
    return new AutoValue_ChoiceResponse.Builder();
  }

  @AutoValue.Builder
  public abstract static class Builder {

    @JsonProperty("id")
    public abstract Builder id(long id);

    @JsonProperty("text")
    public abstract Builder text(String text);

    @JsonProperty("ordering")
    public abstract Builder ordering(int ordering);

    public abstract ChoiceResponse build();
  }
}

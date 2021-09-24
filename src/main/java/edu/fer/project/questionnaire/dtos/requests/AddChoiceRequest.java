package edu.fer.project.questionnaire.dtos.requests;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.auto.value.AutoValue;
@AutoValue
@JsonSerialize(as = AddChoiceRequest.class)
@JsonDeserialize(builder = AutoValue_AddChoiceRequest.Builder.class)
public abstract class AddChoiceRequest {

  @JsonProperty("text")
  public abstract String text();

  @JsonProperty("ordering")
  public abstract int ordering();

  @JsonCreator
  public static Builder builder() {
    return new AutoValue_AddChoiceRequest.Builder();
  }

  @AutoValue.Builder
  public abstract static class Builder {

    @JsonProperty("text")
    public abstract Builder text(String text);

    @JsonProperty("ordering")
    public abstract Builder ordering(int order);

    public abstract AddChoiceRequest build();
  }
}

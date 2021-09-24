package edu.fer.project.questionnaire.dtos.requests;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.auto.value.AutoValue;
import edu.fer.project.questionnaire.model.Question;
import java.util.List;

@AutoValue
@JsonSerialize(as = AddQuestionRequest.class)
@JsonDeserialize(builder = AutoValue_AddQuestionRequest.Builder.class)
public abstract class AddQuestionRequest {

  @JsonProperty("text")
  public abstract String text();

  @JsonProperty("type")
  public abstract Question.Type type();

  @JsonProperty("choices")
  public abstract List<AddChoiceRequest> choices();

  @JsonCreator
  public static Builder builder() {
    return new AutoValue_AddQuestionRequest.Builder();
  }

  @AutoValue.Builder
  public abstract static class Builder {

    @JsonProperty("text")
    public abstract Builder text(String text);

    @JsonProperty("type")
    public abstract Builder type(Question.Type type);

    @JsonProperty("choices")
    public abstract Builder choices(List<AddChoiceRequest> choices);

    public abstract AddQuestionRequest build();
  }
}

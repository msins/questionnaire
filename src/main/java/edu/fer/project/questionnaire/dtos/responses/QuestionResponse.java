package edu.fer.project.questionnaire.dtos.responses;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.auto.value.AutoValue;
import edu.fer.project.questionnaire.model.Question;
import java.util.List;

@AutoValue
@JsonSerialize(as = QuestionResponse.class)
@JsonDeserialize(builder = AutoValue_QuestionResponse.Builder.class)
public abstract class QuestionResponse {

  @JsonProperty("id")
  public abstract long id();

  @JsonProperty("text")
  public abstract String text();

  @JsonProperty("type")
  public abstract Question.Type type();

  @JsonProperty("choices")
  public abstract List<ChoiceResponse> choices();

  @JsonCreator
  public static Builder builder() {
    return new AutoValue_QuestionResponse.Builder();
  }

  @AutoValue.Builder
  public abstract static class Builder {

    @JsonProperty("id")
    public abstract Builder id(long id);

    @JsonProperty("text")
    public abstract Builder text(String text);

    @JsonProperty("type")
    public abstract Builder type(Question.Type type);

    @JsonProperty("choices")
    public abstract Builder choices(List<ChoiceResponse> choices);

    public abstract QuestionResponse build();
  }
}

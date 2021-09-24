package edu.fer.project.questionnaire.dtos.responses;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.auto.value.AutoValue;
import java.util.List;

@AutoValue
@JsonSerialize(as = GameResponse.class)
@JsonDeserialize(builder = AutoValue_GameResponse.Builder.class)
public abstract class GameResponse {

  @JsonProperty("id")
  public abstract long id();

  @JsonProperty("name")
  public abstract String name();

  @JsonProperty("scenarios")
  public abstract List<ScenarioResponse> scenarios();

  @JsonCreator
  public static Builder builder() {
    return new AutoValue_GameResponse.Builder();
  }

  @AutoValue.Builder
  public abstract static class Builder {

    @JsonProperty("id")
    public abstract Builder id(final long id);

    @JsonProperty("name")
    public abstract Builder name(final String name);

    @JsonProperty("scenarios")
    public abstract Builder scenarios(final List<ScenarioResponse> scenarios);

    public abstract GameResponse build();
  }
}

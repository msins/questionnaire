package edu.fer.project.questionnaire.controllers.error;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.auto.value.AutoValue;

/**
 * All api errors should follow this short and clear response.
 * ex.
 * {
 *     "error": "Not Found",
 *     "status": 404,
 *     "message": "Game with id 1 not found."
 * }
 */
@AutoValue
@JsonSerialize(as = ApiError.class)
@JsonDeserialize(builder = AutoValue_ApiError.Builder.class)
public abstract class ApiError {

  @JsonProperty("error")
  public abstract String error();

  @JsonProperty("message")
  public abstract String message();

  @JsonProperty("status")
  public abstract int status();

  @JsonCreator
  public static Builder builder() {
    return new AutoValue_ApiError.Builder();
  }

  @AutoValue.Builder
  public abstract static class Builder {

    @JsonProperty("error")
    public abstract Builder error(String error);

    @JsonProperty("message")
    public abstract Builder message(String message);

    @JsonProperty("status")
    public abstract Builder status(int status);

    public abstract ApiError build();
  }
}

package edu.fer.project.questionnaire.dtos.responses;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.auto.value.AutoValue;
import edu.fer.project.questionnaire.dtos.responses.AutoValue_VoterInformationRequest.Builder;
import javax.persistence.Embeddable;

@Embeddable
@AutoValue
@JsonSerialize(as = VoterInformationRequest.class)
@JsonDeserialize(builder = AutoValue_VoterInformationRequest.Builder.class)
public abstract class VoterInformationRequest {

  @JsonProperty("name")
  public abstract String name();

  @JsonProperty("email")
  public abstract String email();

  @JsonProperty("gender")
  public abstract String gender();

  @JsonProperty("age")
  public abstract Integer age();

  @JsonProperty("ip")
  public abstract String ip();

  @JsonCreator
  public static Builder builder() {
    return new AutoValue_VoterInformationRequest.Builder();
  }

  @AutoValue.Builder
  public abstract static class Builder {

    @JsonProperty("name")
    public abstract Builder name(String name);

    @JsonProperty("email")
    public abstract Builder email(String email);

    @JsonProperty("gender")
    public abstract Builder gender(String gender);

    @JsonProperty("age")
    public abstract Builder age(Integer age);

    @JsonProperty("ip")
    public abstract Builder ip(String ip);

    public abstract VoterInformationRequest build();
  }
}

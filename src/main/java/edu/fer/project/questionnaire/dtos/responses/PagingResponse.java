package edu.fer.project.questionnaire.dtos.responses;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.auto.value.AutoValue;
import java.util.List;
import org.springframework.data.domain.Page;

@AutoValue
@JsonSerialize(as = PagingResponse.class)
@JsonDeserialize(builder = AutoValue_GameResponse.Builder.class)
public abstract class PagingResponse<T> {

  @JsonProperty("paging")
  public abstract Paging paging();

  @JsonProperty("records")
  public abstract List<? extends T> records();

  @JsonCreator
  public static <T> PagingResponse<T> create(Page<? extends T> page) {
    return new AutoValue_PagingResponse<>(
        Paging.create(page),
        page.getContent()
    );
  }

  @AutoValue
  public abstract static class Paging {

    @JsonProperty
    public abstract long total();

    @JsonProperty
    public abstract long page();

    @JsonProperty
    public abstract long pages();

    @JsonCreator
    public static Paging create(Page<?> page) {
      return new AutoValue_PagingResponse_Paging(
          page.getTotalElements(),
          page.getPageable().getPageNumber(),
          page.getTotalPages()
      );
    }

  }
}


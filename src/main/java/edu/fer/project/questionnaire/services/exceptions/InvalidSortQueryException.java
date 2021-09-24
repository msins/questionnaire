package edu.fer.project.questionnaire.services.exceptions;

import java.util.List;

public class InvalidSortQueryException extends RuntimeException {

  public InvalidSortQueryException(List<String> invalidSorts) {
    super("Sort queries: [" + String.join(",", invalidSorts) + "]");
  }
}

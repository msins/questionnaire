package edu.fer.project.questionnaire.controllers;

import edu.fer.project.questionnaire.controllers.error.ApiError;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

  private static final Logger Log = LoggerFactory.getLogger(ControllerExceptionHandler.class);

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiError> handleUnhandledException(Exception exception) {
    Log.error(exception.toString());
    return buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, "Unknown error.");
  }

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<ApiError> handleEntityNotFound(EntityNotFoundException exception) {
    return buildResponseEntity(HttpStatus.NOT_FOUND, exception.getMessage());
  }

  @ExceptionHandler(EntityExistsException.class)
  public ResponseEntity<ApiError> handleEntityExistsException(EntityExistsException exception) {
    return buildResponseEntity(HttpStatus.CONFLICT, exception.getMessage());
  }

  private ResponseEntity<ApiError> buildResponseEntity(HttpStatus status, String message) {
    ApiError error = ApiError.builder()
        .error(status.getReasonPhrase())
        .status(status.value())
        .message(message)
        .build();
    return new ResponseEntity<>(error, status);
  }
}

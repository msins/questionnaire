package edu.fer.project.questionnaire.services.exceptions;

import javax.persistence.EntityNotFoundException;

public class EntityWithIdNotFound extends EntityNotFoundException {

  public EntityWithIdNotFound(Class clazz, long nonExistingId) {
    super(clazz.getSimpleName() + " with id '" + nonExistingId + "' doesn't exist.");
  }
}

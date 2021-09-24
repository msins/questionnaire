package edu.fer.project.questionnaire.services.exceptions;

import javax.persistence.EntityExistsException;

public class EntityWithNaturalIdExists extends EntityExistsException {

  public EntityWithNaturalIdExists(Class clazz, String naturalId) {
    super(clazz.getSimpleName() + " with value '" + naturalId + "' already exists.");
  }
}

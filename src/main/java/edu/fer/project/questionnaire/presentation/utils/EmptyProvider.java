package edu.fer.project.questionnaire.presentation.utils;

import com.vaadin.flow.data.provider.AbstractBackEndDataProvider;
import com.vaadin.flow.data.provider.Query;
import java.util.stream.Stream;

public class EmptyProvider<T> extends AbstractBackEndDataProvider<T, Object> {

  @Override
  protected Stream<T> fetchFromBackEnd(Query<T, Object> query) {
    return Stream.of();
  }

  @Override
  protected int sizeInBackEnd(Query<T, Object> query) {
    return 0;
  }
}

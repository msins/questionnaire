package edu.fer.project.questionnaire.presentation;

public interface BasePresenter {

  void subscribe();

  void unsubscribe();

  void error(Throwable throwable);

  void attach();

  void detach();
}

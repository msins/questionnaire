package edu.fer.project.questionnaire.presentation;

public interface BaseView {

  void showLoading(boolean isLoading);

  void showError(Throwable throwable);
}

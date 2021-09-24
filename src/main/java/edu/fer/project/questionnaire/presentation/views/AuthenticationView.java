package edu.fer.project.questionnaire.presentation.views;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import edu.fer.project.questionnaire.presentation.contracts.AuthenticationContract;
import java.util.List;

@Route("login")
@PageTitle("Login")
public class AuthenticationView extends VerticalLayout implements AuthenticationContract.View, BeforeEnterObserver {

  private final LoginForm loginForm = new LoginForm();

  public AuthenticationView() {
    initLoginForm();
    initStyle();
    add(new H1("Questionnaire"), loginForm);
  }

  private void initLoginForm() {
    loginForm.setAction("login");
    loginForm.setForgotPasswordButtonVisible(false);
  }

  private void initStyle() {
    setSizeFull();
    setJustifyContentMode(JustifyContentMode.CENTER);
    setAlignItems(Alignment.CENTER);
  }

  @Override
  protected void onDetach(DetachEvent detachEvent) {
  }

  @Override
  protected void onAttach(AttachEvent attachEvent) {
  }

  @Override
  public void showLoading(boolean isLoading) {
  }

  @Override
  public void showError(Throwable throwable) {
  }

  @Override
  public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
    var thereIsAnError = !beforeEnterEvent.getLocation()
        .getQueryParameters()
        .getParameters()
        .getOrDefault("error", List.of())
        .isEmpty();

    if (thereIsAnError) {
      loginForm.setError(true);
    }
  }
}

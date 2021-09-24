package edu.fer.project.questionnaire.security;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import edu.fer.project.questionnaire.presentation.views.AuthenticationView;

public class AuthenticationUIServiceInitListener implements VaadinServiceInitListener {

  @Override
  public void serviceInit(ServiceInitEvent serviceInitEvent) {
    serviceInitEvent.getSource().addUIInitListener(uiEvent -> {
      final UI ui = uiEvent.getUI();
      ui.addBeforeEnterListener(this::beforeEnter);
    });
  }

  private void beforeEnter(BeforeEnterEvent event) {
    boolean isNotAuthenticationView = !AuthenticationView.class.equals(event.getNavigationTarget());
    boolean isUserNotLoggedIn = !SecurityUtils.isUserLoggedIn();

    if (isNotAuthenticationView || isUserNotLoggedIn) {
      event.rerouteTo(AuthenticationView.class);
    }
  }
}

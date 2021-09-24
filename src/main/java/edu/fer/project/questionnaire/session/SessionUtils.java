package edu.fer.project.questionnaire.session;

import com.vaadin.flow.server.VaadinSession;

public final class SessionUtils {

  private SessionUtils() {
  }

  @SuppressWarnings("unchecked")
  public static <T> T getAttribute(String name) {
    return (T) getSession().getAttribute(name);
  }

  public static void setAttribute(String name, Object attribute) {
    getSession().setAttribute(name, attribute);
  }

  private static VaadinSession getSession() {
    return VaadinSession.getCurrent();
  }

}

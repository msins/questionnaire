package edu.fer.project.questionnaire.presentation.views.components.buttons;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;

public class CancelButton extends Button {

  {
    addThemeVariants(ButtonVariant.LUMO_ERROR);
  }

  public CancelButton(String text, ComponentEventListener<ClickEvent<Button>> listener) {
    super(text, listener);
  }

  public CancelButton(Component icon, ComponentEventListener<ClickEvent<Button>> listener) {
    super(icon, listener);
  }

  public CancelButton(String text, Component icon, ComponentEventListener<ClickEvent<Button>> listener) {
    super(text, icon, listener);
  }
}

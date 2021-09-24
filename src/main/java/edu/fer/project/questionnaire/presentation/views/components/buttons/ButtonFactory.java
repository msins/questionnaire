package edu.fer.project.questionnaire.presentation.views.components.buttons;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;

public interface ButtonFactory {

  default Button createPrimaryButton(String text, ComponentEventListener<ClickEvent<Button>> listener) {
    return applyStyle(new PrimaryButton(text, listener));
  }

  default Button createPrimaryButton(Icon icon, ComponentEventListener<ClickEvent<Button>> listener) {
    return applyStyle(new PrimaryButton(icon, listener));
  }

  default Button createPrimaryButton(String text, Icon icon, ComponentEventListener<ClickEvent<Button>> listener) {
    return applyStyle(new PrimaryButton(text, icon, listener));
  }

  default Button createCancelButton(String text, ComponentEventListener<ClickEvent<Button>> listener) {
    return applyStyle(new CancelButton(text, listener));
  }

  default Button createCancelButton(Icon icon, ComponentEventListener<ClickEvent<Button>> listener) {
    return applyStyle(new CancelButton(icon, listener));
  }

  default Button createCancelButton(String text, Icon icon, ComponentEventListener<ClickEvent<Button>> listener) {
    return applyStyle(new CancelButton(text, icon, listener));
  }

  default Button createSuccessButton(String text, ComponentEventListener<ClickEvent<Button>> listener) {
    return applyStyle(new SuccessButton(text, listener));
  }

  default Button createSuccessButton(Icon icon, ComponentEventListener<ClickEvent<Button>> listener) {
    return applyStyle(new SuccessButton(icon, listener));
  }

  default Button createSuccessButton(String text, Icon icon, ComponentEventListener<ClickEvent<Button>> listener) {
    return applyStyle(new SuccessButton(text, icon, listener));
  }

  default Button applyStyle(Button button) {
    button.getElement().getStyle().set("font-weight", "bold");
    return button;
  }
}

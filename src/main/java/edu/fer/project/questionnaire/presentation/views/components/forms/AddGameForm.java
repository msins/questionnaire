package edu.fer.project.questionnaire.presentation.views.components.forms;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import edu.fer.project.questionnaire.presentation.views.components.buttons.AbstractButtonFactory;
import java.util.function.Consumer;

public class AddGameForm extends Dialog {

  private Consumer<String> onCreate;

  private TextField gameField;
  private Button createButton;
  private Button closeButton;

  public AddGameForm(Consumer<String> onCreate) {
    this.onCreate = onCreate;
    initGameField();
    initCreateButton();
    initCloseButton();

    add(new HorizontalLayout(gameField, createButton, closeButton));
  }

  private void initGameField() {
    gameField = new TextField();
    gameField.setPlaceholder("New game...");
    gameField.focus();
  }

  private void initCreateButton() {
    createButton = AbstractButtonFactory.rectangular().createPrimaryButton("Create", e -> {
      if (gameField.getValue().trim().isEmpty()) {
        gameField.setErrorMessage("Enter game!");
        gameField.setInvalid(true);
        return;
      }

      onCreate.accept(gameField.getValue().trim());
      close();
    });

    createButton.addClickShortcut(Key.ENTER);
  }

  private void initCloseButton() {
    closeButton = AbstractButtonFactory.rectangular().createCancelButton("Cancel", e -> close());
  }
}

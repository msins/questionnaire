package edu.fer.project.questionnaire.presentation.views.components.forms;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import edu.fer.project.questionnaire.model.Game;
import edu.fer.project.questionnaire.presentation.views.components.buttons.AbstractButtonFactory;
import java.util.function.BiConsumer;

public class AddScenarioForm extends Dialog {

  private final Game game;
  private final BiConsumer<Game, String> onCreate;

  private TextField scenarioField;
  private Button createButton;
  private Button closeButton;

  public AddScenarioForm(Game game, BiConsumer<Game, String> onCreate) {
    this.game = game;
    this.onCreate = onCreate;
    initScenarioField();
    initCreateButton();
    initCloseButton();

    add(new HorizontalLayout(scenarioField, createButton, closeButton));
  }

  private void initScenarioField() {
    scenarioField = new TextField();
    scenarioField.setPlaceholder("New scenario...");
    scenarioField.focus();
  }

  private void initCreateButton() {
    createButton = AbstractButtonFactory.rectangular().createPrimaryButton("Create", e -> {
      if (scenarioField.getValue().trim().isEmpty()) {
        scenarioField.setErrorMessage("Enter scenario");
        scenarioField.setInvalid(true);
        return;
      }

      onCreate.accept(game, scenarioField.getValue().trim());
      close();
    });

    createButton.addClickShortcut(Key.ENTER);
  }

  private void initCloseButton() {
    closeButton = AbstractButtonFactory.rectangular().createCancelButton("Cancel", e -> close());
  }
}

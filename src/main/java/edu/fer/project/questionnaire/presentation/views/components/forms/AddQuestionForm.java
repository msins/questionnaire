package edu.fer.project.questionnaire.presentation.views.components.forms;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.spring.annotation.UIScope;
import edu.fer.project.questionnaire.dtos.requests.AddQuestionRequest;
import edu.fer.project.questionnaire.model.Question;
import edu.fer.project.questionnaire.model.Question.Type;
import edu.fer.project.questionnaire.model.Scenario;
import edu.fer.project.questionnaire.presentation.views.components.buttons.AbstractButtonFactory;
import java.util.Arrays;
import java.util.function.Consumer;

@UIScope
public class AddQuestionForm extends Dialog {

  private Select<Question.Type> typeSelect;
  private VerticalLayout formPlaceholder = new VerticalLayout();
  private MultipleChoicesQuestionForm form;

  private Button createButton;
  private Button closeButton;

  private Scenario scenario;
  private Consumer<AddQuestionRequest> onCreate;

  public AddQuestionForm(Scenario scenario, Consumer<AddQuestionRequest> onCreate) {
    this.scenario = scenario;
    this.onCreate = onCreate;
    initTypeSelect();
    initCreateButton();
    initCloseButton();
    add(new HorizontalLayout(typeSelect, createButton, closeButton), formPlaceholder);
  }

  void initTypeSelect() {
    typeSelect = new Select<>(Question.Type.values());
    typeSelect.setPlaceholder("Pick question type");
    typeSelect.setItemLabelGenerator(Question.Type::getName);
    typeSelect.addValueChangeListener(
        changedEvent -> replaceForm(MultipleChoicesQuestionForm.forType(typeSelect.getValue())));
    typeSelect.setValue(Arrays.stream(Type.values()).findFirst().orElseThrow());
  }

  private void initCreateButton() {
    createButton = AbstractButtonFactory.rectangular().createPrimaryButton("Create", e -> {
      if (scenario == null) {
        return;
      }

      if (form.hasAnyErrors()) {
        return;
      }

      AddQuestionRequest request = form.extractQuestionRequest();

      onCreate.accept(request);
      close();
    });
    createButton.addClickShortcut(Key.ENTER);
  }

  private void initCloseButton() {
    closeButton = AbstractButtonFactory.rectangular().createCancelButton("Cancel", e -> close());
  }

  private void replaceForm(MultipleChoicesQuestionForm newForm) {
    formPlaceholder.removeAll();
    form = newForm;
    formPlaceholder.add(form);
  }
}

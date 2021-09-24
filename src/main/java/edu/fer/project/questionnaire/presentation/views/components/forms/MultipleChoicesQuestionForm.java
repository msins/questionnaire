package edu.fer.project.questionnaire.presentation.views.components.forms;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import edu.fer.project.questionnaire.dtos.requests.AddChoiceRequest;
import edu.fer.project.questionnaire.dtos.requests.AddQuestionRequest;
import edu.fer.project.questionnaire.model.Question;
import edu.fer.project.questionnaire.presentation.views.components.buttons.AbstractButtonFactory;
import edu.fer.project.questionnaire.presentation.views.components.style.StylizedVerticalLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MultipleChoicesQuestionForm extends VerticalLayout {

  private TextArea questionText;
  private EditableChoices editableChoices;
  private Button addChoiceButton;
  private Question.Type type;

  public MultipleChoicesQuestionForm() {
    initAddChoiceButton();
    initQuestionText();
    initChoices();
    initStyle();
    add(questionText, editableChoices, addChoiceButton);
  }

  public static MultipleChoicesQuestionForm forType(Question.Type type) {
    var form = new MultipleChoicesQuestionForm();
    form.type = type;
    form.addChoiceButton.click();
    form.addChoiceButton.click();
    return form;
  }

  public static MultipleChoicesQuestionForm fromChoices(List<String> choices) {
    var form = new MultipleChoicesQuestionForm();
    choices.forEach(form::addChoice);
    return form;
  }

  private void initStyle() {
    questionText.setWidthFull();
    setAlignItems(Alignment.CENTER);
  }

  private void initQuestionText() {
    questionText = new TextArea("Text");
    questionText.setPlaceholder("Question text...");
  }

  private void initAddChoiceButton() {
    addChoiceButton = AbstractButtonFactory.circular().createPrimaryButton(VaadinIcon.PLUS.create(), e -> {
      TextField field = new FullWidthTextField();
      String index = String.valueOf(editableChoices.size() + 1);
      field.setPlaceholder(index);
      editableChoices.add(field);
    });
  }

  private void initChoices() {
    editableChoices = new EditableChoices();
  }

  private void addChoice(String choice) {
    TextField text = new FullWidthTextField();
    String itemNumber = String.valueOf(editableChoices.size() + 1);
    text.setValue(choice);
    text.setPlaceholder(itemNumber);
  }

  public boolean hasAnyErrors() {
    boolean hasAnyErrors = false;
    if(questionText.getValue().trim().isEmpty()) {
      questionText.setErrorMessage("Text can't be empty!");
      hasAnyErrors = true;
    }
    
    if(editableChoices.countNonEmpty() < 2) {
      hasAnyErrors = true;
    }


    return hasAnyErrors;
  }

  public AddQuestionRequest extractQuestionRequest() {
    return AddQuestionRequest.builder()
        .text(questionText.getValue())
        .type(type)
        .choices(
            IntStream.range(0, editableChoices.size())
                .mapToObj(i -> AddChoiceRequest.builder()
                    .text(editableChoices.get(i))
                    .ordering(i)
                    .build())
                .collect(Collectors.toList())
        )
        .build();
  }

  public TextArea getText() {
    return questionText;
  }

  static class FullWidthTextField extends TextField {

    public FullWidthTextField() {
      setWidthFull();
    }
  }

  static class EditableChoices extends StylizedVerticalLayout {

    private final List<TextField> choices = new ArrayList<>();

    EditableChoices() {
      initStyle();
    }

    private void initStyle() {
      setAlignItems(Alignment.CENTER);
    }

    public void add(TextField textField) {
      super.add(textField);
      choices.add(textField);
    }

    public String get(int index) {
      return choices.get(index).getValue();
    }

    public int size() {
      return choices.size();
    }

    public long countNonEmpty() {
      return choices.stream()
          .filter(textField -> !textField.isEmpty())
          .count();
    }
  }
}

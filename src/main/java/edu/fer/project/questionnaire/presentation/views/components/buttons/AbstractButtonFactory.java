package edu.fer.project.questionnaire.presentation.views.components.buttons;

import com.vaadin.flow.component.button.Button;

public class AbstractButtonFactory {

  private enum ButtonFactoryType implements ButtonFactory {
    CIRCULAR {
      @Override
      public Button applyStyle(Button button) {
        super.applyStyle(button);
        button.getStyle().set("border-radius", "50%");
        button.getStyle().set("padding", "0px");
        return button;
      }
    },
    RECTANGLE;
  }

  public static ButtonFactory circular() {
    return ButtonFactoryType.CIRCULAR;
  }

  public static ButtonFactory rectangular() {
    return ButtonFactoryType.RECTANGLE;
  }
}

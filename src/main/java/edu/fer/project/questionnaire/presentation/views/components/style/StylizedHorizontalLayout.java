package edu.fer.project.questionnaire.presentation.views.components.style;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class StylizedHorizontalLayout extends HorizontalLayout {

  public StylizedHorizontalLayout(Component... children) {
    super(children);
    initStyle();
  }

  private void initStyle() {
    getElement().getStyle().set("box-shadow", "var(--lumo-box-shadow-m)");
  }
}

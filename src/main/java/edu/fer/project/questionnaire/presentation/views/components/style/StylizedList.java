package edu.fer.project.questionnaire.presentation.views.components.style;

import com.vaadin.flow.component.listbox.ListBox;

public class StylizedList<T> extends ListBox<T> {

  public StylizedList() {
    initStyle();
  }

  private void initStyle(){
    getElement().getStyle().set("box-shadow", "var(--lumo-boc-shadow-m)");
  }
}

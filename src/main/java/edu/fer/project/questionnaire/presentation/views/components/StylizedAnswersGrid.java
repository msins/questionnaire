package edu.fer.project.questionnaire.presentation.views.components;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import edu.fer.project.questionnaire.model.Answer;

public class StylizedAnswersGrid extends Grid<Answer> {

  public StylizedAnswersGrid() {
    initStyle();
  }

  private void initStyle() {
    getStyle().set("box-shadow", "var(--lumo-box-shadow-s)");
    addThemeVariants(
        GridVariant.LUMO_NO_BORDER,
        GridVariant.LUMO_NO_ROW_BORDERS,
        GridVariant.LUMO_ROW_STRIPES
    );
  }
}

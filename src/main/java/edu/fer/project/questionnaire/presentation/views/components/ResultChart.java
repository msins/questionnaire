package edu.fer.project.questionnaire.presentation.views.components;

import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.AxisType;
import com.vaadin.flow.component.charts.model.ChartType;
import com.vaadin.flow.component.charts.model.Configuration;
import com.vaadin.flow.component.charts.model.ListSeries;
import com.vaadin.flow.component.charts.model.Tooltip;
import com.vaadin.flow.component.charts.model.XAxis;
import com.vaadin.flow.component.charts.model.YAxis;
import edu.fer.project.questionnaire.model.Choice;
import edu.fer.project.questionnaire.model.Question;
import java.util.Objects;

public class ResultChart<T extends Question> extends Chart {

  private final T question;
  private final ListSeries series = new ListSeries();

  public ResultChart(T question) {
    super(ChartType.BAR);
    this.question = Objects.requireNonNull(question);
    createChart();
    initStyle();
  }

  private void initStyle() {
    getElement().getStyle().set("box-shadow", "var(--lumo-box-shadow-m)");
  }

  private void createChart() {
    Configuration conf = getConfiguration();

    // x axis
    XAxis x = new XAxis();
    x.setType(AxisType.CATEGORY);
    x.setCategories(question.getChoices().stream().map(Choice::getText).toArray(String[]::new));
    x.setTickLength(0);
    conf.addxAxis(x);

    // y axis
    YAxis y = new YAxis();
    y.getLabels().setEnabled(false);
    y.setTickAmount(0);
    y.setMin(0);
    y.setMax(100);
    y.setTitle("");
    conf.addyAxis(y);

    // hover
    Tooltip tooltip = new Tooltip();
    tooltip.setShared(true);
    tooltip.setAnimation(true);
    tooltip.setShadow(true);
    conf.getLegend().setEnabled(false);
    // percentage
    tooltip.setFormatter("function() { return this.y.toFixed(1) + '%'; }");

    conf.setTooltip(tooltip);
    series.setData(createEmptyModel());

    conf.addSeries(series);
  }

  public void refresh(Number[] stats) {
    this.series.setData(stats);
    series.updateSeries();
  }

  public T getQuestion() {
    return question;
  }

  private Number[] createEmptyModel() {
    return new Number[question.getChoices().size()];
  }
}


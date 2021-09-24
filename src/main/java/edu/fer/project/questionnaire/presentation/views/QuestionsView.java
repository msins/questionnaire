package edu.fer.project.questionnaire.presentation.views;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.renderer.TemplateRenderer;
import com.vaadin.flow.data.renderer.TextRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import edu.fer.project.questionnaire.model.Answer;
import edu.fer.project.questionnaire.model.Game;
import edu.fer.project.questionnaire.model.Question;
import edu.fer.project.questionnaire.model.Scenario;
import edu.fer.project.questionnaire.presentation.contracts.QuestionsContract;
import edu.fer.project.questionnaire.presentation.presenters.QuestionsPresenter;
import edu.fer.project.questionnaire.presentation.views.components.ResultChart;
import edu.fer.project.questionnaire.presentation.views.components.StylizedAnswersGrid;
import edu.fer.project.questionnaire.presentation.views.components.buttons.AbstractButtonFactory;
import edu.fer.project.questionnaire.presentation.views.components.forms.AddQuestionForm;
import edu.fer.project.questionnaire.presentation.views.components.style.StylizedList;
import edu.fer.project.questionnaire.presentation.views.components.style.StylizedVerticalLayout;
import edu.fer.project.questionnaire.session.GameChangeListener;
import edu.fer.project.questionnaire.session.ScenarioChangeListener;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.xml.crypto.Data;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "questions", layout = MainLayout.class)
@UIScope
@PageTitle("Questions")
public class QuestionsView extends HorizontalLayout
    implements QuestionsContract.View, GameChangeListener, ScenarioChangeListener {

  private QuestionList questionList;
  private QuestionInformationView questionInformation;
  private Button addQuestionButton;
  private Scenario currentScenario;

  private final QuestionsContract.Presenter presenter;

  @Autowired
  public QuestionsView(QuestionsPresenter presenter) {
    initQuestionsList();
    initAddQuestionButton();

    setSizeFull();

    add(createLayout());
    this.presenter = presenter;
  }

  @PostConstruct
  public void init() {
    presenter.setup(this);
  }

  @Override
  protected void onAttach(AttachEvent attachEvent) {
    subscribeToGameNotifier(this);
    subscribeToScenarioNotifier(this);
    presenter.attach();
  }

  @Override
  protected void onDetach(DetachEvent detachEvent) {
    unsubscribeFromGameNotifier(this);
    unsubscribeFromScenarioNotifier(this);
    presenter.detach();
  }

  private void initQuestionsList() {
    questionList = new QuestionList();
    questionList.addValueChangeListener(list -> {
      if (list.getValue() == null) {
        return;
      }
      QuestionInformationView oldView = questionInformation;

      questionInformation = new QuestionInformationView(list.getValue());

      presenter.onRefreshQuestion(questionList.getValue());

      if (oldView != null) {
        replace(oldView, questionInformation);
      }
      add(questionInformation);
    });
  }

  private void initAddQuestionButton() {
    addQuestionButton = AbstractButtonFactory.rectangular()
        .createPrimaryButton("New question", VaadinIcon.PLUS.create(), e -> {
              if (currentScenario == null) {
                showError(new RuntimeException("No scenario selected."));
                return;
              }
              new AddQuestionForm(currentScenario,
                  request -> presenter.onQuestionAdded(currentScenario.getId(), request)).open();
            }
        );
  }

  private VerticalLayout createLayout() {
    addQuestionButton.setWidthFull();
    var layout = new StylizedVerticalLayout(addQuestionButton, questionList);
    layout.setAlignItems(Alignment.CENTER);
    layout.setMaxWidth("25%");
    layout.setMinWidth("25%");
    return layout;
  }

  @Override
  public void showLoading(boolean isLoading) {
  }

  @Override
  public void showError(Throwable throwable) {
    new Notification(throwable.getMessage(), 2000).open();
  }

  @Override
  public void questionDeleted(boolean isDeleted) {
    questionList.refresh();
    questionInformation.removeAll();
  }

  @Override
  public void questionSynced(Question question) {
    questionInformation.text.setValue(question.getText());
    questionInformation.syncButton.setEnabled(false);
  }

  @Override
  public void setQuestionsProvider(DataProvider<Question, Object> questionsProvider) {
    questionList.setDataProvider(questionsProvider);
  }

  @Override
  public void setAnswers(List<Answer> answers) {
    questionInformation.answersGrid.setItems(answers);
  }

  @Override
  public void updateChart(Number[] results) {
    questionInformation.chart.refresh(results);
  }

  @Override
  public void gameChanged(Game game) {
  }

  @Override
  public void scenarioChanged(Scenario scenario) {
    currentScenario = scenario;
    addQuestionButton.setEnabled(scenario != null);

    presenter.onScenarioChanged(scenario);
  }

  static class QuestionList extends StylizedList<Question> {

    QuestionList() {
      initStyle();
    }

    private void initStyle() {
      setSizeFull();
      setRenderer(new TextRenderer<>(Question::getText));
    }

    void refresh() {
      getDataProvider().refreshAll();
    }
  }

  class QuestionInformationView extends VerticalLayout {

    private ResultChart<Question> chart;
    private AnswersGrid answersGrid;
    private Question question;
    private TextArea text;
    private Button refreshButton;
    private Button deleteButton;
    private Button syncButton;

    QuestionInformationView(Question question) {
      this.question = question;
      initChart();
      initGrid();
      initQuestionText();
      initDeleteButton();
      initRefreshButton();
      initSyncButton();

      VerticalLayout finalLayout = new VerticalLayout();

      var gridChartLayout = new HorizontalLayout(answersGrid, chart);
      gridChartLayout.setWidthFull();
      finalLayout.add(gridChartLayout);

      finalLayout.add(text);

      var buttonsLayout = new HorizontalLayout();
      buttonsLayout.add(refreshButton, deleteButton, syncButton);
      finalLayout.add(buttonsLayout);

      finalLayout.setAlignItems(Alignment.CENTER);
      finalLayout.setPadding(false);

      add(finalLayout);
    }

    private void initChart() {
      chart = new ResultChart<>(question);
      chart.setWidthFull();
    }

    private void initGrid() {
      answersGrid = new AnswersGrid();
      answersGrid.setWidthFull();
    }

    private void initRefreshButton() {
      refreshButton = AbstractButtonFactory.circular()
          .createPrimaryButton(
              VaadinIcon.REFRESH.create(),
              e -> presenter.onRefreshQuestion(question)
          );
    }

    private void initDeleteButton() {
      deleteButton = AbstractButtonFactory.rectangular()
          .createCancelButton(
              "Delete",
              e -> presenter.onDeleteQuestion(question)
          );
    }

    private void initSyncButton() {
      syncButton = AbstractButtonFactory.rectangular()
          .createSuccessButton(
              "Sync",
              e -> presenter.onSyncQuestion(question, text.getValue())
          );
      syncButton.setEnabled(false);
    }

    private void initQuestionText() {
      text = new TextArea("Text");
      text.setValue(question.getText());
      text.setWidthFull();
      text.addKeyPressListener(event -> {
        syncButton.setEnabled(true);
      });
    }
  }

  static class AnswersGrid extends StylizedAnswersGrid {

    AnswersGrid() {
      initGrid();
    }

    private void initGrid() {
      addColumn(answer -> answer.getTime().toLocalDateTime()
          .format(DateTimeFormatter.ofPattern("d.MM.yyyy. HH:mm:ss"))
      ).setHeader(new Html("<b>Time</b>"));
      addColumn(TemplateRenderer.<Answer>of("<div>[[item.voter.name]]<b>[</b>[[item.IP]]<b>]</b></div>")
          .withProperty("voter", Answer::getVoterInformation)
          .withProperty("IP", answer -> answer.getVoterInformation().getIp())
      ).setHeader(new Html("<b>User</b>"));
      addColumn(Answer::getChoice)
          .setHeader(new Html("<b>Choice</b>"));

      getColumns().forEach(e -> e.setAutoWidth(true));
      getColumns().forEach(e -> e.setSortable(true));
    }

    public void refresh() {
      getDataProvider().refreshAll();
    }
  }
}

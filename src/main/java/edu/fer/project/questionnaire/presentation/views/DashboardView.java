package edu.fer.project.questionnaire.presentation.views;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import edu.fer.project.questionnaire.model.Answer;
import edu.fer.project.questionnaire.model.Game;
import edu.fer.project.questionnaire.model.Scenario;
import edu.fer.project.questionnaire.presentation.contracts.DashboardContract;
import edu.fer.project.questionnaire.presentation.presenters.DashboardPresenter;
import edu.fer.project.questionnaire.presentation.views.components.StylizedAnswersGrid;
import edu.fer.project.questionnaire.session.GameChangeListener;
import edu.fer.project.questionnaire.session.ScenarioChangeListener;
import java.time.format.DateTimeFormatter;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "dashboard", layout = MainLayout.class)
@UIScope
@PageTitle("Dashboard")
public class DashboardView extends VerticalLayout implements DashboardContract.View, GameChangeListener,
    ScenarioChangeListener {

  private AnswersGrid answersGrid = new AnswersGrid();

  private final DashboardContract.Presenter presenter;

  @Autowired
  public DashboardView(DashboardPresenter presenter) {
    initStyle();
    add(answersGrid);
    this.presenter = presenter;
  }

  private void initStyle() {
    setSizeFull();
  }

  @PostConstruct
  public void init() {
    presenter.setup(this);
  }

  @Override
  protected void onAttach(AttachEvent attachEvent) {
    subscribeToScenarioNotifier(this);
    subscribeToGameNotifier(this);
    presenter.attach();
  }

  @Override
  protected void onDetach(DetachEvent detachEvent) {
    unsubscribeFromScenarioNotifier(this);
    unsubscribeFromGameNotifier(this);
    presenter.detach();
  }

  @Override
  public void showLoading(boolean isLoading) {
  }

  @Override
  public void showError(Throwable throwable) {
    new Notification(throwable.getMessage(), 2000).open();
  }

  @Override
  public void gameChanged(Game game) {
    presenter.onGameChanged(game);
    answersGrid.renderWithoutGameColumn(game != null);
  }

  @Override
  public void scenarioChanged(Scenario scenario) {
    presenter.onScenarioChanged(scenario);
    answersGrid.renderWithoutScenarioColumn(scenario != null);
  }

  @Override
  public void setAnswersProvider(DataProvider<Answer, Object> provider) {
    answersGrid.setDataProvider(provider);
  }

  private static class AnswersGrid extends StylizedAnswersGrid {

    AnswersGrid() {
      initStyle();
      render();
    }

    private void initStyle() {
      setSizeFull();
    }

    /**
     * When game is selected, remove the game column.
     */
    void renderWithoutGameColumn(boolean doRender) {
      render();
      if (doRender) {
        removeColumnByKey("game");
      }
    }

    void renderWithoutScenarioColumn(boolean doRender) {
      render();
      if (doRender) {
        removeColumnByKey("game");
        removeColumnByKey("scenario");
      }
    }

    void render() {
      removeAllColumns();
      addColumn(answer -> answer.getTime().toLocalDateTime()
          .format(DateTimeFormatter.ofPattern("d. MMMM yyyy. HH:mm:ss")))
          .setHeader(new Html("<b>Time</b>"));
      addColumn(answer -> answer.getVoterInformation().getName())
          .setSortable(false)
          .setHeader(new Html("<b>User</b>"));
      addColumn(answer -> answer.getVoterInformation().getIp())
          .setSortable(false)
          .setHeader(new Html("<b>IP</b>"));
      addColumn(Answer::getGame)
          .setKey("game")
          .setHeader(new Html("<b>Game</b>"));
      addColumn(Answer::getScenario)
          .setKey("scenario")
          .setSortable(false)
          .setHeader(new Html("<b>Scenario</b>"));
      addColumn(answer -> answer.getQuestion().getText())
          .setSortable(false)
          .setHeader(new Html("<b>Question</b>"));
      addColumn(Answer::getChoice)
          .setSortable(false)
          .setHeader(new Html("<b>Score</b>"));

      setMultiSort(true);
      getColumns().forEach(e -> e.setAutoWidth(true));
      getColumns().forEach(e -> e.setResizable(true));
      getColumns().forEach(e -> e.setSortable(true));
    }

  }
}

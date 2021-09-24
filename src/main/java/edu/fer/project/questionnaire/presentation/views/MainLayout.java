package edu.fer.project.questionnaire.presentation.views;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.Tabs.Orientation;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.renderer.TextRenderer;
import com.vaadin.flow.router.PreserveOnRefresh;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.shared.communication.PushMode;
import com.vaadin.flow.spring.annotation.UIScope;
import edu.fer.project.questionnaire.model.Game;
import edu.fer.project.questionnaire.model.Scenario;
import edu.fer.project.questionnaire.presentation.contracts.MainContract;
import edu.fer.project.questionnaire.presentation.presenters.MainPresenter;
import edu.fer.project.questionnaire.presentation.views.components.EditableSelect;
import edu.fer.project.questionnaire.presentation.views.components.EditableSelect.EditableSelectListener;
import edu.fer.project.questionnaire.presentation.views.components.NavigationTab;
import edu.fer.project.questionnaire.presentation.views.components.buttons.AbstractButtonFactory;
import edu.fer.project.questionnaire.presentation.views.components.forms.AddGameForm;
import edu.fer.project.questionnaire.presentation.views.components.forms.AddScenarioForm;
import edu.fer.project.questionnaire.session.GameChangeListener;
import edu.fer.project.questionnaire.session.GameChangeNotifier;
import edu.fer.project.questionnaire.session.ScenarioChangeListener;
import edu.fer.project.questionnaire.session.ScenarioChangeNotifier;
import edu.fer.project.questionnaire.session.SessionUtils;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;

@Route("")
@UIScope
@Viewport("width=device-width, minimum-scale=1, initial-scale=1, user-scalable=yes, viewport-fit=cover")
@Push(PushMode.AUTOMATIC)
@PreserveOnRefresh
@PWA(name = "Administrator dashboard", shortName = "Dashboard")
public class MainLayout extends AppLayout implements MainContract.View, GameChangeListener,
    ScenarioChangeListener {

  private final MainContract.Presenter presenter;
  private final EditableSelect<Game> gameSelect;
  private final EditableSelect<Scenario> scenarioSelect;

  @Autowired
  public MainLayout(MainPresenter presenter) {
    gameSelect = new EditableSelect<>(new GameSelectListener());
    gameSelect.setPlaceholder("Select game");
    gameSelect.setRenderer(new TextRenderer<>(Game::getName));

    scenarioSelect = new EditableSelect<>(new ScenarioSelectListener());
    scenarioSelect.setPlaceholder("Select scenario");
    scenarioSelect.setRenderer(new TextRenderer<>(Scenario::getText));

    addToNavbar(new DrawerToggle());
    addToDrawer(gameSelect);
    addToDrawer(scenarioSelect);
    addToDrawer(createLogOutButton());
    addToNavbar(createMenu());

    this.presenter = presenter;
  }

  private Component createLogOutButton() {
    var logOutLayout = new HorizontalLayout(new Anchor("/logout", "Log out"));
    logOutLayout.setWidthFull();
    logOutLayout.setJustifyContentMode(JustifyContentMode.CENTER);
    logOutLayout.setAlignItems(Alignment.CENTER);
    return logOutLayout;
  }

  @PostConstruct
  public void init() {
    presenter.setup(this);
  }

  private FlexLayout createMenu() {
    FlexLayout navigationLayout = new FlexLayout();
    navigationLayout.setWidthFull();
    navigationLayout.setJustifyContentMode(JustifyContentMode.CENTER);
    Tabs navigation = createNavigation();
    navigationLayout.add(navigation);
    return navigationLayout;
  }

  private Tabs createNavigation() {
    Tabs tabs = new Tabs();
    tabs.setOrientation(Orientation.HORIZONTAL);
    tabs.add(new NavigationTab(VaadinIcon.DASHBOARD, "Dashboard", DashboardView.class));
    tabs.add(new NavigationTab(VaadinIcon.QUESTION_CIRCLE, "Questions", QuestionsView.class));
    tabs.add();
    return tabs;
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

  @Override
  public void showLoading(boolean isLoading) {
  }

  @Override
  public void showError(Throwable throwable) {
    new Notification(throwable.getMessage(), 2000).open();
  }

  @Override
  public void setGameProvider(DataProvider<Game, Object> provider) {
    gameSelect.setDataProvider(provider);
  }

  @Override
  public void gameAdded(Game game) {
    gameSelect.setValue(game);
  }

  @Override
  public void gameDeleted(boolean isDeleted) {
    if (!isDeleted) {
      showError(new RuntimeException("Failed to delete game."));
      return;
    }

    gameSelect.refresh();
  }

  @Override
  public void setScenarioProvider(DataProvider<Scenario, Object> provider) {
    scenarioSelect.setDataProvider(provider);
  }

  @Override
  public void scenarioAdded(Scenario scenario) {
    scenarioSelect.setValue(scenario);
  }


  @Override
  public void scenarioDeleted(boolean isDeleted) {
    if (!isDeleted) {
      showError(new RuntimeException("Failed to delete scenario."));
      return;
    }

    scenarioSelect.refresh();
  }

  @Override
  public void gameChanged(Game game) {
    if (game == null) {
      return;
    }

    presenter.onChangeGame(game.getId());
    gameSelect.setValue(game);
  }

  @Override
  public void scenarioChanged(Scenario scenario) {
    if (scenario == null) {
      return;
    }

    scenarioSelect.setValue(scenario);
  }

  private class GameSelectListener implements EditableSelectListener<Game> {

    @Override
    public void onRemoveClicked(Game game) {
      if (gameSelect.areNoneSelected()) {
        showError(new RuntimeException("No game selected."));
        return;
      }

      presenter.onDeleteGame(game.getId());
    }

    @Override
    public void onSelectionChange(Game game) {
      getGameNotifier().notify(game);
    }

    @Override
    public void onAddClicked() {
      new AddGameForm(presenter::onAddGame).open();
    }
  }

  private class ScenarioSelectListener implements EditableSelectListener<Scenario> {

    @Override
    public void onRemoveClicked(Scenario oldValue) {
      if (gameSelect.areNoneSelected()) {
        showError(new RuntimeException("No game selected."));
        return;
      }

      if (scenarioSelect.areNoneSelected()) {
        showError(new RuntimeException("No scenario selected."));
        return;
      }

      presenter.onDeleteScenario(gameSelect.getValue().getId(), oldValue.getId());
    }

    @Override
    public void onSelectionChange(Scenario scenario) {
      getScenarioNotifier().notify(scenario);
    }

    @Override
    public void onAddClicked() {
      if (gameSelect.areNoneSelected()) {
        showError(new RuntimeException("No game selected."));
        return;
      }
      new AddScenarioForm(gameSelect.getValue(), (game, text) -> presenter.onAddScenario(game.getId(), text)).open();
    }
  }

  private GameChangeNotifier getGameNotifier() {
    return SessionUtils.getAttribute("game.notifier");
  }

  private ScenarioChangeNotifier getScenarioNotifier() {
    return SessionUtils.getAttribute("scenario.notifier");
  }
}

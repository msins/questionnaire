package edu.fer.project.questionnaire.presentation.presenters;


import com.vaadin.flow.data.provider.AbstractBackEndDataProvider;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import edu.fer.project.questionnaire.model.Game;
import edu.fer.project.questionnaire.model.Scenario;
import edu.fer.project.questionnaire.presentation.contracts.MainContract;
import edu.fer.project.questionnaire.presentation.utils.EmptyProvider;
import edu.fer.project.questionnaire.services.GameService;
import edu.fer.project.questionnaire.services.ScenarioService;
import io.reactivex.disposables.CompositeDisposable;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;

@UIScope
@SpringComponent
public class MainPresenter implements MainContract.Presenter {

  private MainContract.View view;

  private final GameService gameService;
  private final ScenarioService scenarioService;

  private CompositeDisposable disposable;

  @Autowired
  public MainPresenter(GameService gameService, ScenarioService scenarioService) {
    this.gameService = gameService;
    this.scenarioService = scenarioService;
  }

  @Override
  public void setup(MainContract.View view) {
    this.view = view;
    view.setGameProvider(new GameProvider());
  }

  @Override
  public void subscribe() {
    disposable = new CompositeDisposable();
  }

  @Override
  public void unsubscribe() {
    if (disposable != null && !disposable.isDisposed()) {
      disposable.dispose();
      disposable = null;
    }
  }

  @Override
  public void error(Throwable throwable) {
    view.showError(throwable);
  }

  @Override
  public void attach() {
    subscribe();
    view.setGameProvider(new GameProvider());
  }

  @Override
  public void detach() {
    unsubscribe();
  }

  @Override
  public void onAddGame(String name) {
    disposable.add(
        gameService.addGame(name)
            .subscribe(newGame -> {
              view.setGameProvider(new GameProvider());
              view.setScenarioProvider(new ScenarioProvider(newGame.getId()));
              view.gameAdded(newGame);
            }, this::error)
    );
  }

  @Override
  public void onDeleteGame(long gameId) {
    disposable.add(
        gameService.deleteGame(gameId)
            .subscribe(isDeleted -> {
              view.setScenarioProvider(new EmptyProvider<>());
              view.gameDeleted(isDeleted);
            }, this::error)
    );
  }

  @Override
  public void onAddScenario(long gameId, String text) {
    disposable.add(
        scenarioService.addScenarioToGame(gameId, text)
            .subscribe(newScenario -> {
              view.setScenarioProvider(new ScenarioProvider(gameId));
              view.scenarioAdded(newScenario);
            }, this::error)
    );
  }

  @Override
  public void onDeleteScenario(long gameId, long scenarioId) {
    disposable.add(
        scenarioService.deleteScenario(scenarioId)
            .subscribe(isDeleted -> {
              view.setScenarioProvider(new ScenarioProvider(gameId));
              view.scenarioDeleted(isDeleted);
            }, this::error)
    );
  }

  @Override
  public void onChangeGame(long gameId) {
    view.setScenarioProvider(new ScenarioProvider(gameId));
  }

  private class GameProvider extends AbstractBackEndDataProvider<Game, Object> {

    @Override
    protected Stream<Game> fetchFromBackEnd(Query<Game, Object> query) {
      return gameService.fetchGames().blockingGet().stream();
    }

    @Override
    protected int sizeInBackEnd(Query<Game, Object> query) {
      return gameService.count();
    }
  }

  private class ScenarioProvider extends AbstractBackEndDataProvider<Scenario, Object> {

    private final long gameId;

    ScenarioProvider(long gameId) {
      this.gameId = gameId;
    }

    @Override
    protected Stream<Scenario> fetchFromBackEnd(Query<Scenario, Object> query) {
      return scenarioService.fetchScenariosForGame(gameId).blockingGet().stream();
    }

    @Override
    protected int sizeInBackEnd(Query<Scenario, Object> query) {
      return scenarioService.countForGame(gameId).blockingGet();
    }
  }

}

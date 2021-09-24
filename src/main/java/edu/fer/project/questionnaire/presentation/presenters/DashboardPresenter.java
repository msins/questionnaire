package edu.fer.project.questionnaire.presentation.presenters;

import com.vaadin.flow.data.provider.AbstractBackEndDataProvider;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import edu.fer.project.questionnaire.model.Answer;
import edu.fer.project.questionnaire.model.Game;
import edu.fer.project.questionnaire.model.Scenario;
import edu.fer.project.questionnaire.presentation.contracts.DashboardContract;
import edu.fer.project.questionnaire.presentation.utils.EmptyProvider;
import edu.fer.project.questionnaire.services.AnswerService;
import io.reactivex.disposables.CompositeDisposable;
import java.util.Objects;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;

@UIScope
@SpringComponent
public class DashboardPresenter implements DashboardContract.Presenter {

  private DashboardContract.View view;

  private final AnswerService answerService;

  private CompositeDisposable disposable;

  private Scenario scenario;
  private Game game;

  @Autowired
  public DashboardPresenter(AnswerService answerService) {
    this.answerService = answerService;
  }

  @Override
  public void setup(DashboardContract.View view) {
    this.view = view;
  }

  @Override
  public void onScenarioChanged(Scenario scenario) {
    this.scenario = scenario;
    setAnswersProvider();
  }

  @Override
  public void onGameChanged(Game game) {
    this.game = game;
    setAnswersProvider();
  }

  private void setAnswersProvider() {
    if (game == null) {
      view.setAnswersProvider(new EmptyProvider<>());
      return;
    }

    if (scenario == null) {
      view.setAnswersProvider(new GameAnswersProvider(game));
      return;
    }

    view.setAnswersProvider(new ScenarioAnswersProvider(scenario));
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
  }

  @Override
  public void detach() {
    unsubscribe();
  }

  private class GameAnswersProvider extends AbstractBackEndDataProvider<Answer, Object> {

    private final Game game;

    GameAnswersProvider(Game game) {
      this.game = Objects.requireNonNull(game);
    }

    @Override
    protected Stream<Answer> fetchFromBackEnd(Query<Answer, Object> query) {
      return answerService.fetchAnswersForGame(
              game.getId(),
              query.getOffset(),
              query.getLimit(),
              createSortFromQuery(query)
          )
          .blockingGet().stream();
    }

    @Override
    protected int sizeInBackEnd(Query<Answer, Object> query) {
      return answerService.countForGame(game.getId());
    }
  }

  private class ScenarioAnswersProvider extends AbstractBackEndDataProvider<Answer, Object> {

    private final Scenario scenario;

    ScenarioAnswersProvider(Scenario scenario) {
      this.scenario = Objects.requireNonNull(scenario);
    }

    @Override
    protected Stream<Answer> fetchFromBackEnd(Query<Answer, Object> query) {
      return answerService.fetchAnswersForScenario(
          scenario.getId(),
          query.getOffset(),
          query.getLimit(),
          createSortFromQuery(query)
      ).blockingGet().stream();
    }

    @Override
    protected int sizeInBackEnd(Query<Answer, Object> query) {
      return answerService.countForScenario(scenario.getId());
    }
  }

  private static Sort createSortFromQuery(Query<Answer, Object> query) {
    Sort sort = Sort.unsorted();
    for (var a : query.getSortOrders()) {
      sort.and(Sort.by(a.getDirection().toString(), a.getSorted()));
    }
    return sort;
  }
}

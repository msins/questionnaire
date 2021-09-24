package edu.fer.project.questionnaire.presentation.contracts;

import com.vaadin.flow.data.provider.DataProvider;
import edu.fer.project.questionnaire.model.Game;
import edu.fer.project.questionnaire.model.Scenario;
import edu.fer.project.questionnaire.presentation.BasePresenter;
import edu.fer.project.questionnaire.presentation.BaseView;
import edu.fer.project.questionnaire.presentation.views.components.EditableSelect;

public interface MainContract {

  interface View extends BaseView {

    void gameAdded(Game game);

    void gameDeleted(boolean isDeleted);

    void scenarioAdded(Scenario scenario);

    void scenarioDeleted(boolean isDeleted);

    void setScenarioProvider(DataProvider<Scenario, Object> provider);

    void setGameProvider(DataProvider<Game, Object> provider);
  }

  interface Presenter extends BasePresenter {

    void setup(View view);

    void onAddGame(String newGameText);

    void onDeleteGame(long gameId);

    void onAddScenario(long gameId, String text);

    void onDeleteScenario(long gameId, long scenarioId);

    void onChangeGame(long gameId);
  }
}

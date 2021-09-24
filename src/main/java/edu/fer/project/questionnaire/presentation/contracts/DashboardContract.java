package edu.fer.project.questionnaire.presentation.contracts;

import com.vaadin.flow.data.provider.DataProvider;
import edu.fer.project.questionnaire.model.Answer;
import edu.fer.project.questionnaire.model.Game;
import edu.fer.project.questionnaire.model.Scenario;
import edu.fer.project.questionnaire.presentation.BasePresenter;
import edu.fer.project.questionnaire.presentation.BaseView;

public interface DashboardContract {

  interface View extends BaseView {

    void setAnswersProvider(DataProvider<Answer, Object> provider);
  }

  interface Presenter extends BasePresenter {

    void setup(View view);

    void onScenarioChanged(Scenario scenario);

    void onGameChanged(Game game);
  }
}

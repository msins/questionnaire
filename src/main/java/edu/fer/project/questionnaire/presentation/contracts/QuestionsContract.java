package edu.fer.project.questionnaire.presentation.contracts;

import com.vaadin.flow.data.provider.DataProvider;
import edu.fer.project.questionnaire.dtos.requests.AddQuestionRequest;
import edu.fer.project.questionnaire.model.Answer;
import edu.fer.project.questionnaire.model.Question;
import edu.fer.project.questionnaire.model.Scenario;
import edu.fer.project.questionnaire.presentation.BasePresenter;
import edu.fer.project.questionnaire.presentation.BaseView;
import java.util.List;

public interface QuestionsContract {

  interface View extends BaseView {

    void questionDeleted(boolean isDeleted);

    void questionSynced(Question question);

    void setQuestionsProvider(DataProvider<Question, Object> questionsProvider);

    void setAnswers(List<Answer> answers);

    void updateChart(Number[] results);
  }

  interface Presenter extends BasePresenter {

    void setup(View view);

    void onQuestionAdded(long scenarioId, AddQuestionRequest request);

    void onScenarioChanged(Scenario scenario);

    void onDeleteQuestion(Question question);

    void onRefreshQuestion(Question question);

    void onSyncQuestion(Question question, String newText);
  }
}

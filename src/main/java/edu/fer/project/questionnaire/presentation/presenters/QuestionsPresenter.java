package edu.fer.project.questionnaire.presentation.presenters;

import com.vaadin.flow.data.provider.AbstractBackEndDataProvider;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import edu.fer.project.questionnaire.dtos.requests.AddQuestionRequest;
import edu.fer.project.questionnaire.model.Answer;
import edu.fer.project.questionnaire.model.Choice;
import edu.fer.project.questionnaire.model.Question;
import edu.fer.project.questionnaire.model.Scenario;
import edu.fer.project.questionnaire.presentation.contracts.QuestionsContract;
import edu.fer.project.questionnaire.presentation.utils.EmptyProvider;
import edu.fer.project.questionnaire.services.AnswerService;
import edu.fer.project.questionnaire.services.QuestionService;
import io.reactivex.disposables.CompositeDisposable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

@UIScope
@SpringComponent
public class QuestionsPresenter implements QuestionsContract.Presenter {

  private QuestionsContract.View view;

  private final QuestionService questionService;
  private final AnswerService answerService;

  private CompositeDisposable disposable;

  @Autowired
  public QuestionsPresenter(QuestionService questionService, AnswerService answerService) {
    this.questionService = questionService;
    this.answerService = answerService;
  }

  @Override
  public void setup(QuestionsContract.View view) {
    this.view = view;
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

  @Override
  public void onDeleteQuestion(Question question) {
    disposable.add(
        questionService.removeQuestion(question.getId())
            .subscribe(view::questionDeleted, view::showError)
    );
  }

  @Override
  public void onRefreshQuestion(Question question) {
    disposable.add(
        // TODO: paging is supported, but not implemented here
        answerService.fetchAnswersForQuestion(question.getId(),
                0,
                Integer.MAX_VALUE,
                Sort.by(Direction.DESC, "time")
            )
            .subscribe(
                answers -> {
                  view.setAnswers(answers.getContent());
                  // TODO: should be calculated directly in sql
                  view.updateChart(results(answers.getContent(), question));
                },
                view::showError
            )
    );
  }

  // TODO: do in sql
  private Number[] results(List<Answer> answers, Question question) {
    Map<Choice, Integer> counter = new TreeMap<>();
    // init to zero
    question.getChoices().forEach(choice -> counter.put(choice, 0));
    // count
    answers.forEach(answer -> counter.merge(answer.getChoice(), 1, Integer::sum));

    return counter.values().stream()
        .map(votes -> toPercentage(votes, answers.size()))
        .toArray(Number[]::new);
  }

  private static int toPercentage(int votes, int totalVotes) {
    if (totalVotes == 0) {
      return 0;
    }
    return (int) (((double) votes / totalVotes) * 100);
  }

  @Override
  public void onSyncQuestion(Question question, String newText) {
    question.setText(newText);
    disposable.add(
        questionService.updateQuestion(question)
            .subscribe(view::questionSynced, view::showError)
    );
  }

  @Override
  public void onQuestionAdded(long scenarioId, AddQuestionRequest request) {
    disposable.add(
        questionService.addQuestion(scenarioId, request)
            .subscribe(question -> {
              view.setQuestionsProvider(new QuestionsProvider(scenarioId));
            }, view::showError)
    );
  }

  @Override
  public void onScenarioChanged(Scenario scenario) {
    if (scenario == null) {
      view.setQuestionsProvider(new EmptyProvider<>());
      return;
    }

    view.setQuestionsProvider(new QuestionsProvider(scenario.getId()));
  }

  private class QuestionsProvider extends AbstractBackEndDataProvider<Question, Object> {

    private final long scenarioId;

    QuestionsProvider(long scenarioId) {
      this.scenarioId = scenarioId;
    }

    @Override
    protected Stream<Question> fetchFromBackEnd(Query<Question, Object> query) {
      return questionService.fetchQuestionsForScenario(scenarioId).blockingGet().stream();
    }

    @Override
    protected int sizeInBackEnd(Query<Question, Object> query) {
      return questionService.countForScenario(scenarioId).blockingGet();
    }
  }
}

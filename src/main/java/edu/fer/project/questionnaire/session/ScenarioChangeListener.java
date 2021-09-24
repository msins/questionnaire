package edu.fer.project.questionnaire.session;

import com.vaadin.flow.server.VaadinSession;
import edu.fer.project.questionnaire.model.Scenario;

public interface ScenarioChangeListener {

  void scenarioChanged(Scenario scenario);

  default void subscribeToScenarioNotifier(ScenarioChangeListener listener) {
    ScenarioChangeNotifier notifier = SessionUtils.getAttribute("scenario.notifier");
    if (notifier == null) {
      notifier = new ScenarioChangeNotifier();
      SessionUtils.setAttribute("scenario.notifier", notifier);
    }
    notifier.registerListener(listener);
    if (notifier.getScenario() != null) {
      scenarioChanged(notifier.getScenario());
    }
  }

  default void unsubscribeFromScenarioNotifier(ScenarioChangeListener listener) {
    ScenarioChangeNotifier notifier = SessionUtils.getAttribute("scenario.notifier");
    if (notifier != null) {
      notifier.unregisterListener(listener);
    }
  }
}
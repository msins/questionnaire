package edu.fer.project.questionnaire.session;

import edu.fer.project.questionnaire.model.Scenario;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class ScenarioChangeNotifier {

  private final Set<ScenarioChangeListener> listeners = new CopyOnWriteArraySet<>();
  private Scenario scenario;

  public void registerListener(ScenarioChangeListener listener) {
    listeners.add(listener);
  }

  public void unregisterListener(ScenarioChangeListener listener) {
    listeners.remove(listener);
  }

  public void notify(Scenario scenario) {
    this.scenario = scenario;
    for (ScenarioChangeListener l : listeners) {
      l.scenarioChanged(scenario);
    }
  }

  public Scenario getScenario() {
    return scenario;
  }
}
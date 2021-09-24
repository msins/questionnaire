package edu.fer.project.questionnaire.session;

import edu.fer.project.questionnaire.model.Game;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class GameChangeNotifier {

  private final Set<GameChangeListener> listeners = new CopyOnWriteArraySet<>();
  private Game game;

  public void registerListener(GameChangeListener listener) {
    listeners.add(listener);
  }

  public void unregisterListener(GameChangeListener listener) {
    listeners.remove(listener);
  }

  public void notify(Game game) {
    this.game = game;
    for (GameChangeListener l : listeners) {
      l.gameChanged(game);
    }
  }

  public Game getGame() {
    return game;
  }
}
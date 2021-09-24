package edu.fer.project.questionnaire.session;

import edu.fer.project.questionnaire.model.Game;

public interface GameChangeListener {

  void gameChanged(Game game);

  default void subscribeToGameNotifier(GameChangeListener listener) {
    GameChangeNotifier notifier = SessionUtils.getAttribute("game.notifier");
    if (notifier == null) {
      notifier = new GameChangeNotifier();
      SessionUtils.setAttribute("game.notifier", notifier);
    }
    notifier.registerListener(listener);
    if (notifier.getGame() != null) {
      gameChanged(notifier.getGame());
    }
  }

  default void unsubscribeFromGameNotifier(GameChangeListener listener) {
    GameChangeNotifier notifier = SessionUtils.getAttribute("game.notifier");
    if (notifier != null) {
      notifier.unregisterListener(listener);
    }
  }
}
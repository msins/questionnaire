package edu.fer.project.questionnaire.repositories;

import edu.fer.project.questionnaire.model.Game;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {

  boolean existsByName(String name);

  @Query("SELECT game FROM Game game"
      + " LEFT JOIN FETCH game.scenarios scenario"
      + " WHERE game.id = :gameId")
  Optional<Game> findByIdWithScenarios(long gameId);

  @Query("SELECT game FROM Game game"
      + " LEFT JOIN FETCH game.scenarios scenario"
      + " LEFT JOIN FETCH scenario.questions"
      + " WHERE game.id = :gameId")
  Optional<Game> findByIdEager(long gameId);
}

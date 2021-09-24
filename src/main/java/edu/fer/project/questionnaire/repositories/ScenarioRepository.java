package edu.fer.project.questionnaire.repositories;

import edu.fer.project.questionnaire.model.Game;
import edu.fer.project.questionnaire.model.Scenario;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ScenarioRepository extends JpaRepository<Scenario, Long> {

  boolean existsByText(String text);

  @Query("SELECT scenario FROM Scenario scenario"
      + " LEFT JOIN FETCH scenario.questions question"
      + " WHERE scenario.id = :scenarioId")
  Optional<Scenario> findByIdWithQuestions(long scenarioId);
}

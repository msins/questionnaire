package edu.fer.project.questionnaire.repositories;

import edu.fer.project.questionnaire.model.Question;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

  List<Question> findByScenarioId(long scenarioId);

  @Query("SELECT DISTINCT question FROM Question question"
      + " LEFT JOIN FETCH question.choices choice"
      + " WHERE question.scenario.id = :scenarioId")
  List<Question> findByScenarioIdWithChoices(long scenarioId);

  int countQuestionsByScenarioId(long scenarioId);
}

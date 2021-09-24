package edu.fer.project.questionnaire.repositories;

import edu.fer.project.questionnaire.model.Answer;
import java.util.List;
import javax.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerRepository extends PagingAndSortingRepository<Answer, Long> {

  Page<Answer> findAllByGameId(long gameId, Pageable pageable);

  Page<Answer> findAllByScenarioId(long scenarioId, Pageable pageable);

  Page<Answer> findAllByQuestionId(long questionId, Pageable pageable);

  int countAnswerByGameId(long gameId);

  int countAnswerByScenarioId(long scenarioId);

  int countAnswerByQuestionId(long questionId);
}

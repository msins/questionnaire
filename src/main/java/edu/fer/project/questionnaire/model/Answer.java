package edu.fer.project.questionnaire.model;

import java.sql.Timestamp;
import java.util.Objects;
import java.util.StringJoiner;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Answer {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;

  @Column
  private Timestamp time;

  @Embedded
  private VoterInformation voterInformation;

  @ManyToOne
  private Game game;

  @ManyToOne
  private Scenario scenario;

  @ManyToOne
  private Question question;

  @ManyToOne
  private Choice choice;

  public Answer() {
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Game getGame() {
    return game;
  }

  public void setGame(Game game) {
    this.game = game;
  }

  public Scenario getScenario() {
    return scenario;
  }

  public void setScenario(Scenario scenario) {
    this.scenario = scenario;
  }

  public Question getQuestion() {
    return question;
  }

  public void setQuestion(Question question) {
    this.question = question;
  }

  public Choice getChoice() {
    return choice;
  }

  public void setChoice(Choice choice) {
    this.choice = choice;
  }

  public Timestamp getTime() {
    return time;
  }

  public void setTime(Timestamp time) {
    this.time = time;
  }

  public VoterInformation getVoterInformation() {
    return voterInformation;
  }

  public void setVoterInformation(VoterInformation voterInformation) {
    this.voterInformation = voterInformation;
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", Answer.class.getSimpleName() + "[", "]")
        .add("id=" + id)
        .add("time=" + time)
        .add("user=" + voterInformation)
        .add("game=" + game.getName())
        .add("scenario=" + scenario.getText())
        .add("question=" + question.getText())
        .add("choice=" + choice.getText())
        .toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Answer)) {
      return false;
    }
    Answer answer = (Answer) o;
    return Objects.equals(time, answer.time) && Objects.equals(voterInformation, answer.voterInformation);
  }

  @Override
  public int hashCode() {
    return Objects.hash(time, voterInformation);
  }
}

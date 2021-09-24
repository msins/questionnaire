package edu.fer.project.questionnaire.model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Scenario {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;

  @Column
  private String text;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "game_id")
  private Game game;

  @OneToMany(mappedBy = "scenario", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<Question> questions = new HashSet<>();

  public Scenario() {
  }

  public static Scenario fromText(String text) {
    Scenario scenario = new Scenario();
    scenario.setText(text);
    return scenario;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getId() {
    return id;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public Game getGame() {
    return game;
  }

  public void setGame(Game game) {
    this.game = game;
  }

  public Set<Question> getQuestions() {
    return questions;
  }

  public void addQuestion(Question question) {
    this.questions.add(question);
    question.setScenario(this);
  }

  public void removeQuestion(Question question) {
    this.questions.remove(question);
    question.setScenario(null);
  }

  @Override
  public String toString() {
    return text;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Scenario)) {
      return false;
    }
    Scenario scenario = (Scenario) o;
    return Objects.equals(text, scenario.text);
  }

  @Override
  public int hashCode() {
    return Objects.hash(text);
  }
}

package edu.fer.project.questionnaire.model;

import java.util.Comparator;
import java.util.Objects;
import java.util.SortedSet;
import java.util.TreeSet;
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
import javax.persistence.OrderBy;

@Entity
public class Question {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;

  @Column
  private String text;

  @Column
  private Type type;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "scenario_id")
  private Scenario scenario;

  @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
  @OrderBy("ordering ASC")
  private SortedSet<Choice> choices = new TreeSet<>(Comparator.comparingInt(Choice::getOrdering));

  public Question() {
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getId() {
    return id;
  }

  public void setText(String text) {
    this.text = text;
  }

  public String getText() {
    return text;
  }

  public Type getType() {
    return type;
  }

  public void setType(Type type) {
    this.type = type;
  }

  public Scenario getScenario() {
    return scenario;
  }

  public void setScenario(Scenario scenario) {
    this.scenario = scenario;
  }

  public SortedSet<Choice> getChoices() {
    return choices;
  }

  public void addChoice(Choice choice) {
    this.choices.add(choice);
    choice.setQuestion(this);
  }

  public void removeChoice(Choice choice) {
    this.choices.remove(choice);
    choice.setQuestion(null);
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
    if (!(o instanceof Question)) {
      return false;
    }
    Question question = (Question) o;
    return Objects.equals(text, question.text);
  }

  @Override
  public int hashCode() {
    return Objects.hash(text);
  }

  public enum Type {
    MULTIPLE_CHOICE("Multiple choices"),
    SCALING("Scaling question");

    @Column
    private String name;

    Type(String name) {
      this.name = name;
    }

    public String getName() {
      return name;
    }
  }
}

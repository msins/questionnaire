package edu.fer.project.questionnaire.model;

import java.util.Comparator;
import java.util.Objects;
import java.util.StringJoiner;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Choice implements Comparable<Choice> {

  private static final Comparator<Choice> COMPARING_BY_ORDER = Comparator.comparingInt(Choice::getOrdering);

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;

  @Column
  private String text;

  /**
   * Can be used to always deduce order of choices, ex. scaling question [bad, neutral, good] will have order of [1, 2,
   * 3] so anyone using the API can always arrange then properly on their gui.
   */
  @Column
  private Integer ordering;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "question_id")
  private Question question;

  public Choice() {
  }

  public static Choice fromTextAndOrdering(String text, int order) {
    Choice choice = new Choice();
    choice.text = text;
    choice.ordering = order;
    return choice;
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

  public void setOrdering(Integer order) {
    this.ordering = order;
  }

  public Integer getOrdering() {
    return ordering;
  }

  public Question getQuestion() {
    return question;
  }

  public void setQuestion(Question question) {
    this.question = question;
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
    if (!(o instanceof Choice)) {
      return false;
    }
    Choice choice = (Choice) o;
    return Objects.equals(text, choice.text);
  }

  @Override
  public int hashCode() {
    return Objects.hash(text);
  }

  @Override
  public int compareTo(Choice other) {
    return COMPARING_BY_ORDER.compare(this, other);
  }
}

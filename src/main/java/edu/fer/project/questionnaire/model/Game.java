package edu.fer.project.questionnaire.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Game {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;

  @Column(unique = true, nullable = false)
  private String name;

  @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Scenario> scenarios = new ArrayList<>();

  public Game() {
  }

  public static Game fromName(String name) {
    Game game = new Game();
    game.setName(name);
    return game;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void addScenario(Scenario scenario) {
    scenarios.add(scenario);
    scenario.setGame(this);
  }

  public void removeScenario(Scenario scenario) {
    scenarios.remove(scenario);
    scenario.setGame(null);
  }

  public List<Scenario> getScenarios() {
    return scenarios;
  }

  @Override
  public String toString() {
    return name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Game)) {
      return false;
    }
    Game game = (Game) o;
    return Objects.equals(name, game.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name);
  }
}

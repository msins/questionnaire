package edu.fer.project.questionnaire;

import java.sql.Timestamp;
import java.time.Instant;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;

@SpringBootApplication(exclude = ErrorMvcAutoConfiguration.class)
public class QuestionnaireApplication {

  public static void main(String[] args) {
    SpringApplication.run(QuestionnaireApplication.class, args);
  }
}

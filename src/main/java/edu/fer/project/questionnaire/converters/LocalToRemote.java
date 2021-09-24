package edu.fer.project.questionnaire.converters;

import java.util.List;

public interface LocalToRemote<Local, Remote> {

  List<Remote> localToRemote(List<Local> items);

  Remote localToRemote(Local item);
}

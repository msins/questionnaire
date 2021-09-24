package edu.fer.project.questionnaire.converters;

public interface RemoteToLocal<Remote, Local> {

  Local remoteToLocal(Remote item);
}

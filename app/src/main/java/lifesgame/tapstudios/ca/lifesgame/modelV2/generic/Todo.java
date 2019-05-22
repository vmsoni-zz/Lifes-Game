package lifesgame.tapstudios.ca.lifesgame.modelV2.generic;

import lifesgame.tapstudios.ca.lifesgame.modelV2.TodoTypes;

/**
 * Created by viditsoni on 2019-05-04.
 */

public interface Todo {
    long getId();
    String getTitle();
    String getDescription();
    long getSilver();
    long getTagId();
    TodoTypes.TodoType getTodoType();
}

package lifesgame.tapstudios.ca.lifesgame;

/**
 * Created by viditsoni on 2017-12-29.
 */

public enum TodoType {
    TASK("Task", 0),
    GOAL("Goal", 1),
    DAILY("Daily", 2);

    private String todoTypeString;
    private Integer orderValue;

    TodoType(String todoTypeString, Integer orderValue) {
        this.todoTypeString = todoTypeString;
        this.orderValue = orderValue;
    }

    public String getTodoTypeString() {
        return this.todoTypeString;
    }

    public Integer getOrderValue() {
        return this.orderValue;
    }
}
package lifesgame.tapstudios.ca.lifesgame.model;

import java.util.Map;
import java.text.SimpleDateFormat;
import java.util.Date;

import lifesgame.tapstudios.ca.lifesgame.TodoType;

/**
 * Created by Vidit Soni on 8/5/2017.
 */
public class GoalsAndTasks {
    private String title;
    private String description;
    private String category;
    private Long id;
    private Integer silver;
    private Date deadlineDate;
    private Date completionDate;
    private Date creationDate;
    private Boolean completed;
    private Boolean deleted;
    private Map<String, Boolean> improvementType;

    public GoalsAndTasks(String title, String description, String category, Long id, Integer silver, Map<String, Boolean> improvementType, Date deadlineDate, Date completionDate, Date creationDate, Boolean completed) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.id = id;
        this.silver = silver;
        this.improvementType = improvementType;
        this.deadlineDate = deadlineDate;
        this.completionDate = completionDate;
        this.completed = completed;
        this.creationDate = creationDate;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public TodoType getCategory() {
        TodoType typeToReturn;
        switch (category) {
            case "Task":
                typeToReturn = TodoType.TASK;
                break;
            case "Goal":
                typeToReturn = TodoType.GOAL;
                break;
            case "Daily":
                typeToReturn = TodoType.DAILY;
                break;
            default:
                typeToReturn = TodoType.TASK;
                break;
        }
        return typeToReturn;
    }

    public Date getDeadlineDate() {
        return deadlineDate;
    }

    public String getDeadlineDateString() {
        if (deadlineDate == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
        return sdf.format(deadlineDate);
    }

    public Date getCompletionDate() {
        return completionDate == null ? new Date() : completionDate;
    }

    public String getCompletionDateString() {
        if (completionDate == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");

        return sdf.format(completionDate);
    }

    public Date getCreationDate() {
        return creationDate == null ? new Date() : creationDate;
    }

    public String getCreationDateString() {
        if (creationDate == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");

        return sdf.format(creationDate);
    }

    public Long getId() {
        return id;
    }

    public Integer getSilver() {
        return silver;
    }

    public void setToDoType(String type, Boolean value) {
        improvementType.put(type, value);
    }

    public Boolean getToDoType(String type) {
        return improvementType.get(type);
    }

    public Map<String, Boolean> getImprovementTypeMap() {
        return improvementType;
    }

    public Boolean getCompleted() {
        return completed;
    }
}

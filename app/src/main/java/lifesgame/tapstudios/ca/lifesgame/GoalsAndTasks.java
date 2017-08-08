package lifesgame.tapstudios.ca.lifesgame;

/**
 * Created by Vidit Soni on 8/5/2017.
 */
public class GoalsAndTasks {
    private String title;
    private String description;
    private String category;
    private Long id;
    private Long silver;

    public GoalsAndTasks(String title, String description, String category, Long id, Long silver) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.id = id;
        this.silver = silver;
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

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public Long getId() {
        return id;
    }

    public Long getSilver() { return  silver; }
}

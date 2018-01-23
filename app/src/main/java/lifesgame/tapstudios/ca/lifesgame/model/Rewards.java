package lifesgame.tapstudios.ca.lifesgame.model;

/**
 * Created by viditsoni on 2017-12-28.
 */

public class Rewards {
    private Integer id;
    private String title;
    private String description;
    private Integer cost;
    private Boolean unlimitedConsumption;
    private String styleColor;

    public Rewards(Integer id, String title, String description, Integer cost, Boolean unlimitedConsumption, String styleColor) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.cost = cost;
        this.unlimitedConsumption = unlimitedConsumption;
        this.styleColor = styleColor;
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Integer getCost() {
        return cost;
    }

    public Boolean getUnlimitedConsumption() {
        return unlimitedConsumption;
    }

    public String getStyleColor() {
        return styleColor;
    }

}

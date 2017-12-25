package lifesgame.tapstudios.ca.lifesgame;

/**
 * Created by viditsoni on 2017-12-23.
 */

public enum StatisticFilters {
    DAILY("Today"),
    WEEKLY("This Week"),
    MONTHLY("This Year");

    private String dateRange;
    StatisticFilters(String dateRange) {
        this.dateRange = dateRange;
    }

    public String getDateRange() {
        return dateRange;
    }
}


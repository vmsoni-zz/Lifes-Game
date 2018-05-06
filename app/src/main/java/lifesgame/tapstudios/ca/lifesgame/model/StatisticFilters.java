package lifesgame.tapstudios.ca.lifesgame.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by viditsoni on 2017-12-23.
 */

public enum StatisticFilters {
    DAILY(""),
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


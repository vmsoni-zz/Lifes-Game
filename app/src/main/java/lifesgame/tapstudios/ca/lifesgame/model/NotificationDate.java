package lifesgame.tapstudios.ca.lifesgame.model;

import java.util.Date;

/**
 * Created by viditsoni on 2018-04-13.
 */

public class NotificationDate {
    private int hour;
    private int minute;
    private long milliseconds;
    private Date notificationDate;

    public NotificationDate(int hour, int minute, long milliseconds, Date notificationDate) {
        this.hour = hour;
        this.minute = minute;
        this.milliseconds = milliseconds;
        this.notificationDate = notificationDate;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public void setSeconds(int minute) {
        this.minute = minute;
    }

    public void setMilliseconds(long milliseconds) {
        this.milliseconds = milliseconds;
    }

    public void setNotificationDate(Date notificationDate) {
        this.notificationDate = notificationDate;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public long getMilliseconds() {
        return milliseconds;
    }

    public Date getNotificationDate() {
        return notificationDate;
    }
}

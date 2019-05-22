package lifesgame.tapstudios.ca.lifesgame.modelV2;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import java.util.Date;

import lifesgame.tapstudios.ca.lifesgame.modelV2.converter.TodoTypeConverter;

/**
 * Created by viditsoni on 2019-03-06.
 */

@Entity
public class TodoTag {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "tag_TagId")
    public long tagId;

    @ColumnInfo(name = "tag_Work")
    public boolean Work;
    @ColumnInfo(name = "tag_Exercise")
    public boolean Exercise;
    @ColumnInfo(name = "tag_HealthWellness")
    public boolean HealthWellness;
    @ColumnInfo(name = "tag_School")
    public boolean School;
    @ColumnInfo(name = "tag_Teams")
    public boolean Teams;
    @ColumnInfo(name = "tag_Chores")
    public boolean Chores;
    @ColumnInfo(name = "tag_Creativity")
    public boolean Creativity;
    @ColumnInfo(name = "tag_Home")
    public boolean Home;
    @ColumnInfo(name = "tag_Other")
    public boolean Other;

    public TodoTag() {}

    @Ignore
    public TodoTag(
            boolean Work,
            boolean Exercise,
            boolean HealthWellness,
            boolean School,
            boolean Teams,
            boolean Chores,
            boolean Creativity,
            boolean Home,
            boolean Other) {
        this.Work = Work;
        this.Exercise = Exercise;
        this.HealthWellness = HealthWellness;
        this.School = School;
        this.Teams = Teams;
        this.Chores = Chores;
        this.Creativity = Creativity;
        this.Home = Home;
        this.Other = Other;
    }

    public enum Tag {
        WORK,
        EXERCISE,
        HEALTH_WELLNESS,
        SCHOOL,
        TEAMS,
        CHORES,
        CREATIVITY,
        HOME,
        OTHER
    }

    public long getTagId() {
        return tagId;
    }

    public boolean isWork() {
        return Work;
    }

    public boolean isExercise() {
        return Exercise;
    }

    public boolean isHealthWellness() {
        return HealthWellness;
    }

    public boolean isSchool() {
        return School;
    }

    public boolean isTeams() {
        return Teams;
    }

    public boolean isChores() {
        return Chores;
    }

    public boolean isCreativity() {
        return Creativity;
    }

    public boolean isHome() {
        return Home;
    }

    public boolean isOther() {
        return Other;
    }
}

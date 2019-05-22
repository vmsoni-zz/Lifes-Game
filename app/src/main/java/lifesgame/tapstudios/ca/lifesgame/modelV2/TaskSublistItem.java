package lifesgame.tapstudios.ca.lifesgame.modelV2;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import org.joda.time.DateTime;

/**
 * Created by viditsoni on 2019-03-06.
 */

@Entity
public class TaskSublistItem {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "sublistItem_SublistId")
    public long sublistId;

    @ForeignKey(entity = TaskTodo.class, parentColumns = "TaskId" , childColumns = "TaskId")
    @ColumnInfo(name = "sublistItem_ParentId")
    public long parentId;

    @ColumnInfo(name = "sublistItem_Title")
    public String title;

    public TaskSublistItem() {}

    @Ignore
    public TaskSublistItem(long taskId, String title) {
        this.parentId = taskId;
        this.title = title;
    }

    public long getSublistId() {
        return sublistId;
    }

    public void setSublistId(long sublistId) {
        this.sublistId = sublistId;
    }

    public long getParentId() {
        return parentId;
    }

    public void setParentId(long taskId) {
        this.parentId = taskId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

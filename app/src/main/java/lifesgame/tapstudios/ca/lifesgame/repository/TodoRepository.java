package lifesgame.tapstudios.ca.lifesgame.repository;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import lifesgame.tapstudios.ca.lifesgame.LifesGameDatabase;
import lifesgame.tapstudios.ca.lifesgame.modelV2.DailyTodo;
import lifesgame.tapstudios.ca.lifesgame.modelV2.HabitTodo;
import lifesgame.tapstudios.ca.lifesgame.modelV2.JoinModel.TaskTodoAndTag;
import lifesgame.tapstudios.ca.lifesgame.modelV2.TaskSublistItem;
import lifesgame.tapstudios.ca.lifesgame.modelV2.TaskTodo;
import lifesgame.tapstudios.ca.lifesgame.modelV2.TodoTag;
import lifesgame.tapstudios.ca.lifesgame.modelV2.dao.DailyDao;
import lifesgame.tapstudios.ca.lifesgame.modelV2.dao.HabitDao;
import lifesgame.tapstudios.ca.lifesgame.modelV2.dao.TagDao;
import lifesgame.tapstudios.ca.lifesgame.modelV2.dao.TaskDao;
import lifesgame.tapstudios.ca.lifesgame.modelV2.dao.TaskSublistDao;

/**
 * Created by viditsoni on 2019-03-07.
 */

public class TodoRepository extends AndroidViewModel{

    private LifesGameDatabase lifesGameDatabase;

    private DailyDao dailyDao;
    private TaskDao taskDao;
    private HabitDao habitDao;
    private TaskSublistDao taskSublistDao;
    private TagDao tagDao;
    private ExecutorService executorService;


    public TodoRepository(@NonNull Application application) {
        super(application);

        lifesGameDatabase = LifesGameDatabase.getAppDatabase(application);
        executorService = Executors.newSingleThreadExecutor();

        dailyDao = lifesGameDatabase.dailyDao();
        taskDao = lifesGameDatabase.taskDao();
        habitDao = lifesGameDatabase.habitDao();
        tagDao = lifesGameDatabase.tagDao();
        taskSublistDao = lifesGameDatabase.taskSublistDao();
    }

    //Insertion methods
    public void insertDailyTodo(DailyTodo dailyTodo) {
        executorService.execute(() -> dailyDao.insertDailyTodo(dailyTodo));
    }

    public void insertTaskTodo(TodoTag todoTag, boolean hasSublist, List<TaskSublistItem> taskSublists, TaskTodo taskTodo) {
        executorService.execute(() -> {
            long tagId = tagDao.insertTodoTag(todoTag);
            taskTodo.setTagId(tagId);
            long taskId = taskDao.insertTaskTodo(taskTodo);
            if (hasSublist) {
                for (TaskSublistItem taskSublist : taskSublists) {
                    taskSublist.setParentId(taskId);
                }
                taskSublistDao.insertTaskSublist(taskSublists);
            }
        });
    }

    public void insertHabitTodo(HabitTodo habitTodo) {
        executorService.execute(() -> habitDao.insertHabitTodo(habitTodo));
    }

    public void insertTodoTag(TodoTag todoTag) {
        executorService.execute(() -> tagDao.insertTodoTag(todoTag));
    }

    //Deletion methods
    public void deleteDailyTodo(DailyTodo dailyTodo) {
        executorService.execute(() -> dailyDao.deleteDailyTodo(dailyTodo));
    }

    public void deleteTaskTodo(TaskTodo taskTodo) {
        executorService.execute(() -> taskDao.deleteTaskTodo(taskTodo));
    }

    public void deleteHabitTodo(HabitTodo habitTodo) {
        executorService.execute(() -> habitDao.deleteHabitTodo(habitTodo));
    }

    //Getter Methods
    //Single Getters
    public LiveData<DailyTodo> getDailyTodo(long dailyId) {
        return dailyDao.loadDailyTodoById(dailyId);
    }

    public LiveData<TaskTodo> getTaskTodo(long taskId) {
        return taskDao.loadTaskTodoById(taskId);
    }

    public LiveData<HabitTodo> getHabitTodo(long habitId) {
        return habitDao.loadHabitTodoById(habitId);
    }

    //All Getters
    public LiveData<List<DailyTodo>> getAllDailyTodo() {
        return dailyDao.loadAllDailyTodo();
    }

    public LiveData<List<TaskTodoAndTag>> getAllTaskTodo() {
        return taskDao.loadAllTaskTodo();
    }

    public LiveData<List<HabitTodo>> getAllHabitTodo() {
        return habitDao.loadAllHabitTodo();
    }
}

package com.bawp.todoister.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.bawp.todoister.model.TaskModel;
import com.bawp.todoister.util.TaskRoomDatabase;

import java.util.List;

public class TaskRepository {

    private final TaskDao taskDao;
    private final LiveData<List<TaskModel>> allTasks;

    public TaskRepository(Application application){
        TaskRoomDatabase db = TaskRoomDatabase.getAllData(application);
        taskDao = db.taskDao();
        allTasks = taskDao.getAllTasks();
    }
    public void createTask(TaskModel taskModel){
        taskDao.createTask(taskModel);
    }
    public void updateTask(TaskModel taskModel){
        taskDao.updateTask(taskModel);
    }
    public LiveData<TaskModel> getSingleTask(long id){
       return taskDao.getSingleTask(id);
    }
    public LiveData<List<TaskModel>> getAllTasks(){
        return allTasks;
    }
    public void deleteSingleTask(TaskModel taskModel){
        taskDao.deleteTask(taskModel);
    }
    public void deleteAllTasks(){
        taskDao.deleteAllTasks();
    }

}

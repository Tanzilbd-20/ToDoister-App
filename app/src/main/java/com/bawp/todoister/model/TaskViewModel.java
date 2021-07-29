package com.bawp.todoister.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.bawp.todoister.data.TaskRepository;
import com.bawp.todoister.util.TaskRoomDatabase;

import java.util.List;

public class TaskViewModel extends AndroidViewModel {
    public static TaskRepository repository;
    public final LiveData<List<TaskModel>> allTasks;

    public TaskViewModel(@NonNull Application application) {
        super(application);
        repository = new TaskRepository(application);
        allTasks = repository.getAllTasks();
    }
    public static void createTask(TaskModel taskModel){
        TaskRoomDatabase.databaseWriteExecutor.execute(() ->
                repository.createTask(taskModel));
    }
    public static void updateTask(TaskModel taskModel){
        TaskRoomDatabase.databaseWriteExecutor.execute(() ->
                repository.updateTask(taskModel));
    }
    public  LiveData<TaskModel> getSingleTask(long id){
        return repository.getSingleTask(id);
    }
    public LiveData<List<TaskModel>> getAllTasks(){
        return allTasks;
    }
    public static void deleteSingleTask(TaskModel taskModel){
        TaskRoomDatabase.databaseWriteExecutor.execute(() ->
                repository.deleteSingleTask(taskModel));
    }
    public static void deleteAllTasks(){
        repository.deleteAllTasks();
    }
}

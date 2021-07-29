package com.bawp.todoister.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.bawp.todoister.model.TaskModel;

import java.util.List;

@Dao
public interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void createTask(TaskModel taskModel);
    @Update
    void updateTask (TaskModel taskModel);
    @Query("SELECT * FROM task_table WHERE task_table.task_id== :id")
    LiveData<TaskModel> getSingleTask(long id);
    @Query("SELECT * FROM task_table ORDER BY task_id")
    LiveData<List<TaskModel>>getAllTasks();
    @Delete
    void deleteTask(TaskModel taskModel);
    @Query("DELETE FROM task_table")
    void deleteAllTasks();

}

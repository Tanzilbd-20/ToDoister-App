package com.bawp.todoister.util;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.bawp.todoister.converter.Converter;
import com.bawp.todoister.data.TaskDao;
import com.bawp.todoister.model.Priority;
import com.bawp.todoister.model.TaskModel;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {TaskModel.class},version = 1,exportSchema = false)
@TypeConverters({Converter.class})
public abstract class TaskRoomDatabase extends RoomDatabase {
    public abstract TaskDao taskDao();
    public static final int NUMBER_OF_THREADS = 4;
    public static volatile TaskRoomDatabase INSTANCE;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    private static final RoomDatabase.Callback roomDatabaseCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            databaseWriteExecutor.execute(() -> {
                //Invoke task dao and write
                TaskDao taskDao = INSTANCE.taskDao();
                taskDao.deleteAllTasks(); //cleaning slate

                //Create new Task here


            });

        }
    };

    public static TaskRoomDatabase getAllData(final Context context){
        if (INSTANCE == null) {
            synchronized (TaskRoomDatabase.class){
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),TaskRoomDatabase.class,"task_database")
                            .addCallback(roomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}

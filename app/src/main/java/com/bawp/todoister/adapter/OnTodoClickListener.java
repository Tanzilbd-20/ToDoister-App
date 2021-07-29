package com.bawp.todoister.adapter;

import com.bawp.todoister.model.TaskModel;

public interface OnTodoClickListener {

    void todoClickListener( TaskModel task);
    void deleteOnTodo(TaskModel taskModel);
}

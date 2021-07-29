package com.bawp.todoister.adapter;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bawp.todoister.R;
import com.bawp.todoister.model.TaskModel;
import com.bawp.todoister.util.Utils;
import com.google.android.material.chip.Chip;

import java.util.List;

public class RecyclerAdapterView extends RecyclerView.Adapter<RecyclerAdapterView.ViewHolder> {

    private List<TaskModel> taskModelList;
    private final OnTodoClickListener todoClickListener;


    public RecyclerAdapterView(List<TaskModel> taskModelList ,OnTodoClickListener onTodoClickListener ) {
        this.taskModelList = taskModelList;
        this.todoClickListener = onTodoClickListener;
    }

    @NonNull
    @Override
    public RecyclerAdapterView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_row,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapterView.ViewHolder holder, int position) {
        TaskModel taskModel = taskModelList.get(position);
        String formattedTime = Utils.formattedDate(taskModel.getDueDate());
        ColorStateList colorStateList = new ColorStateList(new int[][]{
                new int[]{-android.R.attr.state_enabled},
                new int[]{android.R.attr.state_enabled}
        },

                new int[]{
                        Color.LTGRAY ,//disabled state
                        Utils.priorityColor(taskModel)

                });


        holder.task.setText(taskModel.getTask());
        holder.today.setText(formattedTime);
        holder.today.setTextColor(Utils.priorityColor(taskModel));
        holder.today.setChipIconTint(colorStateList);
        holder.todo_radio_button.setButtonTintList(colorStateList);
        holder.task.setTextColor(Utils.priorityColor(taskModel));

    }

    @Override
    public int getItemCount() {
        return taskModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public AppCompatTextView task;
        public AppCompatRadioButton todo_radio_button;
        public Chip today;
        public OnTodoClickListener onTodoClickListener;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            task = itemView.findViewById(R.id.todo_row_todo);
            todo_radio_button = itemView.findViewById(R.id.todo_radio_button);
            today = itemView.findViewById(R.id.todo_row_chip);
            this.onTodoClickListener = todoClickListener;
            itemView.setOnClickListener(this);
            todo_radio_button.setOnClickListener(this);


        }

        @Override
        public void onClick(View view) {
            TaskModel currentTask = taskModelList.get(getAdapterPosition());
            int id = view.getId();
            if(id == R.id.todo_row_layout){
                onTodoClickListener.todoClickListener(currentTask);
            }else if(id == R.id.todo_radio_button){
                onTodoClickListener.deleteOnTodo(currentTask);
            }
        }
    }
}

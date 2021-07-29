package com.bawp.todoister;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bawp.todoister.model.Priority;
import com.bawp.todoister.model.SharedViewModel;
import com.bawp.todoister.model.TaskModel;
import com.bawp.todoister.model.TaskViewModel;
import com.bawp.todoister.util.Utils;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.chip.Chip;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Group;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import java.util.Calendar;
import java.util.Date;

public class BottomSheetFragment extends BottomSheetDialogFragment implements View.OnClickListener {
    private EditText enter_todo;
    private ImageButton calender_button, priority_button,save_button;
    private RadioGroup radio_priority_group;
    private RadioButton selected_radio_button;
    private CalendarView calendar_view;
    private Group calender_group;

    private Calendar calendar = Calendar.getInstance();
    private Date dueDate;
    private SharedViewModel sharedViewModel;
    private int selected_button_id;
    private boolean isEdit;
    private Priority priority;


    public BottomSheetFragment() {
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState){
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.bottom_sheet, container, false);

        enter_todo = view.findViewById(R.id.enter_todo_et);
        calender_button = view.findViewById(R.id.today_calendar_button);
        priority_button = view.findViewById(R.id.priority_todo_button);
        save_button = view.findViewById(R.id.save_todo_button);
        radio_priority_group = view.findViewById(R.id.radioGroup_priority);
        calender_group = view.findViewById(R.id.calendar_group);
        calendar_view = view.findViewById(R.id.calendar_view);

        //Setting onClickListener
        Chip today_chip = view.findViewById(R.id.today_chip);
        today_chip.setOnClickListener(this);
        Chip tomorrow_chip = view.findViewById(R.id.tomorrow_chip);
        tomorrow_chip.setOnClickListener(this);
        Chip next_week_chip = view.findViewById(R.id.next_week_chip);
        next_week_chip.setOnClickListener(this);

        return view;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);


        //Taking the date from user from calendar.
        calendar_view.setOnDateChangeListener((calendarView, year, month, day) -> {
            calendar.clear();
            calendar.set(year,month,day);
            dueDate = calendar.getTime();
        });

        save_button.setOnClickListener(view1 -> {
           String task = enter_todo.getText().toString();
           if(!task.isEmpty() && dueDate !=null && priority !=null){
               TaskModel myTask = new TaskModel(task, priority,
                       dueDate,Calendar.getInstance().getTime(), false);
               if(isEdit){
                   TaskModel updateTask = sharedViewModel.getSelectedTask().getValue();
                   updateTask.setTask(task);
                   updateTask.setPriority(priority);
                   updateTask.setDateCreated(Calendar.getInstance().getTime());
                   updateTask.setDueDate(dueDate);
                   TaskViewModel.updateTask(updateTask);
                   sharedViewModel.setIsEdit(false);

               }else{
                   TaskViewModel.createTask(myTask);
               }
               Utils.hideKeyboard(view);
               enter_todo.setText("");

           }else {
               Snackbar.make(save_button,R.string.empty_field,Snackbar.LENGTH_SHORT).show();
           }
            if(this.isVisible()){
                this.dismiss();
            }

        });

        calender_button.setOnClickListener(view1 -> {
            calender_group.setVisibility(calender_group.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
            Utils.hideKeyboard(view);
        });

        priority_button.setOnClickListener(view1 -> {
            Utils.hideKeyboard(view);
            radio_priority_group.setVisibility(radio_priority_group.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
            radio_priority_group.setOnCheckedChangeListener((radioGroup, checkedId) -> {
                if(radio_priority_group.getVisibility() == View.VISIBLE){
                    selected_button_id = checkedId;
                    selected_radio_button = view.findViewById(selected_button_id);
                    if(selected_radio_button.getId() == R.id.radioButton_high){
                        priority = Priority.HIGH;
                    }else if(selected_radio_button.getId() == R.id.radioButton_med){
                        priority = Priority.MEDIUM;
                    }else if(selected_radio_button.getId() == R.id.radioButton_low){
                        priority = Priority.LOW;
                    }else {
                        priority = Priority.LOW;
                    }
                }else{
                    priority = Priority.LOW;
                }
            });
        });

    }

    //Getting the id via implementing OnClickListener
    @Override
    public void onClick(View view) {
        //This is very important to reset calendar each time so the buttons behave as expected
       calendar = Calendar.getInstance();
        int id = view.getId();
        if(id == R.id.today_chip){
            calendar.add(Calendar.DAY_OF_YEAR,0);
            dueDate = calendar.getTime();
            Log.d("TAG", "onClick: "+dueDate.toString());
        }else if(id == R.id.tomorrow_chip){
            calendar.add(Calendar.DAY_OF_YEAR,1);
            dueDate = calendar.getTime();
            Log.d("TAG", "onClick: "+dueDate.toString());
        }else if(id == R.id.next_week_chip){
            calendar.add(Calendar.DAY_OF_YEAR,7);
            dueDate = calendar.getTime();
            Log.d("TAG", "onClick: "+dueDate.toString());
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if(sharedViewModel.getSelectedTask().getValue() !=null){
            isEdit = sharedViewModel.getIsEdit();
            TaskModel taskModel = sharedViewModel.getSelectedTask().getValue();
            Log.d("BottomSheet", "onViewCreated: "+taskModel.getTask());
            enter_todo.setText(taskModel.getTask());
        }
    }
}
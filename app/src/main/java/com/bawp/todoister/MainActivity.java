package com.bawp.todoister;

import android.os.Bundle;

import com.bawp.todoister.adapter.OnTodoClickListener;
import com.bawp.todoister.adapter.RecyclerAdapterView;
import com.bawp.todoister.model.SharedViewModel;
import com.bawp.todoister.model.TaskModel;
import com.bawp.todoister.model.TaskViewModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements OnTodoClickListener {
        //RequiredActivity().get();
    private TaskViewModel taskViewModel;
    private RecyclerView recyclerView;
    private RecyclerAdapterView recyclerAdapterView;
    private BottomSheetFragment bottomSheetFragment;
    private ConstraintLayout bottom_sheet_constrain_layout;
    private SharedViewModel sharedViewModel;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        bottom_sheet_constrain_layout = findViewById(R.id.bottomSheet);
        bottomSheetFragment = new BottomSheetFragment();
      BottomSheetBehavior<ConstraintLayout> bottomSheetBehavior = BottomSheetBehavior.from(bottom_sheet_constrain_layout);
      bottomSheetBehavior.setPeekHeight(BottomSheetBehavior.STATE_HIDDEN);


        taskViewModel = new ViewModelProvider.AndroidViewModelFactory(this.getApplication()).create(TaskViewModel.class);

        taskViewModel.allTasks.observe(this, taskModels -> {
            recyclerAdapterView = new RecyclerAdapterView(taskModels,this);
            recyclerView.setAdapter(recyclerAdapterView);
        });
        sharedViewModel = new ViewModelProvider(this).get(SharedViewModel.class);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
                showBottomSheetFragment();
        });
    }

    private void showBottomSheetFragment() {
        bottomSheetFragment.show(getSupportFragmentManager(),bottomSheetFragment.getTag());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void todoClickListener(TaskModel task) {
        sharedViewModel.setSelectedTask(task);
        sharedViewModel.setIsEdit(true);
        showBottomSheetFragment();
    }

    @Override
    public void deleteOnTodo(TaskModel taskModel) {
        Log.d("TAG", "deleteOnTodo: "+taskModel.getTask());
        TaskViewModel.deleteSingleTask(taskModel);
        recyclerAdapterView.notifyDataSetChanged();
    }
}
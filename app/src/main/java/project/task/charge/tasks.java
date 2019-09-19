package project.task.charge;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import project.task.charge.R;

import project.task.charge.task.taskAdapter;
import project.task.charge.task.task_view;
import project.task.charge.util.Global;
import project.task.charge.task.task;

public class tasks extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);
        ListView task_list = (ListView)findViewById(R.id.task_list);
        ArrayList<task> array_all_task = new ArrayList<task>();
        final Intent intent = getIntent();
        if (intent.getStringExtra("flag").equals("from_project"))
        {
            Integer index = intent.getIntExtra("Index",0);
            array_all_task = Global.array_project.get(index).getProject_created_task();
        }
        else {
            array_all_task = Global.array_all_task;
        }

        array_all_task = Global.array_all_task;
        taskAdapter adapter = new taskAdapter(this,R.layout.item_user_list,array_all_task);
        task_list.setAdapter(adapter);
        task_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent call_intent = new Intent(tasks.this, task_view.class);
                call_intent.putExtra(Global.ORIGIN,Global.ALLTASK);
                call_intent.putExtra(Global.INDEX,i);
                startActivity(call_intent);

            }
        });
    }
}
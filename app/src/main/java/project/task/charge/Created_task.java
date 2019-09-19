package project.task.charge;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import project.task.charge.R;

import project.task.charge.task.task_I_createdAdapter;
import project.task.charge.task.task_view;
import project.task.charge.util.Global;
import project.task.charge.task.task;

public class Created_task extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activiry_created_task);
        ListView task_list = (ListView)findViewById(R.id.list_mytask);
        ArrayList<task> array_created_task = new ArrayList<task>();
        array_created_task = Global.array_created_task;
        task_I_createdAdapter adapter = new task_I_createdAdapter(this,R.layout.item_mytasks_list,array_created_task);
        task_list.setAdapter(adapter);
        task_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent call_intent = new Intent(Created_task.this, task_view.class);
                call_intent.putExtra(Global.ORIGIN,Global.I_CREATED);
                call_intent.putExtra(Global.INDEX,i);
                startActivity(call_intent);

            }
        });
    }
}

package project.task.charge;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

import project.task.charge.task.task_I_createdAdapter;
import project.task.charge.util.Global;
import project.task.charge.util.task;

public class Created_task extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_task);
        ListView task_list = (ListView)findViewById(R.id.list_mytask);
        ArrayList<task> array_created_task = new ArrayList<task>();
        array_created_task = Global.array_created_task;
        task_I_createdAdapter adapter = new task_I_createdAdapter(this,R.layout.item_mytasks_list,array_created_task);
        task_list.setAdapter(adapter);
    }
}
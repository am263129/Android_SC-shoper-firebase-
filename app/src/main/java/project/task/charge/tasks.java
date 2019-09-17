package project.task.charge;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

import project.task.charge.member.Member;
import project.task.charge.member.memberAdapter_list;
import project.task.charge.task.taskAdapter;
import project.task.charge.util.Global;
import project.task.charge.util.task;

public class tasks extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);
        ListView task_list = (ListView)findViewById(R.id.task_list);
        ArrayList<task> array_all_task = new ArrayList<task>();
        array_all_task = Global.array_all_task;
        taskAdapter adapter = new taskAdapter(this,R.layout.item_user_list,array_all_task);
        task_list.setAdapter(adapter);
    }
}

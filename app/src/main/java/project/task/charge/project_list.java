package project.task.charge;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import project.task.charge.R;
import project.task.charge.project.project;
import project.task.charge.project.projectAdapter;
import project.task.charge.task.task;
import project.task.charge.task.taskAdapter;
import project.task.charge.util.Global;

public class project_list extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);
        final ListView project_list = (ListView)findViewById(R.id.project_list);
        ArrayList<project> array_all_task = new ArrayList<project>();
        array_all_task = Global.array_project;
        projectAdapter adapter = new projectAdapter(this,R.layout.item_user_list,array_all_task);
        project_list.setAdapter(adapter);
        project_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(project_list.this, tasks.class);
                intent.putExtra("flag","from_project");
                intent.putExtra("index", i);
                startActivity(intent);
            }
        });
    }
}

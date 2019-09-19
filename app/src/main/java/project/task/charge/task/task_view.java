package project.task.charge.task;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import project.task.charge.R;
import project.task.charge.util.Global;

import static project.task.charge.util.Global.INDEX;
import static project.task.charge.util.Global.ORIGIN;
import static project.task.charge.util.Global.PRO_INDEX;

public class task_view extends AppCompatActivity {
    TextView task_id, task_description, task_creator_name, task_created_date, task_duration, task_start_date, task_end_date, task_hired_member,task_project_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_view);
        task_id = (TextView) findViewById(R.id.task_task_id);
        task_description = (TextView) findViewById(R.id.task_task_description);
        task_creator_name = (TextView) findViewById(R.id.task_creator_name);
        task_created_date = (TextView) findViewById(R.id.task_created_date);
        task_duration = (TextView) findViewById(R.id.task_deadline);
        task_start_date = (TextView) findViewById(R.id.task_start_date);
        task_end_date = (TextView) findViewById(R.id.task_end_date);
        task_hired_member = (TextView) findViewById(R.id.task_hired_members);
        task_project_name = (TextView) findViewById(R.id.task_involving_project_name);
        Intent intent = getIntent();
        Integer index = intent.getIntExtra(INDEX,0);
        Integer pro_index = intent.getIntExtra(PRO_INDEX, 0);
        String from = intent.getStringExtra(ORIGIN);
        ArrayList<task> tasklist = new ArrayList<task>();
        switch (from){
            case "my_task":
                tasklist = Global.array_my_task;
                break;
            case "all_task":
                tasklist = Global.array_all_task;
                break;
            case "from_pro":
                tasklist = Global.array_project.get(pro_index).getProject_created_task();
                break;
            case "I_created":
                tasklist = Global.array_created_task;
                break;
            default:
                break;
        }
        show_task(tasklist, index);

    }
    public void show_task( ArrayList<task> task_array, Integer index){
        String hired_members = "";
        task_id.setText(task_array.get(index).getTask_id());
        task_start_date.setText(task_array.get(index).getTask_start_date());
        task_end_date.setText(task_array.get(index).getTask_end_date());
        task_description.setText(task_array.get(index).getTask_description());
        task_created_date.setText(task_array.get(index).getTask_created_date());
        task_duration.setText(task_array.get(index).getTask_duration());
        task_project_name.setText(task_array.get(index).getTask_involvoing_project());
        task_creator_name.setText(task_array.get(index).getTask_creator());
        for(int i = 0; i < task_array.get(index).getHired_members().size(); i++){
            hired_members = hired_members + ", " + task_array.get(index).getHired_members().get(i).toString();
        }
        task_hired_member.setText(hired_members);
    }
}

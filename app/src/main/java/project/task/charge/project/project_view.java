package project.task.charge.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import project.task.charge.R;
import project.task.charge.feed.feedback;
import project.task.charge.task.feedback_Adapter;
import project.task.charge.task.task;
import project.task.charge.task.taskAdapter;
import project.task.charge.task.task_view;
import project.task.charge.tasks;
import project.task.charge.util.Global;

public class project_view extends AppCompatActivity {
    Integer index;
    TextView project_name, project_description, project_client, project_location, project_start_date, project_cont_date, project_tar_date, project_duration;
    ListView project_created_tasks;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_view);
        final Intent intent = getIntent();
        index = intent.getIntExtra(Global.INDEX,0);

        project_name = (TextView)findViewById(R.id.project_name);
        project_description = (TextView)findViewById(R.id.project_description);
        project_client = (TextView)findViewById(R.id.project_client);
        project_location = (TextView)findViewById(R.id.project_location);
        project_duration = (TextView)findViewById(R.id.project_duration);
        project_start_date = (TextView)findViewById(R.id.project_start_date);
        project_cont_date = (TextView)findViewById(R.id.project_cont_date);
        project_tar_date = (TextView)findViewById(R.id.project_tar_date);
        project_created_tasks = (ListView)findViewById(R.id.created_tasks);
        project current_project = Global.array_project.get(index);
        project_name.setText(current_project.getProject_name().toString());
        project_description.setText(current_project.getProject_description().toString());
        project_client.setText(current_project.getProject_client().toString());
        project_location.setText(current_project.getProject_location().toString());
        project_duration.setText(current_project.getProject_duration().toString());
        project_start_date.setText(current_project.getProject_start_date().toString());
        project_cont_date.setText(current_project.getProject_contractual_end_date().toString());
        project_tar_date.setText(current_project.getPrpject_target_end_date().toString());

        ArrayList<task> created_tasks = current_project.getProject_created_task();
        taskAdapter adapter = new taskAdapter(this,R.layout.item_feedback_list,created_tasks);
        project_created_tasks.setAdapter(adapter);

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(project_created_tasks.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, project_created_tasks);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = project_created_tasks.getLayoutParams();
        params.height = totalHeight;
        project_created_tasks.setLayoutParams(params);

        project_created_tasks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent_to_task = new Intent(project_view.this, task_view.class);
                intent_to_task.putExtra(Global.ORIGIN,Global.FROMPROJECT);
                intent_to_task.putExtra(Global.INDEX, index);
                intent_to_task.putExtra(Global.PRO_INDEX, i);
                startActivity(intent_to_task);

            }
        });


    }
}

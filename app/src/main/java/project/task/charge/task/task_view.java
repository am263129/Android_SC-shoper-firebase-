package project.task.charge.task;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.solver.GoalRow;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

import project.task.charge.MainActivity;
import project.task.charge.R;
import project.task.charge.feed.feedback;
import project.task.charge.make_new_task;
import project.task.charge.util.Global;

import static project.task.charge.util.Global.FLAG;
import static project.task.charge.util.Global.INDEX;
import static project.task.charge.util.Global.ORIGIN;
import static project.task.charge.util.Global.PRO_INDEX;
import static project.task.charge.util.Global.array_all_members;

public class task_view extends AppCompatActivity implements View.OnClickListener {
    TextView task_id, task_description, task_creator_name, task_created_date, task_duration, task_start_date, task_end_date, task_hired_member,task_project_name, task_status;
    LinearLayout action_for_client, action_for_mytask, view_for_client, action_for_both, client_uncompleted;
    Button complete, uncompleted, update;
    ImageView task_status_icon;
    ListView feedback_list;
    EditText user_feedback;
    private Calendar calendar;
    private int year, month, day;
    String current_date;
    Integer end_year;
    Integer end_month;
    Integer end_day;
    String current_status;
    ArrayList<task> tasklist = new ArrayList<task>();
    Integer index;
    String TAG = "Task_View";
    boolean mytask = false;
    boolean created = false;
    boolean changed = false;
    ImageView creator;

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
        task_status = (TextView) findViewById(R.id.task_status);
        action_for_client = (LinearLayout) findViewById(R.id.action_for_client);
        action_for_mytask = (LinearLayout) findViewById(R.id.action_for_mytask);
        action_for_both = (LinearLayout) findViewById(R.id.action_for_both);
        client_uncompleted = (LinearLayout)findViewById(R.id.client_uncomplete);
        complete = (Button) findViewById(R.id.btn_complete);
        update = (Button) findViewById(R.id.btn_update_data);
        uncompleted = (Button)findViewById(R.id.btn_uncompleted);
        task_status_icon = (ImageView)findViewById(R.id.task_status_icon);
        feedback_list = (ListView)findViewById(R.id.task_feedback);
        user_feedback = (EditText)findViewById(R.id.user_feedback);
        creator = (ImageView)findViewById(R.id.creator);
        complete.setOnClickListener(this);
        update.setOnClickListener(this);
        uncompleted.setOnClickListener(this);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        current_date = String.valueOf(day+"/"+(month+1)+"/"+year);


        Intent intent = getIntent();
        index = intent.getIntExtra(INDEX,0);
        Integer pro_index = intent.getIntExtra(PRO_INDEX, 0);
        String from = intent.getStringExtra(ORIGIN);
        tasklist.clear();
        switch (from){
            case "my_task":
                mytask = true;
                tasklist = Global.array_my_task;
                action_for_mytask.setVisibility(View.VISIBLE);
                action_for_both.setVisibility(View.VISIBLE);
                show_task(tasklist, index);
                break;
            case "all_task":
                tasklist = Global.array_all_task;
                show_task(tasklist, index);
                break;
            case "from_pro":
                tasklist = Global.array_project.get(index).getProject_created_task();
                show_task(tasklist, pro_index);

                break;
            case "I_created":
                tasklist = Global.array_created_task;
                created = true;
                action_for_client.setVisibility(View.VISIBLE);
                action_for_both.setVisibility(View.VISIBLE);
                show_task(tasklist, index);
                break;
            default:
                break;
        }


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
            hired_members = hired_members + task_array.get(index).getHired_members().get(i).getName().toString() + "\n";
        }
        task_hired_member.setText(hired_members);

        end_year = Integer.parseInt(task_array.get(index).getTask_end_date().split("/")[2]);
        end_month = Integer.parseInt(task_array.get(index).getTask_end_date().split("/")[1]);
        end_day = Integer.parseInt(task_array.get(index).getTask_end_date().split("/")[0]);

        if(getStatus(end_day, end_month, end_year)){
            task_status_icon.setImageResource(R.drawable.ico_ongoing);
        }else task_status_icon.setImageResource(R.drawable.ico_late);

        if(task_array.get(index).getTask_status().equals("completed")) {
            task_status_icon.setImageResource(R.drawable.ico_completed);
            task_status.setText(MainActivity.getInstance().getString(R.string.status_completed));

            if(created) {
                client_uncompleted.setVisibility(View.VISIBLE);
                action_for_client.setVisibility(View.GONE);
            }
        }else {
            if(created) {
                client_uncompleted.setVisibility(View.GONE);
                action_for_client.setVisibility(View.VISIBLE);
            }


        }
        ArrayList<feedback> feedbacks = task_array.get(index).getFeedbacks();
        feedback_Adapter adapter = new feedback_Adapter(this,R.layout.item_feedback_list,feedbacks);
        feedback_list.setAdapter(adapter);

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(feedback_list.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, feedback_list);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = feedback_list.getLayoutParams();
        params.height = totalHeight;
        feedback_list.setLayoutParams(params);

        String base64 = "";
        for (int i=0; i < array_all_members.size(); i++){
            if (array_all_members.get(i).getName().equals(task_array.get(index).getTask_creator())){
                base64 = array_all_members.get(i).getPhoto();
            }
        }
        if (!base64.equals("")){
            String imageDataBytes = base64.substring(base64.indexOf(",")+1);
            InputStream stream = new ByteArrayInputStream(Base64.decode(imageDataBytes.getBytes(), Base64.DEFAULT));
            Bitmap bitmap = BitmapFactory.decodeStream(stream);
            creator.setImageBitmap(bitmap);
        }



    }

    private boolean getStatus(Integer end_day, Integer end_month, Integer end_year) {
        if (end_year < year){
            return false;
        }else if (end_month < month){
            return false;
        }else if (end_day < day){
            return false;
        }else {
            return true;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_complete:
                changed = true;
                change_to_complete();

                break;
            case R.id.btn_update_data:
                if (validate()) {
                    update_data();
                    user_feedback.setText("");
                }

                else
                    Toast.makeText(task_view.this, "You didn't change any data.", Toast.LENGTH_LONG).show();
                break;
            case R.id.btn_uncompleted:
                changed = true;
                change_to_uncomplete();

            default:
                break;
        }

    }

    private boolean validate() {
        if (!user_feedback.getText().equals("") || changed)
            return true;
        else
            return false;

    }


    private void change_to_uncomplete() {
        client_uncompleted.setVisibility(View.GONE);
        action_for_client.setVisibility(View.VISIBLE);
        if(getStatus(end_day, end_month, end_year)){
            task_status_icon.setImageResource(R.drawable.ico_ongoing);
            task_status.setText(MainActivity.getInstance().getString(R.string.status_ongoing));
        }else {
            task_status_icon.setImageResource(R.drawable.ico_late);
            task_status.setText(MainActivity.getInstance().getString(R.string.status_late));
        }

    }

    private void change_to_complete() {
        client_uncompleted.setVisibility(View.VISIBLE);
        action_for_client.setVisibility(View.GONE);
        task_status_icon.setImageResource(R.drawable.ico_completed);
        task_status.setText(MainActivity.getInstance().getString(R.string.status_completed));

    }

    private void update_data() {
        FirebaseApp.initializeApp(this);
        String id = "Project/" + tasklist.get(index).getTask_involvoing_project() + "/Created Tasks/" + tasklist.get(index).getTask_id();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(id+"/F_Status");

        final int min = 0;
        final int max = 99999999;
        Integer random = new Random().nextInt((max - min) + 1) + min;

        if (created)
            myRef.setValue(task_status.getText().toString());
        if (mytask) {
            String path = id + "/E_Feedback/" + Global.today + ":" + Global.current_user_name +":"+String.valueOf(random)+":"+String.valueOf(tasklist.get(index).getFeedbacks().size()+1);
            myRef = database.getReference( path+ "/Feedback");
            myRef.setValue(user_feedback.getText().toString());
            myRef = database.getReference(path + "/Author");
            myRef.setValue(Global.current_user_name);
        }

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is:" + value);
                Toast.makeText(task_view.this,"Task Data Updated Successfully",Toast.LENGTH_LONG).show();
                refresh();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG,"Failed to rad value ", databaseError.toException());
                Toast.makeText(task_view.this,"Updating Task Data Failed"+databaseError.toString(),Toast.LENGTH_LONG).show();
            }
        });
    }

    private void refresh() {
        show_task(tasklist, index);
    }
}

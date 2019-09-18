package project.task.charge;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

import project.task.charge.R;

import project.task.charge.ui.fragment.newtask_first;
import project.task.charge.ui.fragment.newtask_preview;
import project.task.charge.ui.fragment.newtask_second;
import project.task.charge.ui.fragment.newtask_third;
import project.task.charge.util.Global;

public class make_new_task extends AppCompatActivity implements View.OnClickListener{

    private FrameLayout task_area;
    private Button btn_pre, btn_next, btn_preview, btn_finish;
    private Fragment newtask_first;
    private Fragment newtask_second;
    private Fragment newtask_third;
    private Fragment newtask_preview;
    private TextView header;
    public static make_new_task make_task;

    private Calendar calendar;

    private String TAG = "make_new_task";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_task);

        header = (TextView)findViewById(R.id.header);
        btn_pre = (Button)findViewById(R.id.btn_pre);
        btn_next = (Button)findViewById(R.id.btn_next);
        btn_preview = (Button)findViewById(R.id.btn_preview);
        btn_finish = (Button)findViewById(R.id.btn_finish);
        btn_pre.setOnClickListener(this);
        btn_next.setOnClickListener(this);
        btn_preview.setOnClickListener(this);
        btn_finish.setOnClickListener(this);
        newtask_first = new newtask_first();
        newtask_second = new newtask_second();
        newtask_third = new newtask_third();
        newtask_preview = new newtask_preview();
        make_task = this;



        changeTaskArea(0);
    }
    protected void onResume() {
        btn_next.setEnabled(Global.validate_newtask);
        btn_pre.setEnabled(Global.validate_newtask);
        super.onResume();
    }

    @Override
    public void onClick(View view) {
        Toast.makeText(make_new_task.this,String.valueOf(Global.mk_task_progress),Toast.LENGTH_LONG).show();
        switch (view.getId()){
            case R.id.btn_next:
                Global.mk_task_progress = Global.mk_task_progress+1;

                if ((Global.mk_task_progress > 1)) {
                    if(validateTask()) {
                        btn_next.setVisibility(view.GONE);
                        btn_preview.setVisibility(view.VISIBLE);
                    }
                }
                if(Global.mk_task_progress > 3){
                    Global.mk_task_progress = 3;
                }
                changeTaskArea(Global.mk_task_progress);
                break;
            case R.id.btn_pre:
                Global.mk_task_progress = Global.mk_task_progress-1;
                if(btn_preview.getVisibility() == view.VISIBLE)
                {
                    btn_preview.setVisibility(View.GONE);
                    btn_finish.setVisibility(View.GONE);
                    btn_next.setVisibility(View.VISIBLE);
                }
                if(btn_finish.getVisibility() == View.VISIBLE)
                {
                    btn_finish.setVisibility(View.GONE);
                    btn_preview.setVisibility(View.VISIBLE);
                }
                if ((Global.mk_task_progress < 0)) {
                    Global.mk_task_progress = 0;
                }
                changeTaskArea(Global.mk_task_progress);
                break;
            case R.id.btn_preview:
                Global.mk_task_progress = Global.mk_task_progress + 1;
                btn_preview.setVisibility(view.GONE);
                btn_finish.setVisibility(View.VISIBLE);
                changeTaskArea(Global.mk_task_progress);
                calendar = Calendar.getInstance();
                break;
            case R.id.btn_finish:
                uploadNewTask();
                break;


            default:
                break;
        }
    }

    private void uploadNewTask() {
        FirebaseApp.initializeApp(this);
        String id = "TASK/" + Global.task_id;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(id+"/A_Id");
        myRef.setValue(Global.task_id);
        myRef = database.getReference(id+"/B_Description");
        myRef.setValue(Global.task_description);
        myRef = database.getReference(id+"/C_Duration");
        myRef.setValue(Global.task_deadline);
        myRef = database.getReference(id+"/C_Start_date");
        myRef.setValue(Global.task_start_date);
        myRef = database.getReference(id+"/C_End_date");
        myRef.setValue(Global.task_end_date);
        myRef = database.getReference(id+"/C_Created_date");
        myRef.setValue(Global.task_created_date);
        myRef = database.getReference(id+"/D_Involving_Project");
        myRef.setValue(Global.project_name);
        myRef = database.getReference(id+"/E_Creator");
        myRef.setValue(Global.current_user_name);
        for(int i  = 0;i < Global.array_hired_members.size();i ++){
            myRef = database.getReference(id+"/Hired_Members/"+Global.array_hired_members.get(i).getName() +"/Name");
            myRef.setValue(Global.array_hired_members.get(i).getName());
            myRef = database.getReference(id+"/Hired_Members/"+Global.array_hired_members.get(i).getName() +"/Email");
            myRef.setValue(Global.array_hired_members.get(i).getEmail());
        }

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is:" + value);
                Toast.makeText(make_new_task.this,"Making New Task Finished Successfully",Toast.LENGTH_LONG).show();
                init();
                finish();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG,"Failed to rad value ", databaseError.toException());
                Toast.makeText(make_new_task.this,"Making New Task Failed. the error code is"+databaseError.toString(),Toast.LENGTH_LONG).show();
            }
        });
    }

    private void init() {
        Global.task_id = "";
        Global.array_hired_members.clear();
        Global.task_created_date = "";
        Global.task_deadline = "";
        Global.task_start_date = "";
        Global.task_end_date = "";
        Global.project_name = "";
        Global.task_description = "";
    }

    private boolean validateTask() {
//        if((!Global.task_id_created_date.equals(""))&&(!Global.task_description.equals(""))&&(!Global.task_deadline.equals(""))&&(Global.task_hired_members.length>0))
//        {
//            //Global.task_id =
//            return true;
//        }
//        else
//            return false;
            return true;//only testing;
    }

    private void changeTaskArea(Integer index) {
// create a FragmentManager
        Fragment fragment;
        switch (index){
            case 0:
                fragment = newtask_first;
                header.setText("Task Name & Description");
                break;
            case 1:
                fragment = newtask_second;
                header.setText("Deadline");
                break;
            case 2:
                fragment = newtask_third;
                header.setText("Hire Members");
                break;
            case 3:
                fragment = newtask_preview;
                header.setText("Preview");
                break;
            default:
                fragment = newtask_first;
                break;
        }
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.frame_make_task, fragment);
        fragmentTransaction.commit();
    }
    public static make_new_task getInstance(){
        return make_task;
    }



}

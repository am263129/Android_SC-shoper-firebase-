package project.task.charge;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import javax.mail.AuthenticationFailedException;
import javax.mail.MessagingException;

import project.task.charge.R;

import project.task.charge.email.Mail;
import project.task.charge.ui.fragment.newtask_first;
import project.task.charge.ui.fragment.newtask_preview;
import project.task.charge.ui.fragment.newtask_second;
import project.task.charge.ui.fragment.newtask_third;
import project.task.charge.util.Global;

import static project.task.charge.util.Global.array_hired_members;
import static project.task.charge.util.Global.current_user_email_pass;

public class make_new_task extends AppCompatActivity implements View.OnClickListener{

    private FrameLayout task_area;
    private Button btn_pre, btn_next, btn_preview, btn_finish;
    private Fragment newtask_first;
    private Fragment newtask_second;
    private Fragment newtask_third;
    private Fragment newtask_preview;
    private TextView header;
    public static make_new_task make_task;
    Dialog dialog;

    private Calendar calendar;

    private String TAG = "make_new_task";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_task);
        Global.mk_task_progress = 0;
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
        dialog = new Dialog(this);
        dialog.setTitle("Please input your EmailPassowrd");
        dialog.setContentView(R.layout.request_password);
        dialog.setCanceledOnTouchOutside(false);
        final EditText password = dialog.findViewById(R.id.email_password);
        Button ok = dialog.findViewById(R.id.btn_email_ok);
        Button cancel = dialog.findViewById(R.id.btn_email_cancel);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                current_user_email_pass = password.getText().toString();
                Sending_Email();
                dialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        changeTaskArea(0);
    }
    @Override
    protected void onResume() {
        Global.mk_task_progress = 0;
        btn_next.setEnabled(Global.validate_newtask);
        btn_pre.setEnabled(Global.validate_newtask);
        super.onResume();
    }

    @Override
    public void onClick(View view) {
        Toast.makeText(make_new_task.this,String.valueOf(Global.mk_task_progress),Toast.LENGTH_LONG).show();
        switch (view.getId()){
            case R.id.btn_next:
                boolean valid = true;
                switch (Global.mk_task_progress){
                    case 0:
                        if (Global.task_id.equals("") || Global.task_description.equals(""))
                            valid = false;
                        break;
                    case 1:
                        if(Global.task_start_date.equals("") || Global.task_end_date.equals(""))
                            valid = false;
                        break;
                    case 2:
                        if(Global.array_hired_members.size() == 0)
                            valid = false;
                        break;
                }
                if (!valid)
                    Toast.makeText(make_new_task.this,"Please input required data",Toast.LENGTH_LONG).show();
                else {
                    Global.mk_task_progress = Global.mk_task_progress + 1;
                    if (Global.mk_task_progress >1){
                        btn_next.setVisibility(View.GONE);
                        btn_preview.setVisibility(View.VISIBLE);
                    }
                    if (Global.mk_task_progress > 3) {
                        Global.mk_task_progress = 3;
                    }

                    changeTaskArea(Global.mk_task_progress);
                }
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
                if(Global.array_hired_members.size() == 0)
                    Toast.makeText(make_new_task.this,"Please input required data",Toast.LENGTH_LONG).show();
                else {
                    Global.mk_task_progress = Global.mk_task_progress + 1;
                    btn_preview.setVisibility(view.GONE);
                    btn_finish.setVisibility(View.VISIBLE);
                    changeTaskArea(Global.mk_task_progress);
                    calendar = Calendar.getInstance();
                }
                break;
            case R.id.btn_finish:
                uploadNewTask();
                dialog.show();

                break;


            default:
                break;
        }
    }

    private void Sending_Email(){
    if (!current_user_email_pass.equals("")) {
        Integer size = 0;
        if (Global.array_hired_members.size()>0)
            size = Global.array_hired_members.size();
        String[] send_to  = new String[size];
        for (Integer i = 0; i < Global.array_hired_members.size(); i++) {
            send_to[i] = array_hired_members.get(i).getEmail().toString();
        }
        MainActivity.SendEmailAsyncTask email = new MainActivity.SendEmailAsyncTask();
        email.m = new Mail(Global.current_user_email, current_user_email_pass);
        email.m.set_from(Global.current_user_name);
        email.m.setBody(Global.task_email_body);
        email.m.set_to(send_to);
        email.m.set_subject("New Task has been Created!");
        email.execute();
        init();
        finish();
    }
    else {
        Toast.makeText(make_new_task.this,"Sending Email is Canceled",Toast.LENGTH_LONG).show();
        init();
        finish();
    }
}

    private void uploadNewTask() {
        FirebaseApp.initializeApp(this);
        String id = "Project/" + Global.project_name + "/Created Tasks/" + Global.task_id;
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
        myRef.setValue(Global.getToday());
        myRef = database.getReference(id+"/D_Involving_Project");
        myRef.setValue(Global.project_name);
        myRef = database.getReference(id+"/E_Creator");
        myRef.setValue(Global.current_user_name);
        myRef = database.getReference(id+"/F_Status");
        myRef.setValue("On Going");
        for(int i  = 0;i < Global.array_hired_members.size();i ++){
            myRef = database.getReference(id+"/Hired_Members/"+Global.array_hired_members.get(i).getName() +"/Name");
            myRef.setValue(Global.array_hired_members.get(i).getName());
            myRef = database.getReference(id+"/Hired_Members/"+Global.array_hired_members.get(i).getName() +"/Email");
            myRef.setValue(Global.array_hired_members.get(i).getEmail());
        }
        Global.task_email_body = "Task Id :" + Global.task_id + "\n" +
                                "Task Description :" + Global.task_description + "\n"+
                                "Task Duration : " + Global.task_deadline + "\n"+
                                "Task Creator :" + Global.current_user_name;
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is:" + value);
                Toast.makeText(make_new_task.this,"Making New Task Finished Successfully",Toast.LENGTH_LONG).show();

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
        Global.task_deadline = "";
        Global.task_start_date = "";
        Global.task_end_date = "";
        Global.project_name = "";
        Global.task_description = "";
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

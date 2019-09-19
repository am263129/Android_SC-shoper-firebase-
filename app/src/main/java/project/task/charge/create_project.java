package project.task.charge;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import project.task.charge.util.DateClass;
import project.task.charge.util.Global;

public class create_project extends AppCompatActivity {
    EditText project_name, project_description, project_client, project_location;
    Button finish;
    TextView project_start_date, project_cont_date, project_tar_date, project_duration;
    String TAG = "Create Project";

    private DatePicker datePicker;
    private Calendar calendar;
    private int year, month, day, start_year, start_month, start_day, end_year, end_month, end_day;
    String current_date;
    String duration;
    private DatePickerDialog picker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_project);
        project_name = (EditText)findViewById(R.id.project_name);
        project_description = (EditText)findViewById(R.id.project_description);
        project_client = (EditText)findViewById(R.id.project_client);
        project_location = (EditText)findViewById(R.id.project_location);
        project_duration = (TextView)findViewById(R.id.project_duration);
        project_start_date = (TextView)findViewById(R.id.project_start_date);
        project_cont_date = (TextView)findViewById(R.id.project_cont_date);
        project_tar_date = (TextView)findViewById(R.id.project_tar_date);
        finish = (Button)findViewById(R.id.btn_project_finish);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        current_date = String.valueOf(day+"/"+(month+1)+"/"+year);

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()){
                    if (!project_duration.getText().equals("invalid"))
                        createProject();
                    else
                        Toast.makeText(create_project.this,"Duration is invalid",Toast.LENGTH_LONG).show();
                }
            }
        });
        project_start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                picker = new DatePickerDialog(create_project.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        project_start_date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                        getduration();
                    }
                }, year, month , day);
                picker.show();

            }
        });
        project_cont_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                picker = new DatePickerDialog(create_project.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        project_cont_date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                        getduration();
                    }
                }, year, month , day);
                picker.show();

            }
        });
        project_tar_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                picker = new DatePickerDialog(create_project.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        project_tar_date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                        getduration();
                    }
                }, year, month , day);
                picker.show();
            }
        });

    }

    private void getduration() {
        if (!project_start_date.getText().equals("")&& !project_cont_date.getText().toString().equals(""))
           project_duration.setText(Global.getCountOfDays(project_start_date.getText().toString(),project_cont_date.getText().toString()));

    }

    private void createProject() {
        FirebaseApp.initializeApp(this);
        String id = "Project/" + project_name.getText().toString();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(id+"/Name");
        myRef.setValue(project_name.getText().toString());
        myRef = database.getReference(id+"/Description");
        myRef.setValue(project_description.getText().toString());
        myRef = database.getReference(id+"/Duration");
        myRef.setValue(project_duration.getText().toString());
        myRef = database.getReference(id+"/Start_date");
        myRef.setValue(project_start_date.getText().toString());
        myRef = database.getReference(id+"/Contractual End Date");
        myRef.setValue(project_cont_date.getText().toString());
        myRef = database.getReference(id+"/Created_date");
        myRef.setValue(current_date);
        myRef = database.getReference(id+"/Target End Date");
        myRef.setValue(project_tar_date.getText().toString());
        myRef = database.getReference(id+"/Client");
        myRef.setValue(project_client.getText().toString());
        myRef = database.getReference(id+"/Location");
        myRef.setValue(project_location.getText().toString());


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is:" + value);
                Toast.makeText(create_project.this,"Making New Project Finished Successfully",Toast.LENGTH_LONG).show();
                finish();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG,"Failed to rad value ", databaseError.toException());
                Toast.makeText(create_project.this,"Making New project Failed. the error code is"+databaseError.toString(),Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean validate() {
        if(check(project_name) & check(project_description) & check(project_client) & check(project_location)){
            return true;
        }
        else return false;
    }


    public boolean check(EditText view) {

        if (!TextUtils.isEmpty(view.getText()) & view.getText().length() > 2)
            return true;
        else {
            view.setError("Invalid input");
            return false;
        }
    }


}

package project.task.charge;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import project.task.charge.member.Member;
import project.task.charge.util.Global;
import project.task.charge.util.hired_member;
import project.task.charge.util.task;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public String TAG = "Main";
    private LinearLayout all_members, new_task, my_task, about_us;
    public static MainActivity myself;
    private Intent intent;
    private TextView current_user_name, current_user_email;
    private NavigationView navigationView;
    ViewPager viewPager;
    private ArrayList<Member> array_all_members = new ArrayList<Member>();

    private String USER_NAME = "Name";
    private String USER_GENDER = "Gender";
    private String USER_EMAIL = "Email";
    private String TASK_TITLE = "Title";
    private String TASK_DATE_START = "Start date";
    private String TASK_DATE_END = "End date";
    private String TASK_DURATION = "Duration";
    private String TASK_HIRED = "Hired members";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myself = this;
        all_members = (LinearLayout)findViewById(R.id.btn_all_task);
        new_task = (LinearLayout)findViewById(R.id.btn_newtask);
        my_task = (LinearLayout)findViewById(R.id.btn_mytask);
        about_us = (LinearLayout)findViewById(R.id.btn_about_us);
        navigationView = (NavigationView)findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.menu_all_task:
                        intent = new Intent(MainActivity.this, tasks.class);
                        startActivity(intent);
                        return true;
                    case R.id.menu_make_task:
                        intent = new Intent(MainActivity.this, make_new_task.class);
                        startActivity(intent);
                        return true;
                    case R.id.menu_mytask:
                        intent = new Intent(MainActivity.this, my_task.class);
                        startActivity(intent);
//                showHelp();
                        return true;
                    case R.id.menu_profile_setting:
//                showHelp();
                        return true;
                    case R.id.menu_exit:
                        finish();
                        return true;
                    default:
                        return true;
                }
            }
        });
        View headerView = navigationView.getHeaderView(0);
        current_user_name = (TextView)headerView.findViewById(R.id.current_user_name);
        current_user_email = (TextView)headerView.findViewById(R.id.current_user_email);

        all_members.setOnClickListener(this);
        new_task.setOnClickListener(this);
        my_task.setOnClickListener(this);
        about_us.setOnClickListener(this);
        FirebaseApp.initializeApp(this);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.drawable.logo);
        toolbar.setTitle(R.string.project_id);
        toolbar.setTitleTextColor(Color.BLACK);


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Member");
        Toast.makeText(this, myRef.toString(), Toast.LENGTH_LONG).show();
//        myRef.setValue("test@test.com");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                String value = dataSnapshot.getValue(String.class);
//                Log.d(TAG, "Value is:" + value);
                if (dataSnapshot.exists()){
                    HashMap<String, Object> dataMap = (HashMap<String, Object>) dataSnapshot.getValue();
//                    Member Member = dataSnapshot.getValue(Member.class);
//                    Log.d(TAG, "User name: " + Member.getName() + ", email " + Member.getEmail());
                    for (String key : dataMap.keySet()){

                        Object data = dataMap.get(key);

                        try{
                            HashMap<String, Object> userData = (HashMap<String, Object>) data;
                            String userName = userData.get("Name").toString();
                            String userEmail = userData.get("Email").toString();
                            String userGender = userData.get("Gender").toString();
                            if(userEmail.equals(Global.current_user_email)) {
                                Global.current_user_name = userName;
                                current_user_name.setText(Global.current_user_name);
                                current_user_email.setText(Global.current_user_email);
                            }
                            array_all_members.add(new Member(userName, userEmail, userGender));
                        }catch (ClassCastException cce){


                            try{

                                String mString = String.valueOf(dataMap.get(key));
//                                Log.e(TAG, mString);

                            }catch (ClassCastException cce2){

                            }
                        }

                    }
                    Global.array_all_members = array_all_members;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG,"Failed to rad value ", databaseError.toException());
            }
        });
        DatabaseReference post_Ref = database.getReference("POST");
        post_Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    HashMap<String, Object> postData = (HashMap<String, Object>) dataSnapshot.getValue();
                        try{
                            String post_title = postData.get("Title").toString();
                            String post_desc = postData.get("Description").toString();
                            String post_created = postData.get("Created date").toString();
                            TextView title = (TextView) findViewById(R.id.post_title);
                            TextView desc = (TextView)findViewById(R.id.post_desc);
                            TextView created = (TextView)findViewById(R.id.post_created);
                            title.setText(post_title.toString());
                            desc.setText(post_desc.toString());
                            created.setText(post_created.toString());



                        }catch (ClassCastException cce){
                        }


                    Global.array_all_members = array_all_members;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG,"Failed to rad value ", databaseError.toException());
            }
        });

        DatabaseReference Project_Ref = database.getReference("Project");
        Project_Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.e("Count " ,""+snapshot.getChildrenCount());
                for(DataSnapshot ds : snapshot.getChildren()) {
                    String name = ds.getKey();
                    Global.list_project.add(name);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("The read failed: " ,databaseError.toString());
            }
        });


        DatabaseReference Task_Ref = database.getReference("TASK");
        Toast.makeText(this, myRef.toString(), Toast.LENGTH_LONG).show();
//        myRef.setValue("test@test.com");
        Task_Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    HashMap<String, Object> dataMap = (HashMap<String, Object>) dataSnapshot.getValue();
                    for (String key : dataMap.keySet()){

                        Object data = dataMap.get(key);

                        try{
                            HashMap<String, Object> userData = (HashMap<String, Object>) data;
                            String task_id = userData.get("A_Id").toString();
                            String task_description = userData.get("B_Description").toString();
                            String task_created_date = userData.get("C_Created_date").toString();
                            String task_duration = userData.get("C_Duration").toString();
                            String task_start_date = userData.get("C_Start_date").toString();
                            String task_end_date = userData.get("C_End_date").toString();
                            String task_creator = userData.get("E_Creator").toString();
                            String task_involving_project = userData.get("D_Involving_Project").toString();
                            ArrayList<hired_member> hired_member = new ArrayList<>();
                            HashMap<String, Object> Hired = (HashMap<String, Object>) userData.get("Hired_Members");
                            for (String subkey : Hired.keySet()) {
                                Object sub_data = Hired.get(subkey);
                                hired_member member = null;
                                try {
                                    HashMap<String, Object> hired_Data = (HashMap<String, Object>) sub_data;
                                    String hired_name = hired_Data.get("Name").toString();
                                    String hired_email = hired_Data.get("Email").toString();
                                    member = new hired_member(hired_name,hired_email);
                                    hired_member.add(member);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                            }
                            task task = new task(task_id, task_description,task_created_date, task_involving_project, task_duration, task_start_date, task_end_date,  task_creator, hired_member);
                            if (Global.array_all_task.size()>0){
                                boolean is_new = true;
                                for (int i =0 ; i< Global.array_all_task.size(); i ++){
                                    if(task_id.equals(Global.array_all_task.get(i).getTask_id()))
                                        is_new = false;
                                }
                                if (is_new)
                                    Global.array_all_task.add(task);
                            }
                            else
                                Global.array_all_task.add(task);

                        }catch (Exception cce){
                            continue;
//                            try{
//                                String mString = String.valueOf(dataMap.get(key));
////                                Log.e(TAG, mString);
//                            }catch (ClassCastException cce2){
//
//                            }
                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG,"Failed to rad value ", databaseError.toException());
            }
        });



    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_all_task:
                intent = new Intent(this, tasks.class);
                startActivity(intent);
                break;
            case R.id.btn_mytask:
                break;
            case R.id.btn_newtask:
                intent = new Intent(this, make_new_task.class);
                startActivity(intent);
                break;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_drawer, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_all_task:
                intent = new Intent(this, tasks.class);
                startActivity(intent);
                return true;
            case R.id.menu_make_task:
                intent = new Intent(this, make_new_task.class);
                startActivity(intent);
                return true;
            case R.id.menu_mytask:
//                showHelp();
                return true;
            case R.id.menu_profile_setting:
//                showHelp();
                return true;
            case R.id.menu_exit:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public static MainActivity getInstance(){
        return myself;
    }
}

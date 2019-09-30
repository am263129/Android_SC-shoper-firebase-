package project.task.charge;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

import project.task.charge.R;

import project.task.charge.feed.feed_editor;
import project.task.charge.member.Member;
import project.task.charge.project.project;
import project.task.charge.ui.register.register;
import project.task.charge.util.Global;
import project.task.charge.feed.feed;
import project.task.charge.feed.feedback;
import project.task.charge.member.hired_member;
import project.task.charge.task.task;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public String TAG = "Main";
    private LinearLayout all_members, new_task, my_task, created_task, project_list, new_project, task_list, create_member;
    public static MainActivity myself;
    private Intent intent;
    private TextView current_user_name, current_user_email;
    private NavigationView navigationView;
    private TableRow task_area_a, task_area_b, project_area_a, project_area_b;
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

    private Integer mInterval = 0;
    private Handler mHandler;
    ArrayList<feed> arrayList_feed;
    public Integer index = 0;
    private Calendar calendar;
    private int year, month, day;
    TextView feed_area;
    ImageView facebook, linkedin, twitter, btn_pre_feed, btn_next_feed;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myself = this;
        all_members = (LinearLayout)findViewById(R.id.btn_all_task);
        new_task = (LinearLayout)findViewById(R.id.btn_newtask);
        my_task = (LinearLayout)findViewById(R.id.btn_mytask);
        created_task = (LinearLayout)findViewById(R.id.btn_task_created);
        project_list = (LinearLayout)findViewById(R.id.btn_project);
        new_project = (LinearLayout)findViewById(R.id.btn_newproject);
        task_list = (LinearLayout)findViewById(R.id.btn_task);
        create_member = (LinearLayout)findViewById(R.id.btn_create_member);
        navigationView = (NavigationView)findViewById(R.id.nav_view);
        task_area_a = (TableRow)findViewById(R.id.task_area_a);
        task_area_b = (TableRow)findViewById(R.id.task_area_b);
        project_area_a = (TableRow)findViewById(R.id.project_area_a);
        project_area_b = (TableRow)findViewById(R.id.project_area_b);
        feed_area = (TextView) findViewById(R.id.post_title);
        facebook = (ImageView)findViewById(R.id.ico_facebook);
        linkedin = (ImageView)findViewById(R.id.ico_linkedin);
        twitter = (ImageView)findViewById(R.id.ico_twitter);
        btn_pre_feed = (ImageView)findViewById(R.id.btn_previous_feed);
        btn_next_feed = (ImageView)findViewById(R.id.btn_next_feed);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.add_new_member:
                        intent = new Intent(MainActivity.this, register.class);
                        startActivity(intent);
                        return true;
                    case R.id.menu_all_task:
                        intent = new Intent(MainActivity.this, tasks.class);
                        intent.putExtra(Global.ORIGIN,Global.FROM_MAIN);
                        startActivity(intent);
                        return true;
                    case R.id.menu_make_task:
                        intent = new Intent(MainActivity.this, make_new_task.class);
                        startActivity(intent);
                        return true;
                    case R.id.menu_mytask:
                        intent = new Intent(MainActivity.this, my_task.class);
                        startActivity(intent);
                        return true;
                    case R.id.task_I_created:
                        intent = new Intent(MainActivity.this, Created_task.class);
                        startActivity(intent);
                        return true;
                    case R.id.menu_profile_setting:
                        intent = new Intent(MainActivity.this, profile.class);
                        startActivity(intent);
                        return true;
                    default:
                        return true;
                }
            }
        });
        View headerView = navigationView.getHeaderView(0);
        current_user_name = (TextView)headerView.findViewById(R.id.current_user_name);
        current_user_email = (TextView)headerView.findViewById(R.id.current_user_email);


        arrayList_feed = new ArrayList<feed>();

        all_members.setOnClickListener(this);
        new_task.setOnClickListener(this);
        my_task.setOnClickListener(this);
        created_task.setOnClickListener(this);
        project_list.setOnClickListener(this);
        new_project.setOnClickListener(this);
        task_list.setOnClickListener(this);
        create_member.setOnClickListener(this);
        feed_area.setOnClickListener(this);
        facebook.setOnClickListener(this);
        linkedin.setOnClickListener(this);
        twitter.setOnClickListener(this);
        btn_pre_feed.setOnClickListener(this);
        btn_next_feed.setOnClickListener(this);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
        String str = sdf.format(currentTime);
        String sub_id = String.valueOf(currentTime.getHours())+String.valueOf(currentTime.getMinutes());
//        Global.today = String.valueOf(day+"-"+(month+1)+"-"+year);
        Global.today = currentTime.toString();
        FirebaseApp.initializeApp(this);
        try {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }
        catch (Exception e){
            Log.e(TAG, e.toString());
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Member");
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("Now data loading");
        progressDialog.show();
//        Toast.makeText(this, myRef.toString(), Toast.LENGTH_LONG).show();
//        myRef.setValue("test@test.com");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                String value = dataSnapshot.getValue(String.class);
//                Log.d(TAG, "Value is:" + value);
                Global.array_all_members.clear();
                if (dataSnapshot.exists()){
                    HashMap<String, Object> dataMap = (HashMap<String, Object>) dataSnapshot.getValue();
//                    Member Member = dataSnapshot.getValue(Member.class);
//                    Log.d(TAG, "User name: " + Member.getName() + ", email " + Member.getEmail());
                    for (String key : dataMap.keySet()){

                        Object data = dataMap.get(key);

                        try{
                            HashMap<String, Object> userData = (HashMap<String, Object>) data;
                            String userName = userData.get("Name").toString();
                            String userEmail = userData.get("Personal Email").toString();
                            String userGender = userData.get("Gender").toString();
                            String password = userData.get("Password").toString();
                            String user_designation = userData.get("Designation").toString();
                            String user_official_email = userData.get("Official Email").toString();
                            String user_official_number = userData.get("Official Number").toString();
                            String user_personal_number = userData.get("Personal Number").toString();
                            String base64photo = "";
                            String birthday = null;
                            String address = null;
                            String location = null;
                            String phone = null;
                            try {
                                birthday = userData.get("Birthday").toString();
                                address = userData.get("Address").toString();
                                location = userData.get("Location").toString();
                                phone = userData.get("Phone").toString();
                            }
                            catch (Exception e)
                            {
                                Log.e(TAG, "user profile is not complited");
                                if (userEmail.equals(Global.current_user_email))
                                    Toast.makeText(MainActivity.this,"You didn't complete profile. please complete the profile",Toast.LENGTH_LONG).show();
                            }

                            if(userEmail.equals(Global.current_user_email)) {
                                Global.current_user_name = userName;
                                current_user_name.setText(Global.current_user_name);
                                current_user_email.setText(Global.current_user_email);
                                Global.current_user_index = array_all_members.size();
                                try {
                                    base64photo = userData.get("Photo").toString();
                                    Global.current_user_photo = base64photo;
                                    setPhoto(base64photo);
                                }
                                catch (Exception e){
                                    Log.e(TAG,e.toString());
                                }
                                try {
                                    String permission = userData.get("Permission").toString();
                                    if (permission.equals("admin")) {
                                        Global.is_admin = true;
                                        new_project.setClickable(true);
                                        create_member.setClickable(true);
                                        navigationView.getMenu().getItem(0).setVisible(true);
                                        new_project.setBackgroundResource(R.drawable.main_ico_newproject);
                                        create_member.setBackgroundResource(R.drawable.main_ico_addmember);
                                    }

                                }
                                catch (Exception e){
                                    Log.e(TAG, "No permission");

                                }
                            }
                            array_all_members.add(new Member(userName, userEmail, userGender, base64photo, birthday, address, location, password, user_designation, user_official_email, user_official_number, user_personal_number));
                        }catch (Exception cce){
                            Log.e(TAG, cce.toString());
                        }

                    }
                    Global.array_all_members = array_all_members;
                    progressDialog.dismiss();
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG,"Failed to rad value ", databaseError.toException());
            }
        });
        /*
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
        });*/

        DatabaseReference post_Ref = database.getReference("Feed");
        post_Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Global.array_feed.clear();
                    HashMap<String, Object> dataMap = (HashMap<String, Object>) dataSnapshot.getValue();
                    for (String key : dataMap.keySet()) {

                        Object data = dataMap.get(key);

                        try {
                            HashMap<String, Object> userData = (HashMap<String, Object>) data;
                            String feed_title = userData.get("Title").toString();
                            String feed_desc = userData.get("Description").toString();
                            String feed_created = userData.get("Created_Date").toString();
                            feed feed = new feed(feed_title, feed_desc, feed_created);
                            arrayList_feed.add(feed);
                        } catch (Exception cce) {
                            Log.e(TAG, cce.toString());
                        }

                    }
                    Global.array_feed = arrayList_feed;
                    index = 0;
                    stopRepeatingTask();
                    startRepeatingTask();

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
                Global.list_project.clear();
                Global.array_all_task.clear();
                Global.array_my_task.clear();
                Global.array_created_task.clear();
                Global.array_project.clear();

                for(DataSnapshot ds : snapshot.getChildren()) {
                    String name = ds.getKey();
                    Global.list_project.add(name);
                    HashMap<String, Object> dataMap = (HashMap<String, Object>) snapshot.getValue();

                    for (String key : dataMap.keySet()) {
                        boolean this_is_mine = false;
                        Object data = dataMap.get(key);

                        try {
                            HashMap<String, Object> whole_data = (HashMap<String, Object>) data;
                            String project_name = whole_data.get("Name").toString();
                            String project_description = whole_data.get("Description").toString();
                            String project_client = whole_data.get("Client").toString();
                            String project_location = whole_data.get("Location").toString();
                            String project_duration = whole_data.get("Duration").toString();
                            String project_start_date = whole_data.get("Start_date").toString();
                            String project_con_date = whole_data.get("Contractual End Date").toString();
                            String project_tar_date = whole_data.get("Target End Date").toString();
                            String project_created_date = whole_data.get("Created_date").toString();
                            ArrayList<task> created_tasks = new ArrayList<task>();
                            try {
                                HashMap<String, Object> tasks = (HashMap<String, Object>) whole_data.get("Created Tasks");
                                for (String subkey : tasks.keySet()) {
                                    Object sub_data = tasks.get(subkey);
                                    try {
                                        HashMap<String, Object> userData = (HashMap<String, Object>) sub_data;
                                        String task_id = userData.get("A_Id").toString();
                                        String task_description = userData.get("B_Description").toString();
                                        String task_created_date = userData.get("C_Created_date").toString();
                                        String task_duration = userData.get("C_Duration").toString();
                                        String task_start_date = userData.get("C_Start_date").toString();
                                        String task_end_date = userData.get("C_End_date").toString();
                                        String task_creator = userData.get("E_Creator").toString();
                                        String task_involving_project = userData.get("D_Involving_Project").toString();
                                        String task_status = userData.get("F_Status").toString();
                                        ArrayList<hired_member> hired_member = new ArrayList<>();
                                        HashMap<String, Object> Hired = (HashMap<String, Object>) userData.get("Hired_Members");
                                        for (String sub_subkey : Hired.keySet()) {
                                            Object sub_subdata = Hired.get(sub_subkey);
                                            hired_member member = null;
                                            try {
                                                HashMap<String, Object> hired_Data = (HashMap<String, Object>) sub_subdata;
                                                String hired_name = hired_Data.get("Name").toString();
                                                String hired_email = hired_Data.get("Email").toString();
                                                member = new hired_member(hired_name, hired_email);
                                                hired_member.add(member);
                                                if (hired_email.equals(Global.current_user_email))
                                                    this_is_mine = true;
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }


                                        }
                                        ArrayList<feedback> feedbacks = new ArrayList<>();
                                        try {
                                            HashMap<String, Object> feedback = (HashMap<String, Object>) userData.get("E_Feedback");
                                            for (String sub_sub_subkey : feedback.keySet()) {
                                                String id = sub_sub_subkey;
                                                Object sub_feedback = feedback.get(sub_sub_subkey);
                                                feedback item_feedback = null;
                                                try {
                                                    HashMap<String, Object> hired_Data = (HashMap<String, Object>) sub_feedback;
                                                    String hired_name = hired_Data.get("Author").toString();
                                                    String hired_feedback = hired_Data.get("Feedback").toString();
                                                    item_feedback = new feedback(sub_sub_subkey,hired_name, hired_feedback);
                                                    feedbacks.add(item_feedback);
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }

                                            }
                                            Collections.sort(feedbacks, new Global.FishNameComparator());
                                        } catch (Exception e) {
                                            Log.e("Task", "No Feedback");
                                        }

                                        task task = new task(task_id, task_description, task_created_date, task_involving_project, task_duration, task_start_date, task_end_date, task_creator, hired_member, feedbacks, task_status);

                                        created_tasks.add(task);
                                        {
                                            boolean is_new = true;
                                            for (int i =0; i < Global.array_all_task.size();i ++) {
                                                if (Global.array_all_task.get(i).getTask_id().toString().equals(task_id)) {
                                                    is_new = false;
                                                    break;
                                                }
                                            }
                                            if (is_new)
                                                Global.array_all_task.add(task);
                                        }
                                        if (task.getTask_creator().equals(Global.current_user_name)) {

                                            boolean is_new = true;
                                            for (int i =0; i < Global.array_created_task.size();i ++) {
                                                if (Global.array_created_task.get(i).getTask_id().toString().equals(task_id)) {
                                                    is_new = false;
                                                    break;
                                                }
                                            }
                                            if (is_new)
                                                Global.array_created_task.add(task);
                                        }
                                        if (this_is_mine) {
                                            boolean is_new = true;
                                            for (int i =0; i < Global.array_my_task.size();i ++) {
                                                if (Global.array_my_task.get(i).getTask_id().toString().equals(task_id)) {
                                                    is_new = false;
                                                    break;
                                                }
                                            }
                                            if (is_new)
                                                Global.array_my_task.add(task);
                                            if (!task.getTask_status().toString().equals("completed")){
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                    String creator = task.getTask_creator().toLowerCase();
                                                    String notification_message;
                                                    String status = "";
                                                    if (task.getTask_status().equals("On Going.")) {
                                                        notification_message = "Hi, Your task " + task.getTask_id().toString() + " is on going";
                                                        status = "ongoing";
                                                    }
                                                    else {
                                                        notification_message = "Hi, Your task " + task.getTask_id().toString() + " uncompleted and late. Please hurry up";
                                                        status = "late";
                                                    }
                                                    notificationDialog(creator, notification_message, status);

                                                }
                                            }
                                        }
                                    } catch (Exception e) {
                                        Log.e(TAG, e.toString());
                                    }
                                }
                            }
                            catch (Exception E){

                            }

                            project project = new project(project_name, project_description, project_client, project_location, project_duration, project_start_date, project_con_date, project_tar_date, project_created_date, created_tasks);
                            if (Global.array_project.size() >0){
                                boolean is_new = true;
                                for (int j = 0; j < Global.array_project.size(); j ++){
                                    if (Global.array_project.get(j).getProject_name().equals(project_name))
                                        is_new = false;
                                }
                                if (is_new){
                                    Global.array_project.add(project);
                                }
                            }
                            else
                                Global.array_project.add(project);
                        } catch (Exception cce) {
                            continue;
                        }

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("The read failed: " ,databaseError.toString());
            }
        });



//        DatabaseReference Task_Ref = database.getReference("TASK");
//        Toast.makeText(this, myRef.toString(), Toast.LENGTH_LONG).show();
////        myRef.setValue("test@test.com");
//        Task_Ref.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()){
//                    HashMap<String, Object> dataMap = (HashMap<String, Object>) dataSnapshot.getValue();
//                    for (String key : dataMap.keySet()){
//                        boolean this_is_mine = false;
//                        Object data = dataMap.get(key);
//
//                        try{
//                            HashMap<String, Object> userData = (HashMap<String, Object>) data;
//                            String task_id = userData.get("A_Id").toString();
//                            String task_description = userData.get("B_Description").toString();
//                            String task_created_date = userData.get("C_Created_date").toString();
//                            String task_duration = userData.get("C_Duration").toString();
//                            String task_start_date = userData.get("C_Start_date").toString();
//                            String task_end_date = userData.get("C_End_date").toString();
//                            String task_creator = userData.get("E_Creator").toString();
//                            String task_involving_project = userData.get("D_Involving_Project").toString();
//                            ArrayList<hired_member> hired_member = new ArrayList<>();
//                            HashMap<String, Object> Hired = (HashMap<String, Object>) userData.get("Hired_Members");
//                            for (String subkey : Hired.keySet()) {
//                                Object sub_data = Hired.get(subkey);
//                                hired_member member = null;
//                                try {
//                                    HashMap<String, Object> hired_Data = (HashMap<String, Object>) sub_data;
//                                    String hired_name = hired_Data.get("Name").toString();
//                                    String hired_email = hired_Data.get("Email").toString();
//                                    member = new hired_member(hired_name,hired_email);
//                                    hired_member.add(member);
//                                    if (hired_email.equals(Global.current_user_email))
//                                            this_is_mine = true;
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//
//
//                            }
//                            ArrayList<feedback> feedbacks = new ArrayList<>();
//                            try {
//                                HashMap<String, Object> feedback = (HashMap<String, Object>) userData.get("E_Feedback");
//                                for (String subkey : feedback.keySet()) {
//                                    Object sub_feedback = feedback.get(subkey);
//                                    feedback item_feedback = null;
//                                    try {
//                                        HashMap<String, Object> hired_Data = (HashMap<String, Object>) sub_feedback;
//                                        String hired_name = hired_Data.get("Author").toString();
//                                        String hired_feedback = hired_Data.get("Feedback").toString();
//                                        item_feedback = new feedback(hired_name, hired_feedback);
//                                        feedbacks.add(item_feedback);
//                                    } catch (Exception e) {
//                                        e.printStackTrace();
//                                    }
//
//                                }
//                            }
//                            catch (Exception e){
//                                Log.e("Task", "No Feedback");
//                            }
//
//                            project.task.charge.task.task task = new task(task_id, task_description,task_created_date, task_involving_project, task_duration, task_start_date, task_end_date,  task_creator, hired_member, feedbacks);
//                            if (Global.array_all_task.size()>0){
//                                boolean is_new = true;
//                                for (int i =0 ; i< Global.array_all_task.size(); i ++){
//                                    if(task_id.equals(Global.array_all_task.get(i).getTask_id()))
//                                        is_new = false;
//                                }
//                                if (is_new) {
//                                    Global.array_all_task.add(task);
//                                    if (task.getTask_creator().equals(Global.current_user_name))
//                                        Global.array_created_task.add(task);
//                                }
//                            }
//                            else {
//                                Global.array_all_task.add(task);
//                                if (task.getTask_creator().equals(Global.current_user_name))
//                                    Global.array_created_task.add(task);
//                            }
//                            if(this_is_mine)
//                                Global.array_my_task.add(task);
//
//                        }catch (Exception cce){
//                            continue;
////                            try{
////                                String mString = String.valueOf(dataMap.get(key));
//////                                Log.e(TAG, mString);
////                            }catch (ClassCastException cce2){
////
////                            }
//                        }
//
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Log.w(TAG,"Failed to rad value ", databaseError.toException());
//            }
//        });


        mHandler = new Handler();
        startRepeatingTask();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void notificationDialog(String creator, String message, String status) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "tutorialspoint_01";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            @SuppressLint("WrongConstant") NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, creator, NotificationManager.IMPORTANCE_MAX);
            // Configure the notification channel.
            notificationChannel.setDescription(message);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        notificationBuilder.setAutoCancel(false)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setTicker("Tutorialspoint")
                //.setPriority(Notification.PRIORITY_MAX)
                .setContentTitle("Task status")
                .setContentText(message)
                .setContentInfo(creator);
        if (status.equals("late")){
            notificationBuilder.setSmallIcon(R.drawable.ico_late);
        }else
            notificationBuilder.setSmallIcon(R.drawable.ico_ongoing);
        notificationManager.notify(1, notificationBuilder.build());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Global.current_user_photo != null)
            setPhoto(Global.current_user_photo);

    }

    private void startRepeatingTask() {
        mStatusChecker.run();
    }
    public void stopRepeatingTask() {
        mHandler.removeCallbacks(mStatusChecker);
    }
    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            if (arrayList_feed.size()>0) {
                arrayList_feed.get(index).getFeed_crated_date();
                TextView title = (TextView) findViewById(R.id.post_title);
                TextView desc = (TextView) findViewById(R.id.post_desc);
                TextView created = (TextView) findViewById(R.id.post_created);
                title.setText(arrayList_feed.get(index).getFeed_title());
                desc.setText(arrayList_feed.get(index).getFeed_description());
                created.setText(arrayList_feed.get(index).getFeed_crated_date());
                index++;
                if (index == arrayList_feed.size()){
                    index = 0;
                }
                mInterval = 10000;
            }

            mHandler.postDelayed(mStatusChecker, mInterval);
        }
    };

    private void setPhoto(String base64photo) {
        String imageDataBytes = base64photo.substring(base64photo.indexOf(",")+1);

        InputStream stream = new ByteArrayInputStream(Base64.decode(imageDataBytes.getBytes(), Base64.DEFAULT));

        Bitmap bitmap = BitmapFactory.decodeStream(stream);
        ImageView userPhoto  = (ImageView)navigationView.getHeaderView(0).findViewById(R.id.nav_header_userphoto);
        userPhoto.setImageBitmap(bitmap);
    }

    @Override
    public void onClick(View view) {
        Intent URL_intent = new Intent();
        switch (view.getId()){
            case R.id.btn_all_task:
                intent = new Intent(this, tasks.class);
                intent.putExtra(Global.ORIGIN,Global.FROM_MAIN);
                startActivity(intent);
                break;
            case R.id.btn_mytask:
                intent = new Intent(MainActivity.this, my_task.class);
                startActivity(intent);
                break;
            case R.id.btn_newtask:
                intent = new Intent(this, make_new_task.class);
                startActivity(intent);
                break;
            case R.id.btn_task_created:
                intent = new Intent(this, tasks.class);
                intent.putExtra(Global.ORIGIN,Global.I_CREATED);
                startActivity(intent);
                break;
            case R.id.btn_project:
                intent = new Intent(this, project_list.class);
                startActivity(intent);
                break;
            case R.id.btn_newproject:
                if(Global.is_admin) {
                    intent = new Intent(this, create_project.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(MainActivity.this,"You don't have permission",Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.btn_task:
                Global.is_task_area = true;
                project_area_a.setVisibility(View.GONE);
                project_area_b.setVisibility(View.GONE);
                task_area_a.setVisibility(View.VISIBLE);
                task_area_b.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_create_member:
                if(Global.is_admin){
                intent = new Intent(MainActivity.this, register.class);
                startActivity(intent);
                }
                else {
                    Toast.makeText(MainActivity.this,"You don't have permission",Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.post_title:
                if (Global.is_admin)
                make_dialog();
                else
                    Toast.makeText(MainActivity.this,"You don't have permission",Toast.LENGTH_LONG).show();
                break;
            case R.id.ico_facebook:
                URL_intent.setAction(Intent.ACTION_VIEW);
                URL_intent.addCategory(Intent.CATEGORY_BROWSABLE);
                URL_intent.setData(Uri.parse("http://facebook.com"));
                startActivity(URL_intent);
                break;
            case R.id.ico_linkedin:
                URL_intent.setAction(Intent.ACTION_VIEW);
                URL_intent.addCategory(Intent.CATEGORY_BROWSABLE);
                URL_intent.setData(Uri.parse("http://linkedin.com"));
                startActivity(URL_intent);
                break;
            case R.id.ico_twitter:
                URL_intent.setAction(Intent.ACTION_VIEW);
                URL_intent.addCategory(Intent.CATEGORY_BROWSABLE);
                URL_intent.setData(Uri.parse("http://twitter.com"));
                startActivity(URL_intent);
                break;
            case R.id.btn_previous_feed:
                stopRepeatingTask();
                index = index - 2;

                if (index == -1)
                    index = arrayList_feed.size()-1;
                else if (index == -2)
                    index = arrayList_feed.size()-2;
                startRepeatingTask();

                break;
            case R.id.btn_next_feed:
                if (index == arrayList_feed.size())
                    index = 0;
                stopRepeatingTask();
                startRepeatingTask();

                break;



        }

    }

    private void make_dialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dlg_feed);
        dialog.setTitle("Edit Feed");
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);
        Button feed_edit = (Button)dialog.findViewById(R.id.btn_edit);
        Button feed_new = (Button)dialog.findViewById(R.id.btn_new);
        Button feed_delete = (Button)dialog.findViewById(R.id.btn_delete);

        final Intent intent = new Intent(MainActivity.this, feed_editor.class);
        feed_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer tempindex = index -1;
                if ((tempindex < 0 )||(index > arrayList_feed.size()-1))
                    tempindex = arrayList_feed.size()-1;
                intent.putExtra(Global.TYPE, Global.TYPE_EDIT);
                intent.putExtra(Global.INDEX, tempindex);
                startActivity(intent);
                dialog.dismiss();

            }
        });
        feed_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra(Global.TYPE, Global.TYPE_NEW);
                startActivity(intent);
                dialog.dismiss();
            }
        });
        feed_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer tempindex = index -1;
                if (tempindex < 0 )
                    tempindex = arrayList_feed.size()-1;
                if (index > arrayList_feed.size()-1) {
                    tempindex = arrayList_feed.size() - 1;
                }
                intent.putExtra(Global.TYPE, Global.TYPE_DELETE);
                intent.putExtra(Global.INDEX, tempindex);
                startActivity(intent);
                dialog.dismiss();

            }
        });
        dialog.show();



    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.activity_main_drawer, menu);
//        return true;
//    }
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle item selection
//        switch (item.getItemId()) {
//            case R.id.menu_all_task:
//                intent = new Intent(this, tasks.class);
//                startActivity(intent);
//                return true;
//            case R.id.menu_make_task:
//                intent = new Intent(this, make_new_task.class);
//                startActivity(intent);
//                return true;
//            case R.id.menu_mytask:
//                intent = new Intent(this, Created_task.class);
//                startActivity(intent);
//                return true;
//            case R.id.menu_profile_setting:
//                intent = new Intent(this, profile.class);
//                startActivity(intent);
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }


    public static MainActivity getInstance(){
        return myself;
    }
    @Override
    public void onBackPressed()
    {
        if(Global.is_task_area){
            project_area_a.setVisibility(View.VISIBLE);
            project_area_b.setVisibility(View.VISIBLE);
            task_area_a.setVisibility(View.GONE);
            task_area_b.setVisibility(View.GONE);
            Global.is_task_area = false;
        }
        else {

        }

    }
}

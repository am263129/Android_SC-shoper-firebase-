package project.task.charge;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import java.util.ArrayList;
import java.util.HashMap;

import project.task.charge.R;

import project.task.charge.member.Member;
import project.task.charge.ui.register.register;
import project.task.charge.util.Global;
import project.task.charge.feed.feed;
import project.task.charge.feed.feedback;
import project.task.charge.member.hired_member;
import project.task.charge.task.task;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public String TAG = "Main";
    private LinearLayout all_members, new_task, my_task, created_task, project, new_project, task, create_member;
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

    private Integer mInterval = 15000;
    private Handler mHandler;
    ArrayList<feed> arrayList_feed;
    private Integer index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myself = this;
        all_members = (LinearLayout)findViewById(R.id.btn_all_task);
        new_task = (LinearLayout)findViewById(R.id.btn_newtask);
        my_task = (LinearLayout)findViewById(R.id.btn_mytask);
        created_task = (LinearLayout)findViewById(R.id.btn_task_created);
        project = (LinearLayout)findViewById(R.id.btn_project);
        new_project = (LinearLayout)findViewById(R.id.btn_newproject);
        task = (LinearLayout)findViewById(R.id.btn_task);
        create_member = (LinearLayout)findViewById(R.id.btn_create_member);
        navigationView = (NavigationView)findViewById(R.id.nav_view);
        task_area_a = (TableRow)findViewById(R.id.task_area_a);
        task_area_b = (TableRow)findViewById(R.id.task_area_b);
        project_area_a = (TableRow)findViewById(R.id.project_area_a);
        project_area_b = (TableRow)findViewById(R.id.project_area_b);
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
        project.setOnClickListener(this);
        new_project.setOnClickListener(this);
        task.setOnClickListener(this);
        create_member.setOnClickListener(this);
        FirebaseApp.initializeApp(this);
        try {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }
        catch (Exception e){
            Log.e(TAG, e.toString());
        }

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
                            String password = userData.get("Password").toString();
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
                                        new_project.setBackgroundResource(R.drawable.ico_make_project);
                                        create_member.setBackgroundResource(R.drawable.ico_add_user);
                                    }

                                }
                                catch (Exception e){
                                    Log.e(TAG, "No permission");

                                }
                            }
                            array_all_members.add(new Member(userName, userEmail, userGender, base64photo, birthday, address, location, phone, password));
                        }catch (Exception cce){
                            Log.e(TAG, cce.toString());
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
                    HashMap<String, Object> dataMap = (HashMap<String, Object>) dataSnapshot.getValue();
//                    Member Member = dataSnapshot.getValue(Member.class);
//                    Log.d(TAG, "User name: " + Member.getName() + ", email " + Member.getEmail());
                    for (String key : dataMap.keySet()) {

                        Object data = dataMap.get(key);

                        try {
                            HashMap<String, Object> userData = (HashMap<String, Object>) data;
                            String feed_title = userData.get("Title").toString();
                            String feed_desc = userData.get("Description").toString();
                            String feed_created = userData.get("Created_Date").toString();
                            feed feed = new feed(feed_title, feed_desc, feed_created);
                            arrayList_feed.add(feed);
//                            TextView title = (TextView) findViewById(R.id.post_title);
//                            TextView desc = (TextView) findViewById(R.id.post_desc);
//                            TextView created = (TextView) findViewById(R.id.post_created);
//                            title.setText(feed_title.toString());
//                            desc.setText(feed_desc.toString());
//                            created.setText(feed_created.toString());
                        } catch (Exception cce) {
                            Log.e(TAG, cce.toString());
                        }


                        Global.array_all_members = array_all_members;
                    }
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
                        boolean this_is_mine = false;
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
                                    if (hired_email.equals(Global.current_user_email))
                                            this_is_mine = true;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                            }
                            ArrayList<feedback> feedbacks = new ArrayList<>();
                            try {
                                HashMap<String, Object> feedback = (HashMap<String, Object>) userData.get("E_Feedback");
                                for (String subkey : feedback.keySet()) {
                                    Object sub_feedback = feedback.get(subkey);
                                    feedback item_feedback = null;
                                    try {
                                        HashMap<String, Object> hired_Data = (HashMap<String, Object>) sub_feedback;
                                        String hired_name = hired_Data.get("Author").toString();
                                        String hired_feedback = hired_Data.get("Feedback").toString();
                                        item_feedback = new feedback(hired_name, hired_feedback);
                                        feedbacks.add(item_feedback);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                }
                            }
                            catch (Exception e){
                                Log.e("Task", "No Feedback");
                            }

                            project.task.charge.task.task task = new task(task_id, task_description,task_created_date, task_involving_project, task_duration, task_start_date, task_end_date,  task_creator, hired_member, feedbacks);
                            if (Global.array_all_task.size()>0){
                                boolean is_new = true;
                                for (int i =0 ; i< Global.array_all_task.size(); i ++){
                                    if(task_id.equals(Global.array_all_task.get(i).getTask_id()))
                                        is_new = false;
                                }
                                if (is_new) {
                                    Global.array_all_task.add(task);
                                    if (task.getTask_creator().equals(Global.current_user_name))
                                        Global.array_created_task.add(task);
                                }
                            }
                            else {
                                Global.array_all_task.add(task);
                                if (task.getTask_creator().equals(Global.current_user_name))
                                    Global.array_created_task.add(task);
                            }
                            if(this_is_mine)
                                Global.array_my_task.add(task);

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


        mHandler = new Handler();
        startRepeatingTask();
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
    void stopRepeatingTask() {
        mHandler.removeCallbacks(mStatusChecker);
    }
    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            if (index == arrayList_feed.size()){
                index = 0;
            }
            if (arrayList_feed.size()>0) {
                arrayList_feed.get(index).getFeed_crated_date();
                TextView title = (TextView) findViewById(R.id.post_title);
                TextView desc = (TextView) findViewById(R.id.post_desc);
                TextView created = (TextView) findViewById(R.id.post_created);
                title.setText(arrayList_feed.get(index).getFeed_title());
                desc.setText(arrayList_feed.get(index).getFeed_description());
                created.setText(arrayList_feed.get(index).getFeed_crated_date());
                index++;
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
        switch (view.getId()){
            case R.id.btn_all_task:
                intent = new Intent(this, tasks.class);
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
                intent = new Intent(this, Created_task.class);
                startActivity(intent);
                break;
            case R.id.btn_project:
                break;
            case R.id.btn_newproject:
                intent = new Intent(this, create_project.class);
                startActivity(intent);
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

        }

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

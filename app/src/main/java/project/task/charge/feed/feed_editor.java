package project.task.charge.feed;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import project.task.charge.MainActivity;
import project.task.charge.R;
import project.task.charge.task.task_view;
import project.task.charge.util.Global;

public class feed_editor extends AppCompatActivity implements View.OnClickListener {
    Integer index;
    EditText feed_title;
    EditText feed_content;
    TextView feed_created;
    String type = "";
    String TAG = "feed_creator";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_editor);
        feed_title = (EditText)findViewById(R.id.feed_title);
        feed_content = (EditText)findViewById(R.id.feed_content);
        feed_created = (TextView)findViewById(R.id.feed_created_date);
        Button update = (Button)findViewById(R.id.btn_feed_edit);
        Button post = (Button)findViewById(R.id.btn_feed_new);
        Intent intent = getIntent();
        update.setOnClickListener(this);
        post.setOnClickListener(this);

        if (intent.getStringExtra(Global.TYPE).equals(Global.TYPE_EDIT)){
            type = Global.TYPE_EDIT;
            index = intent.getIntExtra(Global.INDEX,0);
            feed_title.setText(Global.array_feed.get(index).getFeed_title().toString());
            feed_content.setText(Global.array_feed.get(index).getFeed_description().toString());
            feed_created.setText(Global.array_feed.get(index).getFeed_crated_date().toString());
            post.setVisibility(View.GONE);
            update.setVisibility(View.VISIBLE);
        }
        else if (intent.getStringExtra(Global.TYPE).equals(Global.TYPE_NEW)){
            type = Global.TYPE_NEW;
            update.setVisibility(View.GONE);
            post.setVisibility(View.VISIBLE);
            feed_created.setText(Global.today);
        }
        else if (intent.getStringExtra(Global.TYPE).equals(Global.TYPE_DELETE)){
            index = intent.getIntExtra(Global.INDEX,0);
            delete_feed();
            finish();
        }
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_feed_edit:
                if (!feed_content.getText().toString().equals(""))
                    update_data(Global.TYPE_EDIT);
                else
                    Toast.makeText(feed_editor.this,"Invalid Input", Toast.LENGTH_LONG).show();
                finish();
                break;
            case R.id.btn_feed_new:
                update_data(Global.TYPE_NEW);
                finish();
                break;
            default:
                break;
        }
    }

    private void update_data(final String type) {
        FirebaseApp.initializeApp(this);
        String path = "";
        if (type.equals(Global.TYPE_EDIT)) {
            path = "Feed/" + "Feed_" + String.valueOf(index);
        }
        else {
            path = "Feed/" + "Feed_" + String.valueOf(Global.getToday());
        }
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(path+"/Title");
        myRef.setValue(feed_title.getText().toString());
        myRef = database.getReference(path + "/Description");
        myRef.setValue(feed_content.getText().toString());
        myRef = database.getReference(path + "/Created_Date");
        myRef.setValue(feed_created.getText().toString());


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (type.equals(Global.TYPE_EDIT)){
                    Toast.makeText(feed_editor.this,"Feed Edited Successfullly", Toast.LENGTH_LONG).show();
                }
                else
                    Toast.makeText(feed_editor.this,"New Feed Posted Successfullly", Toast.LENGTH_LONG).show();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG,"Failed", databaseError.toException());
                Toast.makeText(feed_editor.this,"Failed"+databaseError.toString(),Toast.LENGTH_LONG).show();
            }
        });
    }
    private void delete_feed() {
        FirebaseApp.initializeApp(this);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Query applesQuery = ref.child("Feed").orderByChild("Title").equalTo(Global.array_feed.get(index).getFeed_title().toString());

        applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                    appleSnapshot.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled", databaseError.toException());
            }
        });
    }
}

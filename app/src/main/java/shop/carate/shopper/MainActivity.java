package shop.carate.shopper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import shop.carate.shopper.ui.login.LoginActivity;
import shop.carate.shopper.ui.register.register;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public String TAG = "Main";
    private Button Login, Register,newTask;
    public static MainActivity myself;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myself = this;
        Login = (Button)findViewById(R.id.btn_login);
        Register = (Button)findViewById(R.id.btn_register);
        newTask = (Button)findViewById(R.id.btn_make_newtask);
        Login.setOnClickListener(this);
        Register.setOnClickListener(this);
        newTask.setOnClickListener(this);
        FirebaseApp.initializeApp(this);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("new");
        Toast.makeText(this, myRef.toString(), Toast.LENGTH_LONG).show();
//        myRef.setValue("test@test.com");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is:" + value);
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
            case R.id.btn_login:
                intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_register:
                intent = new Intent(this, register.class);
                startActivity(intent);
                break;
            case R.id.btn_make_newtask:
                intent = new Intent(this, make_task.class);
                startActivity(intent);
                break;
        }

    }
    public static MainActivity getInstance(){
        return myself;
    }
}

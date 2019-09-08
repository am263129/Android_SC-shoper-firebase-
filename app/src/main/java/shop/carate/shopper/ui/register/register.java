package shop.carate.shopper.ui.register;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.IOException;

import shop.carate.shopper.R;

public class register extends AppCompatActivity {
    EditText username_edt, email_edt, password_edt;
    ImageView profile_pic;
    Button register_btn;
    RadioGroup genderrb;
    RadioButton male, female;
    File upload_file = null;
    public String TAG = "Register";
    public static final int PICK_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username_edt = (EditText) findViewById(R.id.username_edt);
        email_edt = (EditText) findViewById(R.id.email_edt);
        password_edt = (EditText) findViewById(R.id.password_edt);
        genderrb = (RadioGroup) findViewById(R.id.genderrb);
        profile_pic = (ImageView) findViewById(R.id.profile_pic);
        register_btn = (Button) findViewById(R.id.register_btn);
        male = (RadioButton) findViewById(R.id.malerb);
        female = (RadioButton) findViewById(R.id.femalerb);

        profile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });



        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkemail() & check_username() & check_password()) {

                    int selectedId = genderrb.getCheckedRadioButtonId();
                    RadioButton radiogender = (RadioButton) findViewById(selectedId);

                    String selected_gender = radiogender.getText().toString();
                    String username = username_edt.getText().toString();
                    String email = email_edt.getText().toString();
                    String password = password_edt.getText().toString();

                    RegisterAPI(username, email, password, selected_gender, upload_file);

                }


            }
        });




    }

    public boolean checkemail() {

        if (android.util.Patterns.EMAIL_ADDRESS.matcher(
                email_edt.getText().toString()).matches()) {

            return true;
        } else {
            email_edt.setError("Email Invalid");
            return false;
        }
    }

    public boolean check_username() {

        if (!TextUtils.isEmpty(username_edt.getText()) & username_edt.getText().length() > 2)
            return true;
        else {
            username_edt.setError("User Name atleast 3 characters needed");
            return false;
        }
    }

    public boolean check_password() {

        if (!TextUtils.isEmpty(password_edt.getText()) & password_edt.getText().length() > 2)
            return true;
        else {
            password_edt.setError("Atleast three characters required");
            return false;
        }
    }

    public void RegisterAPI(String username, final String email, final String password, String gender, File file1) {
        FirebaseApp.initializeApp(this);
        String id = "DG/" + username;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(id+"/Name");
        myRef.setValue(username);
        myRef = database.getReference(id+"/Email");
        myRef.setValue(email);
        myRef = database.getReference(id+"/Password");
        myRef.setValue(password);
        myRef = database.getReference(id+"/Gender");
        myRef.setValue(gender);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == PICK_IMAGE) {
            //TODO: action
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
            profile_pic.setImageBitmap(bitmap);
            upload_file = new File(data.getData().getPath());
        }
    }
}

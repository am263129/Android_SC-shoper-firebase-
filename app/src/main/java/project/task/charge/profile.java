package project.task.charge;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.soundcloud.android.crop.Crop;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

import project.task.charge.R;

import project.task.charge.task.task;
import project.task.charge.util.Global;

import static project.task.charge.ui.register.register.PICK_IMAGE;

public class profile extends AppCompatActivity {
    Bitmap bitmap;
    String TAG = "Profile";
    ImageView userPhoto;
    EditText userName;
    EditText userEmail;
    EditText userAddress;
    EditText userLocation;
    EditText userPersonalPhone;
    EditText userPass;
    TextView userbirthday;
    EditText oldPass;
    EditText userOfficialEmail;
    EditText userOfficialPhone;
    EditText userDesignation;
    String gender;
    RadioButton userGender;
    FirebaseUser user;
    AuthCredential credential;
    boolean passwordchanged;
    private int year, month, day;
    private DatePickerDialog picker;
    private Calendar calendar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        userPhoto = (ImageView)findViewById(R.id.profile_pic);
        userName = (EditText)findViewById(R.id.username_edt);
        userEmail = (EditText)findViewById(R.id.personal_email_edt);
        userAddress = (EditText)findViewById(R.id.address_edt);
        userLocation = (EditText)findViewById(R.id.location_edt);
        userPersonalPhone = (EditText)findViewById(R.id.personal_phone_edt);
        userPass = (EditText)findViewById(R.id.password_edt);
        userbirthday = (TextView)findViewById(R.id.birthday_edt);
        oldPass = (EditText)findViewById(R.id.previous_password_edt);
        Button Update = (Button)findViewById(R.id.update_btn);
        userGender = (RadioButton)findViewById(R.id.malerb);
        userOfficialEmail = (EditText)findViewById(R.id.email_official_edt);
        userOfficialPhone = (EditText)findViewById(R.id.official_number_edt);
        userDesignation = (EditText)findViewById(R.id.designation_edt);

        user = FirebaseAuth.getInstance().getCurrentUser();
        credential = EmailAuthProvider.getCredential(Global.current_user_email,Global.array_all_members.get(Global.current_user_index).getPassword().toString() );
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        try {
            String imageDataBytes = Global.current_user_photo.substring(Global.current_user_photo.indexOf(",")+1);
            InputStream stream = new ByteArrayInputStream(Base64.decode(imageDataBytes.getBytes(), Base64.DEFAULT));
            Bitmap bitmap = BitmapFactory.decodeStream(stream);
            userPhoto.setImageBitmap(bitmap);
        }catch (Exception E){
            Log.e(TAG, E.toString());
        }
        userbirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                picker = new DatePickerDialog(profile.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                userbirthday.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                            }
                        }, year, month , day);

                picker.show();
            }
        });

        userPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Crop.pickImage(profile.this);

//                Intent intent = new Intent();
//                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
//                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//
//                startActivityForResult(galleryIntent, 1);
            }
        });

        userName.setText(Global.current_user_name);
        userEmail.setText(Global.current_user_email);
        userName.setEnabled(false);
        userEmail.setEnabled(false);
        try {
            userAddress.setText(Global.array_all_members.get(Global.current_user_index).getAddress());
            userLocation.setText(Global.array_all_members.get(Global.current_user_index).getLocation());
            userbirthday.setText(Global.array_all_members.get(Global.current_user_index).getBirthday());
            userDesignation.setText(Global.array_all_members.get(Global.current_user_index).getDesignation());
            userOfficialEmail.setText(Global.array_all_members.get(Global.current_user_index).getOfficial_email());
            userOfficialPhone.setText(Global.array_all_members.get(Global.current_user_index).getOfficial_phone());
            userPersonalPhone.setText(Global.array_all_members.get(Global.current_user_index).getPersonal_phone());
        }
        catch (Exception e)
        {
            Log.e(TAG, e.toString());
        }
        if (Global.array_all_members.get(Global.current_user_index).getGender().equals("Male"))
            userGender.setChecked(true);
        else
            userGender.setChecked(false);

        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfile();
            }
        });

    }

     public void updateProfile(){
        try {


            if (validate()) {
                /*
                user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            user.updatePassword(userPass.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        passwordchanged = true;
                                        Log.d(TAG, "Password updated");
                                    } else {
                                        passwordchanged = false;
                                        Log.d(TAG, "Error password not updated");
                                    }
                                }
                            });
                        } else {
                            Log.d(TAG, "Error auth failed");
                        }
                    }
                });

                 */

                FirebaseApp.initializeApp(this);
                String id = "Member/" + Global.current_user_name;
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference(id + "/Name");
                myRef.setValue(userName.getText().toString());
                myRef = database.getReference(id + "/Email");
                myRef.setValue(userEmail.getText().toString());
                /*
                myRef = database.getReference(id + "/Password");
                if (passwordchanged) {
                    myRef.setValue(userPass.getText().toString());
                } else {
                    myRef.setValue(Global.current_user_email, Global.array_all_members.get(Global.current_user_index).getPassword().toString());
                }

                 */
                myRef = database.getReference(id + "/Gender");
                if (userGender.isChecked())
                    gender = "Male";
                else
                    gender = "Female";
                myRef.setValue(gender);
                myRef = database.getReference(id + "/Address");
                myRef.setValue(userAddress.getText().toString());
                myRef = database.getReference(id + "/Location");
                myRef.setValue(userLocation.getText().toString());
                myRef = database.getReference(id + "/Official Number");
                myRef.setValue(userOfficialPhone.getText().toString());
                myRef = database.getReference(id + "/Personal Number");
                myRef.setValue(userPersonalPhone.getText().toString());
                myRef = database.getReference(id + "/Birthday");
                myRef.setValue(userbirthday.getText().toString());
                try {
                    bitmap = ((BitmapDrawable) userPhoto.getDrawable()).getBitmap();
                    myRef = database.getReference(id + "/Photo");
                    myRef.setValue(getBase64String(bitmap));
                }catch (Exception e){
                    Log.e(TAG, e.toString());
                }


                myRef = database.getReference(id+"/Designation");
                myRef.setValue(userDesignation.getText().toString());


                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String value = dataSnapshot.getValue(String.class);
                        Log.d(TAG, "Value is:" + value);
                        Toast.makeText(profile.this, "Up date completed.", Toast.LENGTH_LONG).show();
                        finish();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.w(TAG, "Failed to rad value ", databaseError.toException());
                    }
                });
            }
        }catch (Exception e){
                Log.e(TAG,e.toString());
         }
    }

    private boolean validate() {
        if(check_birthday() && check_address() && check_phone() && check_location()){
            return true;
        }
        else return false;
    }

    public boolean check_password() {
        if (oldPass.getText().toString().equals(Global.array_all_members.get(Global.current_user_index).getPassword())) {
            if (!TextUtils.isEmpty(userPass.getText()) & userPass.getText().length() > 5)
                return true;
            else {
                userPass.setError("Atleast 5 characters required");
                return false;
            }
        }
        else
        {
            oldPass.setError("Old password is wrong");
            return false;
        }
    }
    public boolean check_birthday() {

        if (!TextUtils.isEmpty(userbirthday.getText()))
            return true;
        else {
            userbirthday.setError("Please input birthday");
            return false;
        }
    }
    public boolean check_address() {

        if (!TextUtils.isEmpty(userAddress.getText()) & userAddress.getText().length() > 2)
            return true;
        else {
            userAddress.setError("User Address at least 3 characters needed");
            return false;
        }
    }
    public boolean check_location() {

        if (!TextUtils.isEmpty(userLocation.getText()) & userLocation.getText().length() > 2)
            return true;
        else {
            userLocation.setError("User location at least 3 characters needed");
            return false;
        }
    }
    public boolean check_phone() {

        if (android.util.Patterns.PHONE.matcher(
                userOfficialPhone.getText().toString()).matches()) {
            return true;
        }
        else {
            userOfficialPhone.setError("Phone number is incorrect");
            return false;
        }
    }

    private String getBase64String(Bitmap bitmap) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 40, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
            beginCrop(data.getData());
        } else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, data);
        }
//        if (requestCode == PICK_IMAGE) {
//            //TODO: action
//
//            try {
//                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
//                userPhoto.setImageBitmap(bitmap);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//        }
    }

    private void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
        Crop.of(source, destination).asSquare().start(this);
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Crop.getOutput(result));
            } catch (IOException e) {
                e.printStackTrace();
            }
            userPhoto.setImageBitmap(bitmap);
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    // Get auth credentials from the user for re-authentication. The example below shows
// email and password credentials but there are multiple possible providers,
// such as GoogleAuthProvider or FacebookAuthProvider.

}

package project.task.charge.ui.register;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import project.task.charge.MainActivity;
import project.task.charge.R;

public class register extends AppCompatActivity {
    EditText username_edt, official_email_edt, password_edt, personal_email_edt, official_num_edt, personal_num_edt, designation_edt;
    ImageView profile_pic;
    Button register_btn;
    RadioGroup genderrb;
    RadioButton male, female;
    File upload_file = null;
    public String TAG = "Register";
    public static final int PICK_IMAGE = 1;
    public Bitmap bitmap = null;
    public Uri filePath ;

    FirebaseAuth mAuth;

    private String userEmail;
    private String userPass;

    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    private static final int PICK_IMAGE_REQUEST = 234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        username_edt = (EditText) findViewById(R.id.username_edt);
        official_email_edt = (EditText) findViewById(R.id.email_official_edt);
        password_edt = (EditText) findViewById(R.id.password_edt);
        personal_email_edt = (EditText) findViewById(R.id.personal_email_edt);
        official_num_edt = (EditText) findViewById(R.id.official_number_edt);
        personal_num_edt = (EditText) findViewById(R.id.personal_number_edt);
        designation_edt = (EditText) findViewById(R.id.designation_edt);

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
                if (checkemail() & check_username() & check_password() & checkemail_personal()) {

                    int selectedId = genderrb.getCheckedRadioButtonId();
                    RadioButton radiogender = (RadioButton) findViewById(selectedId);

                    String selected_gender = radiogender.getText().toString();
                    String username = username_edt.getText().toString();
                    String email = official_email_edt.getText().toString();
                    String password = password_edt.getText().toString();


                    RegisterAPI(username, email, password, selected_gender, upload_file);

                }


            }
        });




    }
    @Override
    protected void onStart() {

        super.onStart();
    }

    public boolean checkemail() {

        if (android.util.Patterns.EMAIL_ADDRESS.matcher(
                official_email_edt.getText().toString()).matches()) {

            return true;
        } else {
            official_email_edt.setError("Email Invalid");
            return false;
        }
    }
    public boolean checkemail_personal() {

        if (android.util.Patterns.EMAIL_ADDRESS.matcher(
                personal_email_edt.getText().toString()).matches()) {

            return true;
        } else {
            personal_email_edt.setError("Email Invalid");
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
        userEmail = personal_email_edt.getText().toString();
        userPass = password;
        FirebaseApp.initializeApp(this);
        String id = "Member/" + username;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(id+"/Name");
        myRef.setValue(username);
        myRef = database.getReference(id+"/Official Email");
        myRef.setValue(email);
        myRef = database.getReference(id+"/Password");
        myRef.setValue(password);
        myRef = database.getReference(id+"/Personal Email");
        myRef.setValue(personal_email_edt.getText().toString());
        myRef = database.getReference(id+"/Official Number");
        myRef.setValue(official_num_edt.getText().toString());
        myRef = database.getReference(id+"/Personal Number");
        myRef.setValue(personal_num_edt.getText().toString());
        myRef = database.getReference(id+"/Designation");
        myRef.setValue(designation_edt.getText().toString());
        myRef = database.getReference(id+"/Gender");
        myRef.setValue(gender);
        if (bitmap!=null) {
            myRef = database.getReference(id + "/Photo");
            myRef.setValue(getBase64String(bitmap));
        }
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is:" + value);
                singUp();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG,"Failed to rad value ", databaseError.toException());
            }
        });
    }
    private void singUp(){
        mAuth.createUserWithEmailAndPassword(userEmail,userPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Log.e(TAG,"Successful");
                    Toast.makeText(MainActivity.getInstance(),"Member added successfully", Toast.LENGTH_LONG).show();
                    finish();
                }else{
                    Toast.makeText(MainActivity.getInstance(),"Adding Failed!", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });
    }
/*
    private void upload_image() {

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://sc-app-1ce6f.appspot.com/");
        StorageReference mountainImagesRef = storageRef.child("images/" + filePath.getLastPathSegment() + ".jpg");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = mountainImagesRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getUploadSessionUri();
                Log.d("downloadUrl-->", "" + downloadUrl);
            }
        });

    }

    private void ImageUpload( final Bitmap bitmap) {

       Bitmap bitmapImage = Bitmap.createScaledBitmap(bitmap, 300, 300, true);
        ByteArrayOutputStream baosImage = new ByteArrayOutputStream();
        bitmapImage.compress(Bitmap.CompressFormat.JPEG, 75, baosImage);
        byte[] byteImage = baosImage.toByteArray();

        final StorageReference storageImage = storageReference.child("images/test.jpg");

        storageImage.putBytes(byteImage)
                .addOnCanceledListener(new OnCanceledListener() {
                    @Override
                    public void onCanceled() {
                        Log.e("error", "calc");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("error", e.toString());
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> taskImage) {
                        if (taskImage.isSuccessful()) {

                            storageImage.getDownloadUrl()
                                    .addOnCompleteListener(new OnCompleteListener<Uri>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Uri> task) {
                                            Uri uriImage = task.getResult();
                                            String stringImage = uriImage.toString();
                                                Log.e("error", stringImage);
                                        }
                                    });
                        }
                        else{
                            Log.e("error", "incomplte");
                        }
                    }
                });

    }*/


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE) {
            //TODO: action

            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                profile_pic.setImageBitmap(bitmap);
                upload_file = new File(data.getData().getPath());
                filePath = data.getData();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * encode
     * @param bitmap
     * @return String
     */
    private String getBase64String(Bitmap bitmap) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 40, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();

        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }
    /* decode
    private void decodeBase64AndSetImage(String completeImageData, ImageView imageView) {

        // Incase you're storing into aws or other places where we have extension stored in the starting.
        String imageDataBytes = completeImageData.substring(completeImageData.indexOf(",")+1);

        InputStream stream = new ByteArrayInputStream(Base64.decode(imageDataBytes.getBytes(), Base64.DEFAULT));

        Bitmap bitmap = BitmapFactory.decodeStream(stream);

        imageView.setImageBitmap(bitmap);
    }*/
}

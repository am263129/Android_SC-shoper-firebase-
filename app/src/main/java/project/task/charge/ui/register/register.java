package shop.carate.shopper.ui.register;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
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

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import shop.carate.shopper.MainActivity;
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
    @Override
    protected void onStart() {

        super.onStart();
        mAuth.signOut();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {

            Log.e(TAG,"loged in?"+user.isAnonymous());
        } else if(userEmail!=null){
            signIn();
        }
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
        userEmail = email;
        userPass = password;
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
        singUp();
      //  uploadFile();
//        upload_image();



    }
    private void singUp(){
        mAuth.createUserWithEmailAndPassword(userEmail,userPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Log.e(TAG,"Successful");
                    signIn();
                }else{
                    Toast.makeText(MainActivity.getInstance()
                            ,"error on creating user",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void signIn() {
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(userEmail, userPass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            ImageUpload(bitmap);

//                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
//                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private void upload_image() {
//        FirebaseStorage storage = FirebaseStorage.getInstance();
//        StorageReference storageRef = storage.getReference();
////        StorageReference riversRef = storageRef.child("images/"+filePath.getLastPathSegment());
////        UploadTask uploadTask = riversRef.putFile(filePath);
////        Uri file = Uri.fromFile(upload_file);
//        StorageReference riversRef = storageRef.child("test.jpg");
//        String URL = String.valueOf(riversRef.getDownloadUrl());
//        riversRef.putFile(filePath)
//                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                        // Get a URL to the uploaded content
////                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception exception) {
//                        // Handle unsuccessful uploads
//                        // ...
//                    }
//                });

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
//                                            Map<String, Object> mapImage = new HashMap<>();
//                                            mapImage.put("user_image", stringImage);
//
//                                            firebaseFirestore.collection("users")
//                                                    .document(currentUser)
//                                                    .update(mapImage)
//                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                        @Override
//                                                        public void onComplete(@NonNull Task<Void> task) {
//                                                            if (task.isSuccessful()) {
//                                                               // CoverUpload(currentUser, bitmap, currentCover, carouselNumber);
//                                                            } else {
//                                                                Toast.makeText(register.this,
//                                                                        "Something went wrong. Please try again!",
//                                                                        Toast.LENGTH_SHORT).show();
//                                                            }
//                                                        }
//                                                    });
                                        }
                                    });
                        }
                        else{
                            Log.e("error", "incomplte");
                        }
                    }
                });

    }

    private void uploadFile() {
        //if there is a file to upload
        if (filePath != null) {
            //displaying a progress dialog while upload is going on
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            StorageReference riversRef = storageRef.child("images/image1.jpg");
            riversRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //if the upload is successfull
                            //hiding the progress dialog
                            progressDialog.dismiss();

                            //and displaying a success toast
                            Toast.makeText(getApplicationContext(), "File Uploaded ", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            //if the upload is not successfull
                            //hiding the progress dialog
                            progressDialog.dismiss();

                            //and displaying error message
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //calculating progress percentage
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                            //displaying percentage in progress dialog
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                        }
                    });
        }
        //if there is not any file
        else {
            //you can display an error toast
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == PICK_IMAGE) {
            //TODO: action

            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
            profile_pic.setImageBitmap(bitmap);
            upload_file = new File(data.getData().getPath());
            filePath = data.getData();
        }
    }
}

package project.task.charge;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import project.task.charge.R;
import project.task.charge.project.project;
import project.task.charge.project.projectAdapter;
import project.task.charge.project.project_view;
import project.task.charge.task.task;
import project.task.charge.task.taskAdapter;
import project.task.charge.util.Global;

public class project_list extends AppCompatActivity {

    String TAG ="Project_list";
    ListView project_list_view = null;
    ArrayList<project> array_all_task = new ArrayList<project>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);
        project_list_view = (ListView)findViewById(R.id.project_list);

        array_all_task = Global.array_project;
        init_list();

        project_list_view.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
                if(Global.is_CEO) {
                    Global.idid = true;
                    AlertDialog alertDialog = new AlertDialog.Builder(project_list.this).create();
                    alertDialog.setTitle("Wanning!");
                    alertDialog.setMessage("Do you want delete Project?");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    deleteProject(i);
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int i) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
                return true;
            }
        });
        project_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(project_list.this, project_view.class);
                intent.putExtra(Global.INDEX, i);
                startActivity(intent);
            }
        });
    }
    public void deleteProject(Integer index){
        FirebaseApp.initializeApp(this);
        String id = "Project/" + Global.array_project.get(index).getProject_name();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(id + "/Visible");
        myRef.setValue("InVisible");
        array_all_task.remove(array_all_task.get(index));
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Toast.makeText(project_list.this, "Project Deleted.", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "Failed to rad value ", databaseError.toException());
            }
        });

    }
    private void init_list(){

        projectAdapter adapter = new projectAdapter(this,R.layout.item_user_list,array_all_task);
        project_list_view.setAdapter(adapter);
    }
}

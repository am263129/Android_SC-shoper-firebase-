package project.task.charge.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import project.task.charge.R;
import project.task.charge.make_new_task;
import project.task.charge.util.Global;

public class newtask_first extends Fragment {

    private EditText task_title;
    private EditText task_description;
    private Spinner project;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.newtask_first, container, false);
        task_title = (EditText)view.findViewById(R.id.task_title);
        task_description = (EditText)view.findViewById(R.id.task_description);
        project = (Spinner)view.findViewById(R.id.project_name);
        task_title.setText(Global.task_title);
        task_description.setText(Global.task_description);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(make_new_task.getInstance(),
                android.R.layout.simple_spinner_item, Global.list_project);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        project.setAdapter(dataAdapter);
        project.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(make_new_task.getInstance(),Global.list_project.get(i),Toast.LENGTH_LONG).show();
                Global.project_name = Global.list_project.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        task_title.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Global.task_title = String.valueOf(task_title.getText());
            }
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }
        });

        task_description.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Global.task_description = String.valueOf(task_description.getText());
            }
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }
        });


        return view;
    }
}


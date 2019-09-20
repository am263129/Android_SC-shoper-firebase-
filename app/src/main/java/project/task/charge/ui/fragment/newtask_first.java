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
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import project.task.charge.R;
import project.task.charge.make_new_task;
import project.task.charge.util.Global;

public class newtask_first extends Fragment {

    private TextView task_id;
    private EditText task_description;
    private Spinner project;
    private Calendar calendar;
    private int year, month, day;
    int random;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.newtask_first, container, false);
        task_id = (TextView)view.findViewById(R.id.task_id);
        task_description = (EditText)view.findViewById(R.id.task_description);
        project = (Spinner)view.findViewById(R.id.project_name);
        task_description.setText(Global.task_description);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(make_new_task.getInstance(),
                android.R.layout.simple_spinner_item, Global.list_project);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        project.setAdapter(dataAdapter);
        project.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Global.project_name = Global.list_project.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        final int min = 1000;
        final int max = 9999;
        random = new Random().nextInt((max - min) + 1) + min;
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
        String str = sdf.format(currentTime);
        String sub_id = String.valueOf(currentTime.getHours())+String.valueOf(currentTime.getMinutes());
        Global.task_id =  String.valueOf(year)  + String.valueOf(month + 1)  +String.valueOf(day)  + str;
        task_id.setText(Global.task_id);

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


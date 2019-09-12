package shop.carate.shopper.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.NumberFormat;

import shop.carate.shopper.R;
import shop.carate.shopper.make_task;
import shop.carate.shopper.util.Global;

public class newtask_first extends Fragment {

    private EditText task_title;
    private EditText task_description;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.newtask_first, container, false);
        task_title = (EditText)view.findViewById(R.id.task_title);
        task_description = (EditText)view.findViewById(R.id.task_description);
        task_title.setText(Global.task_title);
        task_description.setText(Global.task_description);
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


package shop.carate.shopper.ui.fragment;

import android.app.DialogFragment;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import shop.carate.shopper.R;

public class newtask_second extends Fragment implements View.OnClickListener{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
// Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.newtask_second, container, false);
        Button btn_start_date = (Button)view.findViewById(R.id.btn_start_date);
        Button btn_end_date = (Button)view.findViewById(R.id.btn_end_date);
        TextView start_date = (TextView)view.findViewById(R.id.start_date);
        TextView end_date = (TextView)view.findViewById(R.id.end_date);
        btn_end_date.setOnClickListener(this);
        btn_start_date.setOnClickListener(this);


        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_start_date:
//                DialogFragment datePicker = new DateClass();
//                datePicker.show(getSupportFragmentManager(), "Date Picker");
        }

    }
}


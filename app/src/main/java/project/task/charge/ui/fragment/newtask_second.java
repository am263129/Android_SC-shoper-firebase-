package project.task.charge.ui.fragment;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import project.task.charge.R;
import project.task.charge.make_new_task;
import project.task.charge.util.Global;

public class newtask_second extends Fragment implements View.OnClickListener{


    TextView start_date;
    TextView end_date;

    private DatePicker datePicker;
    private Calendar calendar;
    private TextView dateView;
    private int year, month, day, start_year, start_month, start_day, end_year, end_month, end_day;
    private DatePickerDialog picker;
    private TextView toatldays;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
// Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.newtask_second, container, false);
        start_date = (TextView)view.findViewById(R.id.start_date);
        end_date = (TextView)view.findViewById(R.id.end_date);
        toatldays = (TextView)view.findViewById(R.id.total_date);
        toatldays.setText(Global.task_deadline);
        start_date.setOnClickListener(this);
        end_date.setOnClickListener(this);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.start_date:
//                start_date.setText("Selected Date: "+ picker.getDayOfMonth()+"/"+ (picker.getMonth() + 1)+"/"+picker.getYear());
                if(String.valueOf(start_date.getText()).equals(""))
                {
                    start_year = year;
                    start_month = month;
                    start_day = day;
                }
                picker = new DatePickerDialog(make_new_task.getInstance(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                start_date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                start_year = year;
                                start_month = monthOfYear;
                                start_day = dayOfMonth;
                                getCountOfDays();
                            }
                        }, year, month , day);

                picker.show();

                break;
            case R.id.end_date:
                if(String.valueOf(end_date.getText()).equals(""))
                {
                    end_year = year;
                    end_month = month;
                    end_day = day;
                }
                picker = new DatePickerDialog(make_new_task.getInstance(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                end_date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                end_year = year;
                                end_month = monthOfYear;
                                end_day = dayOfMonth;
                                getCountOfDays();
                            }
                        }, year, month , day);
                picker.show();
                break;

        }

    }



    public void getCountOfDays() {
        Log.e("HERER!",String.valueOf(start_date.getText())+String.valueOf(end_date.getText()));
        if((!(String.valueOf(start_date.getText()).equals("")))&&(!(String.valueOf(end_date.getText()).equals(""))) ){
            Global.task_start_date = String.valueOf(start_date.getText());
            Global.task_end_date = String.valueOf(end_date.getText());
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

            Date createdConvertedDate = null, expireCovertedDate = null, todayWithZeroTime = null;
            try {
                createdConvertedDate = dateFormat.parse(String.valueOf(start_date.getText()));
                expireCovertedDate = dateFormat.parse(String.valueOf(end_date.getText()));

                Date today = new Date();

                todayWithZeroTime = dateFormat.parse(dateFormat.format(today));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            int cYear = 0, cMonth = 0, cDay = 0;

            if (createdConvertedDate.after(todayWithZeroTime)) {
                Calendar cCal = Calendar.getInstance();
                cCal.setTime(createdConvertedDate);
                cYear = cCal.get(Calendar.YEAR);
                cMonth = cCal.get(Calendar.MONTH);
                cDay = cCal.get(Calendar.DAY_OF_MONTH);

            } else {
                Calendar cCal = Calendar.getInstance();
                cCal.setTime(todayWithZeroTime);
                cYear = cCal.get(Calendar.YEAR);
                cMonth = cCal.get(Calendar.MONTH);
                cDay = cCal.get(Calendar.DAY_OF_MONTH);
            }


    /*Calendar todayCal = Calendar.getInstance();
    int todayYear = todayCal.get(Calendar.YEAR);
    int today = todayCal.get(Calendar.MONTH);
    int todayDay = todayCal.get(Calendar.DAY_OF_MONTH);
    */

            Calendar eCal = Calendar.getInstance();
            eCal.setTime(expireCovertedDate);

            int eYear = eCal.get(Calendar.YEAR);
            int eMonth = eCal.get(Calendar.MONTH);
            int eDay = eCal.get(Calendar.DAY_OF_MONTH);

            Calendar date1 = Calendar.getInstance();
            Calendar date2 = Calendar.getInstance();

            date1.clear();
            date1.set(cYear, cMonth, cDay);
            date2.clear();
            date2.set(eYear, eMonth, eDay);

            long diff = date2.getTimeInMillis() - date1.getTimeInMillis();

            float dayCount = (float) diff / (24 * 60 * 60 * 1000);
            if(dayCount > 0) {
                toatldays.setText("" + (int) dayCount);
                Global.task_deadline = String.valueOf((int)dayCount);

            }
            else {
                Global.validate_newtask = false;
                toatldays.setText("" + 0);
                toatldays.setError("Please input valid date");
            }
        }
    }

}


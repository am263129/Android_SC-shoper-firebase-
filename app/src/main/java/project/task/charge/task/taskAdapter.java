package project.task.charge.task;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import project.task.charge.MainActivity;
import project.task.charge.R;
import project.task.charge.member.hired_member;

public class taskAdapter extends ArrayAdapter<task> implements Filterable {


    ArrayList<task> array_task = new ArrayList<>();
    task task;
    public taskAdapter(Context context, int textViewResourceId, ArrayList<task> objects) {
        super(context, textViewResourceId, objects);
        array_task = objects;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.item_tasks_list, null);

        TextView task_id = (TextView)v.findViewById(R.id.task_id);
        TextView task_start_date = (TextView)v.findViewById(R.id.task_start_date);
        TextView task_end_date = (TextView)v.findViewById(R.id.task_end_date);
        TextView task_duration = (TextView)v.findViewById(R.id.task_duration);
        TextView task_hired_person = (TextView)v.findViewById(R.id.task_hired_person);
        LinearLayout task_area = (LinearLayout)v.findViewById(R.id.item_task);
        String id = array_task.get(position).getTask_id();
        String start_date = array_task.get(position).getTask_start_date();
        String end_date = array_task.get(position).getTask_end_date();
        String duration = array_task.get(position).getTask_duration();

        if (array_task.get(position).getTask_status().equals("Late")) {
            task_area.setBackgroundResource(R.drawable.red_border);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                task_id.setTextColor(MainActivity.getInstance().getColor(R.color.flatui_pomegranate));
                task_start_date.setTextColor(MainActivity.getInstance().getColor(R.color.flatui_pomegranate));
                task_end_date.setTextColor(MainActivity.getInstance().getColor(R.color.flatui_pomegranate));
                task_duration.setTextColor(MainActivity.getInstance().getColor(R.color.flatui_pomegranate));
                task_hired_person.setTextColor(MainActivity.getInstance().getColor(R.color.flatui_pomegranate));
            }
        }
        if (array_task.get(position).getTask_status().equals("On Going")) {
            task_area.setBackgroundResource(R.drawable.yellow_border);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                task_id.setTextColor(MainActivity.getInstance().getColor(R.color.flatui_sun_flower));
                task_start_date.setTextColor(MainActivity.getInstance().getColor(R.color.flatui_sun_flower));
                task_end_date.setTextColor(MainActivity.getInstance().getColor(R.color.flatui_sun_flower));
                task_duration.setTextColor(MainActivity.getInstance().getColor(R.color.flatui_sun_flower));
                task_hired_person.setTextColor(MainActivity.getInstance().getColor(R.color.flatui_sun_flower));
            }
        }
        if (array_task.get(position).getTask_status().equals("completed")) {
            task_area.setBackgroundResource(R.drawable.green_border);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                task_id.setTextColor(MainActivity.getInstance().getColor(R.color.emerald));
                task_start_date.setTextColor(MainActivity.getInstance().getColor(R.color.emerald));
                task_end_date.setTextColor(MainActivity.getInstance().getColor(R.color.emerald));
                task_duration.setTextColor(MainActivity.getInstance().getColor(R.color.emerald));
                task_hired_person.setTextColor(MainActivity.getInstance().getColor(R.color.emerald));
            }

        }
        String upper_project = array_task.get(position).getTask_involvoing_project();
        String description = array_task.get(position).getTask_description();
        String created_date = array_task.get(position).getTask_created_date();
        String creator = array_task.get(position).getTask_creator();
        ArrayList<hired_member> hired = array_task.get(position).getHired_members();
        String hired_member = "";
        for (int i = 0; i < hired.size(); i ++){
            hired_member = hired_member + hired.get(i).getName() + ", ";
        }

        task_id.setText(id);
        task_start_date.setText(start_date);
        task_end_date.setText(end_date);
        task_duration.setText("("+duration+"days)");
        task_hired_person.setText(hired_member);
        task_hired_person.setSelected(true);

//        final Dialog dialog = new Dialog(super.getContext());
//        dialog.setContentView(R.layout.item_task_full);
//        dialog.setTitle("Task Detail");
//
//        // set the custom dialog components - text, image and button
//        TextView dlg_id = (TextView) dialog.findViewById(R.id.dlg_task_id);
//        TextView dlg_description = (TextView) dialog.findViewById(R.id.dlg_task_description);
//        TextView dlg_creator_name = (TextView) dialog.findViewById(R.id.dlg_creator_name);
//        TextView dlg_created_date = (TextView) dialog.findViewById(R.id.dlg_created_date);
//        TextView dlg_duration = (TextView) dialog.findViewById(R.id.dlg_deadline);
//        TextView dlg_start_date = (TextView) dialog.findViewById(R.id.dlg_start_date);
//        TextView dlg_end_date = (TextView) dialog.findViewById(R.id.dlg_end_date);
//        TextView dlg_hired_member = (TextView) dialog.findViewById(R.id.dlg_hired_members);
//        TextView dlg_project_name = (TextView) dialog.findViewById(R.id.dlg_involving_project_name);
//        dlg_id.setText(id);
//        dlg_start_date.setText(start_date);
//        dlg_end_date.setText(end_date);
//        dlg_description.setText(description);
//        dlg_created_date.setText(created_date);
//        dlg_duration.setText(duration);
//        dlg_hired_member.setText(hired_member);
//        dlg_project_name.setText(upper_project);
//        dlg_creator_name.setText(creator);
//        Button dialogButton = (Button) dialog.findViewById(R.id.dlg_btn_ok);
//        dialogButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
//
//
//        v.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog.show();
//            }
//        });
        return v;

    }
}
package project.task.charge.project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import project.task.charge.R;
import project.task.charge.task.task;

public class projectAdapter extends ArrayAdapter<project> {
    ArrayList<project> array_project = new ArrayList<>();
    task task;
    public projectAdapter(Context context, int textViewResourceId, ArrayList<project> objects) {
        super(context, textViewResourceId, objects);
        array_project = objects;
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

        TextView project_name = (TextView)v.findViewById(R.id.item_project_name);

        String name = array_project.get(position).getProject_name();

        project_name.setText(name);

        return v;

    }
}

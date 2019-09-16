package project.task.charge.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import project.task.charge.R;
import project.task.charge.make_new_task;
import project.task.charge.member.memberAdapter_grid;
import project.task.charge.util.Global;

public class newtask_preview extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
// Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.newtask_preview, container, false);

        TextView employser_name = view.findViewById(R.id.employer_name);
        TextView title = (TextView)view.findViewById(R.id.task_title);
        TextView description = (TextView)view.findViewById(R.id.task_description);
        TextView deadline = (TextView)view.findViewById(R.id.deadline);
        TextView project_name = (TextView)view.findViewById(R.id.involving_project_name);
        GridView hired_members = (GridView)view.findViewById(R.id.hired_members);
        memberAdapter_grid adapter_grid = new memberAdapter_grid(make_new_task.getInstance(),R.layout.item_user_grid,Global.array_hired_members);
        hired_members.setAdapter(adapter_grid);

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(hired_members.getWidth(), View.MeasureSpec.AT_MOST);
        int item_height = 0;
        for (int i = 0; i < adapter_grid.getCount(); i++) {
            View listItem = adapter_grid.getView(i, null, hired_members);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
            item_height = listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = hired_members.getLayoutParams();
        params = hired_members.getLayoutParams();
        params.height = (int)totalHeight/3 + item_height;
        hired_members.setLayoutParams(params);

        employser_name.setText(Global.current_user_name);
        title.setText(Global.task_title);
        description.setText(Global.task_description);
        deadline.setText(Global.task_deadline);
        project_name.setText(Global.project_name);
        return view;
    }
}


package shop.carate.shopper.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import shop.carate.shopper.R;
import shop.carate.shopper.make_new_task;
import shop.carate.shopper.member.memberAdapter_grid;
import shop.carate.shopper.util.Global;

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
        GridView gridView = (GridView)view.findViewById(R.id.hired_members);
        memberAdapter_grid adapter_grid = new memberAdapter_grid(make_new_task.getInstance(),R.layout.item_user_grid,Global.array_hired_members);
        gridView.setAdapter(adapter_grid);

        employser_name.setText(Global.current_user_name);
        title.setText(Global.task_title);
        description.setText(Global.task_description);
        deadline.setText(Global.task_deadline);
        return view;
    }
}


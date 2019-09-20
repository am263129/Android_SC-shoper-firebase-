package project.task.charge.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import project.task.charge.R;
import project.task.charge.make_new_task;
import project.task.charge.member.Member;
import project.task.charge.member.memberAdapter_grid;
import project.task.charge.member.memberAdapter_list;
import project.task.charge.util.Global;

public class newtask_third extends Fragment {
    memberAdapter_list adapter_list;
    memberAdapter_grid adapter_grid;
    ListView all_members;
    GridView hired_members;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
// Inflate the layout for this fragment
        Global.is_hiring_member = true;

        View view = inflater.inflate(R.layout.newtask_third, container, false);
        all_members = (ListView)view.findViewById(R.id.all_members);
        hired_members = (GridView)view.findViewById(R.id.hired_members);
        final TextView nobody = (TextView)view.findViewById(R.id.label_nobody);
        Button hire_members = (Button)view.findViewById(R.id.hire_users);
        ArrayList<Member> array_all_members = new ArrayList<Member>();
        array_all_members = Global.array_all_members;
        for(int i=0;i<array_all_members.size();i++){
            Global.hired_status.add(false);
        }
        adapter_list = new memberAdapter_list(make_new_task.getInstance(),R.layout.item_user_list,array_all_members);
        all_members.setAdapter(adapter_list);
        adapter_grid = new memberAdapter_grid(make_new_task.getInstance(),R.layout.item_user_grid,Global.array_hired_members);
        hired_members.setAdapter(adapter_grid);

        hire_members.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Global.array_hired_members.clear();
                for(int i = 0; i < Global.array_all_members.size(); i++ ){
                    if(Global.hired_status.get(i))
                        Global.array_hired_members.add(Global.array_all_members.get(i));
                }
                adapter_grid = new memberAdapter_grid(make_new_task.getInstance(),R.layout.item_user_grid,Global.array_hired_members);
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

                if(Global.array_hired_members.size()>0)
                    nobody.setVisibility(View.GONE);
                else
                    nobody.setVisibility(View.VISIBLE);

            }
        });
        hired_members.setOnItemClickListener(new GridView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                all_members.setAdapter(adapter_list);
                hired_members.setAdapter(adapter_grid);
            }
        });

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(all_members.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < adapter_list.getCount(); i++) {
            View listItem = adapter_list.getView(i, null, all_members);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = all_members.getLayoutParams();
        params.height = totalHeight + (all_members.getDividerHeight() * (Global.array_all_members.size() - 1));
        all_members.setLayoutParams(params);




        return view;

    }
}


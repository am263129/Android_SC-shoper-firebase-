package shop.carate.shopper.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import shop.carate.shopper.R;
import shop.carate.shopper.make_new_task;
import shop.carate.shopper.member.Member;
import shop.carate.shopper.member.memberAdapter_grid;
import shop.carate.shopper.member.memberAdapter_list;
import shop.carate.shopper.util.Global;

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
        TextView nobody = (TextView)view.findViewById(R.id.label_nobody);
        Button hire_members = (Button)view.findViewById(R.id.hire_users);

        if(!Global.array_hired_members.isEmpty())
            nobody.setVisibility(View.GONE);
        else
            nobody.setVisibility(View.VISIBLE);
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
            }
        });
        hired_members.setOnItemClickListener(new GridView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                all_members.setAdapter(adapter_list);
                hired_members.setAdapter(adapter_grid);
            }
        });
        return view;

    }
}


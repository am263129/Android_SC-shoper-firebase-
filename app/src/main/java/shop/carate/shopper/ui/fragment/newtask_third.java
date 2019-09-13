package shop.carate.shopper.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import shop.carate.shopper.R;
import shop.carate.shopper.make_new_task;
import shop.carate.shopper.member.Member;
import shop.carate.shopper.member.memberAdapter;
import shop.carate.shopper.util.Global;

public class newtask_third extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
// Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.newtask_third, container, false);
        ListView all_members = (ListView)view.findViewById(R.id.all_members);
        ArrayList<Member> array_all_members = new ArrayList<Member>();
        array_all_members = Global.array_all_members;
        memberAdapter adapter = new memberAdapter(make_new_task.getInstance(),R.layout.item_user_list,array_all_members);
        all_members.setAdapter(adapter);
        return view;

    }
}


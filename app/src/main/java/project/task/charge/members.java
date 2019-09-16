package shop.carate.shopper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

import shop.carate.shopper.member.Member;
import shop.carate.shopper.member.memberAdapter_list;
import shop.carate.shopper.util.Global;

public class members extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_members);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setSubtitle("ALL MEMBERS");
        toolbar.setSubtitleTextColor(Color.GRAY);
        Global.is_hiring_member = false;
        ListView all_members = (ListView)findViewById(R.id.list_all_members);
        ArrayList<Member> array_all_members = new ArrayList<Member>();
        array_all_members = Global.array_all_members;
        memberAdapter_list adapter = new memberAdapter_list(this,R.layout.item_user_list,array_all_members);
        all_members.setAdapter(adapter);
    }
}

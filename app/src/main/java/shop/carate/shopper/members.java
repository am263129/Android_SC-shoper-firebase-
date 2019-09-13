package shop.carate.shopper;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Message;
import android.widget.ListView;

import java.util.ArrayList;

import shop.carate.shopper.member.Member;
import shop.carate.shopper.member.memberAdapter;
import shop.carate.shopper.util.Global;

public class members extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_members);
        ListView all_members = (ListView)findViewById(R.id.list_all_members);
        ArrayList<Member> array_all_members = new ArrayList<Member>();
        array_all_members = Global.array_all_members;
        memberAdapter adapter = new memberAdapter(this,R.layout.item_user_list,array_all_members);
        all_members.setAdapter(adapter);
    }
}

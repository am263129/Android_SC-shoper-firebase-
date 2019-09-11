package shop.carate.shopper;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import shop.carate.shopper.ui.fragment.newtask_first;
import shop.carate.shopper.ui.fragment.newtask_second;
import shop.carate.shopper.ui.fragment.newtask_third;
import shop.carate.shopper.util.Global;

public class make_task extends AppCompatActivity implements View.OnClickListener{

    private FrameLayout task_area;
    private Button btn_pre, btn_next;
    private Fragment newtask_first;
    private Fragment newtask_second;
    private Fragment newtasl_third;
    private TextView header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_task);

        header = (TextView)findViewById(R.id.header);
        btn_pre = (Button)findViewById(R.id.btn_pre);
        btn_next = (Button)findViewById(R.id.btn_next);
        btn_pre.setOnClickListener(this);
        btn_next.setOnClickListener(this);
        newtask_first = new newtask_first();
        newtask_second = new newtask_second();
        newtasl_third = new newtask_third();
        changeTaskArea(0);
    }

    @Override
    public void onClick(View view) {
        Toast.makeText(make_task.this,String.valueOf(Global.mk_task_progress),Toast.LENGTH_LONG).show();
        switch (view.getId()){
            case R.id.btn_next:
                Global.mk_task_progress = Global.mk_task_progress+1;
                if ((Global.mk_task_progress > 2)) {
                    Global.mk_task_progress = 2;
                }
                changeTaskArea(Global.mk_task_progress);
                break;
            case R.id.btn_pre:
                Global.mk_task_progress = Global.mk_task_progress-1;
                if ((Global.mk_task_progress < 0)) {
                    Global.mk_task_progress = 0;
                }
                changeTaskArea(Global.mk_task_progress);
                break;
            default:
                break;
        }
    }

    private void changeTaskArea(Integer index) {
// create a FragmentManager
        Fragment fragment;
        switch (index){
            case 0:
                fragment = newtask_first;
                header.setText("Project Name & Description");
                break;
            case 1:
                fragment = newtask_second;
                header.setText("Deadline");
                break;
            case 2:
                fragment = newtasl_third;
                header.setText("Hire Members");
                break;
            default:
                fragment = newtask_first;
                break;
        }
        FragmentManager fm = getFragmentManager();
// create a FragmentTransaction to begin the transaction and replace the Fragment
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
// replace the FrameLayout with new Fragment
        fragmentTransaction.replace(R.id.frame_make_task, fragment);
        fragmentTransaction.commit(); // save the changes
    }



}

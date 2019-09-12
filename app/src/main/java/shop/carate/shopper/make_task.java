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
import shop.carate.shopper.ui.fragment.newtask_preview;
import shop.carate.shopper.ui.fragment.newtask_second;
import shop.carate.shopper.ui.fragment.newtask_third;
import shop.carate.shopper.util.Global;

public class make_task extends AppCompatActivity implements View.OnClickListener{

    private FrameLayout task_area;
    private Button btn_pre, btn_next, btn_preview, btn_finish;
    private Fragment newtask_first;
    private Fragment newtask_second;
    private Fragment newtask_third;
    private Fragment newtask_preview;
    private TextView header;
    public static make_task make_task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_task);

        header = (TextView)findViewById(R.id.header);
        btn_pre = (Button)findViewById(R.id.btn_pre);
        btn_next = (Button)findViewById(R.id.btn_next);
        btn_preview = (Button)findViewById(R.id.btn_preview);
        btn_finish = (Button)findViewById(R.id.btn_finish);
        btn_pre.setOnClickListener(this);
        btn_next.setOnClickListener(this);
        btn_preview.setOnClickListener(this);
        newtask_first = new newtask_first();
        newtask_second = new newtask_second();
        newtask_third = new newtask_third();
        newtask_preview = new newtask_preview();
        make_task = this;
        changeTaskArea(0);
    }
    protected void onResume() {
        btn_next.setEnabled(Global.validate_newtask);
        btn_pre.setEnabled(Global.validate_newtask);
        super.onResume();
    }

    @Override
    public void onClick(View view) {
        Toast.makeText(make_task.this,String.valueOf(Global.mk_task_progress),Toast.LENGTH_LONG).show();
        switch (view.getId()){
            case R.id.btn_next:
                Global.mk_task_progress = Global.mk_task_progress+1;

                if ((Global.mk_task_progress > 1)) {
                    if(validateTask()) {
                        btn_next.setVisibility(view.GONE);
                        btn_preview.setVisibility(view.VISIBLE);
                    }
                }
                if(Global.mk_task_progress > 3){
                    Global.mk_task_progress = 3;
                }
                changeTaskArea(Global.mk_task_progress);
                break;
            case R.id.btn_pre:
                Global.mk_task_progress = Global.mk_task_progress-1;
                if(btn_preview.getVisibility() == view.VISIBLE)
                {
                    btn_preview.setVisibility(View.GONE);
                    btn_finish.setVisibility(View.GONE);
                    btn_next.setVisibility(View.VISIBLE);
                }
                if(btn_finish.getVisibility() == View.VISIBLE)
                {
                    btn_finish.setVisibility(View.GONE);
                    btn_preview.setVisibility(View.VISIBLE);
                }
                if ((Global.mk_task_progress < 0)) {
                    Global.mk_task_progress = 0;
                }
                changeTaskArea(Global.mk_task_progress);
                break;
            case R.id.btn_preview:
                Global.mk_task_progress = Global.mk_task_progress + 1;
                btn_preview.setVisibility(view.GONE);
                btn_finish.setVisibility(View.VISIBLE);
                changeTaskArea(Global.mk_task_progress);

            default:
                break;
        }
    }

    private boolean validateTask() {
//        if((!Global.task_title.equals(""))&&(!Global.task_description.equals(""))&&(!Global.task_deadline.equals(""))&&(Global.task_hired_members.length>0))
//        {
//            //Global.task_id =
//            return true;
//        }
//        else
//            return false;
            return true;//only testing;
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
                fragment = newtask_third;
                header.setText("Hire Members");
                break;
            case 3:
                fragment = newtask_preview;
                header.setText("Preview");
                break;
            default:
                fragment = newtask_first;
                break;
        }
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.frame_make_task, fragment);
        fragmentTransaction.commit();
    }
    public static make_task getInstance(){
        return make_task;
    }



}

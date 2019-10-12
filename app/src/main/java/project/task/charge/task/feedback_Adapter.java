package project.task.charge.task;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import project.task.charge.R;
import project.task.charge.feed.feedback;

public class feedback_Adapter extends ArrayAdapter<feedback> implements Filterable {


    ArrayList<feedback> feedbacks = new ArrayList<feedback>();
    feedback feedback;
    public feedback_Adapter(Context context, int textViewResourceId, ArrayList<feedback> objects) {
        super(context, textViewResourceId, objects);
        feedbacks = objects;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.item_feedback_list, null);

        TextView feedback_author = (TextView)v.findViewById(R.id.feedback_author);
        TextView feedback_content = (TextView)v.findViewById(R.id.feedback_content);
        TextView feedback_date = (TextView)v.findViewById(R.id.feed_back_date);
        LinearLayout feedback_back = (LinearLayout)v.findViewById(R.id.feedback_back);
        String author = feedbacks.get(position).getName();
        String content = feedbacks.get(position).getFeedback_content();
        String date = feedbacks.get(position).getFeedback_date();
        if (feedbacks.get(position).getFeedback_from().equals("True"))
            feedback_back.setBackgroundResource(R.drawable.back_feedback_creator);
        feedback_author.setText(author);
        feedback_content.setText(content);
        feedback_date.setText(date);
        return v;

    }
}
package project.task.charge.task;

import java.util.ArrayList;

import project.task.charge.feed.feedback;
import project.task.charge.member.hired_member;

public class task {
    public String task_id;
    public String task_description;
    public String task_created_date;
    public String task_involvoing_project;
    public String task_duration;
    public String task_start_date;
    public String task_end_date;
    public String task_creator;
    public String task_status;
    public ArrayList<hired_member> hired_members;
    public ArrayList<feedback> feedbacks;
    public task(){

    }
    public task(String id, String description, String created_date, String project, String duration, String start_date, String end_date, String creator, ArrayList<hired_member> hired, ArrayList<feedback> feedbacks, String status){
        this.task_id = id;
        this.task_description = description;
        this.task_created_date = created_date;
        this.task_involvoing_project = project;
        this.task_duration = duration;
        this.task_start_date = start_date;
        this.task_end_date =  end_date;
        this.task_creator = creator;
        this.hired_members = hired;
        this.feedbacks = feedbacks;
        this.task_status = status;

    }

    public String getTask_id(){
        return this.task_id;
    }
    public String getTask_description(){
        return this.task_description;
    }
    public String getTask_involvoing_project(){
        return this.task_involvoing_project;
    }
    public String getTask_duration(){
        return this.task_duration;
    }
    public String getTask_start_date(){
        return this.task_start_date;
    }
    public String getTask_end_date(){
        return this.task_end_date;
    }
    public String getTask_created_date(){
        return this.task_created_date;
    }

    public String getTask_creator() {
        return task_creator;
    }

    public ArrayList<hired_member> getHired_members(){
        return this.hired_members;
    }

    public ArrayList<feedback> getFeedbacks() {
        return feedbacks;
    }

    public String getTask_status() {
        return task_status;
    }
}

package project.task.charge.util;

import java.util.ArrayList;

public class task {
    public String task_title;
    public String task_description;
    public String task_created_date;
    public String task_involvoing_project;
    public String task_duration;
    public String task_start_date;
    public String task_end_date;
    public ArrayList<String> hired_members;
    public task(){

    }
    public task(String title, String description, String created_date, String project, String duration, String start_date, String end_date, ArrayList<String> hired){
        this.task_title = title;
        this.task_description = description;
        this.task_created_date = created_date;
        this.task_involvoing_project = project;
        this.task_duration = duration;
        this.task_start_date = start_date;
        this.task_end_date =  end_date;
        this.hired_members = hired;
    }

    public String getTask_title(){
        return this.task_title;
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
    public ArrayList<String> getHired_members(){
        return this.hired_members;
    }
}

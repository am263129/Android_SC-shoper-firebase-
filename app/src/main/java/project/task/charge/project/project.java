package project.task.charge.project;

import java.util.ArrayList;

import project.task.charge.task.task;

public class project {
    public String project_name;
    public String project_description;
    public String project_client;
    public String project_location;
    public String project_duration;
    public String project_start_date;
    public String project_contractual_end_date;
    public String prpject_target_end_date;
    public String project_created_date;
    public ArrayList<task> project_created_task;

    public project(){

    }
    public project(String name, String description, String client, String location, String duration, String start_date, String contractual_date, String target_date, String created_date, ArrayList<task> created_task){
        this.project_name = name;
        this.project_description = description;
        this.project_client = client;
        this.project_location = location;
        this.project_duration = duration;
        this.project_start_date = start_date;
        this.project_contractual_end_date = contractual_date;
        this.prpject_target_end_date = target_date;
        this.project_created_date = created_date;
        this.project_created_task = created_task;
    }

    public String getProject_name() {
        return project_name;
    }

    public String getProject_description() {
        return project_description;
    }

    public String getProject_client() {
        return project_client;
    }

    public String getProject_location() {
        return project_location;
    }

    public String getProject_duration() {
        return project_duration;
    }

    public String getProject_start_date() {
        return project_start_date;
    }

    public String getProject_contractual_end_date() {
        return project_contractual_end_date;
    }

    public String getPrpject_target_end_date() {
        return prpject_target_end_date;
    }

    public String getProject_created_date() {
        return project_created_date;
    }

    public ArrayList<task> getProject_created_task() {
        return project_created_task;
    }
}


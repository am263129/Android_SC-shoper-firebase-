package project.task.charge.util;

import java.util.ArrayList;
import java.util.List;

import project.task.charge.member.Member;

public class Global {
    public static String current_user_name = "";
    public static String current_user_email = "";
    public static String current_user_photo;
    public static int current_user_index;

    public static Integer mk_task_progress = 0;
    public static boolean validate_newtask = true;
    public static boolean is_admin = false;

    public static String task_id;
    public static String task_description;
    public static String task_start_date;
    public static String task_end_date;
    public static String task_deadline;
    public static String project_name;
    public static String[] task_hired_members;
    public static String task_created_date;

    public static boolean is_hiring_member = false;
    public static ArrayList<Member> array_all_members = new ArrayList<Member>();
    public static ArrayList<Member> array_hired_members = new ArrayList<Member>();
    public static ArrayList<Boolean> hired_status = new ArrayList<Boolean>();
    public static List<String> list_project = new ArrayList<String>();

    public static ArrayList<task> array_all_task = new ArrayList<task>();
    public static ArrayList<task> array_created_task = new ArrayList<task>();
    public static ArrayList<task> array_my_task = new ArrayList<task>();

}

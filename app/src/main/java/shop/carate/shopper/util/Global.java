package shop.carate.shopper.util;

import java.util.ArrayList;

import shop.carate.shopper.member.Member;

public class Global {
    public static String current_user_name = "";
    public static String current_user_email = "";

    public static Integer mk_task_progress = 0;
    public static boolean validate_newtask = true;

    public static String task_id;
    public static String task_title;
    public static String task_description;
    public static String task_start_date;
    public static String task_end_date;
    public static String task_deadline;
    public static String[] task_hired_members;

    public static boolean is_select_member = false;
    public static ArrayList<Member> array_all_members = new ArrayList<Member>();
}

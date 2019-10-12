package project.task.charge.feed;

public class feedback {
    public String name;
    public String feedback_content;
    public String feedback_id;
    public String feedback_date;
    public String feedback_from;

    public feedback(){}

    public feedback(String id,  String name, String feedback_content,String feedback_date, String feedback_from){
        this.name = name;
        this.feedback_content = feedback_content;
        this.feedback_id = id;
        this.feedback_date = feedback_date;
        this.feedback_from = feedback_from;
    }

    public String getName() {
        return name;
    }
    public String getFeedback_content() {
        return feedback_content;
    }

    public String getFeedback_id() {
        return feedback_id;
    }

    public String getFeedback_date() {
        return feedback_date;
    }

    public String getFeedback_from() {
        return feedback_from;
    }
}

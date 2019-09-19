package project.task.charge.feed;

public class feedback {
    public String name;
    public String feedback_content;

    public feedback(){}

    public feedback(String name, String feedback_content){
        this.name = name;
        this.feedback_content = feedback_content;
    }

    public String getName() {
        return name;
    }
    public String getFeedback_content() {
        return feedback_content;
    }
}

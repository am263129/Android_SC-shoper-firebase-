package project.task.charge.feed;

public class feed {
    public String feed_title;
    public String feed_description;
    public String feed_crated_date;
    public feed(){

    }
    public feed (String title, String description, String created_date){
        this.feed_crated_date = created_date;
        this.feed_title = title;
        this.feed_description = description;
    }

    public String getFeed_crated_date() {
        return feed_crated_date;
    }

    public String getFeed_description() {
        return feed_description;
    }

    public String getFeed_title() {
        return feed_title;
    }
}
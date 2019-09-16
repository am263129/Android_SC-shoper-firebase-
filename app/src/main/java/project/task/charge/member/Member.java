package project.task.charge.member;

public class Member {
    public String name;
    public String email;
    public String gender;
    public boolean hired;
    public Member(){

    }
    public Member(String name, String email, String gender){
        this.name = name;
        this.email = email;
        this.gender = gender;
        this.hired = hired;
    }

    public String getName(){
        return this.name;
    }
    public String getEmail(){
        return this.email;
    }
    public String getGender(){
        return this.gender;
    }
    public boolean getStatus(){
        return this.hired;
    }
}

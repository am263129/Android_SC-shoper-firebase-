package shop.carate.shopper.member;

public class Member {
    public String name;
    public String email;
    public String gender;

    public Member(){

    }
    public Member(String name, String email, String gender){
        this.name = name;
        this.email = email;
        this.gender = gender;
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
}

package project.task.charge.member;

public class Member {
    public String name;
    public String email;
    public String gender;
    public boolean hired;
    public String photo;
    public String address;
    public String location;
    public String phone;
    public String birthday;
    public String password;
    public Member(){

    }
    public Member(String name, String email, String gender, String photo, String birthday, String address, String location, String phone, String password){
        this.name = name;
        this.email = email;
        this.gender = gender;
        this.photo = photo;
        this.address = address;
        this.location = location;
        this.phone = phone;
        this.birthday = birthday;
        this.password = password;
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
    public String getPhoto() {
        return photo;
    }
    public String getAddress() {
        return address;
    }
    public String getBirthday() {
        return birthday;
    }
    public String getLocation() {
        return location;
    }
    public String getPhone() {
        return phone;
    }
    public String getPassword() {
        return password;
    }
}

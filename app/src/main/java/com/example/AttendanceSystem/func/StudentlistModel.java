package com.example.AttendanceSystem.func;

public class StudentlistModel {

    private String ID;
    private String name;
    private String userid;
    private String email;
    private String programme;
    private String IC;
    private String phone;

    public StudentlistModel(){

    }

    public StudentlistModel(String ID, String name, String userid, String email, String programme, String IC, String phone) {
        this.ID = ID;
        this.name = name;
        this.userid = userid;
        this.email = email;
        this.programme = programme;
        this.IC = IC;
        this.phone = phone;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProgramme() {
        return programme;
    }

    public void setProgramme(String programme) {
        this.programme = programme;
    }

    public String getIC() {
        return IC;
    }

    public void setIC(String IC) {
        this.IC = IC;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}

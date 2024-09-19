package com.example.giaodien.Activities.Model;

public class Staff {
    private int staff_id;
    private String fullname;
    private String sex;
    private String position;
    private String email;
    private String phone;
    private String username;
    private String password;

    public Staff(int staff_id, String fullname, String sex, String position, String email, String phone, String username, String password) {
        this.staff_id = staff_id;
        this.fullname = fullname;
        this.sex = sex;
        this.position = position;
        this.email = email;
        this.phone = phone;
        this.username = username;
        this.password = password;
    }

    public Staff(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public int getStaff_id() {
        return staff_id;
    }

    public void setStaff_id(int staff_id) {
        this.staff_id = staff_id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

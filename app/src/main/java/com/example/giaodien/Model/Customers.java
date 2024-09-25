package com.example.giaodien.Model;

public class Customers {
    private int customer_id;
    private String fullname;
    private String sex;
    private String cccd;
    private String phone;

    public Customers(int customer_id, String fullname, String sex, String cccd, String phone) {
        this.customer_id = customer_id;
        this.fullname = fullname;
        this.sex = sex;
        this.cccd = cccd;
        this.phone = phone;
    }

    public Customers(String fullname, String sex, String cccd, String phone) {
        this.fullname = fullname;
        this.sex = sex;
        this.cccd = cccd;
        this.phone = phone;
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
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

    public String getCccd() {
        return cccd;
    }

    public void setCccd(String cccd) {
        this.cccd = cccd;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}

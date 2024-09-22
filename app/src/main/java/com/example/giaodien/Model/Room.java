package com.example.giaodien.Model;

public class Room {
    private int room_id;
    private String room_number;
    private String room_type;
    private String price;
    private String status;

    public Room(int room_id, String room_number, String room_type, String price, String status) {
        this.room_id = room_id;
        this.room_number = room_number;
        this.room_type = room_type;
        this.price = price;
        this.status = status;
    }

    public int getRoom_id() {
        return room_id;
    }

    public void setRoom_id(int room_id) {
        this.room_id = room_id;
    }

    public String getRoom_number() {
        return room_number;
    }

    public void setRoom_number(String room_number) {
        this.room_number = room_number;
    }

    public String getRoom_type() {
        return room_type;
    }

    public void setRoom_type(String room_type) {
        this.room_type = room_type;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

package com.example.imetu.imet.Model;

import org.parceler.Parcel;


@Parcel
public class Member {
    //  TODO:Data of member
    private int id;
    private String name;
    private String imgPath;
    private String phoneNumber;
    private String event;
    private boolean isEdited;

    public Member() {
    }

    //  TODO:Getter
    public int getId() {
        return id;
    }

    public String getImgPath() {
        return imgPath;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEvent() {
        return event;
    }

    public boolean isEdited() {
        return isEdited;
    }
    //  TODO:Setter

    public void setId(int id) {
        this.id = id;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public void setEdited(boolean edited) {
        isEdited = edited;
    }
}

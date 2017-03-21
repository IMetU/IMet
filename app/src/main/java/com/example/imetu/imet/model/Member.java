package com.example.imetu.imet.model;

import org.parceler.Parcel;


@Parcel
public class Member {
    //  Data of member
    private int id;
    private String name;
    private String imgPath;
    private String phone;
    private String email;
    private String relationship;
    private String event;
    private String location;
    private String YearMet;
    private String topicDiscussed;
    private int gender;
    private int height;
    private int bodyShape;
    private int hairLength;
    private boolean permed;
    private boolean dyed;
    private int glasses;
    private String other;

    public Member() {
    }

    //  Getter
    public int getId() {
        return id;
    }

    public String getImgPath() {
        return imgPath;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getEvent() {
        return event;
    }

    public String getEmail() {
        return email;
    }

    public String getRelationship() {
        return relationship;
    }

    public String getLocation() {
        return location;
    }

    public String getYearMet() {
        return YearMet;
    }

    public String getTopicDiscussed() {
        return topicDiscussed;
    }

    public int getGender() {
        return gender;
    }

    public int getHeight() {
        return height;
    }

    public int getBodyShape() {
        return bodyShape;
    }

    public int getHairLength() {
        return hairLength;
    }

    public boolean isPermed() {
        return permed;
    }

    public boolean isDyed() {
        return dyed;
    }

    public int getGlasses() {
        return glasses;
    }

    public String getOther() {
        return other;
    }

    //  Setter
    public void setId(int id) {
        this.id = id;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setYearMet(String yearMet) {
        YearMet = yearMet;
    }

    public void setTopicDiscussed(String topicDiscussed) {
        this.topicDiscussed = topicDiscussed;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setBodyShape(int bodyShape) {
        this.bodyShape = bodyShape;
    }

    public void setHairLength(int hairLength) {
        this.hairLength = hairLength;
    }

    public void setPermed(boolean permed) {
        this.permed = permed;
    }

    public void setDyed(boolean dyed) {
        this.dyed = dyed;
    }

    public void setGlasses(int glasses) {
        this.glasses = glasses;
    }

    public void setOther(String other) {
        this.other = other;
    }
}

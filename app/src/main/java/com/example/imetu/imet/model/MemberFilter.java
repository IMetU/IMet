package com.example.imetu.imet.model;

/**
 * Created by Quietus on 2017/3/14.
 */
import org.parceler.Parcel;

import static com.example.imetu.imet.widget.Util.BODY_UNDEFINED;
import static com.example.imetu.imet.widget.Util.GENDER_UNDEFINED;
import static com.example.imetu.imet.widget.Util.GLASSES_UNDEFINED;
import static com.example.imetu.imet.widget.Util.HAIR_UNDEFINED;
import static com.example.imetu.imet.widget.Util.HEIGHT_UNDEFINED;
import static com.example.imetu.imet.widget.Util.REQUEST_FILTER;


@Parcel
public class MemberFilter {
    private String name;
    private String relationship;
    private String event;
    private String yearMet;
    private String topicDiscussed;
    private int gender;
    private int height;
    private int bodyShape;
    private int hairLength;
    private boolean permed;
    private boolean dyed;
    private int glasses;
    private String other;
    private int filterOrAdvance;

    public MemberFilter() {
        this.name = "";
        this.relationship = "";
        this.event = "";
        this.yearMet = "";
        this.topicDiscussed = "";
        this.gender = GENDER_UNDEFINED;
        this.height = HEIGHT_UNDEFINED;
        this.bodyShape = BODY_UNDEFINED;
        this.hairLength = HAIR_UNDEFINED;
        this.permed = false;
        this.dyed = false;
        this.glasses = GLASSES_UNDEFINED;
        this.other = "";
        this.filterOrAdvance = REQUEST_FILTER;
    }

    public String getName() {
        return name;
    }

    public String getRelationship() {
        return relationship;
    }

    public String getEvent() {
        return event;
    }

    public String getYearMet() {
        return yearMet;
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

    public int getFilterOrAdvance() {
        return filterOrAdvance;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public void setYearMet(String yearMet) {
        this.yearMet = yearMet;
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

    public void setFilterOrAdvance(int filterOrAdvance) {
        this.filterOrAdvance = filterOrAdvance;
    }

}

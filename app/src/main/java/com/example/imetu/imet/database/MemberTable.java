package com.example.imetu.imet.database;


import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

@Table(database = MemberDB.class)
public class MemberTable extends BaseModel {
    @Column
    @PrimaryKey(autoincrement = true)
    int id;
    //  TODO:Table create
    @Column
    String imgPath;

    @Column
    String name;

    @Column
    String phone;

    @Column
    String email;

    @Column
    String relationship;

    @Column
    String event;

    @Column
    String location;

    @Column
    String yearMet;

    @Column
    String topicDiscussed;

    @Column
    int gender;

    @Column
    int height;

    @Column
    int bodyShape;

    @Column
    int hairLength;

    @Column
    boolean permed;

    @Column
    boolean dyed;

    @Column
    int glasses;

    @Column
    String other;
}

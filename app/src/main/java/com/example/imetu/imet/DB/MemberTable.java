package com.example.imetu.imet.DB;


import com.example.imetu.imet.Model.Member;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import org.parceler.Parcel;

@Table(database = MemberDB.class)
public class MemberTable extends BaseModel {
    @Column
    @PrimaryKey(autoincrement = true)
    int id;
    //  TODO:Table create
    @Column
    String name;

    @Column
    String phoneNumber;

    @Column
    String event;

    @Column
    String imgPath;

    @Column
    boolean isEdited;

}

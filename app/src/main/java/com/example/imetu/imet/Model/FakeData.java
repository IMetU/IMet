package com.example.imetu.imet.Model;

import java.util.ArrayList;

public class FakeData extends Member {

    public FakeData() {
    }

    public static ArrayList<Member> CreateFakeMemberList() {
        String name[] = {"Quietus", "Kelly", "Tom", "Jane", "Jenni", ""
                , "John", "Summit", "Tony", "Allen", "Bob", "", "Mery", "Robbert"
                , "Summer", "Irene", "Iron", "Adela", "Agatha", "Amy", "", "Camille"
                , "Cherry", "Claire", "Coral", "Elsie", "Fay", "Gill", "Hulda"
                , "Jennifer", "Joy", "Lynn", "Nina"};

        ArrayList<Member> memberArrayList = new ArrayList<>();

        for (int i = 0; i < name.length; i++) {
            Member member = new Member();
            member.setId(i);
            member.setName(name[i]);
            memberArrayList.add(member);
        }
        return memberArrayList;
    }
}

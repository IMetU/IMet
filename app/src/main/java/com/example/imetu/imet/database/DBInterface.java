package com.example.imetu.imet.database;

import com.example.imetu.imet.model.Member;
import com.example.imetu.imet.model.MemberFilter;

import java.util.ArrayList;

public interface DBInterface {
    public ArrayList<Member> selectAll();
    public Member selectOne(int id);
    public ArrayList<Member> queryBy(MemberFilter mf);
    public void editMember(Member member);
    public void deleteMember(int id);
}

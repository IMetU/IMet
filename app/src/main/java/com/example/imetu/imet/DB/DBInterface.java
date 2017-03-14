package com.example.imetu.imet.DB;

import com.example.imetu.imet.Model.Member;
import com.example.imetu.imet.Model.MemberFilter;

import java.util.ArrayList;

public interface DBInterface {
    public ArrayList<Member> selectAll();
    public Member selectOne(int id);
    public ArrayList<Member> queryBy(MemberFilter mf);
    public void editMember(Member member);
    public void deleteMember(int id);

}

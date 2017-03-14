package com.example.imetu.imet.DB;

import com.example.imetu.imet.Model.Member;
import com.example.imetu.imet.Model.MemberFilter;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.ArrayList;
import java.util.List;


public class DBEngine implements DBInterface {
    @Override
    public void deleteMember(int id) {
        MemberTable memberTable = new MemberTable();
        memberTable.id = id;
        memberTable.delete();
    }

    @Override
    public ArrayList<Member> selectAll() {
        List<MemberTable> memberList = SQLite.select().from(MemberTable.class).queryList();
        ArrayList<Member> memberArrayList = new ArrayList<>();
        for(int i = 0;i < memberList.size();i++){
            Member member = new Member();
            member.setId(memberList.get(i).id);
            member.setName(memberList.get(i).name);
            member.setPhoneNumber(memberList.get(i).phoneNumber);
            member.setEvent(memberList.get(i).event);
            member.setImgPath(memberList.get(i).imgPath);
            memberArrayList.add(member);
        }

        return memberArrayList;
    }

    @Override
    public Member selectOne(int id) {
        MemberTable memberTable = SQLite.select().from(MemberTable.class).querySingle();
        Member member = new Member();
        member.setId(memberTable.id);
        member.setName(memberTable.name);
        member.setPhoneNumber(memberTable.phoneNumber);
        member.setEvent(memberTable.event);
        member.setImgPath(memberTable.imgPath);
        return member;
    }

    @Override
    public ArrayList<Member> queryBy(MemberFilter mf) {
        //  TODO: query db
        return null;
    }

    @Override
    public void editMember(Member member) {
        //  TODO: edit member and add new member
        MemberTable memberTable = new MemberTable();
        //  Check is id already used?
        //  if used, save table with id
        //  if not, save table without id
        if (member.isEdited()){
            memberTable.id = member.getId();
        }
        memberTable.name = member.getName();
        memberTable.phoneNumber = member.getPhoneNumber();
        memberTable.imgPath = member.getImgPath();
        memberTable.event = member.getEvent();
        memberTable.isEdited = member.isEdited();
        memberTable.save();
    }
}

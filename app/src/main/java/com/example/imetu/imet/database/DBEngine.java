package com.example.imetu.imet.database;

import com.example.imetu.imet.model.Member;
import com.example.imetu.imet.model.MemberFilter;
import com.example.imetu.imet.widget.Util;
import com.raizlabs.android.dbflow.sql.language.ConditionGroup;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.ArrayList;
import java.util.List;

import static com.example.imetu.imet.database.MemberTable_Table.name;
import static com.example.imetu.imet.widget.Util.BODY_UNDEFINED;
import static com.example.imetu.imet.widget.Util.GENDER_UNDEFINED;
import static com.example.imetu.imet.widget.Util.GLASSES_UNDEFINED;
import static com.example.imetu.imet.widget.Util.HEIGHT_UNDEFINED;


public class DBEngine implements DBInterface {
    @Override
    public void deleteMember(int id) {
        MemberTable memberTable = new MemberTable();
        memberTable.id = id;
        memberTable.delete();
    }

    @Override
    public ArrayList<Member> selectAll() {
        // true for 'ASC', false for 'DESC'
        List<MemberTable> memberList = SQLite.select()
                                            .from(MemberTable.class)
                                            .orderBy(name, true)
                                            .queryList();

        return getMemberArrayList(memberList);
    }

    @Override
    public Member selectOne(int id) {
        MemberTable memberTable = SQLite.select()
                                        .from(MemberTable.class)
                                        .where(MemberTable_Table.id.eq(id))
                                        .querySingle();

        Member member = new Member();
        member.setId(memberTable.id);
        member.setName(memberTable.name);
        member.setPhone(memberTable.phone);
        member.setEmail(memberTable.email);
        member.setRelationship(memberTable.relationship);
        member.setEvent(memberTable.event);
        member.setLocation(memberTable.location);
        member.setYearMet(memberTable.yearMet);
        member.setTopicDiscussed(memberTable.topicDiscussed);
        member.setGender(memberTable.gender);
        member.setHeight(memberTable.height);
        member.setBodyShape(memberTable.bodyShape);
        member.setHairLength(memberTable.hairLength);
        member.setPermed(memberTable.permed);
        member.setDyed(memberTable.dyed);
        member.setGlasses(memberTable.glasses);
        member.setOther(memberTable.other);
        member.setImgPath(memberTable.imgPath);
        return member;
    }

    @Override
    public ArrayList<Member> fullQuery(MemberFilter mf) {
        //  TODO: query db, check the mf to add ConditionGroup
        ConditionGroup conditionQueryBuilder = ConditionGroup.clause();

        if(!mf.getName().equals("")) {
            conditionQueryBuilder.and(MemberTable_Table.name.eq(mf.getName()));
        }
        if(!mf.getRelationship().equals("")) {
            conditionQueryBuilder.and(MemberTable_Table.relationship.is(mf.getRelationship()));
        }
        if(!mf.getEvent().equals("")) {
            conditionQueryBuilder.and(MemberTable_Table.event.is(mf.getEvent()));
        }
        if(mf.getGender() != GENDER_UNDEFINED) {
            conditionQueryBuilder.and(MemberTable_Table.gender.is(mf.getGender()));
        }
        if(mf.getHeight() != HEIGHT_UNDEFINED) {
            conditionQueryBuilder.and(MemberTable_Table.height.between(mf.getHeight()-5).and(mf.getHeight()+5));
        }
        if(mf.getBodyShape() != BODY_UNDEFINED) {
            conditionQueryBuilder.and(MemberTable_Table.bodyShape.is(mf.getBodyShape()));
        }
        if(mf.getGlasses() != GLASSES_UNDEFINED) {
            conditionQueryBuilder.and(MemberTable_Table.glasses.is(mf.getGlasses()));
        }

        // true for 'ASC', false for 'DESC'
        List<MemberTable> memberList = SQLite.select()
                                            .from(MemberTable.class)
                                            .where(conditionQueryBuilder)
                                            .orderBy(name, true)
                                            .queryList();

        return getMemberArrayList(memberList);
    }

    @Override
    public ArrayList<Member> simpleQuery(String querystring) {
        // true for 'ASC', false for 'DESC'
        List<MemberTable> memberList = SQLite.select()
                .from(MemberTable.class)
                .where(MemberTable_Table.name.eq(querystring))
                .or(MemberTable_Table.event.eq(querystring))
                .orderBy(name, true)
                .queryList();

        return getMemberArrayList(memberList);
    }

    private ArrayList<Member> getMemberArrayList(List<MemberTable> memberList) {
        ArrayList<Member> memberArrayList = new ArrayList<>();
        for(int i = 0;i < memberList.size();i++){
            Member member = new Member();
            member.setId(memberList.get(i).id);
            member.setName(memberList.get(i).name);
            member.setPhone(memberList.get(i).phone);
            member.setEmail(memberList.get(i).email);
            member.setRelationship(memberList.get(i).relationship);
            member.setEvent(memberList.get(i).event);
            member.setLocation(memberList.get(i).location);
            member.setYearMet(memberList.get(i).yearMet);
            member.setTopicDiscussed(memberList.get(i).topicDiscussed);
            member.setGender(memberList.get(i).gender);
            member.setHeight(memberList.get(i).height);
            member.setBodyShape(memberList.get(i).bodyShape);
            member.setHairLength(memberList.get(i).hairLength);
            member.setPermed(memberList.get(i).permed);
            member.setDyed(memberList.get(i).dyed);
            member.setGlasses(memberList.get(i).glasses);
            member.setOther(memberList.get(i).other);
            member.setImgPath(memberList.get(i).imgPath);
            memberArrayList.add(member);
        }

        return memberArrayList;
    }

    @Override
    public void editMember(Member member) {
        //  TODO: edit member and add new member
        MemberTable memberTable = new MemberTable();
        //  Check is id already used?
        //  if used, save table with id
        //  if not, save table without id
        if (member.getId() != Util.ADD_MEMBER){
            memberTable.id = member.getId();
        }
        memberTable.name = member.getName();
        memberTable.phone = member.getPhone();
        memberTable.email = member.getEmail();
        memberTable.relationship = member.getRelationship();
        memberTable.event = member.getEvent();
        memberTable.location = member.getLocation();
        memberTable.yearMet = member.getYearMet();
        memberTable.topicDiscussed = member.getTopicDiscussed();
        memberTable.gender = member.getGender();
        memberTable.height = member.getHeight();
        memberTable.bodyShape = member.getBodyShape();
        memberTable.hairLength = member.getHairLength();
        memberTable.permed = member.isPermed();
        memberTable.dyed = member.isDyed();
        memberTable.glasses = member.getGlasses();
        memberTable.other = member.getOther();
        memberTable.imgPath = member.getImgPath();
        memberTable.save();
    }
}

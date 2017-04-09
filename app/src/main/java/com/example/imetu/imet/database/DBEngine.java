package com.example.imetu.imet.database;

import android.util.Log;

import com.example.imetu.imet.model.Member;
import com.example.imetu.imet.model.MemberFilter;
import com.example.imetu.imet.widget.Util;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.raizlabs.android.dbflow.sql.language.ConditionGroup;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

import static com.example.imetu.imet.database.MemberTable_Table.name;
import static com.example.imetu.imet.widget.ConfidentialUtil.FIREBASE_URL;
import static com.example.imetu.imet.widget.Util.BODY_UNDEFINED;
import static com.example.imetu.imet.widget.Util.GENDER_UNDEFINED;
import static com.example.imetu.imet.widget.Util.GLASSES_UNDEFINED;
import static com.example.imetu.imet.widget.Util.HAIR_UNDEFINED;
import static com.example.imetu.imet.widget.Util.HEIGHT_UNDEFINED;
import static com.example.imetu.imet.widget.Util.REQUEST_ADVANCE_SEARCH;
import static com.raizlabs.android.dbflow.config.FlowManager.getContext;


public class DBEngine implements DBInterface {
    public String iMetUserId;

    public DBEngine(String iMetUserId){
        this.iMetUserId = iMetUserId;
    }

    @Override
    public void deleteMember(int id) {
        MemberTable memberTable = new MemberTable();
        memberTable.id = id;
        memberTable.delete();

        AsyncHttpClient client = new AsyncHttpClient();
        String deleteMemberURL = FIREBASE_URL + "/users/" + iMetUserId + "/member/" + id + ".json";

        client.delete(deleteMemberURL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.d("IMet", "DBEngine, deleteMember, statusCode: " + statusCode + "responseString: " + responseString);
            }
        });
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
        //  query db, check the mf to add ConditionGroup
        ConditionGroup conditionQueryBuilder = ConditionGroup.clause();

        if(!mf.getName().equals("")) {
            conditionQueryBuilder.and(MemberTable_Table.name.like('%'+mf.getName()+'%'));
        }
        if(!mf.getRelationship().equals("")) {
            conditionQueryBuilder.and(MemberTable_Table.relationship.like('%'+mf.getRelationship()+'%'));
        }
        if(!mf.getEvent().equals("")) {
            conditionQueryBuilder.and(MemberTable_Table.event.like('%'+mf.getEvent()+'%'));
        }
        if(!mf.getYearMet().equals("")) {
            conditionQueryBuilder.and(MemberTable_Table.yearMet.like('%'+mf.getYearMet()+'%'));
        }
        if(!mf.getTopicDiscussed().equals("")) {
            conditionQueryBuilder.and(MemberTable_Table.topicDiscussed.like('%'+mf.getTopicDiscussed()+'%'));
        }
        if(!mf.getOther().equals("")) {
            conditionQueryBuilder.and(MemberTable_Table.other.like('%'+mf.getOther()+'%'));
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
        if(mf.getHairLength() != HAIR_UNDEFINED) {
            conditionQueryBuilder.and(MemberTable_Table.hairLength.is(mf.getHairLength()));
        }
        if(mf.getFilterOrAdvance() == REQUEST_ADVANCE_SEARCH) {
            conditionQueryBuilder.and(MemberTable_Table.permed.is(mf.isPermed()));
            conditionQueryBuilder.and(MemberTable_Table.dyed.is(mf.isDyed()));
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
                .where(MemberTable_Table.name.like('%'+querystring+'%'))
                .or(MemberTable_Table.event.like('%'+querystring+'%'))
                .or(MemberTable_Table.other.like('%'+querystring+'%'))
                .or(MemberTable_Table.phone.like('%'+querystring+'%'))
                .or(MemberTable_Table.email.like('%'+querystring+'%'))
                .or(MemberTable_Table.relationship.like('%'+querystring+'%'))
                .or(MemberTable_Table.location.like('%'+querystring+'%'))
                .or(MemberTable_Table.yearMet.like('%'+querystring+'%'))
                .or(MemberTable_Table.topicDiscussed.like('%'+querystring+'%'))
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
        //  edit member and add new member
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

        AsyncHttpClient client = new AsyncHttpClient();
        String editMemberURL = FIREBASE_URL + "/users/" + iMetUserId + "/member/" + memberTable.id  + ".json";

        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("name", memberTable.name);
            jsonParams.put("phone", memberTable.phone);
            jsonParams.put("email", memberTable.email);
            jsonParams.put("relationship", memberTable.relationship);
            jsonParams.put("event", memberTable.event);
            jsonParams.put("location", memberTable.location);
            jsonParams.put("yearMet", memberTable.yearMet);
            jsonParams.put("topicDiscussed", memberTable.topicDiscussed);
            jsonParams.put("gender", memberTable.gender);
            jsonParams.put("height", memberTable.height);
            jsonParams.put("bodyShape", memberTable.bodyShape);
            jsonParams.put("hairLength", memberTable.hairLength);
            jsonParams.put("permed", memberTable.permed);
            jsonParams.put("dyed", memberTable.dyed);
            jsonParams.put("glasses", memberTable.glasses);
            jsonParams.put("other", memberTable.other);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        StringEntity entity = null;
        try {
            entity = new StringEntity(jsonParams.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        client.put(getContext(), editMemberURL, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.d("IMet", "DBEngine, editMember, statusCode: " + statusCode + "responseString: " + responseString);
            }
        });
    }
}

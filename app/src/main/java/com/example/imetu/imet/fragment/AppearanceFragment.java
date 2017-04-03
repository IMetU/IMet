package com.example.imetu.imet.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.imetu.imet.model.Member;
import com.example.imetu.imet.R;

import static com.example.imetu.imet.widget.Util.BODY_MEDIUM;
import static com.example.imetu.imet.widget.Util.BODY_PLUMP;
import static com.example.imetu.imet.widget.Util.BODY_THIN;
import static com.example.imetu.imet.widget.Util.GENDER_FEMALE;
import static com.example.imetu.imet.widget.Util.GENDER_MALE;
import static com.example.imetu.imet.widget.Util.GLASSES_WITH;
import static com.example.imetu.imet.widget.Util.GLASSES_WITHOUT;
import static com.example.imetu.imet.widget.Util.HAIR_LONG;
import static com.example.imetu.imet.widget.Util.HAIR_MEDIUM;
import static com.example.imetu.imet.widget.Util.HAIR_SHORT;

/**
 * Created by kelly79126 on 2017/3/14.
 */

public class AppearanceFragment  extends Fragment {

    public View v;

    private ViewGroup vgInformation[];
    private TextView tvTitle[];
    private TextView tvContent[];
    private String sTitle[] = {"Gender", "Height", "bodyShape", "Hair", "Glasses", "Other"};
    private String sContent[];

    private int vgInformationId[] = { R.id.gender,
            R.id.height,
            R.id.bodyShape,
            R.id.hair,
            R.id.glasses,
            R.id.other};

    static final private int NUM_INFOS = 6;
    private Member member;

    public AppearanceFragment() {}
    public AppearanceFragment(Member m) {
        member = m;
    }


    //inflation logic
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_appearance, parent, false);

        vgInformation = new ViewGroup[NUM_INFOS];
        tvTitle = new TextView[NUM_INFOS];
        tvContent = new TextView[NUM_INFOS];

        for (int idx = 0; idx < NUM_INFOS; idx++) {
            vgInformation[idx] = (ViewGroup) v.findViewById(vgInformationId[idx]);
            if (null != vgInformation[idx]) {
                inflateInformatilGroup(vgInformation[idx], idx, R.id.detail_title, R.id.detail_content);
            }
        }
        return v;
    }

    private void inflateInformatilGroup(ViewGroup vgParent, int idx, int iTitle, int iContent){
        tvTitle[idx] = (TextView)vgParent.findViewById(iTitle);
        tvTitle[idx].setText(sTitle[idx]);
        tvContent[idx] = (TextView)vgParent.findViewById(iContent);
        if(sContent[idx] == null || sContent[idx].equals("")){
            vgInformation[idx].setVisibility(View.GONE);
        }else {
            tvContent[idx].setText(sContent[idx]);
        }
    }


    //create lifecycle event
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sContent = new String[NUM_INFOS];
        setMemberValue();
    }

    public void refresh(Member newMember) {
        member = newMember;
        setMemberValue();
        setTextContent();
    }

    private void setMemberValue() {
        switch (member.getGender()) {
            case GENDER_MALE:
                sContent[0] = "Male";
                break;
            case GENDER_FEMALE:
                sContent[0] = "Female";
                break;
            default:
                sContent[0] = "";
        }

        sContent[1] = "" + member.getHeight();

        switch (member.getBodyShape()){
            case BODY_THIN:
                sContent[2] = "Thin";
                break;
            case BODY_MEDIUM:
                sContent[2] = "Medium";
                break;
            case BODY_PLUMP:
                sContent[2] = "Plump";
                break;
            default:
                sContent[2] = "";
        }

        switch (member.getHairLength()){
            case HAIR_SHORT:
                sContent[3] = "Short";
                break;
            case HAIR_MEDIUM:
                sContent[3] = "Medium";
                break;
            case HAIR_LONG:
                sContent[3] = "Long";
                break;
            default:
                sContent[3] = "";
        }

        sContent[3] += member.isPermed() ? " Permed" : "";
        sContent[3] += member.isDyed() ? " Dyed" : "";

        switch (member.getGlasses()){
            case GLASSES_WITH:
                sContent[4] = "With Glasses";
                break;
            case GLASSES_WITHOUT:
                sContent[4] = "Without Glasses";
                break;
            default:
                sContent[4] = "";
        }

        sContent[5] = member.getOther();
    }

    private void setTextContent() {
        for (int idx = 0; idx < NUM_INFOS; idx++) {
            if(sContent[idx] == null || sContent[idx].equals("")){
                vgInformation[idx].setVisibility(View.GONE);
            }else {
                tvContent[idx].setText(sContent[idx]);
            }
        }
    }
}

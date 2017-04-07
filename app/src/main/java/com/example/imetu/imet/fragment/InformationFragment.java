package com.example.imetu.imet.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.imetu.imet.R;
import com.example.imetu.imet.model.Member;

import org.parceler.Parcels;

public class InformationFragment extends Fragment {

    public View v;

    private ViewGroup vgInformation[];
    private TextView tvTitle[];
    private TextView tvContent[];
    private String sTitle[] = {"Phone", "Email", "Relationship", "Event", "Location", "Date Met","Topic Discussed"};
    private String sContent[];

    private int vgInformationId[] = { R.id.phone,
                                R.id.email,
                                R.id.relationship,
                                R.id.event,
                                R.id.location,
                                R.id.yearMet,
                                R.id.topicDiscussed};

    static final private int NUM_INFOS = 7;
    private Member member;

    // newInstance constructor for creating fragment with arguments
    public static InformationFragment newInstance(Member m) {
        InformationFragment informationFragment = new InformationFragment();
        Bundle args = new Bundle();
        args.putParcelable("member" , Parcels.wrap(m));
        informationFragment.setArguments(args);
        return informationFragment;
    }


    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        member = (Member) Parcels.unwrap(getArguments().getParcelable("member"));
        sContent = new String[NUM_INFOS];
        setMemberValue();
    }


    //inflation logic
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_information, parent, false);
        vgInformation = new ViewGroup[NUM_INFOS];
        tvTitle = new TextView[NUM_INFOS];
        tvContent = new TextView[NUM_INFOS];

        for (int idx = 0; idx < NUM_INFOS; idx++) {
            vgInformation[idx] = (ViewGroup) v.findViewById(vgInformationId[idx]);
            if (null != vgInformation[idx]) {
                inflateInformationGroup(vgInformation[idx], idx, R.id.detail_title, R.id.detail_content);
            }
        }
        return v;
    }

    private void inflateInformationGroup(ViewGroup vgParent, int idx, int iTitle, int iContent){
        tvTitle[idx] = (TextView)vgParent.findViewById(iTitle);
        tvTitle[idx].setText(sTitle[idx]);
        tvContent[idx] = (TextView)vgParent.findViewById(iContent);

        if(sContent[idx] == null || sContent[idx].equals("")){
            vgInformation[idx].setVisibility(View.GONE);
        }else {
            tvContent[idx].setText(sContent[idx]);
        }
    }

    public void refresh(Member newMember) {
        member = newMember;
        setMemberValue();
        setTextContent();
    }

    private void setMemberValue() {
        sContent[0] = member.getPhone();
        sContent[1] = member.getEmail();
        sContent[2] = member.getRelationship();
        sContent[3] = member.getEvent();
        sContent[4] = member.getLocation();
        sContent[5] = member.getYearMet();
        sContent[6] = member.getTopicDiscussed();
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

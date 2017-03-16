package com.example.imetu.imet.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.imetu.imet.R;

/**
 * Created by kelly79126 on 2017/3/14.
 */

public class InformationFragment extends Fragment {

    public View v;

    private ViewGroup vgInformation[];
    private TextView tvTitle[];
    private TextView tvContent[];
    private String sTitle[] = {"Phone", "Email", "Relationship", "Event", "Location", "Year Met","TopicDiscussed"};
    private String sContent[];

    private int vgInformationId[] = { R.id.phone,
                                R.id.email,
                                R.id.relationship,
                                R.id.event,
                                R.id.location,
                                R.id.yearMet,
                                R.id.topicDiscussed};

    static final private int NUM_INFOS = 7;


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
                inflateInformatilGroup(vgInformation[idx], idx, R.id.detail_title, R.id.detail_content);
            }
        }
        return v;
    }

    private void inflateInformatilGroup(ViewGroup vgParent, int idx, int iTitle, int iContent){
        tvTitle[idx] = (TextView)vgParent.findViewById(iTitle);
        tvTitle[idx].setText(sTitle[idx]);
        tvContent[idx] = (TextView)vgParent.findViewById(iContent);
        tvContent[idx].setText(sContent[idx]);
    }


    //create lifecycle event
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sContent = new String[NUM_INFOS];
        sContent[0] = "0910000000";
        sContent[1] = "kelly79126@gmail.com";
        sContent[2] = "friends";
        sContent[3] = "android";
        sContent[4] = "taipei";
        sContent[5] = "";
        sContent[6] = "";
    }

}
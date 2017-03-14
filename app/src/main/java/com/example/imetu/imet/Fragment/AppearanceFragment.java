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
        tvContent[idx].setText(sContent[idx]);
    }


    //create lifecycle event
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sContent = new String[NUM_INFOS];
        sContent[0] = "Female";
        sContent[1] = "163cm";
        sContent[2] = "Medium";
        sContent[3] = "Long";
        sContent[4] = "With Glasses";
        sContent[5] = "";
    }
}

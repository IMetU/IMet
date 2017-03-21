package com.example.imetu.imet.fragment;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.SeekBar;

import com.example.imetu.imet.model.MemberFilter;
import com.example.imetu.imet.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class FilterFragment extends DialogFragment implements Button.OnClickListener {

    @BindView(R.id.etName) EditText etName;
    @BindView(R.id.etRelationship) EditText etRepationship;
    @BindView(R.id.etEvent) EditText etEvent;
    @BindView(R.id.btnReset) Button btnReset;
    @BindView(R.id.btnSearch) Button btnSearch;
    @BindView(R.id.btnAdvanceSearch) Button btnAdvanceSearch;
    @BindView(R.id.rgGender) RadioGroup rgGender;
    @BindView(R.id.sbHeight) SeekBar sbHeight;

    private Unbinder unbinder;
    MemberFilter memberFilter;


    public interface FilterSearchListener {
        void onFilterSearch(MemberFilter memberFilter);
    }

    public interface FilterAdvanceSearchListener {
        void onFilterAdvanceSearch(MemberFilter memberFilter);
    }

    public FilterFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static FilterFragment newInstance(String filter) {
        FilterFragment frag = new FilterFragment();
        Bundle args = new Bundle();
        args.putString("filter" , filter);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filter, container);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        memberFilter = new MemberFilter();
        btnReset.setOnClickListener(this);
        btnSearch.setOnClickListener(this);
        btnAdvanceSearch.setOnClickListener(this);
        sbHeight.setMax(40);

        sbHeight.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @TargetApi(Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                Log.d("Kelly", "Height: " + (150 + sbHeight.getProgress()));
            }
        });
        // rgGender.check(int id)
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnReset:
                rgGender.clearCheck();
                break;
            case R.id.btnSearch:
                FilterSearchListener searchListener = (FilterSearchListener) getActivity();
                searchListener.onFilterSearch(memberFilter);
                dismiss();
                break;
            case R.id.btnAdvanceSearch:
                FilterAdvanceSearchListener advanceSearchListener = (FilterAdvanceSearchListener) getActivity();
                advanceSearchListener.onFilterAdvanceSearch(memberFilter);
                dismiss();
                break;
        }
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}

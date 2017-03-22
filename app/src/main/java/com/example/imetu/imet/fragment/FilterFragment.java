package com.example.imetu.imet.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.example.imetu.imet.R;
import com.example.imetu.imet.model.MemberFilter;
import com.example.imetu.imet.widget.ObservableScrollView;
import com.xw.repo.BubbleSeekBar;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.example.imetu.imet.widget.Util.BODY_MEDIUM;
import static com.example.imetu.imet.widget.Util.BODY_PLUMP;
import static com.example.imetu.imet.widget.Util.BODY_THIN;
import static com.example.imetu.imet.widget.Util.BODY_UNDEFINED;
import static com.example.imetu.imet.widget.Util.GENDER_FEMALE;
import static com.example.imetu.imet.widget.Util.GENDER_MALE;
import static com.example.imetu.imet.widget.Util.GENDER_UNDEFINED;
import static com.example.imetu.imet.widget.Util.GLASSES_UNDEFINED;
import static com.example.imetu.imet.widget.Util.GLASSES_WITH;
import static com.example.imetu.imet.widget.Util.GLASSES_WITHOUT;
import static com.example.imetu.imet.widget.Util.HEIGHT_UNDEFINED;

public class FilterFragment extends DialogFragment implements Button.OnClickListener {

    @BindView(R.id.btnReset) Button btnReset;
    @BindView(R.id.btnSearch) Button btnSearch;
    @BindView(R.id.btnAdvanceSearch) Button btnAdvanceSearch;

    @BindView(R.id.etName) EditText etName;
    @BindView(R.id.etRelationship) EditText etRelationship;
    @BindView(R.id.etEvent) EditText etEvent;

    @BindView(R.id.rgGender) RadioGroup rgGender;
    @BindView(R.id.rgBodyShape) RadioGroup rgBodyShape;
    @BindView(R.id.rgGlasses) RadioGroup rgGlasses;
    @BindView(R.id.sbHeight) BubbleSeekBar seekbarHeight;
    @BindView(R.id.cbAny) CheckBox cbAny;
    @BindView(R.id.fragment_filter) ObservableScrollView obsScrollView;

    private Unbinder unbinder;
    int seekbarHeightProgress = 165;


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

    public static FilterFragment newInstance(MemberFilter memberFilter) {
        FilterFragment frag = new FilterFragment();
        Bundle args = new Bundle();
        args.putParcelable("memberFilter" , Parcels.wrap(memberFilter));
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
        final MemberFilter memberFilter = (MemberFilter) Parcels.unwrap(getArguments().getParcelable("memberFilter"));

        btnReset.setOnClickListener(this);
        btnSearch.setOnClickListener(this);
        btnAdvanceSearch.setOnClickListener(this);

        seekbarHeight.correctOffsetWhenContainerOnScrolling();
        seekbarHeight.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(int progress, float progressFloat) {
                seekbarHeightProgress = progress;
                Log.d("Kelly", " " + progress);
            }

            @Override
            public void getProgressOnActionUp(int progress, float progressFloat) {

            }

            @Override
            public void getProgressOnFinally(int progress, float progressFloat) {

            }
        });

        obsScrollView.setOnScrollChangedListener(new ObservableScrollView.OnScrollChangedListener() {
            @Override
            public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
                seekbarHeight.correctOffsetWhenContainerOnScrolling();
            }
        });

        cbAny.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

            }
        });

        setInitFilterState(memberFilter);
    }

    private void setInitFilterState(MemberFilter mf) {
        etName.setText(mf.getName());
        etRelationship.setText(mf.getRelationship());
        etEvent.setText(mf.getEvent());

        switch (mf.getGender()){
            case GENDER_MALE:
                rgGender.check(R.id.radio_Male);
                break;
            case GENDER_FEMALE:
                rgGender.check(R.id.radio_Female);
                break;
            default:
                // do nothing
        }

        if(mf.getHeight() == HEIGHT_UNDEFINED){
            seekbarHeight.setProgress(seekbarHeightProgress);
            cbAny.setChecked(true);
        }else {
            seekbarHeight.setProgress(mf.getHeight());
        }

        switch (mf.getBodyShape()){
            case BODY_THIN:
                rgBodyShape.check(R.id.radio_Thin);
                break;
            case BODY_MEDIUM:
                rgBodyShape.check(R.id.radio_Medium);
                break;
            case  BODY_PLUMP:
                rgBodyShape.check(R.id.radio_Plump);
                break;
            default:
                // do nothing
        }

        switch (mf.getGlasses()) {
            case GLASSES_WITH:
                rgGlasses.check(R.id.radio_WithGlasses);
                break;
            case GLASSES_WITHOUT:
                rgGlasses.check(R.id.radio_WithoutGlasses);
                break;
            default:
                // do nothing
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnReset:
                etName.getText().clear();
                etRelationship.getText().clear();
                etEvent.getText().clear();
                rgGender.clearCheck();
                rgBodyShape.clearCheck();
                rgGlasses.clearCheck();
                cbAny.setChecked(true);
                break;
            case R.id.btnSearch:
                FilterSearchListener searchListener = (FilterSearchListener) getActivity();
                searchListener.onFilterSearch(getMemberFilterValue());
                dismiss();
                break;
            case R.id.btnAdvanceSearch:
                FilterAdvanceSearchListener advanceSearchListener = (FilterAdvanceSearchListener) getActivity();
                advanceSearchListener.onFilterAdvanceSearch(getMemberFilterValue());
                dismiss();
                break;
        }
    }

    private MemberFilter getMemberFilterValue() {
        MemberFilter memberFilter = new MemberFilter();

        memberFilter.setName(etName.getText().toString());
        memberFilter.setRelationship(etRelationship.getText().toString());
        memberFilter.setEvent(etEvent.getText().toString());

        switch (rgGender.getCheckedRadioButtonId()){
            case R.id.radio_Male:
                memberFilter.setGender(GENDER_MALE);
                break;
            case R.id.radio_Female:
                memberFilter.setGender(GENDER_FEMALE);
                break;
            default:
                memberFilter.setGender(GENDER_UNDEFINED);
        }

        if(cbAny.isChecked() == true){
            memberFilter.setHeight(HEIGHT_UNDEFINED);
        }else {
            memberFilter.setHeight(seekbarHeightProgress);
        }

        switch (rgBodyShape.getCheckedRadioButtonId()){
            case R.id.radio_Thin:
                memberFilter.setBodyShape(BODY_THIN);
                break;
            case R.id.radio_Medium:
                memberFilter.setBodyShape(BODY_MEDIUM);
                break;
            case R.id.radio_Plump:
                memberFilter.setBodyShape(BODY_PLUMP);
                break;
            default:
                memberFilter.setBodyShape(BODY_UNDEFINED);
        }

        switch (rgGlasses.getCheckedRadioButtonId()){
            case R.id.radio_WithGlasses:
                memberFilter.setGlasses(GLASSES_WITH);
                break;
            case R.id.radio_WithoutGlasses:
                memberFilter.setGlasses(GLASSES_WITHOUT);
                break;
            default:
                memberFilter.setGlasses(GLASSES_UNDEFINED);
        }

        return memberFilter;
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}

package com.example.imetu.imet.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;

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

    @BindView(R.id.rgGender) RadioGroup rgGender;
    @BindView(R.id.sbHeight) BubbleSeekBar seekbarHeight;
    @BindView(R.id.cbAny) CheckBox cbAny;
    @BindView(R.id.bodyShape_spinner) Spinner bodyShapeSpinner;
    @BindView(R.id.glasses_switch) Switch glassesSwitch;
    @BindView(R.id.cbGlassesAny) CheckBox cbGlassAny;

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

        seekbarHeight.correctOffsetWhenContainerOnScrolling();
        seekbarHeight.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(int progress, float progressFloat) {
                seekbarHeightProgress = progress;
            }

            @Override
            public void getProgressOnActionUp(int progress, float progressFloat) {
                if(cbAny.isChecked()){
                    cbAny.setChecked(false);
                }
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

        glassesSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(cbGlassAny.isChecked()){
                    cbGlassAny.setChecked(false);
                }
            }
        });

        setInitFilterState(memberFilter);
    }

    private void setInitFilterState(MemberFilter mf) {
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
                bodyShapeSpinner.setSelection(1);
                break;
            case BODY_MEDIUM:
                bodyShapeSpinner.setSelection(2);
                break;
            case  BODY_PLUMP:
                bodyShapeSpinner.setSelection(3);
                break;
            default:
                bodyShapeSpinner.setSelection(0);
        }

        switch (mf.getGlasses()) {
            case GLASSES_WITH:
                glassesSwitch.setChecked(true);
                break;
            case GLASSES_WITHOUT:
                glassesSwitch.setChecked(false);
                break;
            default:
                cbGlassAny.setChecked(true);
                glassesSwitch.setChecked(false);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnReset:
                rgGender.clearCheck();
                bodyShapeSpinner.setSelection(0);
                glassesSwitch.setChecked(false);
                cbAny.setChecked(true);
                cbGlassAny.setChecked(true);
                break;
            case R.id.btnSearch:
                FilterSearchListener searchListener = (FilterSearchListener) getActivity();
                searchListener.onFilterSearch(getMemberFilterValue());
                dismiss();
                break;
           }
    }

    private MemberFilter getMemberFilterValue() {
        MemberFilter memberFilter = new MemberFilter();

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

        if(cbAny.isChecked()){
            memberFilter.setHeight(HEIGHT_UNDEFINED);
        }else {
            memberFilter.setHeight(seekbarHeightProgress);
        }

        String value = bodyShapeSpinner.getSelectedItem().toString();
        switch (value){
            case "Thin":
                memberFilter.setBodyShape(BODY_THIN);
                break;
            case "Medium":
                memberFilter.setBodyShape(BODY_MEDIUM);
                break;
            case "Plump":
                memberFilter.setBodyShape(BODY_PLUMP);
                break;
            default:
                memberFilter.setBodyShape(BODY_UNDEFINED);
        }

        if(cbGlassAny.isChecked()){
            memberFilter.setGlasses(GLASSES_UNDEFINED);
        }else {
            if (glassesSwitch.isChecked()) {
                memberFilter.setGlasses(GLASSES_WITH);
            } else {
                memberFilter.setGlasses(GLASSES_WITHOUT);
            }
        }

        return memberFilter;
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}

package com.example.imetu.imet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.example.imetu.imet.R;
import com.example.imetu.imet.model.MemberFilter;
import com.example.imetu.imet.widget.ObservableScrollView;
import com.xw.repo.BubbleSeekBar;

import org.parceler.Parcels;

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
import static com.example.imetu.imet.widget.Util.HAIR_LONG;
import static com.example.imetu.imet.widget.Util.HAIR_MEDIUM;
import static com.example.imetu.imet.widget.Util.HAIR_SHORT;
import static com.example.imetu.imet.widget.Util.HAIR_UNDEFINED;
import static com.example.imetu.imet.widget.Util.HEIGHT_UNDEFINED;

/**
 * Created by kelly79126 on 2017/3/15.
 */

public class AdvanceSearchActivity extends AppCompatActivity {

    ObservableScrollView obsScrollView;
    EditText etName;
    EditText etRelationship;
    EditText etEvent;
    EditText etYearMet;
    EditText etTopicDiscussed;
    EditText etOther;
    BubbleSeekBar seekbarHeight;
    int seekbarHeightProgress = 165; //default
    CheckBox cbAny;
    RadioGroup genderRadioGroup;
    RadioGroup bodyShapeRadioGroup;
    RadioGroup hairLengthRadioGroup;
    RadioGroup glassesRadioGroup;
    CheckBox checkbox_Permed;
    CheckBox checkbox_Dyed;
    MemberFilter memberFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advance_search);

        memberFilter = (MemberFilter) Parcels.unwrap(getIntent().getParcelableExtra("memberFilter"));

        getSupportActionBar().setTitle("Advance Search");
        setView();
        setInitState();
        // hide keyboard
//        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_advance_search, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuReset:
                resetAllState();
                return true;
            case R.id.menuSearch:
                searchClick();
                return true;
            default:
                return false;
        }
    }

    private void resetAllState() {
        etName.getText().clear();
        etRelationship.getText().clear();
        etEvent.getText().clear();
        etYearMet.getText().clear();
        etTopicDiscussed.getText().clear();
        etOther.getText().clear();

        genderRadioGroup.clearCheck();
        bodyShapeRadioGroup.clearCheck();
        hairLengthRadioGroup.clearCheck();
        glassesRadioGroup.clearCheck();

        checkbox_Dyed.setChecked(false);
        checkbox_Permed.setChecked(false);
        cbAny.setChecked(true);
    }

    private void searchClick() {
        setFinishState();

        // Prepare data intent
        Intent data = new Intent();
        data.putExtra("memberFilter" , Parcels.wrap(memberFilter));
        // Activity finished ok, return the data
        setResult(RESULT_OK, data); // set result code and bundle data for response
        finish(); // closes the activity, pass data to parent
    }

    private void setFinishState() {
        memberFilter.setName(etName.getText().toString());
        memberFilter.setRelationship(etRelationship.getText().toString());
        memberFilter.setEvent(etEvent.getText().toString());
        memberFilter.setYearMet(etYearMet.getText().toString());
        memberFilter.setTopicDiscussed(etTopicDiscussed.getText().toString());
        memberFilter.setOther(etOther.getText().toString());

        switch (genderRadioGroup.getCheckedRadioButtonId()){
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

        switch (bodyShapeRadioGroup.getCheckedRadioButtonId()){
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

        switch (hairLengthRadioGroup.getCheckedRadioButtonId()){
            case R.id.radio_ShortHair:
                memberFilter.setHairLength(HAIR_SHORT);
                break;
            case R.id.radio_MediumHair:
                memberFilter.setHairLength(HAIR_MEDIUM);
                break;
            case R.id.radio_LongHair:
                memberFilter.setHairLength(HAIR_LONG);
                break;
            default:
                memberFilter.setHairLength(HAIR_UNDEFINED);
        }

        memberFilter.setPermed(checkbox_Permed.isChecked());
        memberFilter.setDyed(checkbox_Dyed.isChecked());

        switch (glassesRadioGroup.getCheckedRadioButtonId()){
            case R.id.radio_WithGlasses:
                memberFilter.setGlasses(GLASSES_WITH);
                break;
            case R.id.radio_WithoutGlasses:
                memberFilter.setGlasses(GLASSES_WITHOUT);
                break;
            default:
                memberFilter.setGlasses(GLASSES_UNDEFINED);
        }
    }


    private void setView() {
        etName = (EditText) findViewById(R.id.etName);
        etRelationship = (EditText) findViewById(R.id.etRelationship);
        etEvent = (EditText) findViewById(R.id.etEvent);
        etYearMet = (EditText) findViewById(R.id.etYearMet);
        etTopicDiscussed = (EditText) findViewById(R.id.etTopicDiscussed);
        etOther = (EditText) findViewById(R.id.etOther);

        genderRadioGroup = (RadioGroup) findViewById(R.id.GenderRadioGroup);
        bodyShapeRadioGroup = (RadioGroup) findViewById(R.id.BodyShapeRadioGroup);
        hairLengthRadioGroup = (RadioGroup) findViewById(R.id.HairLengthRadioGroup);
        glassesRadioGroup = (RadioGroup) findViewById(R.id.GlassesRadioGroup);

        checkbox_Permed = (CheckBox) findViewById(R.id.checkbox_Permed);
        checkbox_Dyed = (CheckBox) findViewById(R.id.checkbox_Dyed);

        cbAny = (CheckBox) findViewById(R.id.cbAny);
        seekbarHeight = (BubbleSeekBar) findViewById(R.id.seekbarHeight);
        seekbarHeight.correctOffsetWhenContainerOnScrolling();
        seekbarHeight.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(int progress, float progressFloat) {
                seekbarHeightProgress = progress;
            }

            @Override
            public void getProgressOnActionUp(int progress, float progressFloat) {

            }

            @Override
            public void getProgressOnFinally(int progress, float progressFloat) {

            }
        });

        obsScrollView = (ObservableScrollView) findViewById(R.id.activity_advance_search);

        obsScrollView.setOnScrollChangedListener(new ObservableScrollView.OnScrollChangedListener() {
            @Override
            public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
                seekbarHeight.correctOffsetWhenContainerOnScrolling();
            }
        });
    }

    private void setInitState() {
        etName.setText(memberFilter.getName());
        etRelationship.setText(memberFilter.getRelationship());
        etEvent.setText(memberFilter.getEvent());

        switch (memberFilter.getGender()){
            case GENDER_MALE:
                genderRadioGroup.check(R.id.radio_Male);
                break;
            case GENDER_FEMALE:
                genderRadioGroup.check(R.id.radio_Female);
                break;
            default:
                // do nothing
        }

        if(memberFilter.getHeight() == HEIGHT_UNDEFINED){
            seekbarHeight.setProgress(seekbarHeightProgress);
            cbAny.setChecked(true);
        }else {
            seekbarHeight.setProgress(memberFilter.getHeight());
        }

        switch (memberFilter.getBodyShape()){
            case BODY_THIN:
                bodyShapeRadioGroup.check(R.id.radio_Thin);
                break;
            case BODY_MEDIUM:
                bodyShapeRadioGroup.check(R.id.radio_Medium);
                break;
            case  BODY_PLUMP:
                bodyShapeRadioGroup.check(R.id.radio_Plump);
                break;
            default:
                // do nothing
        }

        switch (memberFilter.getGlasses()) {
            case GLASSES_WITH:
                glassesRadioGroup.check(R.id.radio_WithGlasses);
                break;
            case GLASSES_WITHOUT:
                glassesRadioGroup.check(R.id.radio_WithoutGlasses);
                break;
            default:
                // do nothing
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // Prepare data intent
//        Intent data = new Intent();
//        data.putExtra("test", "It is return from Advance backPressed");

        // Activity finished ok, return the data
//        setResult(RESULT_OK, data); // set result code and bundle data for response
//        finish(); // closes the activity, pass data to parent
    }
}

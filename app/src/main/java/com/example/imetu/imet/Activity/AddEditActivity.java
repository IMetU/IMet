package com.example.imetu.imet.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.SeekBar;

import com.example.imetu.imet.DB.DBEngine;
import com.example.imetu.imet.Model.Member;
import com.example.imetu.imet.R;

import static com.example.imetu.imet.Util.ADD_MEMBER;
import static com.example.imetu.imet.Util.BODY_MEDIUM;
import static com.example.imetu.imet.Util.BODY_PLUMP;
import static com.example.imetu.imet.Util.BODY_THIN;
import static com.example.imetu.imet.Util.BODY_UNDEFINED;
import static com.example.imetu.imet.Util.GENDER_FEMALE;
import static com.example.imetu.imet.Util.GENDER_MALE;
import static com.example.imetu.imet.Util.GENDER_UNDEFINED;
import static com.example.imetu.imet.Util.GLASSES_UNDEFINED;
import static com.example.imetu.imet.Util.GLASSES_WITH;
import static com.example.imetu.imet.Util.GLASSES_WITHOUT;
import static com.example.imetu.imet.Util.HAIR_LONG;
import static com.example.imetu.imet.Util.HAIR_MEDIUM;
import static com.example.imetu.imet.Util.HAIR_SHORT;
import static com.example.imetu.imet.Util.HAIR_UNDEFINED;

public class AddEditActivity extends AppCompatActivity {

    EditText etName;
    EditText etPhone;
    EditText etEmail;
    EditText etRelationship;
    EditText etEvent;
    EditText etLocation;
    EditText etYearMet;
    EditText etTopicDiscussed;
    EditText etOther;
    SeekBar seekbarHeight;
    int seekbarHeightProgress = 0; //default

    RadioGroup genderRadioGroup;
    RadioGroup bodyShapeRadioGroup;
    RadioGroup hairLengthRadioGroup;
    RadioGroup glassesRadioGroup;
//    RadioButton radio_Male;
//    RadioButton radio_Female;

//    RadioButton radio_Thin;
//    RadioButton radio_Medium;
//    RadioButton radio_Plump;

//    RadioButton radio_ShortHair;
//    RadioButton radio_MediumHair;
//    RadioButton radio_LongHair;
//    String ShortHairChecked;
//    String MediumHairChecked;
//    String LongHairChecked;
    CheckBox checkbox_Permed;
    CheckBox checkbox_Dyed;
//    String PermedChecked;
//    String DyedChecked;

//    RadioButton radio_WithGlasses;
//    RadioButton radio_WithoutGlasses;
//    String WithGlassesChecked;
//    String WithoutGlassesChecked;

    private DBEngine dbEngine;
    private Member member;
    private int type;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);
        dbEngine = new DBEngine();
        setView();

        type = getIntent().getIntExtra("TYPE", ADD_MEMBER);
        if (type == ADD_MEMBER) {
            member = new Member();
        } else {
            member = dbEngine.selectOne(getIntent().getIntExtra("id", 0));
            setMemberValue();
        }
    }

    private void setMemberValue() {
        etName.setText(member.getName());
        etPhone.setText(member.getPhone());
        etEmail.setText(member.getEmail());
        etRelationship.setText(member.getRelationship());
        etEvent.setText(member.getEvent());
        etLocation.setText(member.getLocation());
        etYearMet.setText(member.getYearMet());
        etTopicDiscussed.setText(member.getTopicDiscussed());

        switch (member.getGender()){
            case GENDER_MALE:
                genderRadioGroup.check(R.id.radio_Male);
                break;
            case GENDER_FEMALE:
                genderRadioGroup.check(R.id.radio_Female);
                break;
            default:
                // do nothing
        }

//        seekbarHeight.setProgress(member.getHeight()-150);
        seekbarHeight.setProgress((member.getHeight()-150)*100/40);

        switch (member.getBodyShape()){
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

        switch (member.getHairLength()) {
            case HAIR_SHORT:
                hairLengthRadioGroup.check(R.id.radio_ShortHair);
                break;
            case HAIR_MEDIUM:
                hairLengthRadioGroup.check(R.id.radio_MediumHair);
                break;
            case HAIR_LONG:
                hairLengthRadioGroup.check(R.id.radio_LongHair);
                break;
            default:
                // do nothing
        }

        checkbox_Permed.setChecked(member.isPermed());
        checkbox_Dyed.setChecked(member.isDyed());

        switch (member.getGlasses()) {
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_edit, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //  save action
    public void saveClick(MenuItem item) {
        if (type == ADD_MEMBER) {
            member.setId(ADD_MEMBER);
        }

        member.setName(etName.getText().toString());
        member.setPhone(etPhone.getText().toString());
        member.setEmail(etEmail.getText().toString());
        member.setRelationship(etRelationship.getText().toString());
        member.setEvent(etEvent.getText().toString());
        member.setLocation(etLocation.getText().toString());
        member.setYearMet(etYearMet.getText().toString());
        member.setTopicDiscussed(etTopicDiscussed.getText().toString());
        member.setOther(etOther.getText().toString());

        switch (genderRadioGroup.getCheckedRadioButtonId()){
            case R.id.radio_Male:
                member.setGender(GENDER_MALE);
                break;
            case R.id.radio_Female:
                member.setGender(GENDER_FEMALE);
                break;
            default:
                member.setGender(GENDER_UNDEFINED);
        }

//        member.setHeight(150 + seekbarHeightProgress);
        member.setHeight((seekbarHeightProgress*40/100)+150);

        switch (bodyShapeRadioGroup.getCheckedRadioButtonId()){
            case R.id.radio_Thin:
                member.setBodyShape(BODY_THIN);
                break;
            case R.id.radio_Medium:
                member.setBodyShape(BODY_MEDIUM);
                break;
            case R.id.radio_Plump:
                member.setBodyShape(BODY_PLUMP);
                break;
            default:
                member.setBodyShape(BODY_UNDEFINED);
        }

        switch (hairLengthRadioGroup.getCheckedRadioButtonId()){
            case R.id.radio_ShortHair:
                member.setHairLength(HAIR_SHORT);
                break;
            case R.id.radio_MediumHair:
                member.setHairLength(HAIR_MEDIUM);
                break;
            case R.id.radio_LongHair:
                member.setHairLength(HAIR_LONG);
                break;
            default:
                member.setHairLength(HAIR_UNDEFINED);
        }

        member.setPermed(checkbox_Permed.isChecked() ? true : false);
        member.setDyed(checkbox_Dyed.isChecked() ? true : false);

        switch (glassesRadioGroup.getCheckedRadioButtonId()){
            case R.id.radio_WithGlasses:
                member.setGlasses(GLASSES_WITH);
                break;
            case R.id.radio_WithoutGlasses:
                member.setGlasses(GLASSES_WITHOUT);
                break;
            default:
                member.setGlasses(GLASSES_UNDEFINED);
        }

        dbEngine.editMember(member);
        finish();
/*
        String etNameContent = etName.getText().toString();
        String etPhoneContent = etPhone.getText().toString();
        String etEmailContent = etEmail.getText().toString();
        String etRelationshipContent = etRelationship.getText().toString();
        String etEventContent = etEvent.getText().toString();
        String etLocationContent = etLocation.getText().toString();
        String etYearMetContent = etYearMet.getText().toString();
        String etTopicDiscussedContent = etTopicDiscussed.getText().toString();
        String etOtherContent = etOther.getText().toString();

        MaleChecked = radio_Male.isChecked() ? "Y" : "N";
        FemaleChecked = radio_Female.isChecked() ? "Y" : "N";

        ThinChecked = radio_Thin.isChecked() ? "Y" : "N";
        MediumChecked = radio_Medium.isChecked() ? "Y" : "N";
        PlumpChecked = radio_Plump.isChecked() ? "Y" : "N";

        ShortHairChecked = radio_ShortHair.isChecked() ? "Y" : "N";
        MediumHairChecked = radio_MediumHair.isChecked() ? "Y" : "N";
        LongHairChecked = radio_LongHair.isChecked() ? "Y" : "N";
        PermedChecked = checkbox_Permed.isChecked() ? "Y" : "N";
        DyedChecked = checkbox_Dyed.isChecked() ? "Y" : "N";

        WithGlassesChecked = radio_WithGlasses.isChecked() ? "Y" : "N";
        WithoutGlassesChecked = radio_WithoutGlasses.isChecked() ? "Y" : "N";

        Log.d("DEGUG", "Name=" + etNameContent + ", Phone=" + etPhoneContent + ", Email=" + etEmailContent + ", Relationship=" + etRelationshipContent + ", Event=" + etEventContent + ", Location=" + etLocationContent + ", YearMet=" + etYearMetContent + ", TopicDiscussed=" + etTopicDiscussedContent + ", Height=" + seekbarHeightProgress);
        Log.d("DEBUG", "MaleChecked=" + MaleChecked + ", FemaleChecked=" + FemaleChecked);
        Log.d("DEBUG", "ThinChecked=" + ThinChecked + ", MediumChecked=" + MediumChecked + ", PlumpChecked=" + PlumpChecked);
        Log.d("DEBUG", "ShortHairChecked=" + ShortHairChecked + ", MediumHairChecked=" + MediumHairChecked + ", LongHairChecked=" + LongHairChecked + ", PermedChecked=" + PermedChecked + ", DyedChecked=" + DyedChecked);
        Log.d("DEBUG", "WithGlassesChecked=" + WithGlassesChecked + ", WithoutGlassesChecked=" + WithoutGlassesChecked);
        Log.d("DEBUG", "Other=" + etOtherContent);
*/
    }

    private void setView() {
        etName = (EditText) findViewById(R.id.etName);
        etPhone = (EditText) findViewById(R.id.etPhone);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etRelationship = (EditText) findViewById(R.id.etRelationship);
        etEvent = (EditText) findViewById(R.id.etEvent);
        etLocation = (EditText) findViewById(R.id.etLocation);
        etYearMet = (EditText) findViewById(R.id.etYearMet);
        etTopicDiscussed = (EditText) findViewById(R.id.etTopicDiscussed);
        etOther = (EditText) findViewById(R.id.etOther);

        genderRadioGroup = (RadioGroup) findViewById(R.id.GenderRadioGroup);
        bodyShapeRadioGroup = (RadioGroup) findViewById(R.id.BodyShapeRadioGroup);
        hairLengthRadioGroup = (RadioGroup) findViewById(R.id.HairLengthRadioGroup);
        glassesRadioGroup = (RadioGroup) findViewById(R.id.GlassesRadioGroup);

//        radio_Male = (RadioButton) findViewById(radio_Male);
//        radio_Female = (RadioButton) findViewById(radio_Female);
//
//        radio_Thin = (RadioButton) findViewById(R.id.radio_Thin);
//        radio_Medium = (RadioButton) findViewById(R.id.radio_Medium);
//        radio_Plump = (RadioButton) findViewById(R.id.radio_Plump);
//
//        radio_ShortHair = (RadioButton) findViewById(R.id.radio_ShortHair);
//        radio_MediumHair = (RadioButton) findViewById(R.id.radio_MediumHair);
//        radio_LongHair = (RadioButton) findViewById(radio_LongHair);
        checkbox_Permed = (CheckBox) findViewById(R.id.checkbox_Permed);
        checkbox_Dyed = (CheckBox) findViewById(R.id.checkbox_Dyed);

//        radio_WithGlasses = (RadioButton) findViewById(radio_WithGlasses);
//        radio_WithoutGlasses = (RadioButton) findViewById(radio_WithoutGlasses);

        seekbarHeight = (SeekBar) findViewById(R.id.seekbarHeight);
        seekbarHeight.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                seekbarHeightProgress = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }
}

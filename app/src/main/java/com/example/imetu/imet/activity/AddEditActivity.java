package com.example.imetu.imet.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.example.imetu.imet.database.DBEngine;
import com.example.imetu.imet.model.Member;
import com.example.imetu.imet.widget.ObservableScrollView;
import com.example.imetu.imet.R;
import com.xw.repo.BubbleSeekBar;

import static com.example.imetu.imet.widget.Util.ADD_MEMBER;
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

public class AddEditActivity extends AppCompatActivity {

    ObservableScrollView obsScrollView;
    EditText etName;
    EditText etPhone;
    EditText etEmail;
    EditText etRelationship;
    EditText etEvent;
    EditText etLocation;
    EditText etYearMet;
    EditText etTopicDiscussed;
    EditText etOther;
    BubbleSeekBar seekbarHeight;
    int seekbarHeightProgress = 165; //default

    RadioGroup genderRadioGroup;
    RadioGroup bodyShapeRadioGroup;
    RadioGroup hairLengthRadioGroup;
    RadioGroup glassesRadioGroup;
    CheckBox checkbox_Permed;
    CheckBox checkbox_Dyed;

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
            getSupportActionBar().setTitle("ADD NEW MEMBER");
        } else {
            member = dbEngine.selectOne(getIntent().getIntExtra("id", 0));
            getSupportActionBar().setTitle("EDIT MEMBER");
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

        seekbarHeight.setProgress(member.getHeight());

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

        member.setHeight(seekbarHeightProgress);

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

        checkbox_Permed = (CheckBox) findViewById(R.id.checkbox_Permed);
        checkbox_Dyed = (CheckBox) findViewById(R.id.checkbox_Dyed);

        seekbarHeight = (BubbleSeekBar) findViewById(R.id.seekbarHeight);
        seekbarHeight.correctOffsetWhenContainerOnScrolling();
        seekbarHeight.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(int progress, float progressFloat) {
                seekbarHeightProgress = progress;
                Log.d("Kelly", "" + seekbarHeightProgress);
            }

            @Override
            public void getProgressOnActionUp(int progress, float progressFloat) {

            }

            @Override
            public void getProgressOnFinally(int progress, float progressFloat) {

            }
        });

        obsScrollView = (ObservableScrollView) findViewById(R.id.activity_add_edit);

        obsScrollView.setOnScrollChangedListener(new ObservableScrollView.OnScrollChangedListener() {
            @Override
            public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
                seekbarHeight.correctOffsetWhenContainerOnScrolling();
            }
        });
    }
}

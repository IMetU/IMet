package com.example.imetu.imet.Activity;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.SeekBar;

import com.example.imetu.imet.R;

import java.util.Calendar;

import static com.example.imetu.imet.R.id.btnSave;
import static com.example.imetu.imet.R.id.etName;
import static com.example.imetu.imet.R.id.etOther;
import static com.example.imetu.imet.R.id.radio_Thin;

public class AddEditActivity extends AppCompatActivity {

    Button btnSave;
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
    String seekbarHeightProgress = "0"; //default

    RadioButton radio_Male;
    RadioButton radio_Female;
    String MaleChecked;
    String FemaleChecked;

    RadioButton radio_Thin;
    RadioButton radio_Medium;
    RadioButton radio_Plump;
    String ThinChecked;
    String MediumChecked;
    String PlumpChecked;

    RadioButton radio_ShortHair;
    RadioButton radio_MediumHair;
    RadioButton radio_LongHair;
    String ShortHairChecked;
    String MediumHairChecked;
    String LongHairChecked;
    CheckBox checkbox_Permed;
    CheckBox checkbox_Dyed;
    String PermedChecked;
    String DyedChecked;

    RadioButton radio_WithGlasses;
    RadioButton radio_WithoutGlasses;
    String WithGlassesChecked;
    String WithoutGlassesChecked;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);

        etName = (EditText) findViewById(R.id.etName);
        etPhone = (EditText) findViewById(R.id.etPhone);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etRelationship = (EditText) findViewById(R.id.etRelationship);
        etEvent = (EditText) findViewById(R.id.etEvent);
        etLocation = (EditText) findViewById(R.id.etLocation);
        etYearMet = (EditText) findViewById(R.id.etYearMet);
        etTopicDiscussed = (EditText) findViewById(R.id.etTopicDiscussed);
        etOther = (EditText) findViewById(R.id.etOther);

        radio_Male = (RadioButton) findViewById(R.id.radio_Male);
        radio_Female = (RadioButton) findViewById(R.id.radio_Female);

        radio_Thin = (RadioButton) findViewById(R.id.radio_Thin);
        radio_Medium = (RadioButton) findViewById(R.id.radio_Medium);
        radio_Plump = (RadioButton) findViewById(R.id.radio_Plump);

        radio_ShortHair = (RadioButton) findViewById(R.id.radio_ShortHair);
        radio_MediumHair = (RadioButton) findViewById(R.id.radio_MediumHair);
        radio_LongHair = (RadioButton) findViewById(R.id.radio_LongHair);
        checkbox_Permed = (CheckBox) findViewById(R.id.checkbox_Permed);
        checkbox_Dyed = (CheckBox) findViewById(R.id.checkbox_Dyed);

        radio_WithGlasses = (RadioButton) findViewById(R.id.radio_WithGlasses);
        radio_WithoutGlasses = (RadioButton) findViewById(R.id.radio_WithoutGlasses);

        seekbarHeight = (SeekBar) findViewById(R.id.seekbarHeight);
        seekbarHeight.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                seekbarHeightProgress = String.valueOf(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });


        btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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

            }
        });


    }
}

package com.example.imetu.imet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.example.imetu.imet.R;
import com.example.imetu.imet.widget.ObservableScrollView;
import com.xw.repo.BubbleSeekBar;

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

    RadioGroup genderRadioGroup;
    RadioGroup bodyShapeRadioGroup;
    RadioGroup hairLengthRadioGroup;
    RadioGroup glassesRadioGroup;
    CheckBox checkbox_Permed;
    CheckBox checkbox_Dyed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advance_search);

        getSupportActionBar().setTitle("Advance Search");
        setView();
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

        obsScrollView = (ObservableScrollView) findViewById(R.id.activity_advance_search);

        obsScrollView.setOnScrollChangedListener(new ObservableScrollView.OnScrollChangedListener() {
            @Override
            public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
                seekbarHeight.correctOffsetWhenContainerOnScrolling();
            }
        });
    }

    @Override
    public void onBackPressed() {
        // Prepare data intent
        Intent data = new Intent();
        data.putExtra("test", "It is return from Advance backPressed");

        // Activity finished ok, return the data
        setResult(RESULT_OK, data); // set result code and bundle data for response
        finish(); // closes the activity, pass data to parent
    }
}

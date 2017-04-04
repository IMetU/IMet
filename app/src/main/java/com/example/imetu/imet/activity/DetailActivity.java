package com.example.imetu.imet.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.imetu.imet.R;
import com.example.imetu.imet.adapter.SmartFragmentStatePagerAdapter;
import com.example.imetu.imet.database.DBEngine;
import com.example.imetu.imet.fragment.AppearanceFragment;
import com.example.imetu.imet.fragment.InformationFragment;
import com.example.imetu.imet.image.CircleTransform;
import com.example.imetu.imet.model.Member;

import static com.example.imetu.imet.widget.Util.EDIT_MEMBER;

public class DetailActivity extends AppCompatActivity {
    private Member member;
    ViewPager vpPager;
    private DBEngine dbEngine;
    private final int REQUEST_CODE_EDIT_MEMBER = 31;
    private boolean modifyFlag = false;

    private ImageView ivPhoto;
    private TextView tvName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //  get user info
        SharedPreferences mSettings = PreferenceManager.getDefaultSharedPreferences(this);
        String iMetUserId = mSettings.getString("iMetUserId", null);

        dbEngine = new DBEngine(iMetUserId);
        int id = getIntent().getIntExtra("id", 0);
        member = dbEngine.selectOne(id);

        // get viewpager
        vpPager = (ViewPager) findViewById(R.id.viewpager);
        // set the viewpager adapter for the pager
        vpPager.setAdapter(new DetailPagerAdapter(getSupportFragmentManager()));

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(vpPager);

        setPersonInfo();
    }

    private void setPersonInfo(){
        ivPhoto = (ImageView) findViewById(R.id.personImage);
        if(member.getImgPath() == null || member.getImgPath().equals("")){
            ivPhoto.setVisibility(View.GONE);
        }else {
            Glide.with(this).load(member.getImgPath()).transform(new CircleTransform(this)).into(ivPhoto);
        }
        tvName = (TextView) findViewById(R.id.personName);
        tvName.setText(member.getName());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuEdit:
                editClick();
                return true;
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return false;
        }
    }

    //  edit action
    public void editClick() {
        Intent intent = new Intent(DetailActivity.this, AddEditActivity.class);
        intent.putExtra("TYPE", EDIT_MEMBER);
        intent.putExtra("id", member.getId());
        startActivityForResult(intent, REQUEST_CODE_EDIT_MEMBER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_EDIT_MEMBER) {
            member = dbEngine.selectOne(member.getId());
            DetailPagerAdapter detailPagerAdapter = (DetailPagerAdapter) vpPager.getAdapter();
            InformationFragment informationFragment = (InformationFragment) detailPagerAdapter.getRegisteredFragment(0);
            if(informationFragment != null) {
                informationFragment.refresh(member);
            }
            AppearanceFragment appearanceFragment = (AppearanceFragment) detailPagerAdapter.getRegisteredFragment(1);
            if(appearanceFragment != null) {
                appearanceFragment.refresh(member);
            }
            setPersonInfo();
            modifyFlag = true;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if(savedInstanceState != null){
            int id = savedInstanceState.getInt("id");
            member = dbEngine.selectOne(id);
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("id", member.getId());
        super.onSaveInstanceState(outState);
    }

    //return the order of the fragment in the view pager
    public class DetailPagerAdapter extends SmartFragmentStatePagerAdapter {
        private String tabTitles[] = {"Information", "Appearance"};

        //Adapter gets the manager insert or remove fragment from activity
        public DetailPagerAdapter(FragmentManager fm){
            super(fm);
        }

        //The order and creation of fragments within the pager
        @Override
        public Fragment getItem(int position) {
            if(position ==0){
                return InformationFragment.newInstance(member);
            }else if(position ==1){
                return AppearanceFragment.newInstance(member);
            }else{
                return null;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

        @Override
        public int getCount() {
            return tabTitles.length;
        }
    }

    @Override
    public void onBackPressed() {
        if(modifyFlag) {
            setResult(RESULT_OK);
        }
        finish(); // closes the activity, pass data to parent
    }
}

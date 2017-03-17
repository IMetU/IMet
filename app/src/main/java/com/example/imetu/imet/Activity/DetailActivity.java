package com.example.imetu.imet.Activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.example.imetu.imet.DB.DBEngine;
import com.example.imetu.imet.Fragment.AppearanceFragment;
import com.example.imetu.imet.Fragment.InformationFragment;
import com.example.imetu.imet.Model.Member;
import com.example.imetu.imet.R;

public class DetailActivity extends AppCompatActivity {
    private Member member;
    ViewPager vpPager;
    private DBEngine dbEngine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //  get user info
        dbEngine = new DBEngine();
        int id = getIntent().getIntExtra("id", 0);
        member = dbEngine.selectOne(id);

        getSupportActionBar().setTitle(member.getName());

        // get viewpager
        vpPager = (ViewPager) findViewById(R.id.viewpager);
        // set the viewpager adapter for the pager
        vpPager.setAdapter(new DetailPagerAdapter(getSupportFragmentManager()));
        // find the sliding tabstrip
        PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        // attach the tabstrip to the viewer
        tabStrip.setViewPager(vpPager);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    //  edit action
    public void editClick(MenuItem item) {
        //  TODO: edit action
        Toast.makeText(this, "Click edit button", Toast.LENGTH_SHORT).show();
    }

    //return the order of the fragment in the view pager
    public class DetailPagerAdapter extends FragmentStatePagerAdapter {
        private String tabTitles[] = {"Information", "Appearance"};

        //Adapter gets the manager insert or remove fragment from activity
        public DetailPagerAdapter(FragmentManager fm){
            super(fm);
        }

        //The order and creation of fragments within the pager
        @Override
        public Fragment getItem(int position) {
            if(position ==0){
                return new InformationFragment(member);
            }else if(position ==1){
                return new AppearanceFragment(member);
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
}

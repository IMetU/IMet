package com.example.imetu.imet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.imetu.imet.R;
import com.example.imetu.imet.adapter.MemberListAdapter;
import com.example.imetu.imet.database.DBEngine;
import com.example.imetu.imet.fragment.DeleteDialogFragment;
import com.example.imetu.imet.fragment.FilterFragment;
import com.example.imetu.imet.fragment.FilterFragment.FilterAdvanceSearchListener;
import com.example.imetu.imet.model.Member;
import com.example.imetu.imet.model.MemberFilter;

import org.parceler.Parcels;

import java.util.ArrayList;

import static com.example.imetu.imet.widget.Util.ADD_MEMBER;
import static com.example.imetu.imet.widget.Util.REQUEST_ADVANCE_SEARCH;

public class MainActivity extends BaseActivity implements FilterFragment.FilterSearchListener, FilterAdvanceSearchListener{
    private final int REQUEST_CODE_ADVANCE_SEARCH = 21;
    private final int REQUEST_CODE_ADD_MEMBER = 22;
    private final int REQUEST_CODE_DETAIL_VIEW = 23;

    private ListView lvMemberList;
    private ArrayList<Member> memberArrayList;
    private MemberListAdapter memberArrayAdapter;
    private FloatingActionButton fabAddMember;
    private DBEngine dbEngine;
    private MemberFilter memberFilter;
    private String query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbEngine = new DBEngine();
        memberFilter = new MemberFilter();
        lvMemberList = (ListView)findViewById(R.id.lvMemberList);
        memberArrayList = new ArrayList<>();

        //  long click event
        lvMemberList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Member member = memberArrayAdapter.getItem(position);
                FragmentManager fm = getSupportFragmentManager();
                DeleteDialogFragment alertDialog = DeleteDialogFragment.newInstance(member);
                alertDialog.show(fm, "fragment_alert");
                return true;
            }
        });
        //  item click event
        lvMemberList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // view detail
                Member member = memberArrayAdapter.getItem(position);
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra("id", member.getId());
                startActivityForResult(intent, REQUEST_CODE_DETAIL_VIEW);
            }
        });

        fabAddMember = (FloatingActionButton)findViewById(R.id.fabAddMember);
        fabAddMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  add member event
                Intent intent = new Intent(MainActivity.this, AddEditActivity.class);
                intent.putExtra("TYPE", ADD_MEMBER);
                startActivityForResult(intent, REQUEST_CODE_ADD_MEMBER);
            }
        });

        //set Data
        getAllDataSetView();
    }

    private void getAllDataSetView() {
        //  Import fake data to arraylist
//        memberArrayList = FakeData.CreateFakeMemberList();
        memberArrayList = dbEngine.selectAll();
        //  init memberArrayAdapter
        memberArrayAdapter = new MemberListAdapter(this, memberArrayList);
        //  set adapter to listview
        lvMemberList.setAdapter(memberArrayAdapter);
    }

    //  MenuBar Init
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem searchItem = menu.findItem(R.id.menuSearch);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //  query submit action
                memberArrayList = dbEngine.simpleQuery(query);
                //  init memberArrayAdapter
                memberArrayAdapter = new MemberListAdapter(MainActivity.this, memberArrayList);
                //  set adapter to listview
                lvMemberList.setAdapter(memberArrayAdapter);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                query = newText;
                return false;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                getAllDataSetView();
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    //  filter action
    public void filterClick(MenuItem item) {
        //  filter action: showFilterDialog()
        FragmentManager fm = getSupportFragmentManager();
        FilterFragment filterFragment = FilterFragment.newInstance(memberFilter);
        filterFragment.show(fm, "Filter");
    }

    // reset action
    public void resetClick(MenuItem item) {
        getAllDataSetView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_ADVANCE_SEARCH){

            MemberFilter mf = (MemberFilter) Parcels.unwrap(data.getParcelableExtra("memberFilter"));
            mf.setFilterOrAdvance(REQUEST_ADVANCE_SEARCH);

            memberArrayList = dbEngine.fullQuery(mf);
            //  init memberArrayAdapter
            memberArrayAdapter = new MemberListAdapter(MainActivity.this, memberArrayList);
            //  set adapter to listview
            lvMemberList.setAdapter(memberArrayAdapter);

            memberFilter.setName(mf.getName());
            memberFilter.setRelationship(mf.getRelationship());
            memberFilter.setEvent(mf.getEvent());
            memberFilter.setGender(mf.getGender());
            memberFilter.setHeight(mf.getHeight());
            memberFilter.setBodyShape(mf.getBodyShape());
            memberFilter.setGlasses(mf.getGlasses());

        }else if(resultCode == RESULT_OK && requestCode == REQUEST_CODE_ADD_MEMBER){
            getAllDataSetView();
        }else if(resultCode == RESULT_OK && requestCode == REQUEST_CODE_DETAIL_VIEW){
            getAllDataSetView();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("query", query);
        super.onSaveInstanceState(outState);
    }

    //  delete action
    public void deleteMember(Member member) {
        dbEngine.deleteMember(member.getId());
        getAllDataSetView();
    }

    @Override
    public void onFilterAdvanceSearch(MemberFilter mf) {
        Intent intent = new Intent(MainActivity.this, AdvanceSearchActivity.class);
        intent.putExtra("memberFilter" , Parcels.wrap(mf));
        startActivityForResult(intent, REQUEST_CODE_ADVANCE_SEARCH);
    }

    @Override
    public void onFilterSearch(MemberFilter mf) {
        memberFilter = mf;
        memberArrayList = dbEngine.fullQuery(memberFilter);
        //  init memberArrayAdapter
        memberArrayAdapter = new MemberListAdapter(MainActivity.this, memberArrayList);
        //  set adapter to listview
        lvMemberList.setAdapter(memberArrayAdapter);
    }
}

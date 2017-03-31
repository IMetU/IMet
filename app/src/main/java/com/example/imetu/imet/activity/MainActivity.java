package com.example.imetu.imet.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.imetu.imet.R;
import com.example.imetu.imet.adapter.MemberListAdapter;
import com.example.imetu.imet.adapter.MemberListRvAdapter;
import com.example.imetu.imet.database.DBEngine;
import com.example.imetu.imet.fragment.DeleteDialogFragment;
import com.example.imetu.imet.fragment.FilterFragment;
import com.example.imetu.imet.fragment.FilterFragment.FilterAdvanceSearchListener;
import com.example.imetu.imet.model.Member;
import com.example.imetu.imet.model.MemberFilter;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;
import static com.example.imetu.imet.widget.Util.ADD_MEMBER;
import static com.example.imetu.imet.widget.Util.REQUEST_ADVANCE_SEARCH;

public class MainActivity extends BaseActivity implements FilterFragment.FilterSearchListener, FilterAdvanceSearchListener{
    private final int REQUEST_CODE_ADVANCE_SEARCH = 21;
    private final int REQUEST_CODE_ADD_MEMBER = 22;
    private final int REQUEST_CODE_DETAIL_VIEW = 23;
    private final String SEARCH_KEY = "search";

    private RecyclerView rvMemberList;
    private ArrayList<Member> memberArrayList;
//    private MemberListAdapter memberArrayAdapter;
    private MemberListRvAdapter memberListRvAdapter;
    private FloatingActionButton fabAddMember;
    private SearchView searchView;
    private DBEngine dbEngine;
    private MemberFilter memberFilter;
    private String query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences mSettings = PreferenceManager.getDefaultSharedPreferences(this);
        String iMetUserId = mSettings.getString("iMetUserId", null);

        if(iMetUserId == null){
            Log.d("IMet", "iMetUserId is null");
            SharedPreferences.Editor editor = mSettings.edit();
            String username = UUID.randomUUID().toString();
            editor.putString("iMetUserId", username);
            editor.apply();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbEngine = new DBEngine(iMetUserId);
        memberFilter = new MemberFilter();
        rvMemberList = (RecyclerView) findViewById(R.id.rvMemberList);
        rvMemberList.setHasFixedSize(true);
        final GridLayoutManager layout = new GridLayoutManager(MainActivity.this, 2);
        rvMemberList.setLayoutManager(layout);
        memberArrayList = new ArrayList<>();

//
//        //  long click event
//        lvMemberList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                Member member = memberArrayAdapter.getItem(position);
//                FragmentManager fm = getSupportFragmentManager();
//                DeleteDialogFragment alertDialog = DeleteDialogFragment.newInstance(member);
//                alertDialog.show(fm, "fragment_alert");
//                return true;
//            }
//        });
//        //  item click event
//        lvMemberList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                // view detail
//                Member member = memberArrayAdapter.getItem(position);
//                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
//                intent.putExtra("id", member.getId());
//                startActivityForResult(intent, REQUEST_CODE_DETAIL_VIEW);
//            }
//        });

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
        if (savedInstanceState != null){
            query = savedInstanceState.getString(SEARCH_KEY);
        }
        //set Data
        getAllDataSetView();
    }

    private void getAllDataSetView() {
        //  Import fake data to arraylist
//        memberArrayList = FakeData.CreateFakeMemberList();
        memberArrayList = dbEngine.selectAll();
        //  init memberArrayAdapter
//        memberArrayAdapter = new MemberListAdapter(this, memberArrayList);
        memberListRvAdapter = new MemberListRvAdapter(this, memberArrayList);
        //  set adapter to listview
        rvMemberList.setAdapter(memberListRvAdapter);

    }

    //  MenuBar Init
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem searchItem = menu.findItem(R.id.menuSearch);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        if (query != null && !query.isEmpty()){
            searchView.onActionViewExpanded();
            searchView.setQuery(query, true);
            searchView.setFocusable(true);
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //  query submit action
                memberArrayList = dbEngine.simpleQuery(query);
                //  init memberArrayAdapter
                memberListRvAdapter = new MemberListRvAdapter(MainActivity.this, memberArrayList);
                //  set adapter to listview
                rvMemberList.setAdapter(memberListRvAdapter);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
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
            memberListRvAdapter = new MemberListRvAdapter(MainActivity.this, memberArrayList);
            //  set adapter to listview
            rvMemberList.setAdapter(memberListRvAdapter);

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
        query = searchView.getQuery().toString();
        outState.putString(SEARCH_KEY, query);
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
        memberListRvAdapter = new MemberListRvAdapter(MainActivity.this, memberArrayList);
        //  set adapter to listview
        rvMemberList.setAdapter(memberListRvAdapter);
    }
}

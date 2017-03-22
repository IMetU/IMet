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
import android.widget.Toast;

import com.example.imetu.imet.adapter.MemberListAdapter;
import com.example.imetu.imet.database.DBEngine;
import com.example.imetu.imet.fragment.DeleteDialogFragment;
import com.example.imetu.imet.fragment.FilterFragment;
import com.example.imetu.imet.fragment.FilterFragment.FilterAdvanceSearchListener;
import com.example.imetu.imet.model.Member;
import com.example.imetu.imet.model.MemberFilter;
import com.example.imetu.imet.R;

import java.util.ArrayList;

import static com.example.imetu.imet.widget.Util.ADD_MEMBER;

public class MainActivity extends BaseActivity implements FilterFragment.FilterSearchListener, FilterAdvanceSearchListener{
    private final int REQUEST_CODE_ADVANCE_SEARCH = 21;
    private ListView lvMemberList;
    private ArrayList<Member> memberArrayList;
    private MemberListAdapter memberArrayAdapter;
    private FloatingActionButton fabAddMember;
    private DBEngine dbEngine;
    private MemberFilter memberFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbEngine = new DBEngine();
        memberFilter = new MemberFilter();

        lvMemberList = (ListView)findViewById(R.id.lvMemberList);
        memberArrayList = new ArrayList<>();

        //  setView
//        setView();
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
                //  TODO: view activity
                Member member = memberArrayAdapter.getItem(position);
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra("id", member.getId());
                startActivity(intent);

                Toast.makeText(getApplicationContext(), member.getName() + " is select", Toast.LENGTH_SHORT).show();
            }
        });

        fabAddMember = (FloatingActionButton)findViewById(R.id.fabAddMember);
        fabAddMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  TODO:add member event
                Intent intent = new Intent(MainActivity.this, AddEditActivity.class);
                intent.putExtra("TYPE", ADD_MEMBER);
                startActivity(intent);
                Toast.makeText(getApplicationContext(), "Add Member Event", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        setView();
        super.onResume();
    }

    //  TODO:setView
    private void setView() {
        //  Import fake data to arraylist
//        memberArrayList = FakeData.CreateFakeMemberList();

        syncData();
        //  init memberArrayAdapter
        memberArrayAdapter = new MemberListAdapter(this, memberArrayList);
        //  set adapter to listview
        lvMemberList.setAdapter(memberArrayAdapter);
    }

    private void syncData() {
        memberArrayList = dbEngine.selectAll();
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
                //  TODO:query submit action
                Toast.makeText(getBaseContext(), query, Toast.LENGTH_SHORT).show();
                memberArrayList = dbEngine.simpleQuery(query);
                //  init memberArrayAdapter
                memberArrayAdapter = new MemberListAdapter(MainActivity.this, memberArrayList);
                //  set adapter to listview
                lvMemberList.setAdapter(memberArrayAdapter);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //  TODO:query text change action
                return false;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                setView();
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    //  filter action
    public void filterClick(MenuItem item) {
        //  TODO: filter action
        Toast.makeText(this, "Click filter button", Toast.LENGTH_SHORT).show();
        showFilterDialog();
    }

    private void showFilterDialog() {
        FragmentManager fm = getSupportFragmentManager();
        FilterFragment filterFragment = FilterFragment.newInstance(memberFilter);
        filterFragment.show(fm, "Filter");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_ADVANCE_SEARCH){
            String textForTest = data.getExtras().getString("test");
            Toast.makeText(this, textForTest, Toast.LENGTH_SHORT).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //  delete action
    public void deleteMember(Member member) {
        dbEngine.deleteMember(member.getId());
        setView();
    }

    @Override
    public void onFilterAdvanceSearch(MemberFilter mf) {
        Toast.makeText(this, "Return From AdvanceSearch", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MainActivity.this, AdvanceSearchActivity.class);
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
        Toast.makeText(this, "Return From FilterSearch", Toast.LENGTH_SHORT).show();
    }
}

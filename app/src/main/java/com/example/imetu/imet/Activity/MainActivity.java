package com.example.imetu.imet.Activity;

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

import com.example.imetu.imet.Adapter.MemberListAdapter;
import com.example.imetu.imet.DB.DBEngine;
import com.example.imetu.imet.Fragment.DeleteDialogFragment;
import com.example.imetu.imet.Fragment.FilterFragment;
import com.example.imetu.imet.Fragment.FilterFragment.FilterAdvanceSearchListener;
import com.example.imetu.imet.Model.FakeData;
import com.example.imetu.imet.Model.Member;
import com.example.imetu.imet.Model.MemberFilter;
import com.example.imetu.imet.R;

import org.parceler.Parcels;

import java.util.ArrayList;

public class MainActivity extends BaseActivity implements FilterFragment.FilterSearchListener, FilterAdvanceSearchListener{
    private final int REQUEST_CODE_ADDMEMBER = 20;
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
                intent.putExtra("member", Parcels.wrap(member));
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
                startActivityForResult(intent, REQUEST_CODE_ADDMEMBER);
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
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //  TODO:query text change action
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
        FilterFragment filterFragment = FilterFragment.newInstance("Filter");
        filterFragment.show(fm, "Filter");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_ADDMEMBER){
//            Member member = (Member) Parcels.unwrap(data.getParcelableExtra("member"));
//            memberArrayList.add(member);
//            memberArrayAdapter.notifyDataSetChanged();
        } else if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_ADVANCE_SEARCH){
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
    public void onFilterAdvanceSearch(MemberFilter memberFilter) {
        Toast.makeText(this, "Return From AdvanceSearch", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MainActivity.this, AdvanceSearchActivity.class);
        startActivityForResult(intent, REQUEST_CODE_ADVANCE_SEARCH);
    }

    @Override
    public void onFilterSearch(MemberFilter memberFilter) {
        Toast.makeText(this, "Return From FilterSearch", Toast.LENGTH_SHORT).show();
    }
}

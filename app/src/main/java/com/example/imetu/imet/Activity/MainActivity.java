package com.example.imetu.imet.Activity;

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
import com.example.imetu.imet.Fragment.DeleteDialogFragment;
import com.example.imetu.imet.Model.FakeData;
import com.example.imetu.imet.Model.Member;
import com.example.imetu.imet.R;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {
    private ListView lvMemberList;
    private ArrayList<Member> memberArrayList;
    private MemberListAdapter memberArrayAdapter;
    private FloatingActionButton fabAddMember;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //  setView
        setView();
    }
    //  TODO:setView
    private void setView() {
        lvMemberList = (ListView)findViewById(R.id.lvMemberList);
        //  Import fake data to arraylist
        memberArrayList = FakeData.CreateFakeMemberList();
        //  init memberArrayAdapter
        memberArrayAdapter = new MemberListAdapter(this, memberArrayList);
        //  set adapter to listview
        lvMemberList.setAdapter(memberArrayAdapter);
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
                Toast.makeText(getApplicationContext(), member.getName() + " is select", Toast.LENGTH_SHORT).show();
            }
        });

        fabAddMember = (FloatingActionButton)findViewById(R.id.fabAddMember);
        fabAddMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  TODO:add member event
                Toast.makeText(getApplicationContext(), "Add Member Event", Toast.LENGTH_SHORT).show();
            }
        });
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
    }
}

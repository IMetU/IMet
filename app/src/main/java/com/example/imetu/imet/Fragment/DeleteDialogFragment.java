package com.example.imetu.imet.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.example.imetu.imet.DB.DBEngine;
import com.example.imetu.imet.Model.Member;

import org.parceler.Parcels;



public class DeleteDialogFragment extends DialogFragment {
    private DBEngine dbEngine;
    //  constructor
    public DeleteDialogFragment() {
    }
    public static DeleteDialogFragment newInstance(Member member){
        DeleteDialogFragment fragment = new DeleteDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable("member" , Parcels.wrap(member));
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Member member = (Member) Parcels.unwrap(getArguments().getParcelable("member"));
        dbEngine = new DBEngine();
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle("Delete");
        alertDialogBuilder.setMessage("Please make sure you want to delete " + member.getName() + "'s profile?");
        alertDialogBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //  TODO:Delete action
                dbEngine.deleteMember(member.getId());
                Toast.makeText(getActivity(), "Delete action start", Toast.LENGTH_SHORT).show();
            }
        });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //  TODO:Back without delete
                Toast.makeText(getActivity(), "Delete action cancel", Toast.LENGTH_SHORT).show();
            }
        });
        return alertDialogBuilder.create();
    }

}

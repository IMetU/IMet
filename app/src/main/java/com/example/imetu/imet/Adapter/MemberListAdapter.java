package com.example.imetu.imet.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.imetu.imet.Model.Member;
import com.example.imetu.imet.R;

import java.util.List;



public class MemberListAdapter extends ArrayAdapter<Member> {
    public MemberListAdapter(Context context, List<Member> objects) {
        super(context, 0, objects);
    }
    //  TODO:getview method
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Member member = getItem(position);
        ViewHolder viewHolder;
        //  TODO:if convertview null, inflate a layout and create a new viewholder
        if (convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_memberlist, parent, false);
            viewHolder.ivPhoto = (ImageView)convertView.findViewById(R.id.ivPhoto);
            viewHolder.tvName = (TextView)convertView.findViewById(R.id.tvName);
            viewHolder.tvPhoneNumber = (TextView)convertView.findViewById(R.id.tvPhoneNumber);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }
        //  TODO:Set ImageView and Textview form data Model
        if (member.getImgPath() != null){
            //  TODO:Picasso action
        }
        if (member.getName() != null){
            viewHolder.tvName.setText(member.getName());
        }
        if (member.getPhone() != null){
            viewHolder.tvPhoneNumber.setText(member.getPhone());
        }
        return convertView;
    }

    //  TODO:Viewholder
    public static class ViewHolder{
        ImageView ivPhoto;
        TextView tvName;
        TextView tvPhoneNumber;
    }
}

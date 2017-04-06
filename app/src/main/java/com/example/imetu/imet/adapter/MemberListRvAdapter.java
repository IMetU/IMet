package com.example.imetu.imet.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.imetu.imet.R;
import com.example.imetu.imet.model.Member;

import java.util.ArrayList;


public class MemberListRvAdapter extends RecyclerView.Adapter<MemberListRvAdapter.ViewHolder> {
    private Activity mContext;
    private ArrayList<Member> mMember;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView ivPhoto;
        public TextView tvName;
        public TextView tvEvent;
        public View rootView;
        public ImageView ivLocation;
        public ViewHolder(View itemView){
            super(itemView);
            rootView = itemView;
            ivPhoto = (ImageView)itemView.findViewById(R.id.ivProfile);
            tvName = (TextView)itemView.findViewById(R.id.tvName);
            tvEvent = (TextView)itemView.findViewById(R.id.tvEvent);
            ivLocation = (ImageView)itemView.findViewById(R.id.ivLocation);
        }
    }

    public MemberListRvAdapter(Activity Context, ArrayList<Member> members){
        mContext = Context;
        if (members == null){
            throw new IllegalArgumentException("contacts must not be null");
        }
        mMember = new ArrayList<>();
        mMember = members;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_memeberlist1, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Member member = mMember.get(position);
        holder.rootView.setTag(member);
        holder.tvName.setText(member.getName());
        if (member.getEvent().length() != 0){
            holder.tvEvent.setText(member.getEvent());
            holder.ivLocation.setVisibility(View.GONE);
        }else if ( member.getLocation().length() != 0){
            holder.tvEvent.setText(member.getLocation());
            holder.ivLocation.setVisibility(View.VISIBLE);
        }else{
            holder.ivLocation.setVisibility(View.GONE);
        }

        if (member.getImgPath() != null){
            Glide.with(mContext).load(member.getImgPath()).into(holder.ivPhoto);
        }else{
            Glide.with(mContext).load(R.mipmap.default_person).into(holder.ivPhoto);
        }

    }

    @Override
    public int getItemCount() {
        return mMember.size();
    }
}

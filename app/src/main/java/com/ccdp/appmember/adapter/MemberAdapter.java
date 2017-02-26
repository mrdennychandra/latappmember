package com.ccdp.appmember.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ccdp.appmember.Constants;
import com.ccdp.appmember.R;
import com.ccdp.appmember.model.Member;

import java.util.ArrayList;

public class MemberAdapter extends ArrayAdapter<Member> {

    private ArrayList<Member> dataset;
    Context context;

    public MemberAdapter(Context context, int resource, ArrayList<Member> objects) {
        super(context, resource, objects);
        this.context = context;
        this.dataset = objects;
    }

    //View Holder
    private static class ViewHolder {
        TextView txtId;
        TextView txtMemberName;
        TextView txtMemberEmail;
        TextView txtMemberBirthDate;
        TextView txtMemberSex;
        TextView txtMemberReligion;
        TextView txtMemberAddress;
        ImageView   photo;
    }

    //getView
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        Member member = getItem(position);

        viewHolder = new ViewHolder();
        LayoutInflater inflater = LayoutInflater.from(getContext());
        convertView = inflater.inflate(R.layout.row_item, parent, false);
        viewHolder.txtId = (TextView) convertView.findViewById(R.id.id);
        viewHolder.txtMemberName = (TextView) convertView.findViewById(R.id.member_name);
        viewHolder.txtMemberEmail = (TextView) convertView.findViewById(R.id.member_email);
        viewHolder.txtMemberBirthDate = (TextView) convertView.findViewById(R.id.member_birthdate);
        viewHolder.txtMemberSex = (TextView) convertView.findViewById(R.id.member_sex);
        viewHolder.txtMemberAddress = (TextView) convertView.findViewById(R.id.member_address);
        viewHolder.txtMemberReligion = (TextView) convertView.findViewById(R.id.member_religion);
        viewHolder.photo = (ImageView) convertView.findViewById(R.id.photo);

        try {
            String memberId = String.valueOf(member.getId());
            viewHolder.txtId.setText(memberId);
            viewHolder.txtMemberName.setText(member.getMemberName());
            viewHolder.txtMemberEmail.setText(member.getMemberEmail());
            viewHolder.txtMemberBirthDate.setText(member.getMemberBirthDate());
            viewHolder.txtMemberSex.setText(member.getMemberSex());
            viewHolder.txtMemberAddress.setText(member.getMemberAddress());
            viewHolder.txtMemberReligion.setText(member.getMemberReligion());
            String photoUrl = Constants.BASE_ASSETS+member.getId()+".jpg";
            //https://inthecheesefactory.com/blog/get-to-know-glide-recommended-by-google/en
            Glide.with(context)
                    .load(photoUrl)
                    .error(R.mipmap.ic_launcher)
                    .override(150, 150)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(viewHolder.photo);
        }catch(Exception e){
            e.printStackTrace();
        }

        return convertView;
    }

}

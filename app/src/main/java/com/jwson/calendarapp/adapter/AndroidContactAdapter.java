package com.jwson.calendarapp.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jwson.calendarapp.R;
import com.jwson.calendarapp.activity.AddFriendActivity;

import java.util.List;

/**
 * Created by jason_000 on 05/02/2017.
 */

public class AndroidContactAdapter extends BaseAdapter {

    Context mContext;
    List<AddFriendActivity.AndroidContacts> mContactList;

    public AndroidContactAdapter(Context mContext, List<AddFriendActivity.AndroidContacts> mContact) {
        this.mContext = mContext;
        this.mContactList = mContact;
    }

    @Override
    public int getCount() {
        return mContactList.size();
    }

    @Override
    public Object getItem(int position) {
        return mContactList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = View.inflate(mContext, R.layout.item_phone_contact, null);
        TextView contactNameTextView = (TextView) view.findViewById(R.id.phone_userName);
        TextView phoneNumTextView = (TextView) view.findViewById(R.id.phone_number);
        String name = mContactList.get(position).contactName;
        String phone = mContactList.get(position).phoneNum;

        contactNameTextView.setText(mContactList.get(position).contactName);
        phoneNumTextView.setText(mContactList.get(position).phoneNum);

        view.setTag(mContactList.get(position).contactName);
        return view;
    }
}
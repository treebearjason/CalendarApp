package com.jwson.calendarapp.adapter;

/**
 * Created by user on 9/26/2016.
 */
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.firebase.ui.FirebaseListAdapter;
import com.jwson.calendarapp.R;
import com.jwson.calendarapp.domain.User;
import com.jwson.calendarapp.utils.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FriendAdapter extends ArrayAdapter<User> {
    private static final String LOG_TAG = FriendAdapter.class.getSimpleName();

    private List<User> userList;

    public FriendAdapter(Context context,  int textViewResourceId,
                         List<User> userList ){
        super(context,textViewResourceId,userList);
        this.userList = new ArrayList<User>();
        this.userList.addAll(userList);

        System.out.println("=============="+userList.size());    }

    private class ViewHolder{
        TextView userName;
        CheckBox checkBox;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        Log.v("Convert View", String.valueOf(position));

        final boolean[] isChecked = {false};
        if(convertView == null){
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.single_user_item, null);

            holder = new ViewHolder();
            holder.userName = (TextView) convertView.findViewById(R.id.userName);
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);

            convertView.setTag(holder);

            holder.checkBox.setOnClickListener( new View.OnClickListener() {
                public void onClick(View v) {
                    CheckBox cb = (CheckBox) v ;
                    User user = (User) cb.getTag();
                    Toast.makeText(getContext(),
                            "Clicked on Checkbox: " + cb.getText() +
                                    " is " + cb.isChecked(),
                            Toast.LENGTH_LONG).show();
                  isChecked[0] = true;
                }
            });


        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        User user = userList.get(position);
        holder.userName.setText(user.getEmail());
        holder.checkBox.setChecked(isChecked[0]);

        return convertView;
    }

}


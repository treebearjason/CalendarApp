package com.jwson.calendarapp.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jwson.calendarapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationFragment extends Fragment {

    List<String> notificationList = new ArrayList<String>();

    public static NotificationFragment newInstance() {
        NotificationFragment fragment = new NotificationFragment();
        return fragment;
    }

    public NotificationFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        populateNotificationList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        populateNotificationListView();
    }

    private void populateNotificationListView() {
        ArrayAdapter<String> adapter = new MyNotificationAdapter();
        ListView list = (ListView) getView().findViewById(R.id.notificationListView);
        list.setAdapter(adapter);
    }

    private class MyNotificationAdapter extends ArrayAdapter<String> {
        public MyNotificationAdapter() {
            super(getActivity(), R.layout.item_notification, notificationList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = convertView;
            if (itemView == null) {
                itemView = getActivity().getLayoutInflater().inflate(R.layout.item_notification, parent, false);
            }
            //Find the event to work with
            String message = notificationList.get(position);

            TextView notificationText = (TextView) itemView.findViewById(R.id.notification_message);
            notificationText.setText(message);

            return itemView;
        }
    }

    private void populateNotificationList(){
        notificationList.add("Mit created an event on 10th June - Mum's birthday");
        notificationList.add("Jason left a note on the event - Mum's birthday");
        notificationList.add("Mit added a photo on the event - Hiking");
        notificationList.add("Mit created an event on 18th June - Project Meeting");
    }

}

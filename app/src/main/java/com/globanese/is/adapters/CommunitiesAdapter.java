package com.globanese.is.adapters;

/**
 * Created by walaa on 27/6/2016.
 */


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.globanese.is.R;
import com.globanese.is.model.CommunityObject;

import java.util.ArrayList;


public class CommunitiesAdapter extends ArrayAdapter<CommunityObject> {
    private Context context;
    ArrayList<CommunityObject> items;
    int resource;
    private int selected_index = -1;

    public CommunitiesAdapter(Context context, int resource, ArrayList<CommunityObject> items) {
        super(context, resource, items);
        this.context = context;
        this.items = items;
        this.resource = resource;
    }


    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflater.inflate(resource, null);
        }

        TextView textView = (TextView) convertView.findViewById(R.id.text);
        textView.setText(items.get(position).getName());

        if (selected_index == position) {
            textView.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        } else

        {

            textView.setTextColor(context.getResources().getColor(R.color.text_gray));


        }

        return convertView;

    }

    @Override
    public int getCount() {
        return items.size();
    }


    @Override
    public long getItemId(int position) {
        return 0;
    }


    public int getSelected_index() {
        return selected_index;
    }

    public void setSelected_index(int selected_index) {
        this.selected_index = selected_index;
        notifyDataSetChanged();
    }
}

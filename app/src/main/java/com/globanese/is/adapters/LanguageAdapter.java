package com.globanese.is.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.globanese.is.R;
import com.globanese.is.model.LanguageObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by akshay on 1/2/15.
 */

public class LanguageAdapter extends ArrayAdapter<LanguageObject> {

    Context context;
    int resource, textViewResourceId;
    List<LanguageObject> items, tempItems, suggestions;

    public LanguageAdapter(Context context
            , int resource, int textViewResourceId
            , List<LanguageObject> items) {
        super((Context) context, resource, textViewResourceId, items);
        this.context = (Context) context;
        this.resource = resource;
        this.textViewResourceId = textViewResourceId;
        this.items = items;
        tempItems = new ArrayList<LanguageObject>(items); // this makes the difference.
        suggestions = new ArrayList<LanguageObject>();

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_item_search, parent, false);
        }

        LanguageObject people = items.get(position);
        if (people != null) {
            TextView lblName = (TextView) view.findViewById(R.id.list_item);
            if (lblName != null)
                lblName.setText(people.getName());
        }
        return view;
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    /**
     * Custom Filter implementation for custom suggestions we provide.
     */
    Filter nameFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            String str = ((LanguageObject) resultValue).getName();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (LanguageObject people : tempItems) {
                    if (people.getName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        suggestions.add(people);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            List<LanguageObject> filterList = (ArrayList<LanguageObject>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (LanguageObject people : filterList) {
                    add(people);
                    notifyDataSetChanged();
                }
            }
        }
    };
}
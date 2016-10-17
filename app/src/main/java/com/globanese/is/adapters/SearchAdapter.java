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
import com.globanese.is.model.SearchObject;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by akshay on 1/2/15.
 */

public class SearchAdapter extends ArrayAdapter<SearchObject> {

    Context context;
    int resource, textViewResourceId;
    List<SearchObject> items, tempItems, suggestions;

    public SearchAdapter(Context context
            , int resource, int textViewResourceId
            , List<SearchObject> items) {
        super((Context) context, resource, textViewResourceId, items);
        this.context = (Context) context;
        this.resource = resource;
        this.textViewResourceId = textViewResourceId;
        this.items = items;
        tempItems = new ArrayList<SearchObject>(items); // this makes the difference.
        suggestions = new ArrayList<SearchObject>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_item_search_friend, parent, false);
        }
        SearchObject people = items.get(position);
        if (people != null) {
            TextView name = (TextView) view.findViewById(R.id.prfile_name);
            TextView country = (TextView) view.findViewById(R.id.profile_country);
            CircleImageView cimage = (CircleImageView) view.findViewById(R.id.profile_image);
            name.setText(people.getPrfile_name());
            country.setText(people.getProfile_country());
            Picasso.with(context).load(people.getProfile_pic()).into(cimage);

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
            String str = ((SearchObject) resultValue).getPrfile_name();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (SearchObject people : tempItems) {
                    if (people.getPrfile_name().toLowerCase().contains(constraint.toString().toLowerCase())) {
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
            List<SearchObject> filterList = (ArrayList<SearchObject>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (SearchObject people : filterList) {
                    add(people);
                    notifyDataSetChanged();
                }
            }
        }
    };
}
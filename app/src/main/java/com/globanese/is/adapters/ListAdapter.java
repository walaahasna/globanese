package com.globanese.is.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.globanese.is.R;
import com.globanese.is.model.NationalityObject;

import java.util.List;

public class ListAdapter extends BaseAdapter {


    public static List<NationalityObject> product;

    public static Context context;

    public ListAdapter(Context context, List<NationalityObject> product) {
        this.context = context;
        this.product= product;
    }



    @Override
    public int getCount() {
        return product.size();
    }

    @Override
    public Object getItem(int position) {
        return product.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder mViewHolder;

        ImageLoader imageLoader = null;
        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_search, parent, false);
            mViewHolder = new MyViewHolder(convertView);
            convertView.setTag(mViewHolder);
        } else {

            mViewHolder = (MyViewHolder) convertView.getTag();
        }

        NationalityObject s = product.get(position);

        mViewHolder.tv1.setText(s.getName());



        return convertView;
    }

    private class MyViewHolder {
        TextView tv1,tv2;

        public MyViewHolder(View item) {
            tv1 = (TextView)item.findViewById(R.id.list_item);

        }
    }
}
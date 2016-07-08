package com.globanese.is.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.globanese.is.R;

/**
 * Created by walaa on 31/05/16.
 */
public class FragmentLanguage extends Fragment{
    TextView addresstitle;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=  inflater.inflate(R.layout.fragment_community, container, false);

        return v;
    }


    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //addresstitle=(TextView)this.getView().findViewById(R.id.address_title);

    }

}

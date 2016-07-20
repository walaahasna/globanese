package com.globanese.is.fragments;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.globanese.is.R;

public class HorizontalDropDownIconMenu extends AppCompatActivity {

    LinearLayout androidDropDownMenuIconItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.floating_menu);

        androidDropDownMenuIconItem = (LinearLayout) findViewById(R.id.horizontal_dropdown_icon_menu_items);
    }

    public void horizontalDropDownIconMenu(View view) {
        if (androidDropDownMenuIconItem.getVisibility() == View.VISIBLE) {
            androidDropDownMenuIconItem.setVisibility(View.INVISIBLE);
        } else {
            androidDropDownMenuIconItem.setVisibility(View.VISIBLE);
        }
    }

    public void menuItemClick(View view) {
        androidDropDownMenuIconItem.setVisibility(View.INVISIBLE);
    }
}
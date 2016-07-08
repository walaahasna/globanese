package com.globanese.is.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.Toast;

import com.globanese.is.R;
import com.globanese.is.adapters.PlacesAutoCompleteAdapter;

public class LocationActivity extends AppCompatActivity {
    AutoCompleteTextView  actv;
    ImageView cancel,back;
    String description;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        actv = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView1);
        cancel=  (ImageView)findViewById(R.id.cancel);
        back=  (ImageView)findViewById(R.id.back);




        actv.setAdapter(new PlacesAutoCompleteAdapter(getApplicationContext(), R.layout.autocomplete_list_item));
        actv.setThreshold(1);







        actv .setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

              description = (String) parent.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(),description,Toast.LENGTH_SHORT).show();

            }
        });



cancel.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        actv.setText(null);
    }
});



back.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent myIntent = new Intent(LocationActivity.this, WritePostActivity.class);
        myIntent.putExtra("loaction",description);
        startActivity(myIntent);
    }
});

    }
}

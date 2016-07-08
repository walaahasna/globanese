package com.globanese.is.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.globanese.is.R;
import com.globanese.is.adapters.MyRecyclerViewAdapt;
import com.globanese.is.model.AddFriendObject;

import java.util.ArrayList;
import java.util.List;

public class HorizantalRecycleView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_add_friend);
      List<AddFriendObject> AddFreinds=new ArrayList<AddFriendObject>();
      AddFriendObject a=new AddFriendObject();
        a.setName("Craig Lopez");
        a.setPic(R.drawable.profile);

      AddFriendObject a1=new AddFriendObject();
        a1.setName("Craig Lopez");
        a1.setPic(R.drawable.profile);

      AddFriendObject a2=new AddFriendObject();
        a2.setName("Craig Lopez");
        a2.setPic(R.drawable.profile);
      AddFriendObject a3=new AddFriendObject();
        a3.setName("Craig Lopez");
        a3.setPic(R.drawable.profile);
      AddFriendObject a4=new AddFriendObject();
        a4.setName("Craig Lopez");
        a4.setPic(R.drawable.profile);
      AddFreinds.add(a);
      AddFreinds.add(a1);
      AddFreinds.add(a2);
      AddFreinds.add(a3);
      AddFreinds.add(a4);
        final RecyclerView rv = (RecyclerView)findViewById(R.id.recyclerView);
        MyRecyclerViewAdapt  adapter = new MyRecyclerViewAdapt(getApplicationContext(),AddFreinds);

        rv.setAdapter(adapter);

        DefaultItemAnimator animator = new DefaultItemAnimator();
        animator.setRemoveDuration(2000);
        animator.setAddDuration(2000);
        rv.setItemAnimator(animator);
        final LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());


        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv.setLayoutManager(manager);

    }
}

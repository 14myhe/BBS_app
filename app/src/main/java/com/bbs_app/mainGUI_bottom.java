package com.bbs_app;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

/**
 * Created by Nicole on 2016/11/22.
 */
public class mainGUI_bottom extends RelativeLayout{
    public mainGUI_bottom(final Context context, AttributeSet attrs)
    {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.bottom, this);
        Button home=(Button)findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "home", Toast.LENGTH_SHORT).show();
            }
        });

        Button collection=(Button)findViewById(R.id.collection);
        collection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "collection", Toast.LENGTH_SHORT).show();

            }
        });

        Button set=(Button)findViewById(R.id.set);
        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "set", Toast.LENGTH_SHORT).show();
            }
        });

    }

}

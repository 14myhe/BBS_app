package com.bbs_app;

/**
 * Created by Nicole on 2016/11/22.
 */

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;


public class mainGUI_title extends LinearLayout {
    public mainGUI_title(final Context context, AttributeSet attrs)
    {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.title,this);
        Button message=(Button)findViewById(R.id.message);
        message.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "message", Toast.LENGTH_SHORT).show();

            }
        });

    }

}

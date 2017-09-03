package com.bbs_app;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.bbs_app.card_part.Main_cardAdapter;
import com.bbs_app.card_part.Main_cardcls;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nicole on 2016/11/25.
 */
public class My_collection extends Activity {
    List<Main_cardcls> title_list=new ArrayList<Main_cardcls>();
    public void onCreate(Bundle savedInstancedState)
    {
        super.onCreate(savedInstancedState);
        setContentView(R.layout.collection_layout);
        initCard();
        Main_cardAdapter adapter=new Main_cardAdapter(My_collection.this,R.layout.main_show_four_card,title_list);
        ListView col_list=(ListView)findViewById(R.id.collection_list);
        col_list.setAdapter(adapter);

    }
    public void initCard(){
        Main_cardcls cardcls1=new Main_cardcls("1","阿迪达斯","699.0元","2016-12-05 18:29:06");
        title_list.add(cardcls1);

        Main_cardcls cardcls2=new Main_cardcls("2","甜品","9.0元","2016-12-06 14:29:06");
        title_list.add(cardcls2);


        }



    }


package com.bbs_app.card_part;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bbs_app.R;
import com.bbs_app.Show_card_content;

import java.util.List;

/**
 * Created by Nicole on 2016/12/16.
 */
public class NoImageCardAdapter extends ArrayAdapter<Cardcls>{
    private int resourceId;
    public NoImageCardAdapter(Context context,int textViewResourceId,List<Cardcls> objects)
    {
        super(context,textViewResourceId,objects);
        resourceId=textViewResourceId;
    }

    public View getView(int position,View convertView,ViewGroup parent)
    {
        Cardcls cardcls=getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        TextView sname=(TextView)view.findViewById(R.id.show_title);
        TextView sprice=(TextView)view.findViewById(R.id.shopprice);
        TextView coommitime=(TextView)view.findViewById(R.id.add_shop_time);
        TextView collection=(TextView)view.findViewById(R.id.collection_card);
        sname.setText(cardcls.getShopname());
        sprice.setText(cardcls.getShopprice());
        coommitime.setText(cardcls.getCommittime());
        collection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"收藏成功",Toast.LENGTH_SHORT).show();
            }

        });

        return view;
    }
    public String getName(String name)
    {
        return name;
    }
    public boolean areAllItemsEnabled()
    {
        // all items are separator
        return true;
    }

    @Override
    public boolean isEnabled(int position)
    {
        // all items are separator
        return true;
    }


}



package com.bbs_app.card_part;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bbs_app.R;

import java.util.List;

/**
 * Created by Nicole on 2016/11/26.
 */
public class CardAdapter extends ArrayAdapter<Cardcls> {
    private int resourceId;
    public CardAdapter(Context context,int textViewResourceId,List<Cardcls> objects)
    {
        super(context,textViewResourceId,objects);
        resourceId=textViewResourceId;
    }

    public View getView(int position,View convertView,ViewGroup parent)
    {
        Cardcls cardcls=getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        ImageView shopimg=(ImageView)view.findViewById(R.id.shopimg);
        TextView sname=(TextView)view.findViewById(R.id.show_title);
        TextView sprice=(TextView)view.findViewById(R.id.shopprice);
        TextView coommitime=(TextView)view.findViewById(R.id.add_shop_time);
        shopimg.setImageResource(cardcls.getImageId());
        sname.setText(cardcls.getShopname());
        sprice.setText(cardcls.getShopprice());
        coommitime.setText(cardcls.getCommittime());

        return view;
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

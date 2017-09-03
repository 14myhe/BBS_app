package com.bbs_app.card_part;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bbs_app.R;

import java.util.List;

/**
 * Created by Nicole on 2016/12/16.
 */
public class Show_card_detail  extends ArrayAdapter<Detail_cardcls> {
    private int resourceId;
    public Show_card_detail(Context context,int textViewResourceId,List<Detail_cardcls> objects)
    {
        super(context,textViewResourceId,objects);
        resourceId=textViewResourceId;
    }

    public View getView(int position,View convertView,ViewGroup parent)
    {
        Detail_cardcls detail_cardcls=getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        ImageView shopimg=(ImageView)view.findViewById(R.id.shopimg);
        TextView sname=(TextView)view.findViewById(R.id.show_title);
        TextView sprice=(TextView)view.findViewById(R.id.shopprice);
        TextView coommitime=(TextView)view.findViewById(R.id.add_shop_time);
        TextView descrip=(TextView)view.findViewById(R.id.shop_des);
        TextView user=(TextView)view.findViewById(R.id.show_username);


        user.setText(detail_cardcls.getPoster());
        sname.setText(detail_cardcls.getShopname());
        sprice.setText(detail_cardcls.getShopprice());
        descrip.setText(detail_cardcls.getDescrip());
        coommitime.setText(detail_cardcls.getCommittime());
        shopimg.setImageResource(detail_cardcls.getImageId());



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

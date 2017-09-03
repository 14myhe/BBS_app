package com.bbs_app.card_part;

/**
 * Created by Nicole on 2016/11/26.
 */
public class Cardcls {

    //表名
    public static final String TABLE="card";

    //表的各域名
    public static final String KEY_title="title";
    public static final String KEY_content="content";
    public static final String KEY_clickcount="clickcount";
    public static final String KEY_committime="committime";


    public String delete="删除";
    public String shopname;//商品名称
    public String shopprice;
    public int clickcount=0;//帖子的点击率
    public String committime;//贴子发表的时间
    public int imageId;//图片的id;




    public Cardcls(String shopname,String shopprice,String committime)
    {
        this.shopname=shopname;
        this.shopprice=shopprice;
        this.committime=committime;


    }
    public void setClickcount(int clickcount)
    {
        this.clickcount=clickcount;
    }

    public String getShopname()
    {
        return shopname;
    }
    public String getShopprice(){
        return shopprice;
    }
    public String getCommittime()
    {
        return committime;
    }
    public int getImageId()
    {
        return imageId;
    }


    public String getDelete()
    {
        return delete;
    }
    public int getClickcount()
    {
        return clickcount;
    }

}


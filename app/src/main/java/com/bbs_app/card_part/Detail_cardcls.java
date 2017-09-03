package com.bbs_app.card_part;

/**
 * Created by Nicole on 2016/12/17.
 */
public class Detail_cardcls {

    public String delete="删除";
    public String poster;//发布者
    public String shopname;//商品名称
    public String shopprice;//商品价格
    public int clickcount=0;//帖子的点击率
    public String committime;//贴子发表的时间
    public int imageId;//图片的id;
    public String descrip;//商品详细描述




    public Detail_cardcls (String poster,String shopname,String shopprice,String descrip,String committime)
    {
        this.poster=poster;
        this.shopname=shopname;
        this.shopprice=shopprice;
        this.committime=committime;
        this.descrip=descrip;


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
    public String getPoster(){
        return poster;
    }
    public String getDescrip()
    {
        return descrip;
    }

}

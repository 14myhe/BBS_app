package com.bbs_app;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;

/**
 * Created by Nicole on 2016/11/25.
 * //显示帖子的具体内容
 */
public class Show_card_content extends Activity{

    //实现搜索功能
    SQLiteDatabase db;
    public static final int success=0;
    public static final int fail=1;

    private Handler handler=new Handler(){

        public void handleMessage(Message msg)
        {
            String tip=(String)msg.obj;
            switch (msg.what){
                case success:
                    //处理可能的结果
                   // Toast.makeText(Show_card_content.this, "寻找成功", Toast.LENGTH_SHORT).show();
                    //解析json,并将结果然后显示在listcview上
                    parseJSONWithJSONObject(tip);

                    break;

                case fail:
                    Toast.makeText(Show_card_content.this, "找不到相关产品", Toast.LENGTH_SHORT).show();

                    break;
                default:break;




            }
        }

    };
    public void onCreate(Bundle savedInstancedState)
    {
        super.onCreate(savedInstancedState);
        setContentView(R.layout.show_card_content);

        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        String key=bundle.getString("key");
        sendRequestWithHttpURLConnection(key);

    }



    private void sendRequestWithHttpURLConnection(final String shopkey)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection=null;
                try{
                    //按照商品的关键词来查询
                    String str="http://192.168.191.1:8084/SecondhandWebsiteWithSH/shoppingQuery.do?column=id&value="+shopkey;
                    URL url=new URL(str);
                    connection=(HttpURLConnection)url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    InputStream in=connection.getInputStream();
                    BufferedReader reader=new BufferedReader(new InputStreamReader(in));
                    StringBuilder response=new StringBuilder();
                    String line;
                    while((line =reader.readLine())!=null)
                    {
                        //原始数据
                        response.append(line);
                    }
                    Message message = new Message();
                    //将服务器返回的结果存放到message
                    message.obj = response.toString();
                    String tip=(String) message.obj;
                    if(tip.equalsIgnoreCase("[]"))
                    {
                        message.what=fail;


                    }
                    else {
                        message.what=success;
                        // String jsonData=response.toString();
                        //parseJSONWithJSONObject(jsonData);

                    }
                    handler.sendMessage(message);

                }catch (Exception e)
                {
                    e.printStackTrace();
                }finally {
                    if(connection!=null)
                    {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }
    //解析搜索商品的返回值
    private void parseJSONWithJSONObject(String jsonData)
    {
        try{
            JSONArray jsonArray=new JSONArray(jsonData);
            for(int i=0;i<6;i++)
            {
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                String id=jsonObject.getString("id");
                String username=jsonObject.getString("userid");
                String name=jsonObject.getString("name");
                String price=jsonObject.getString("price");
                String des=jsonObject.getString("descrip");
                //转换时间格式
                JSONObject timeall=jsonObject.getJSONObject("time");
                long time=timeall.getLong("time");
                //调时差
                long finaltime=time+480 * 60 * 1000;
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String dateString = formatter.format(finaltime);
                View view=findViewById(R.id.show_card_content);
                ImageView shopimg=(ImageView)view.findViewById(R.id.shopimg);
                TextView sname=(TextView)view.findViewById(R.id.show_title);
                TextView sprice=(TextView)view.findViewById(R.id.shopprice);
                TextView coommitime=(TextView)view.findViewById(R.id.add_shop_time);
                TextView descrip=(TextView)view.findViewById(R.id.shop_des);
                TextView user=(TextView)view.findViewById(R.id.show_username);

                user.setText(username);
                sname.setText(name);
                sprice.setText(price);
                descrip.setText(des);
                coommitime.setText(dateString);

            }






        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public void onBackPressed()
    {
        Show_card_content.this.finish();
    }





}

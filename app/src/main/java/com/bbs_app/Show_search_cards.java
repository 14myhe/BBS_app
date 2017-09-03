package com.bbs_app;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.bbs_app.card_part.Main_cardAdapter;
import com.bbs_app.card_part.Main_cardcls;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nicole on 2016/11/25.
 */
public class Show_search_cards extends Activity {
    //实现搜索功能
    SQLiteDatabase db;
    public static final int success=0;
    public static final int fail=1;
    final List<Main_cardcls> title_list=new ArrayList<Main_cardcls>();

    private Handler handler=new Handler(){

        public void handleMessage(Message msg)
        {
            String tip=(String)msg.obj;
            switch (msg.what){
                case success:
                   //处理可能的结果
                    Toast.makeText(Show_search_cards.this, "寻找成功", Toast.LENGTH_SHORT).show();
                    //解析json,并将结果然后显示在listcview上
                    parseJSONWithJSONObject(tip);

                    break;

                case fail:
                    Toast.makeText(Show_search_cards.this, "找不到相关产品", Toast.LENGTH_SHORT).show();

                    break;
                default:break;




            }
        }

    };
    public void onCreate(Bundle savedInstancedState)
    {
        super.onCreate(savedInstancedState);
        setContentView(R.layout.show_search_cards);
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        String key=bundle.getString("key");
       // searchList(key);
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
                    String str="http://192.168.191.1:8084/SecondhandWebsiteWithSH/shoppingQuery.do?"+"column=name&value="+shopkey+"&pageindex=1";
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
           for(int i=0;i<jsonArray.length();i++)
           {
               JSONObject jsonObject=jsonArray.getJSONObject(i);
               String id=jsonObject.getString("id");
               String name=jsonObject.getString("name");
               String price=jsonObject.getString("price");
               //转换时间格式
               JSONObject timeall=jsonObject.getJSONObject("time");
               long time=timeall.getLong("time");
               //调时差
               long finaltime=time+480 * 60 * 1000;
               SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
               String dateString = formatter.format(finaltime);

               Main_cardcls cardcls1=new Main_cardcls(id,name,price,dateString);
               title_list.add(cardcls1);

           }
           Main_cardAdapter adapter=new Main_cardAdapter(Show_search_cards.this,R.layout.main_show_four_card,title_list);
           ListView show_search_cards=(ListView)findViewById(R.id.show_search_cards);
           show_search_cards.setAdapter(adapter);
           show_search_cards.setOnItemClickListener(new AdapterView.OnItemClickListener() {
               @Override
               public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                   Main_cardcls c=title_list.get(position);
                   String select_id=c.getId();
                   Bundle bundle=new Bundle();
                   bundle.putString("key",select_id);
                   Intent intent=new Intent(Show_search_cards.this,Show_card_content.class);
                   intent.putExtra("key",select_id);
                   startActivity(intent);
               }
           });

       }catch (Exception e)
       {
           e.printStackTrace();
       }
  }
public void onBackPressed()
{
    Show_search_cards.this.finish();
}

}

package com.bbs_app;

/*先从简单的出发，后期再实现活动的效果
* */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
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

public class MainActivity extends Activity {
    public static final int success=0;
    public static final int fail=1;
    private Context mContext = null;
    EditText editText;

    private Handler handler=new Handler(){

        public void handleMessage(Message msg)
        {
            String tip=(String)msg.obj;
            switch (msg.what){
                case success:
                   // Toast.makeText(MainActivity.this, "有数据", Toast.LENGTH_SHORT).show();
                  //  TextView editText=(TextView)findViewById(R.id.contentall);
                   // editText.setText(tip);
                    //解析json,并将结果然后显示在listcview上
                   parseJSONWithJSONObject(tip);

                    break;

                case fail:
                    Toast.makeText(MainActivity.this, "快来抢沙发", Toast.LENGTH_SHORT).show();

                    break;
                default:break;




            }
        }

    };
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
        sendRequestWithHttpURLConnection();

//获取Title的组件
        View main_title = (View) findViewById(R.id.main_title);
        Button message_title = (Button) main_title.findViewById(R.id.message);
        message_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ListViewActivity.class);
                startActivity(intent);


            }
        });
        Button refresh=(Button)main_title.findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendRequestWithHttpURLConnection();
            }
        });

//先获取搜索框的Layout
        View search=(View)findViewById(R.id.searchtext);
        final EditText editText=(EditText)findViewById(R.id.Search_text);
        editText.setOnEditorActionListener(new EditText.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                //先隐藏键盘
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    ((InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(MainActivity.this
                                    .getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);

                    //把关键字传递到Show_search_cards活动
                    String editkey=editText.getText().toString();
                    Bundle bundle=new Bundle();
                    bundle.putString("key",editkey);
                    Intent intent=new Intent(MainActivity.this,Show_search_cards.class);
                    intent.putExtra("key",editkey);
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });

        //获得bottom的组件
        View main_bottom=(View)findViewById(R.id.main_bottom);

        Button login=(Button)findViewById(R.id.home);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, User_Login.class);
                startActivity(intent);

            }
        });
        Button home=(Button)main_bottom.findViewById(R.id.collection);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        Button setting=(Button)main_bottom.findViewById(R.id.set);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, personalInformationActivity.class);
                startActivity(intent);
            }
        });


    }

    private void sendRequestWithHttpURLConnection()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection=null;
                try{
                    //按照商品的关键词来查询
                    String str="http://192.168.191.1:8084/SecondhandWebsiteWithSH/shoppingQuery.do?";
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
        final List<Main_cardcls> title_list=new ArrayList<Main_cardcls>();
        final List<String> list=new ArrayList<String>();
        try{
            JSONArray jsonArray=new JSONArray(jsonData);
            //显示前六条帖子的内容
            for(int i=0;i<6;i++)
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
                //list.add(name);


            }
            Main_cardAdapter adapter=new Main_cardAdapter(MainActivity.this,R.layout.main_show_four_card,title_list);
            final ListView news_list=(ListView)findViewById(R.id.new_list_layout).findViewById(R.id.new_list);
            news_list.setAdapter(adapter);
            news_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Main_cardcls c=title_list.get(position);
                    String select_id=c.getId();
                    Bundle bundle=new Bundle();
                    bundle.putString("key",select_id);
                    Intent intent=new Intent(MainActivity.this,Show_card_content.class);
                    intent.putExtra("key",select_id);
                    startActivity(intent);

                }
            });

           // ArrayAdapter<String> adapter=new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1,list);
          //  news_list.setAdapter(adapter);

        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

public void onBackPressed()
{
   finish();
}



}

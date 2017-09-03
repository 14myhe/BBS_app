package com.bbs_app;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.content.Intent;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class  personalInformationActivity extends Activity {
    private Button bg1;
    private Button bg2;
    private Button bg3;
    private Button bg4;
    public static final int SHOW_success=0;
    public static final int SHOW_fail=1;
    private Handler handler=new Handler(){

        public void handleMessage(Message msg)
        {
            String tip=(String)msg.obj;
            switch (msg.what){
                case SHOW_success:
                    Toast.makeText(personalInformationActivity.this, "注销成功", Toast.LENGTH_SHORT).show();
                    //跳转到主页面
                    Intent intent=new Intent(personalInformationActivity.this,MainActivity.class);
                    startActivity(intent);
                    personalInformationActivity.this.finish();
                    break;

                case SHOW_fail:
                    Toast.makeText(personalInformationActivity.this, "注销失败", Toast.LENGTH_SHORT).show();
                    break;
                default:break;




            }
        }

    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personalinformation);
        bg1 = (Button)this.findViewById(R.id.button9);
        bg2 = (Button)this.findViewById(R.id.button12);
        bg3 = (Button)this.findViewById(R.id.button11);
        bg4 = (Button)this.findViewById(R.id.button15);
        Button logout=(Button)findViewById(R.id.user_logout);

        bg1.setOnClickListener(new OnClickListener(){
             public void onClick(View v){
                 Intent intent = new Intent( personalInformationActivity.this,ResetusernamesActivity.class);
                 startActivity(intent);
                 personalInformationActivity.this.finish();
             }
        });

        bg2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(personalInformationActivity.this,User_post_card.class);
                startActivity(intent);
                personalInformationActivity.this.finish();
            }
        });

        bg3.setOnClickListener(new OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent( personalInformationActivity.this,ResetpasswordActivity.class);
                startActivity(intent);
                personalInformationActivity.this.finish();
            }
        });

        bg4.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        //注销账号
        logout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent=new Intent(personalInformationActivity.this,logoff.class);
                    startActivity(intent);
                   personalInformationActivity.this.finish();

            }
        });
    }
    private void sendRequestWithHttpURLConnection(final String id)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection=null;
                try{
                    String str="http://192.168.191.1:8084/SecondhandWebsiteWithSHuserDelete.do?id="+id;

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
                        response.append(line);
                    }
                    Message message = new Message();
                    //将服务器返回的结果存放到message
                    message.obj = response.toString();
                    String tip=(String) message.obj;
                    if(tip.equalsIgnoreCase("success"))
                    {
                        message.what=SHOW_success;
                    }
                    else if(tip.equalsIgnoreCase("fail"))
                    {
                        message.what=SHOW_fail;
                    }
                    else {
                        ;
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
}

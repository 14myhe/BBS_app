package com.bbs_app;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Nicole on 2016/12/18.
 */
public class logoff extends Activity {
    public static final int SHOW_success=0;
    public static final int SHOW_fail=1;
    private final String TAG = "User_Login";

    private Handler handler=new Handler(){

        public void handleMessage(Message msg)
        {
            String tip=(String)msg.obj;
            switch (msg.what){
                case SHOW_success:

                    Toast.makeText(logoff.this, "注销成功", Toast.LENGTH_SHORT).show();
                    logoff.this.finish();
                    break;

                case SHOW_fail:
                    Toast.makeText(logoff.this, "注销失败，请重试", Toast.LENGTH_SHORT).show();
                    break;
                default:break;




            }
        }

    };
    public void onCreate(Bundle saveInstancedState)
    {
        super.onCreate(saveInstancedState);
        setContentView(R.layout.logout_layout);

        Button logoff=(Button)findViewById(R.id.ensure_logoff);
        logoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取输入账号名和密码
                EditText account_edit=(EditText)findViewById(R.id.logout_account);
                EditText password_edit=(EditText)findViewById(R.id.logout_pwd);
                String account=account_edit.getText().toString();
                String password=password_edit.getText().toString();
                sendRequestWithHttpURLConnection(account);

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
                    String str="http://192.168.191.1:8084/SecondhandWebsiteWithSH//userDelete.do?"+"id="+id;
                    //String str="http:/10.0.2.2:8084/SecondhandWebsiteWithSH/login.do?"+"id="+id+"&psw="+password;
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

package com.bbs_app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;

import java.util.List;

import org.apache.http.Header;
import org.apache.http.cookie.Cookie;

/**
 * Created by Nicole on 2016/11/12.
 */
public class User_Login extends Activity {
    public static final int SHOW_success=0;
    public static final int SHOW_fail=1;
    private final String TAG = "User_Login";
    String username_s;

   /*   private Handler handler=new Handler(){

        public void handleMessage(Message msg)
        {
            String tip=(String)msg.obj;
            switch (msg.what){
                case SHOW_success:
                    Toast.makeText(User_Login.this, "登录成功", Toast.LENGTH_SHORT).show();
                    //跳转到主页面
                    Intent intent=new Intent(User_Login.this,MainActivity.class);
                    startActivity(intent);
                    User_Login.this.finish();
                    break;

                case SHOW_fail:
                    Toast.makeText(User_Login.this, "登录失败", Toast.LENGTH_SHORT).show();
                    //提示是否要重新登陆
                    relogin();
                    break;
                default:break;




            }
        }

    };
    public void onCreate(Bundle savedInstancedState)
    {


        super.onCreate(savedInstancedState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login);

        //点击免费注册按钮跳转到注册页面
        View view=(View)findViewById(R.id.login_table);

        Button register=(Button)view.findViewById(R.id.register_btn);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(User_Login.this, User_Register.class);
                startActivity(intent1);
            }
        });


        Button submit=(Button)findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获得用户的输入
                EditText account_login = (EditText) findViewById(R.id.account);
                EditText password_login = (EditText) findViewById(R.id.password);
                String account = account_login.getText().toString();
                String password = password_login.getText().toString();

               //getUserByUsername(account, password);

                sendRequestWithHttpURLConnection(account,password);
             //   login(account,password);



            }

        });
    }



  private void sendRequestWithHttpURLConnection(final String id,final String password)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection=null;
                try{
                  String str="http://192.168.191.1:8084/SecondhandWebsiteWithSH/login.do?"+"id="+id+"&psw="+password;
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

    public void relogin()
    {
        AlertDialog.Builder alertDialog=new AlertDialog.Builder(User_Login.this);
        //注册成功后询问是否现在登录
        alertDialog.setTitle("登录失败");
        alertDialog.setMessage("是否重新登录?");
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent1 = new Intent(User_Login.this, User_Login.class);
                startActivity(intent1);

            }
        });
        alertDialog.setNegativeButton("否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               finish();

            }
        });
        alertDialog.show();
    }*/


    public void onCreate(Bundle savedInstancedState)
    {


        super.onCreate(savedInstancedState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login);

        //点击免费注册按钮跳转到注册页面
        View view=(View)findViewById(R.id.login_table);

        Button register=(Button)view.findViewById(R.id.register_btn);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(User_Login.this, User_Register.class);
                startActivity(intent1);
            }
        });


        Button submit=(Button)findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获得用户的输入
                EditText account_login = (EditText) findViewById(R.id.account);
                EditText password_login = (EditText) findViewById(R.id.password);
                String account = account_login.getText().toString();
                String password = password_login.getText().toString();
                username_s=account;
                //sendRequestWithHttpURLConnection(account,password);
                login(account,password);



            }

        });
    }

    private void login(String username, String password){
        String url="http://192.168.191.1:8084/SecondhandWebsiteWithSH/login.do?"+"id="+username+"&psw="+password;
        final AsyncHttpClient client = new AsyncHttpClient();

        //保存cookie，自动保存到了shareprefercece
        PersistentCookieStore myCookieStore = new PersistentCookieStore(User_Login.this);
        client.setCookieStore(myCookieStore);
        client.post(url, new AsyncHttpResponseHandler() {

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  byte[] errorResponse, Throwable e) {
                Log.e(TAG, "获取数据异常 ", e);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                String json = new String(response);
                Log.d(TAG, "onSuccess json = " + json);
                //测试获得已经保存的cookie
                Toast.makeText(getApplication(), "登录成功，cookie=" + getCookieText(), Toast.LENGTH_SHORT).show();

                SharedPreferences sharedPreferences = getSharedPreferences("Users",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("id",username_s);
                editor.commit();

                startActivity(new Intent(User_Login.this, MainActivity.class));

            }

        });

    }


    /**
     * 获取标准 Cookie
     */
    private String getCookieText() {
        PersistentCookieStore myCookieStore = new PersistentCookieStore(User_Login.this);
        List<Cookie> cookies = myCookieStore.getCookies();
        Log.d(TAG, "cookies.size() = " + cookies.size());
        Util Util=new Util();
        Util.setCookies(cookies);
        for (Cookie cookie : cookies) {
            Log.d(TAG, cookie.getName() + " = " + cookie.getValue());
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < cookies.size(); i++) {
            Cookie cookie = cookies.get(i);
            String cookieName = cookie.getName();
            String cookieValue = cookie.getValue();
            if (!TextUtils.isEmpty(cookieName)
                    && !TextUtils.isEmpty(cookieValue)) {
                sb.append(cookieName + "=");
                sb.append(cookieValue + ";");
            }
        }
        Log.e("cookie", sb.toString());
        return sb.toString();
    }
}






package com.bbs_app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;
import android.os.Handler;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



/**
 * 注册页面
 * Created by Nicole on 2016/11/22.
 */
public class User_Register extends Activity implements View.OnClickListener{
    private int flag=1;
    SQLiteDatabase db;
    public static final int SHOW_Register=0;
    public static final int SHOW_account=1;
    public static final int SHOW_username=2;

    private Handler handler=new Handler(){

        public void handleMessage(Message msg)
        {
            String tip=(String)msg.obj;
            switch (msg.what){
                case SHOW_Register:
                    Toast.makeText(User_Register.this, "注册成功", Toast.LENGTH_SHORT).show();
                    loginnow();
                    break;

                case SHOW_account:
                    Toast.makeText(User_Register.this, "该邮箱注册过", Toast.LENGTH_SHORT).show();
                    break;

                case SHOW_username:
                    Toast.makeText(User_Register.this, "该用户名已被注册", Toast.LENGTH_SHORT).show();
                    break;
                default:break;




            }
        }

    };


    public void onCreate(Bundle savedInstancedState) {

        super.onCreate(savedInstancedState);
        setContentView(R.layout.register);
        Button submit = (Button) findViewById(R.id.register_submit);
        submit.setOnClickListener(this);
    }
    public void onClick(View v) {
                //获得用户的各个属性的输入
                EditText account = (EditText) findViewById(R.id.account);
                EditText password = (EditText) findViewById(R.id.password);
                EditText username=(EditText)findViewById(R.id.username);
                RadioButton gender_male=(RadioButton)findViewById(R.id.male);
                RadioButton gender_female=(RadioButton)findViewById(R.id.female);
                final String uaccount = account.getText().toString();
                final String upassword = password.getText().toString();
                final String uusername=username.getText().toString();
                final String gender=gender_male.isChecked()?"MALE":"FEMALE";
                //判断各项是否符合要求
                if(isId(uaccount)) {
                    if (upassword.length() >= 6 && upassword.length() <= 8) {
                        if (uusername.length() >= 2 && uusername.length() <= 8) {
                            ;
                        } else {
                            flag = 0;
                            Toast.makeText(getApplicationContext(), "昵称长度错误", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        flag = 0;
                        Toast.makeText(getApplicationContext(), "密码长度错误", Toast.LENGTH_SHORT).show();

                    }
                }
                else {
                    flag=0;
                    Toast.makeText(getApplicationContext(),"学号格式错误",Toast.LENGTH_SHORT).show();
                }

                //各项数据符合要求，传到服务器端进行存储
           if(flag==1) {
                  sendRequestWithHttpURLConnection(uaccount,uusername,upassword,gender);
               // sendRequestWithHttpURLConnection("13heaj@","13daf","123456","FEMALE");

           }


    }

    //验证用户名（学号）
    public boolean isId(String strId) {
     //  String strPattern = ".*@stu.edu.cn$";
        String strPattern="[0-9]{10}$";
        Pattern p = Pattern.compile(strPattern);
        Matcher m = p.matcher(strId);
        return m.matches();
    }

    //判断是否为手机号码
    public boolean isMobileNO(String mobiles)
    {
       //Pattern p = Pattern.compile("^((13[0-9])|(15[^4,//D])|(18[0,5-9]))//d{8}$");
        String str="^((13[0-9])|(15[0|1|2|3|5|6|7|8|9])|18[0|5|6|8|9])[0-9]{8}$";
       // Pattern p = Pattern.compile("^1(3|4|5|7|8)[0-9]{9}$");
        Pattern p = Pattern.compile(str);

        Matcher m = p.matcher(mobiles);
        return m.matches();
    }
    private void sendRequestWithHttpURLConnection(final String idt,final  String usert,final String pswt,final String gender)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection=null;
                try{
                    String str="http://192.168.191.1:8084/SecondhandWebsiteWithSH/userregister.do?"+"id="+idt+"&username="+usert+"&psw="+pswt+"&gender="+gender+"&type=USER";
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
                    if(tip.equalsIgnoreCase("1"))
                    {
                        message.what=SHOW_Register;
                    }
                    else if(tip.equalsIgnoreCase("2"))
                    {
                        message.what=SHOW_account;
                    }else if(tip.equalsIgnoreCase("3"))
                    {
                        message.what=SHOW_username;
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


    public void loginnow()
    {
        AlertDialog.Builder alertDialog=new AlertDialog.Builder(User_Register.this);
            //注册成功后询问是否现在登录
            alertDialog.setTitle("账号注册成功");
            alertDialog.setMessage("是否现在登录?");
            alertDialog.setCancelable(false);
            alertDialog.setPositiveButton("是", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    User_Register.this.finish();
                    Intent intent1 = new Intent(User_Register.this, User_Login.class);
                    startActivity(intent1);

                }
            });
            alertDialog.setNegativeButton("否", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    User_Register.this.finish();
                    Intent intent2 = new Intent(User_Register.this, MainActivity.class);
                    startActivity(intent2);

                }
            });
            alertDialog.show();
        }


}


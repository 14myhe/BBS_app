package com.bbs_app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import android.widget.EditText;
import android.util.Log;

public class ResetpasswordActivity extends Activity {
    private EditText old;
    private EditText news;
    private Button  sure;
    private Button exitButton;
    private boolean needverify = true;
    private Toast toast;
    
    private static final int SHOW_RESULT = 1;
    private static final String TAG = "ResetpasswordActivity";
    private static final String SERVER_ADDRESS = "http://192.168.191.1:8084/SecondhandWebsiteWithSH/";
    
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW_RESULT:
            //       res.setText(msg.obj.toString());
                	if(msg.obj.toString().equals("2")){
                		showToast("密码修改失败!");
                	}else{
                		showToast("密码修改成功!");
                	}
                    break;
                default:
                    break;
            }
        }
    };

    private void showToast(CharSequence message) {
        if (null == toast) {
            toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
//			toast.setGravity(Gravity.CENTER, 0, 0);
        } else {
            toast.setText(message);
        }

        toast.show();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resetpassword);
        old = (EditText) this.findViewById(R.id.editText7);
        sure = (Button) this.findViewById(R.id.button2);
        news = (EditText) this.findViewById(R.id.editText3);
        exitButton = (Button) this.findViewById(R.id.button_exit);
        exitButton.setOnClickListener(new OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(ResetpasswordActivity.this,MainActivity.class);
                startActivity(intent);
                ResetpasswordActivity.this.finish();
            }
        });
        sure.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = old.getText().toString();
                String newpassword = news.getText().toString();
            //    SharedPreferences settings = getSharedPreferences("user", Context.MODE_PRIVATE);
            //    String currentpassword = settings.getString("password", "");
            //    String currentusername = settings.getString("useName","");

                if (needverify) {
                	if(!id.equals("")&&!newpassword.equals("")){
                  //  if (!id.equals("") && currentpassword.equals(oldpassword)) {
                  //      Editor editor = settings.edit();
                  //      editor.putString("password", newpassword);
                  //      editor.commit();
                      //  DataHelper.UpdateUserPassword(currentusername,newpassword);
                      //  showToast("�����޸ĳɹ�!");
                        String address = SERVER_ADDRESS+"/userupdate.do?id="+id+"&column=PASSWORD&"+"value="+newpassword;
                        HttpUtil.postHttpRequest(address, new HttpCallbackListener() {
                            @Override
                            public void onFinish(String response) {
                                Message msg = new Message();
                                msg.what = SHOW_RESULT;
                                msg.obj = response;
                                handler.sendMessage(msg);
                            }
                            
                            public void onError(Exception e) {
                            	String address = SERVER_ADDRESS+"/userupdate.do?id="+"&column=PASSWORD&"+"value=";
                            	Log.e(TAG,address);
                                Log.e(TAG, "链接出错");
                            }
                        });
                        needverify = false;
                     //   ResetpasswordActivity.this.finish();
                    } else if (id.equals("")) {
                        showToast("学号不能为空,请重新输入!");
                        old.setText("");
                        news.setText("");
                    } else if(newpassword.equals("")) {
                    	showToast("密码不能为空,请重新输入");
                        old.setText("");
                        news.setText("");
                    }
             //       else {
             //           showToast("�������������,����������!");
             //           old.setText("");
             //           news.setText("");
             //       }
                }
            }
        });
    }
}
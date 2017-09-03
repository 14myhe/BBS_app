package com.bbs_app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import android.widget.EditText;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


public class ResetusernamesActivity extends Activity {
    private EditText old;
    private EditText news;
    private Button  sure;
    private Button exitButton;
    private boolean needverify = true;
    private Toast toast;
    
    private static final int SHOW_RESULT = 1;
    private static final String TAG = "ResetusernamesActivity";
    private static final String SERVER_ADDRESS = "http://192.168.191.1:8084/SecondhandWebsiteWithSH/";
    
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW_RESULT:
            //       res.setText(msg.obj.toString());
                	if(msg.obj.toString().equals("2")){
                		showToast("用户名修改失败");
                	}else{
                		showToast("用户名修改成功");
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
        setContentView(R.layout.activity_resetusernames);
        old = (EditText) this.findViewById(R.id.editText);
        sure = (Button) this.findViewById(R.id.button);
        news = (EditText) this.findViewById(R.id.editText2);
        exitButton = (Button) this.findViewById(R.id.button_exit);
        exitButton.setOnClickListener(new OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(ResetusernamesActivity.this,MainActivity.class);
                startActivity(intent);
                ResetusernamesActivity.this.finish();
            }
        });

        sure.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldid = old.getText().toString();
                String newid = news.getText().toString();
           //     SharedPreferences settings = getSharedPreferences("user", Context.MODE_PRIVATE);
           //     String currentid = settings.getString("id", "");
                

                if (needverify) {
                //    if (!oldid.equals("") && currentid.equals(oldid)) {
                	if(!oldid.equals("")&&!newid.equals("")){
                     //   Editor editor = settings.edit();
                     //   editor.putString("id", newid);
                     //   editor.commit();
                     //   User user = DataHelper.getUserByName(oldid,DataHelper.GetUserList());//��ȡ��ǰ�û���Ϣ
                     //   user.setUserName(newid);
                     //   DataHelper.UpdateUserInfo(user);
                     //   showToast("�û����޸ĳɹ�!");
                		String address = SERVER_ADDRESS+"/userupdate.do?id="+oldid+"&column=NAME&"+"value="+newid;
                        HttpUtil.postHttpRequest(address, new HttpCallbackListener() {
                            public void onFinish(String response) {
                                Message msg = new Message();
                                msg.what = SHOW_RESULT;
                                msg.obj = response;
                                handler.sendMessage(msg);
                            }
                            public void onError(Exception e) {
                                Log.e(TAG, "链接出错!");
                            }
                        });
                        needverify = false;
                  //      ResetusernamesActivity.this.finish();
                    } else if (oldid.equals("")) {
                        showToast("学号不能为空，请重新输入");
                        old.setText("");
                        news.setText("");
                    } else if (newid.equals("")){
                        showToast("新用户名不能为空，请重新输入");
                        old.setText("");
                        news.setText("");
                    }
                }
            }
        });
    }
}

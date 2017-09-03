package com.bbs_app;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SendMessageActivity extends Activity {
	  private EditText old;
	    private EditText news;
	    private Button  sure;
	    private Button exitButton;
	    private boolean needverify = true;
	    private Toast toast;
	    
	    private static final int SHOW_RESULT = 1;
	    private static final String TAG = "SendMessageActivity";
	    private static final String SERVER_ADDRESS = "http://192.168.191.1:8084/SecondhandWebsiteWithSH/";
	    
	    private Handler handler = new Handler() {
	        public void handleMessage(Message msg) {
	            switch (msg.what) {
	                case SHOW_RESULT:
	                	if(msg.obj.toString().equals("1")){
	                		showToast("发送成功！");
	                	}else if(msg.obj.toString().equals("2")){
	                		showToast("发送失败");
	                	}else{
	                		showToast("未登录！");
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
//				toast.setGravity(Gravity.CENTER, 0, 0);
	        } else {
	            toast.setText(message);
	        }

	        toast.show();
	    }
	    @Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_send_message);
			old = (EditText) this.findViewById(R.id.editText3);
	        sure = (Button) this.findViewById(R.id.button);
	        news = (EditText) this.findViewById(R.id.editText4);
	        exitButton = (Button) this.findViewById(R.id.button_exit);
	        exitButton.setOnClickListener(new OnClickListener(){
	            public void onClick(View v){
	                Intent intent = new Intent(SendMessageActivity.this,ListViewActivity.class);
	                startActivity(intent);
	                SendMessageActivity.this.finish();
	            }
	        });

	        sure.setOnClickListener(new OnClickListener() {
	            @Override
	            public void onClick(View v) {
	                String id = old.getText().toString();
	                String content = news.getText().toString();	      
	                SharedPreferences sharedPreference = getSharedPreferences("Users",Context.MODE_PRIVATE);
					String postid = sharedPreference.getString("id","");
	                if (needverify) {
	                	if(!id.equals("")&&!content.equals("")){
	                		String address = SERVER_ADDRESS+"/msgPub?to="+id+"&content="+content+"&id="+postid;
	                        HttpUtil.postHttpRequest(address, new HttpCallbackListener() {
	                            public void onFinish(String response) {
	                                Message msg = new Message();
	                                msg.what = SHOW_RESULT;
	                                msg.obj = response;
	                                handler.sendMessage(msg);
	                            }
	                            public void onError(Exception e) {
	                                Log.e(TAG, "链接出错");
	                            }
	                        });
	                        needverify = false;
	                    } else if (id.equals("")) {
	                        showToast("发送者id不能为空,请重新输入!");
	                        old.setText("");
	                        news.setText("");
	                    } else if (content.equals("")){
	                        showToast("发送内容不能为空,请重新输入!");
	                        old.setText("");
	                        news.setText("");
	                    }
	                }
	            }
	        });
		}
	    
	

}

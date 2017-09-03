package com.bbs_app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ListViewActivity extends Activity {
	/** Called when the activity is first created. */
	private List<Map<String, Object>> mData;
	public String[] messages = new String[500];
	public String[] title = new String[500];
	public Button notReadButton;
	public Button checkedButton;
	public Button publicButton;
	public ListView listView;
	public MyAdapter adapter;
	public Toast toast;
	private static final int SHOW_RESULT = 1;
	private static final int SHOW_SUCCESS = 10;
	private static final int SHOW_MESSAGE = 20;
	private static final String TAG = "ListViewActivity";
	private static final String SERVER_ADDRESS = "http://192.168.191.1:8084/SecondhandWebsiteWithSH/";
	    
	    private Handler nhandler = new Handler() {
	        public void handleMessage(Message msg) {
	            switch (msg.what) {
	                case SHOW_RESULT:
	                	if(msg.obj.toString().equals("1")){
	                		showToast("未能登录");
	                	}else if(msg.obj.toString().equals("[]")){
	                		showToast("站内信息为空");
	                	}
	                	break;
	                default:	
	                    break;
	            }
	        }
	    };
	    
	    private Handler mhandler = new Handler() {
	        public void handleMessage(Message msg) {
	            switch (msg.what) {
	                case SHOW_MESSAGE:
	                	if(msg.obj.toString().equals("1")){
	                		showToast("标记成功");
	                	}else if(msg.obj.toString().equals("2")){
	                		showToast("标记失败");
	                	}
	                default:	
	                    break;
	            }
	        }
	    };
        private void showToast(CharSequence message) {
	        if (null == toast) {
	            toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
	        } else {
	            toast.setText(message);
	        }

	        toast.show();
	    }
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.message_main);
		notReadButton = (Button)findViewById(R.id.view_btn3);
		checkedButton = (Button)findViewById(R.id.view_btn2);
		publicButton = (Button)findViewById(R.id.view_btn4);
	  /*  SharedPreferences sharedPreferences = getSharedPreferences("Users",Context.MODE_PRIVATE);
    	    Editor editor = sharedPreferences.edit();
		editor.putString("id", "");
		editor.commit();*/
		checkedButton.setOnClickListener(new OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(ListViewActivity.this,CheckedActivity.class);
                startActivity(intent);
                ListViewActivity.this.finish();
            }
       });
		notReadButton.setOnClickListener(new OnClickListener(){
	             public void onClick(View v){
	            	 Intent intent = new Intent(ListViewActivity.this,SendMessageActivity.class);
	                 startActivity(intent);
	                 ListViewActivity.this.finish();
	             }
	        });
		publicButton.setOnClickListener(new OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(ListViewActivity.this,PublicMessageActivity.class);
                startActivity(intent);
                ListViewActivity.this.finish();
            }
       });
		
		mData = getData();
	    listView = (ListView) findViewById(R.id.listView);
	    adapter = new MyAdapter(this);
		listView.setAdapter(adapter);
			
	}



	//获取动态数组的数据，可以由其他地方传来
	private List<Map<String, Object>> getData() {

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		/*添加获取数据库信息代码，将信息存入title message数组*/
   
			getAllMessageByHttpClient();
		
    	for(int i=0;i<500;i++){	
		if(title[i]!=null)
		{
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("title", title[i]);
			map.put("info", messages[i]);
			list.add(map);
		}	
	    }
		return list;
	}

	public class MyAdapter extends BaseAdapter {

		private LayoutInflater mInflater;

		public MyAdapter(Context context) {
			this.mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mData.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			 ViewHolder holder = null;
				holder=new ViewHolder();  
				
				//可以理解为从vlist.xml获取view之后把view返回给ListView
				
				convertView = mInflater.inflate(R.layout.vlist, null);
				holder.title = (TextView)convertView.findViewById(R.id.title);
		//		holder.message = (TextView)convertView.findViewById(R.id.info);
		//		holder.deleteButton = (Button)convertView.findViewById(R.id.view_btn);
				holder.readButton = (Button)convertView.findViewById(R.id.view_btn1);
				convertView.setTag(holder);	
				holder.readButton.setTag(position);
			
			holder.title.setText((String)mData.get(position).get("title"));
		//	holder.message.setText((String)mData.get(position).get("info"));
        //    holder.deleteButton.setOnClickListener(new View.OnClickListener() {
		//		public void onClick(View v) {
		//			mData.remove(position);
		//			int i;
		//			for(i=position+1;i<messages.length;i++)
		//			{
		//				messages[i-1]=messages[i];
		//				title[i-1]=title[i];
		//			}
		//			messages[i-1]=null;
		//			title[i-1]=null;
		//			listView.setAdapter(adapter);

		//		}
		//	});
		//	holder.deleteButton.setTag(position);

			holder.readButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					showInfo(position);	
					/*、将该信息置为已读状态并更新到数据库里*/
				      SharedPreferences sharedPreference = getSharedPreferences("Users",Context.MODE_PRIVATE);
						String id = sharedPreference.getString("id","");
						if(!id.equals("")){
					String address = SERVER_ADDRESS+"/message.read?id="+id;		
					  ControlService.postHttpTitleRequest(address, new HttpCallbackListener() {
                         @Override
                         public void onFinish(String response) {
                             Message msg = new Message();
                             msg.what = SHOW_MESSAGE;
                             msg.obj = response;
                             mhandler.sendMessage(msg);
                         }
                         public void onError(Exception e) {
                             Log.e(TAG, "链接出错");
                         }
                     });
					}//if
						else{
							showToast("用户尚未登录");
						}
				}//onClick
			});
			return convertView;
		}
	}

	//提取出来方便点
	public final class ViewHolder {
		public TextView title;
		public TextView message;
		public Button deleteButton;
		public Button readButton;
	}
	public void showInfo(int position){

		new AlertDialog.Builder(this)
		.setTitle(title[position])
		.setMessage(messages[position])
		.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		})
		.show();
	}
	
	public void getAllMessageByHttpClient()
	{
		SharedPreferences sharedPreference = getSharedPreferences("Users",Context.MODE_PRIVATE);
		String id = sharedPreference.getString("id","");
		if(!id.equals("")){
		String address = SERVER_ADDRESS+"/messageQuery.do?read=no&id="+id;
        ControlService.postHttpTitleRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                Message msg = new Message();
                msg.what = SHOW_RESULT;
                msg.obj = response;                
                nhandler.sendMessage(msg);	
                try{
                  	 JSONArray jsonArray=new JSONArray(response);       
                  	 for(int i=0;i<jsonArray.length();i++){
                  	      JSONObject  message = (JSONObject) jsonArray.get(i);
                  	       title[i] = "发送者id:"+(String) message.getString("id");
                           messages[i] =(String) message.getString("content");	
                  	 }
                  	 for(int j=jsonArray.length();j<500;j++){
                        	title[j] = null;
                        	messages[j] = null;
                        }
                   }catch(JSONException e){
                       	   e.printStackTrace();
                          } 
            }
            public void onError(Exception e) {
                Log.e(TAG, "链接错误");
            }
        });
		}//if
		else{
			showToast("用户尚未登录");
		}
	}
	
	
}
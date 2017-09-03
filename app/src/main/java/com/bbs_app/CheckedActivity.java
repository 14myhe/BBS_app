package com.bbs_app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

public class CheckedActivity extends Activity {
	/** Called when the activity is first created. */
	private List<Map<String, Object>> mData;
	public Button exitButton;
	public ListView listView;
	public MyAdapter adapter;
	private Toast toast;
	public String[] messages = new String[500];
	public String[] title = new String[500];
	private static final int SHOW_RESULT = 1;
	private static final String TAG = "CheckedActivity";
	private static final String SERVER_ADDRESS = "http://192.168.191.1:8084/SecondhandWebsiteWithSH/";
	private Handler handler = new Handler() {
	        public void handleMessage(Message msg) {
	            switch (msg.what) {
	                case SHOW_RESULT:
	                	if(msg.obj.toString().equals("[]")){
	                		showToast("没有站内信息！");
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
	        } else {
	            toast.setText(message);
	        }

	        toast.show();
	    }
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_checked);
		exitButton = (Button)findViewById(R.id.view_btn4);
		exitButton.setOnClickListener(new OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(CheckedActivity.this,ListViewActivity.class);
                startActivity(intent);
                CheckedActivity.this.finish();
            }
       });
		
		mData = getData();
	    listView = (ListView) findViewById(R.id.listView);
	    adapter = new MyAdapter(this);
		listView.setAdapter(adapter);
		
		
	}



	//获取动态数组数据，可以由其他地方传来
	private List<Map<String, Object>> getData() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		//添加获取数据库信息代码，将信息存入title message数组
		getAllMessage();
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
			
			holder.title.setText((String)mData.get(position).get("title"));
		//	holder.message.setText((String)mData.get(position).get("info"));
        //    holder.deleteButton.setOnClickListener(new View.OnClickListener() {
		//		public void onClick(View v) {
		//			mData.remove(position);
		//			listView.setAdapter(adapter);
		//			int i;
		//			for(i=position+1;i<messages.length;i++)
		//			{
		//				messages[i-1]=messages[i];
		//				title[i-1]=title[i];
		//			}
		//			messages[i-1]=null;
		//			title[i-1]=null;

		//		}
		//	});
		//	holder.deleteButton.setTag(position);
			holder.readButton.setTag(position);
			//给Button添加单击事件，添加Button之后Listview将失去焦点，需要的直接把Button的焦点去掉
			holder.readButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					showInfo(position);	
				}
			});
			
			//holder.viewBtn.setOnClickListener(MyListener(position));
					
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
	
	public void getAllMessage()
	{
	    SharedPreferences sharedPreferences = getSharedPreferences("Users",Context.MODE_PRIVATE);
		String id=sharedPreferences.getString("id","");
	    if(!id.equals("")){
			String address = SERVER_ADDRESS+"/messageQuery.do?read=yes&id="+id;
		ControlService.postHttpTitleRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                Message msg = new Message();
                msg.what = SHOW_RESULT;
                msg.obj = response;
                handler.sendMessage(msg);
               try{
            	   JSONArray jsonArray = new JSONArray(response);
                
                    for(int i=0;i<jsonArray.length();i++){
                	JSONObject message = (JSONObject) jsonArray.get(i);
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
                Log.e(TAG, "链接出错");
             }
        });
          
			}//if
			else{
				showToast("用户尚未登录");
			}
	}
}

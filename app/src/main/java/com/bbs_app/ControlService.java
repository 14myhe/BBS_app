package com.bbs_app;

import java.io.IOException;  
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.apache.http.HttpEntity;  
import org.apache.http.HttpResponse;  
import org.apache.http.HttpStatus;  
import org.apache.http.ParseException;  
import org.apache.http.client.ClientProtocolException;  
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;  
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;  
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;  
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.util.EntityUtils;  

import android.widget.Toast;
  
  
public class ControlService{  
	 
  public static void postHttpTitleRequest(String httpUrl,final HttpCallbackListener listener){
      
	 
      /**NameValuePair是传送给服务器的请求参数   param.get("name") **/
      UrlEncodedFormEntity entity=null;  
      String strResult="";  
      String[] title=new String[500];
     /**新建一个get请求**/
      DefaultHttpClient client = new DefaultHttpClient();     
      HttpGet get = new HttpGet(httpUrl);   
      HttpResponse response=null;  
     
   		  try {  
    		         /**客户端向服务器发送请求**/
    		         response = client.execute(get);  
    		     } catch (ClientProtocolException e) {  
    		         // TODO Auto-generated catch block  
    		         e.printStackTrace();  
    		     } catch (IOException e) {  
    		         // TODO Auto-generated catch block  
    		         e.printStackTrace();  
    		     }    

        /**请求发送成功，并得到响应**/
      if(response.getStatusLine().getStatusCode()==200){    
         try {  
               /**读取服务器返回过来的json字符串数据**/
                strResult = EntityUtils.toString(response.getEntity());                  
                if (listener != null) {
                    listener.onFinish(strResult);
                }
         } catch (IllegalStateException e) {  
             // TODO Auto-generated catch block  
             e.printStackTrace();  
         } catch (IOException e) {  
             // TODO Auto-generated catch block  
             e.printStackTrace();  
             if (listener != null) {
                 listener.onError(e);
             }
         }//catch
      }
  }
}
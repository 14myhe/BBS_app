package com.bbs_app;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.bbs_app.Util;

import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by Nicole on 2016/12/18.
 */
public class LoginManager  {
    public static final int SHOW_success=0;
    public static final int SHOW_fail=1;
    private final String TAG = "User_Login";
    private Handler handler=new Handler(){

         public void handleMessage(Message msg)
         {
             String tip=(String)msg.obj;
             switch (msg.what){
                 case SHOW_success:

                     break;

                 case SHOW_fail:

                     break;
                 default:break;




             }
         }

     };

     public interface ICallBack {
         /** 登录成功回调接口 */
        public void onSuccess();

        /** 登录失败回调接口 */
        public void onFailed(String error);
    }

    public void login(final String username, final String password, final ICallBack call) {

        new AsyncTask<Void, Void, Integer>() {

            @Override
            protected Integer doInBackground(Void... params) {
                int result = 0;
                HttpURLConnection connection = null;
                try {
                    String str = "http://192.168.191.1:8084/SecondhandWebsiteWithSH/login.do?" + "id=" + username + "&psw=" + password;
                    //String str="http:/10.0.2.2:8084/SecondhandWebsiteWithSH/login.do?"+"id="+id+"&psw="+password;
                    URL url = new URL(str);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    Message message = new Message();
                    //将服务器返回的结果存放到message
                    message.obj = response.toString();
                    String tip = (String) message.obj;
                    if (tip.equalsIgnoreCase("success")) {
                        message.what = SHOW_success;
                        result = 1;
                    } else if (tip.equalsIgnoreCase("fail")) {
                        message.what = SHOW_fail;
                        result = 0;
                    } else {
                        result = 0;
                    }
                    handler.sendMessage(message);

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
                return result;
            }
        }.execute();
    }



       /*     protected void onPostExecute(Integer result) {
                switch (result) {
                    case 1:
                        if (call != null)
                            call.onFailed("登录失败");
                    case 0:
                        if (call != null)
                            call.onSuccess();
                        break;
                    default:
                        break;
                }
                super.onPostExecute(result);
            }

        }.execute();
    }*/


    /**
     * 获取标准 Cookie ，并存储
     * @param httpClient
     */
    private void getCookie(DefaultHttpClient httpClient) {
        List<Cookie> cookies = httpClient.getCookieStore().getCookies();
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
        Util Util=new Util();
        Util.savePreference("cookie", sb.toString());
    }


}

package com.bbs_app;

import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;


import com.loopj.android.http.AsyncHttpClient;

public class FinalAsyncHttpClient {

    AsyncHttpClient client;

    public FinalAsyncHttpClient() {
        client = new AsyncHttpClient();
        client.setConnectTimeout(5);//5s超时
        if (Util.getCookies() != null) {//每次请求都要带上cookie
            BasicCookieStore bcs = new BasicCookieStore();
            bcs.addCookies(Util.getCookies().toArray(
                    new Cookie[Util.getCookies().size()]));
            client.setCookieStore(bcs);
        }
    }

    public AsyncHttpClient getAsyncHttpClient(){
        return this.client;
    }


}

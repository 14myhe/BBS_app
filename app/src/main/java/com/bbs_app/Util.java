package com.bbs_app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.cookie.Cookie;

import android.preference.PreferenceManager;

public class Util {

    public final static String LINE_SEPARATOR = System
            .getProperty("line.separator");

    public static String md5(String s) {
        try {
            MessageDigest digest = java.security.MessageDigest
                    .getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) {
                String h = Integer.toHexString(0xFF & messageDigest[i]);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String convertStreamToString(InputStream is) {

        final BufferedReader reader = new BufferedReader(new InputStreamReader(
                is));
        final StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + LINE_SEPARATOR);
            }
        } catch (IOException e) {
        } finally {
            try {
                is.close();
            } catch (IOException e) {

            }
        }
        return sb.toString();
    }

    /**
     * 保存数据到sp
     * @param key
     * @param value
     */
    public static void savePreference(String key, String value) {
        PreferenceManager.getDefaultSharedPreferences(App.mContext).edit()
                .putString(key, value).commit();
    }


    /**
     * 读取配置参数
     * @param
     * @param key
     * @return
     */
    public static String getPreference(String key) {
        return PreferenceManager.getDefaultSharedPreferences(App.mContext)
                .getString(key, "");
    }

    private static List<Cookie> cookies;

    public static List<Cookie> getCookies() {
        return cookies != null ? cookies : new ArrayList<Cookie>();
    }

    public static void setCookies(List<Cookie> cookies) {
        Util.cookies = cookies;
    }
}

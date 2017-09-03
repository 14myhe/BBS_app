package com.bbs_app;

import java.io.Serializable;
import java.lang.reflect.Type;

import android.graphics.drawable.Drawable;

public class User implements Serializable {

    public static final String ID = "_id";
    public static final String USERNAME = "userName";
    public static final String PASSWORD = "passWord";
    public static final String TYPE = "type";

    private String id;
    private String password;//����
    private String userName;//�û���
    private enum Type{USER,ADMIN };
    private Type types;

    public void setId(String id){
        this.id = id;
    }
    public void setPassword(String password){
        this.password = password;
    }
    public void setUserName(String userName){
        this.userName = userName;
    }
    public void setType(Type types){
        this.types = types;
    }
    public Type getType(){
        return this.types;
    }
    public String getId(){
        return this.id;
    }
    public String getUserName(){
        return this.userName;
    }
    public String getPassword(){
        return this.password;
    }

}
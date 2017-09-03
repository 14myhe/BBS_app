package com.bbs_app;

/**
 * Created by Nicole on 2016/12/18.
 */
public class LoginResult {

    /**状态*/
    public String status;
    /**结果码*/
    public int code;

    public User results;

    public class User {
        public String username;

        public String userid;
    }
}

package com.ajs.components;

import com.ajs.model.Message;

import java.io.Serializable;

public class Query implements Serializable {
    private static final long serialVersionUID = -1211567902429240106L;
    public static final String WRITING = "writing";
    public static final String RECEIVE_WRITING = "receive_writing";
    public static final String SEND_TXT = "send_txt";
    public static final String RECEIVE_TXT = "receive_txt";
    public static final String SEND_FILE = "send_file";
    public static final String RECEIVE_FILE = "receive_file";
    public static final String LOGIN = "login";
    public static final String LOGOUT = "logout";
    public static final String ONLINE = "online";
    public static final String OFFLINE = "offline";
    public static final String REGISTER = "register";
    public static final String UNREGISTER = "unregister";

    public static final String BAD_QUERY = "bad_query";
    public static final String REGISTER_OK = "register_ok";
    public static final String REGISTER_FAILED = "register_failed";
    public static final String UNREGISTER_OK = "unregister_ok";
    public static final String LOGIN_OK = "login_ok";
    public static final String LOGIN_FAILED = "login_failed";
    public static final String LOGOUT_OK = "logout_ok";

    private String cmd;
    private Message message;

    public Query(){}

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}

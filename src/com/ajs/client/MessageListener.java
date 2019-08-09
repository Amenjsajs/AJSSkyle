package com.ajs.client;

import com.ajs.model.User;

public interface MessageListener {
    public void onMessage(User sender, String msgBody, int msgHeight);
}

package com.ajs.client;

import com.ajs.model.User;

public interface UserStatusListener {
    public void online(User sender);
    public void offline(User sender);
}

package com.ajs.client;

import com.ajs.model.User;

public interface UserWritingStatusListener {
    void onWriting(User receiver);
}

package com.ajs.client;

import com.ajs.components.DirectoriesPath;
import com.ajs.components.Query;
import com.ajs.model.Message;
import com.ajs.model.User;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Base64;

public class Client {
    private User sender;
    private String host;
    private int serverPort;
    private Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    private ArrayList<UserStatusListener> userStatusListeners = new ArrayList<>();
    private ArrayList<MessageListener> messageListeners = new ArrayList<>();
    private ArrayList<UserWritingStatusListener> userWritingStatusListeners = new ArrayList<>();

    public Client(String host, int serverPort) {
        this.host = host;
        this.serverPort = serverPort;
    }

    public static String encodeFile(byte[] fileByteArray) {
        return Base64.getEncoder().encodeToString(fileByteArray);
    }

    public static byte[] decodeFile(String fileDataString) {
        return Base64.getDecoder().decode(fileDataString);
    }

    public void register(User user) throws IOException {
        user.encodeAvatar();
        Message message = new Message();
        message.setSender(user);
        message.setType(Query.REGISTER);
        Query query = new Query();
        query.setCmd(Query.REGISTER);
        query.setMessage(message);
        oos.writeObject(query);
    }

    public void msg(Message message) throws IOException {
        Query query = new Query();
        query.setCmd(message.getType());
        query.setMessage(message);
        oos.writeObject(query);
    }

    public void notifyWriting(User sender, User receiver, boolean stopped) throws IOException {
        Message message = new Message();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setType(Query.WRITING);

        if(stopped) message.setContent("stop");
        else message.setContent("continuous");

        Query query = new Query();
        query.setMessage(message);
        query.setCmd(Query.WRITING);
        oos.writeObject(query);
    }

    public boolean login(String email, String password) throws IOException, ClassNotFoundException {
        User sender = new User();
        sender.setEmail(email);
        sender.setPassword(password);
        Message message = new Message();
        message.setSender(sender);
        message.setType(Query.LOGIN);

        Query query = new Query();
        query.setCmd("login");
        query.setMessage(message);
        oos.writeObject(query);

        Query response = (Query) ois.readObject();
        if (Query.LOGIN_OK.equals(response.getCmd())) {
            this.sender = response.getMessage().getSender();
            File avatarFile = new File(DirectoriesPath.getAvatarPath() + "/" + this.sender.getAvatarName());
            if (!Files.exists(avatarFile.toPath())) {
                this.sender.setAvatarPath(DirectoriesPath.getAvatarPath() + "/" + this.sender.getAvatarName());
                this.sender.saveAvatar();
            }
            this.sender.setAvatar(new ImageIcon(DirectoriesPath.getAvatarPath() + "/" + this.sender.getAvatarName()).getImage());

            System.out.println(response.getMessage().getSender().getFullName() + ", vous êtes connecté");
            startMessageReader();
            return true;
        } else {
            return false;
        }
    }

    private void startMessageReader() {
        Thread t = new Thread(this::readMessageLoop);
        t.start();
    }

    private void readMessageLoop() {
        try {
            Query query;
            while ((query = (Query) ois.readObject()) != null) {
                String cmd = query.getCmd();
                if (Query.ONLINE.equals(cmd)) {
                    handleOnline(query);
                } else if (Query.OFFLINE.equals(cmd)) {
                    handleOffline(query);
                } else if (Query.RECEIVE_TXT.equals(cmd) || Query.RECEIVE_FILE.equals(cmd)) {
                    handleMessage(query);
                } else if (Query.RECEIVE_WRITING.equals(cmd)) {
                    handleUserWriting(query);
                }
            }
        } catch (Exception ex) {
            //ex.printStackTrace();
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void logout() throws IOException {
        Message message = new Message();
        message.setType(Query.LOGOUT);
        Query query = new Query();
        query.setCmd(Query.LOGOUT);
        query.setMessage(message);
        oos.writeObject(query);
    }

    private void handleMessage(Query query) throws IOException {
        if (query.getCmd().equals(Query.RECEIVE_TXT)) {
            Message message = query.getMessage();
            setOrSaveUserAvatar(query.getMessage().getSender());
            setOrSaveUserAvatar(query.getMessage().getReceiver());
            for (MessageListener listener : messageListeners) {
                listener.onMessage(message);
            }
        } else if (query.getCmd().equals(Query.RECEIVE_FILE)) {

        }
    }

    private void handleUserWriting(Query query) {
        for (UserWritingStatusListener listener : userWritingStatusListeners) {
            listener.onWriting(query.getMessage());
        }
    }

    private void handleOnline(Query query) throws IOException {
        User sender = query.getMessage().getSender();
        if (sender != null) {
            setOrSaveUserAvatar(sender);
            for (UserStatusListener listener : userStatusListeners) {
                listener.online(sender);
            }
        }
    }

    private void setOrSaveUserAvatar(User user) throws IOException {
        File avatarFile = new File(DirectoriesPath.getAvatarPath() + "/" + user.getAvatarName());
        if (!Files.exists(avatarFile.toPath())) {
            user.setAvatarPath(DirectoriesPath.getAvatarPath() + "/" + user.getAvatarName());
            user.saveAvatar();
        }
        user.setAvatar(new ImageIcon(DirectoriesPath.getAvatarPath() + "/" + user.getAvatarName()).getImage());
    }

    private void handleOffline(Query query) {
        User sender = query.getMessage().getSender();
        if (sender != null) {
            for (UserStatusListener listener : userStatusListeners) {
                listener.offline(sender);
            }
        }
    }

    public boolean connect() {
        try {
            this.socket = new Socket(host, serverPort);
            this.oos = new ObjectOutputStream(socket.getOutputStream());
            this.ois = new ObjectInputStream(socket.getInputStream());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public User getUser() {
        return sender;
    }

    public void addUserStatusListener(UserStatusListener listerner) {
        userStatusListeners.add(listerner);
    }

    public void removeUserStatusListener(UserStatusListener listerner) {
        userStatusListeners.remove(listerner);
    }

    public void addMessageListenr(MessageListener listener) {
        messageListeners.add(listener);
    }

    public void removeMessageListener(MessageListener listener) {
        messageListeners.remove(listener);
    }

    public void addUserStatusWritingListener(UserWritingStatusListener listener) {
        userWritingStatusListeners.add(listener);
    }

    public void removeUserStatusWritingListener(UserWritingStatusListener listener) {
        userWritingStatusListeners.remove(listener);
    }
}
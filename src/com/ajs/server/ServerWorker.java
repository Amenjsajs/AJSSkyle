package com.ajs.server;

import com.ajs.components.DirectoriesPath;
import com.ajs.components.Query;
import com.ajs.db.dao.MessageDAO;
import com.ajs.db.dao.UserDAO;
import com.ajs.db.factory.AbstractDaoFactory;
import com.ajs.db.factory.DaoFactory;
import com.ajs.model.Message;
import com.ajs.model.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class ServerWorker extends Thread {
    private final Server server;
    private Socket clientSocket;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private User user;
    private AbstractDaoFactory adf = DaoFactory.getFactory(DaoFactory.DAO_FACTORY);

    public ServerWorker(Server server, Socket clientSocket) {
        this.server = server;
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            handleClientSocket();
        } catch (IOException | ClassNotFoundException e) {
            try {
                handleLogout();
                if (user != null) {
                    System.out.println(user.getFullName() + " s'est déconnecter");
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            server.getWorkers().remove(this);
        }
    }

    private void handleClientSocket() throws IOException, ClassNotFoundException {
        ois = new ObjectInputStream(clientSocket.getInputStream());
        oos = new ObjectOutputStream(clientSocket.getOutputStream());

        Query query;
        while ((query = (Query) ois.readObject()) != null) {
            String cmd = query.getCmd();
            switch (cmd) {
                case Query.REGISTER:
                    handleRegister(query);
                    break;
                case Query.LOGIN:
                    handleLogin(query);
                    break;
                case Query.LOGOUT:
                    handleLogout();
                    return;
                case Query.SEND_TXT:
                case Query.SEND_FILE:
                    handleSendMessage(query);
                    break;
                case Query.WRITING:
                    handleMessageWriting(query);
                    break;
            }
        }
    }

    private void handleMessageWriting(Query query) throws IOException {
        Query response = new Query();
        for (ServerWorker worker : server.getWorkers()) {
            if (query.getMessage().getReceiver().compareTo(worker.user) == 0) {
                response.setCmd(Query.RECEIVE_WRITING);
                response.setMessage(query.getMessage());
                worker.sendMessage(response);
            }
        }
    }

    private void handleSendMessage(Query query) throws IOException {
        Query response = new Query();
        for (ServerWorker worker : server.getWorkers()) {
            if (query.getMessage().getReceiver().compareTo(worker.user) == 0) {
                MessageDAO messageDAO = adf.getMessageDAO();

                messageDAO.create(query.getMessage());
                switch (query.getCmd()) {
                    case Query.SEND_TXT:
                        response.setCmd(Query.RECEIVE_TXT);
                        //message.setContent(String.format("%s a ecrit: %s", user.getFirstName(), msgContent));
                        break;
                    case Query.SEND_FILE:
                        response.setCmd(Query.RECEIVE_FILE);
                        break;
                }

                response.setMessage(query.getMessage());
                worker.sendMessage(response);

                switch (query.getCmd()) {
                    case Query.SEND_TXT:
                        System.out.println(String.format("%s a ecrit à %s: %s", user.getFirstName(), worker.user.getFullName(), query.getMessage().getContent()));
                        break;
                    case Query.SEND_FILE:
                        System.out.println(String.format("%s a envoyé le fichier %s à %s", user.getFirstName(), query.getMessage().getFileName(), worker.user.getFirstName()));
                        break;
                }
                worker.sendMessage(query);
            }
        }
    }

    private void handleLogout() throws IOException {
        List<ServerWorker> workers = server.getWorkers();
        for (ServerWorker worker : workers) {
            if (worker.getUser() != null) {
                if (user.compareTo(worker.getUser()) != 0) {
                    Query query = new Query();
                    Message message = new Message();
                    query.setCmd(Query.OFFLINE);

                    message.setContent(String.format("%s est connecté", user.getFullName()));
                    message.setSender(worker.getUser());
                    query.setMessage(message);
                    sendMessage(query);
                }
            }
        }

        for (ServerWorker worker : workers) {
            if (worker.getUser() != null) {
                if (user.compareTo(worker.getUser()) != 0) {
                    Query query = new Query();
                    Message message = new Message();
                    query.setCmd(Query.OFFLINE);

                    message.setContent(String.format("%s s'est connecté", worker.getUser().getFullName()));
                    message.setSender(user);
                    query.setMessage(message);
                    worker.sendMessage(query);
                }
            }
        }
        workers.remove(this);
        clientSocket.close();
    }

    private void handleLogin(Query query) throws IOException {
        Query response = new Query();
        Message message = new Message();
        UserDAO userDAO = adf.getUserDAO();

        User sender = query.getMessage().getSender();
        User userSearch = userDAO.findByEmailAndPassword(sender.getEmail(), sender.getPassword());
        if (userSearch == null) {
            System.err.println(String.format("Erreur de connexion pour login=%s et password=%s", sender.getEmail(), sender.getPassword()));

            response.setCmd(Query.LOGIN_FAILED);
            message.setContent("Erreur Login");
            response.setMessage(message);
            oos.writeObject(response);
        } else {
            user = userSearch;
            user.encodeAvatar();
            response.setCmd(Query.LOGIN_OK);
            message.setSender(user);
            response.setMessage(message);
            oos.writeObject(response);

            System.out.println(String.format("%s est connecté", user.getFullName()));

            List<ServerWorker> workers = server.getWorkers();

            for (ServerWorker worker : workers) {
                if (worker.getUser() != null) {
                    if (user.compareTo(worker.getUser()) != 0) {
                        Query query1 = new Query();
                        Message message1 = new Message();
                        query1.setCmd(Query.ONLINE);

                        message1.setContent(String.format("%s est connecté", user.getFullName()));
                        message1.setSender(worker.getUser());
                        query1.setMessage(message1);
                        sendMessage(query1);
                    }
                }
            }

            for (ServerWorker worker : workers) {
                if (worker.getUser() != null) {
                    if (user.compareTo(worker.getUser()) != 0) {
                        Query query1 = new Query();
                        Message message1 = new Message();
                        query1.setCmd(Query.ONLINE);

                        message1.setContent(String.format("%s est connecté", worker.getUser().getFullName()));
                        message1.setSender(user);
                        query1.setMessage(message1);
                        worker.sendMessage(query1);
                    }
                }
            }
        }
    }

    private void handleRegister(Query query) throws IOException {
        Query response = new Query();
        Message message = new Message();
        UserDAO userDAO = adf.getUserDAO();
        User user1 = query.getMessage().getSender();

        int pos = user1.getAvatarPath().lastIndexOf(".");
        String ext = user1.getAvatarPath().substring(pos);
        user1.setAvatarPath(DirectoriesPath.getAvatarPath() + "/" + user1.getEmail() + ext);

        if (userDAO.create(user1)) {
            user1.saveAvatar();

            System.out.println(user1.getFullName() + " vient de créer un compte");
            response.setCmd(Query.REGISTER_OK);
            message.setContent("Compte créé");
            response.setMessage(message);
        } else {
            System.err.println("Echec de create de compte");
            response.setCmd(Query.REGISTER_FAILED);
            message.setContent("Erreur");
            response.setMessage(message);
        }
    }

    private void sendMessage(Query query) throws IOException {
        if (user != null) {
            oos.writeObject(query);
        }
    }

    private User getUser() {
        return user;
    }

}

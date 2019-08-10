package com.ajs.ui;

import com.ajs.client.Client;
import com.ajs.client.MessageListener;
import com.ajs.client.UserWritingStatusListener;
import com.ajs.components.MessageShowPane;
import com.ajs.components.Query;
import com.ajs.components.RoundedBorder;
import com.ajs.components.UserPresenceStatusPane;
import com.ajs.model.Message;
import com.ajs.model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.Date;

public class MessagePane extends JPanel implements MessageListener, UserWritingStatusListener {
    private MainFrame mainFrame;
    private Client client;
    private User user;

    //private Box messagePanelBox;
    private JPanel messagePanel;

    private JTextArea messageArea;

    public MessagePane(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.client = mainFrame.getClient();
        this.user = mainFrame.getClient().getUser();
        client.addMessageListenr(this);
        client.addUserStatusWritingListener(this);

        setLayout(new BorderLayout());

        JLabel topLabel = new JLabel("Conversion avec ");
        topLabel.setHorizontalAlignment(JLabel.LEFT);
        add(topLabel, BorderLayout.NORTH);

        //messagePanelBox = new Box(BoxLayout.Y_AXIS);
        messagePanel = new JPanel();
        BoxLayout bxl = new BoxLayout(messagePanel,BoxLayout.Y_AXIS);
        messagePanel.setLayout(bxl);
        //messagePanel.add(messagePanelBox);
        add(new JScrollPane(messagePanel), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        add(bottomPanel, BorderLayout.SOUTH);

        messageArea = new JTextArea(1, 80);
        messageArea.setLineWrap(true);

        JScrollPane messageAreaScrollPane = new JScrollPane(messageArea);
        messageAreaScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        messageAreaScrollPane.setBorder(BorderFactory.createCompoundBorder(new RoundedBorder(), BorderFactory.createEmptyBorder()));
        messageAreaScrollPane.setPreferredSize(new Dimension(800, 26 + 20));
        bottomPanel.add(messageAreaScrollPane);

        messageArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                String msg = messageArea.getText();
                try {
                    client.notifyWriting(user,mainFrame.getCurrentUserPresenceStatusPane().getUser(), false);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                if ((e.getKeyCode() == KeyEvent.VK_ENTER) && ((e.getModifiersEx() & InputEvent.SHIFT_DOWN_MASK) != 0)) {
                    int i = messageArea.getCaretPosition();
                    messageArea.insert("\n", i);
                }
                handleScrollPaneHeight(messageArea, messageAreaScrollPane, bottomPanel);

                if (e.getKeyCode() == KeyEvent.VK_ENTER && ((e.getModifiersEx() & InputEvent.SHIFT_DOWN_MASK) == 0)) {
                    if (!msg.isBlank()) {
                        try {
                            int msgHeight = messageArea.getPreferredSize().height;

                            Message message = new Message();
                            message.setSender(user);
                            message.setReceiver(mainFrame.getCurrentUserPresenceStatusPane().getUser());
                            message.setContent(msg);
                            message.setType(Query.SEND_TXT);
                            message.setMsgHeight(msgHeight);
                            message.setCreatedDate(new Date());
                            client.msg(message);

                            JPanel panel = new JPanel(new BorderLayout());
                            MessageShowPane messageShowPane = new MessageShowPane(message, FlowLayout.RIGHT);
                            panel.add(messageShowPane);
                            messagePanel.add(messageShowPane);

                            messageArea.setRows(1);
                            messageArea.setColumns(80);
                            messageArea.setPreferredSize(new Dimension(800, 16));
                            messageArea.setText("");
                            messageArea.repaint();
                            messageAreaScrollPane.setPreferredSize(new Dimension(800, 26 + 20));
                            bottomPanel.revalidate();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                try {
                    client.notifyWriting(user,mainFrame.getCurrentUserPresenceStatusPane().getUser(), true);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private void handleScrollPaneHeight(JTextArea messageArea, JScrollPane scrollPane, JPanel parent) {
        int height = messageArea.getPreferredSize().height;

        if (height <= 16) {
            height = 32;
        } else if (height >= 112) {
            height = 112;
        }
        scrollPane.setPreferredSize(new Dimension(800, height + 20));
        scrollPane.revalidate();
        parent.revalidate();
    }

    @Override
    public void onWriting(Message message) {
        UserPresenceStatusPane userPresenceStatusPane = mainFrame.getCurrentUserPresenceStatusPane();
        if (userPresenceStatusPane.getUser().compareTo(message.getSender()) == 0) {
            if(message.getContent().equals("stop")){
                userPresenceStatusPane.getDotAnimated().stop();
            }else{
                userPresenceStatusPane.getDotAnimated().start();
            }
        }
    }

    @Override
    public void onMessage(Message message) {
        UserPresenceStatusPane userPresenceStatusPane = mainFrame.getCurrentUserPresenceStatusPane();
        if (userPresenceStatusPane.getUser().compareTo(message.getSender()) == 0) {
            MessageShowPane messageShowPane = new MessageShowPane(message, FlowLayout.LEFT);
            JPanel panel = new JPanel(new BorderLayout());
            panel.add(messageShowPane);
            messagePanel.add(messageShowPane);
            messagePanel.revalidate();
        }
    }
}

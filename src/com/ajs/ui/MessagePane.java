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
import java.util.Date;

public class MessagePane extends JPanel implements MessageListener, UserWritingStatusListener {
    private MainFrame mainFrame;
    private Client client;
    private User user;
    private UserPresenceStatusPane userPresenceStatusPane;

    private Box messagePanelBox;
    private JPanel messagePanel;

    private JTextArea messageArea;
    public MessagePane(MainFrame mainFrame){
        this.mainFrame = mainFrame;
        this.client = mainFrame.getClient();
        this.user = mainFrame.getClient().getUser();
        this.userPresenceStatusPane = mainFrame.getCurrentUserPresenceStatusPane();
        client.addMessageListenr(this);
        client.addUserStatusWritingListener(this);

        setLayout(new BorderLayout());

        JLabel topLabel = new JLabel("Conversion avec ");
        topLabel.setHorizontalAlignment(JLabel.LEFT);
        add(topLabel, BorderLayout.NORTH);

        messagePanelBox = new Box(BoxLayout.Y_AXIS);
        messagePanel = new JPanel();
        messagePanel.add(messagePanelBox);
        add(new JScrollPane(messagePanel), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        add(bottomPanel, BorderLayout.SOUTH);

        messageArea = new JTextArea(1,80);
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
                            message.setReceiver(userPresenceStatusPane.getUser());
                            message.setContent(msg);
                            message.setType(Query.SEND_TXT);
                            message.setMsgHeight(msgHeight);
                            message.setCreatedDate(new Date());
                            client.msg(message);

                            MessageShowPane messageShowPane = new MessageShowPane(message, FlowLayout.RIGHT);
                            messagePanelBox.add(messageShowPane);

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
                super.keyReleased(e);
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
        parent.revalidate();
    }

    @Override
    public void onWriting(User receiver) {

    }

    @Override
    public void onMessage(Message message) {
        if(user.compareTo(message.getSender()) == 0){
            MessageShowPane messageShowPane = new MessageShowPane(message, FlowLayout.LEFT);
            messagePanelBox.add(messageShowPane);
        }
    }
}

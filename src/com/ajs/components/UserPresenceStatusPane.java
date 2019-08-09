package com.ajs.components;

import com.ajs.model.User;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class UserPresenceStatusPane extends JPanel {
    private UserAvatarPane userAvatarPane;
    private NumberMessageNotRead numberMessageNotRead;
    private DotAnimated dotAnimated;
    private int width = 300;
    private int height = 50 + 5 + 5;
    private User user;
    private Color bgDefaultColor;
    private static ArrayList<UserPresenceStatusPane> userPresenceStatusPanes = new ArrayList<>();
    private boolean active;

    public UserPresenceStatusPane(User user) {
        this.user = user;
        this.bgDefaultColor = getBackground();
        userPresenceStatusPanes.add(this);
        FlowLayout fl = new FlowLayout();
        fl.setAlignment(FlowLayout.LEFT);
        setLayout(fl);

        userAvatarPane = new UserAvatarPane(user);
        numberMessageNotRead = new NumberMessageNotRead();
        dotAnimated = new DotAnimated(3, 12, 50);

        setPreferredSize(new Dimension(width,height));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel label = new JLabel();
        add(userAvatarPane);
        label.setText(user.getFullName());
        add(label);
        add(numberMessageNotRead);
        add(dotAnimated);
    }

    public User getUser() {
        return user;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public static ArrayList<UserPresenceStatusPane> getUserPresenceStatusPanes() {
        return userPresenceStatusPanes;
    }

    public Color getBgDefaultColor() {
        return bgDefaultColor;
    }

    public UserAvatarPane getUserAvatarPane() {
        return userAvatarPane;
    }

    public NumberMessageNotRead getNumberMessageNotRead() {
        return numberMessageNotRead;
    }

    public DotAnimated getDotAnimated() {
        return dotAnimated;
    }
}

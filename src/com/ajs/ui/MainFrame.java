package com.ajs.ui;

import com.ajs.client.Client;
import com.ajs.client.UserStatusListener;
import com.ajs.components.ImageResizer;
import com.ajs.components.UserPresenceStatusPane;
import com.ajs.model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainFrame extends JFrame implements UserStatusListener {
    private final Container container;
    private final JPanel topPanelLeftPanel;
    private Client client;
    private JPanel leftPanel;
    private JPanel topPanel;
    private JPanel centerPanel;
    private JPanel centerPanelTopPanel;

    private JButton registrationBtn;
    private JButton loginBtn;
    private JButton logoutBtn;

    private Box leftPanelBox;
    private CardLayout centerPanelLayout;

    private Color bgHoverColor = new Color(200, 200, 200);
    private Color bgActiveColor = Color.gray;

    private Map<User, UserPresenceStatusPane> statusPaneMap = new HashMap<>();
    private Map<User, MessagePane> cardMap;

    private UserPresenceStatusPane currentUserPresenceStatusPane;

    public MainFrame(String title) {
        super(title);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(new Dimension(1000, 700));
        setLocationRelativeTo(null);

        client = new Client("localhost", 8899);
        client.connect();
        client.addUserStatusListener(this);

        container = getContentPane();
        topPanel = new JPanel(new BorderLayout());

        JPanel topPanelRightPanel = new JPanel();
        topPanel.add(topPanelRightPanel, BorderLayout.EAST);

        topPanelLeftPanel = new JPanel();
        topPanel.add(topPanelLeftPanel, BorderLayout.WEST);

        registrationBtn = new JButton("");
        registrationBtn.setToolTipText("Créer un nouveau compte");
        Image registerIcon = ImageResizer.scaleImage(new ImageIcon("images/icon/register-icon.jpg").getImage(), 20, 20);
        registrationBtn.setIcon(new ImageIcon(registerIcon));

        registrationBtn.addActionListener((e -> {
            if (RegistrationDialog.showRegisterDialog() == RegistrationDialog.OK_OPTION) {
                try {
                    client.register(RegistrationDialog.getUser());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }));
        topPanelRightPanel.add(registrationBtn);

        loginBtn = new JButton("");
        Image loginIcon = ImageResizer.scaleImage(new ImageIcon("images/icon/login-icon.jpg").getImage(),20,20);
        loginBtn.setToolTipText("Se connecter");
        loginBtn.setIcon(new ImageIcon(loginIcon));
        loginBtn.addActionListener(e -> {
            if (LoginDialog.showDialog() == LoginDialog.CONNECT_OPTION) {
                try {
                    if (client.login(LoginDialog.getLogin(), LoginDialog.getPassword())) {
                        UserPresenceStatusPane userPresenceStatusPane = new UserPresenceStatusPane(client.getUser());
                        topPanelLeftPanel.add(userPresenceStatusPane);
                        container.revalidate();
                        loginBtn.setEnabled(false);
                        logoutBtn.setEnabled(true);
                    } else {
                        String errorMsg = String.format("Erreur de login pour \nEmail: %s\nMot de passe: %s", LoginDialog.getLogin(), LoginDialog.getPassword());
                        JOptionPane.showMessageDialog(null, errorMsg, "Erreur de login", JOptionPane.INFORMATION_MESSAGE);
                        System.out.println("Erreur de login");
                    }
                } catch (IOException | ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        });
        topPanelRightPanel.add(loginBtn);
        logoutBtn = new JButton("");
        logoutBtn.setToolTipText("Se déconnecter");
        Image logoutIcon = ImageResizer.scaleImage(new ImageIcon("images/icon/logout-icon.jpg").getImage(),20,20);
        logoutBtn.setIcon(new ImageIcon(logoutIcon));
        logoutBtn.setEnabled(false);
        logoutBtn.addActionListener(e -> {
            try {
                client.logout();
                client = new Client("localhost", 8899);
                client.connect();
                client.addUserStatusListener(this);
                loginBtn.setEnabled(true);
                logoutBtn.setEnabled(false);
                topPanelLeftPanel.removeAll();
                container.revalidate();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        topPanelRightPanel.add(logoutBtn);

        container.add(topPanel, BorderLayout.NORTH);

        leftPanel = new JPanel();
        leftPanelBox = new Box(BoxLayout.Y_AXIS);
        leftPanelBox.setAlignmentY(Box.TOP_ALIGNMENT);
        leftPanel.setPreferredSize(new Dimension(310, 0));
        leftPanel.add(leftPanelBox);
        container.add(new JScrollPane(leftPanel), BorderLayout.WEST);

        BorderLayout bl = new BorderLayout();
        JPanel centerPanelContainer = new JPanel(bl);
        container.add(centerPanelContainer, BorderLayout.CENTER);
        centerPanelTopPanel = new JPanel();
        centerPanelContainer.add(centerPanelTopPanel, BorderLayout.NORTH);
        centerPanelContainer.setBackground(Color.red);
        centerPanelLayout = new CardLayout();
        centerPanel = new JPanel(centerPanelLayout);
        centerPanelContainer.add(centerPanel, BorderLayout.CENTER);

        addWindowFocusListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    client.logout();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        setVisible(true);
    }

    public Client getClient() {
        return client;
    }

    public UserPresenceStatusPane getCurrentUserPresenceStatusPane() {
        return currentUserPresenceStatusPane;
    }

    @Override
    public void online(User sender) {
        UserPresenceStatusPane userPresenceStatusPane = new UserPresenceStatusPane(sender);
        userPresenceStatusPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() > 1) {
                    handleUserStatusPaneMouseClicked(userPresenceStatusPane);
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                handleUserStatusPaneMouseEntered(userPresenceStatusPane);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                handleUserStatusPaneMouseExited(userPresenceStatusPane);
            }
        });
        leftPanelBox.add(userPresenceStatusPane);
        leftPanelBox.revalidate();
        statusPaneMap.put(sender, userPresenceStatusPane);
        centerPanel.add(new MessagePane(this), sender.getEmail());
    }

    @Override
    public void offline(User sender) {
        UserPresenceStatusPane upsp = statusPaneMap.get(sender);
        leftPanelBox.remove(upsp);
        leftPanelBox.revalidate();
        statusPaneMap.remove(sender, upsp);
    }

    private void handleUserStatusPaneMouseClicked(UserPresenceStatusPane current) {
        if (!current.isActive()) {
            currentUserPresenceStatusPane = current;
            for (UserPresenceStatusPane userPresenceStatusPane : UserPresenceStatusPane.getUserPresenceStatusPanes()) {
                if (!current.equals(userPresenceStatusPane)) {
                    userPresenceStatusPane.setActive(false);
                    userPresenceStatusPane.setBackground(userPresenceStatusPane.getBgDefaultColor());
                }
            }
            current.setBackground(bgActiveColor);
            current.setActive(true);
            centerPanelLayout.show(centerPanel, current.getUser().getEmail());
        }
    }

    private void handleUserStatusPaneMouseEntered(UserPresenceStatusPane current) {
        if (!current.isActive()) {
            current.setBackground(bgHoverColor);
        }
    }

    private void handleUserStatusPaneMouseExited(UserPresenceStatusPane current) {
        if (!current.isActive()) {
            current.setBackground(current.getBgDefaultColor());
        }
    }
}

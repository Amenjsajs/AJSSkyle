package com.ajs.UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class LoginDialog extends JDialog {
    private JPanel centerPanel;
    private Box centerPanelBox;
    private JLabel errorLabel;
    private JTextField loginField;
    private JPasswordField passwordField;
    private static String login = null;
    private static String password = null;
    private JButton btnConnect;
    private JButton btnCancel;
    public static final int CONNECT_OPTION = 0;
    public static final int CANCEL_OPTION = -1;
    private static int response;
    private Container container;

    private static LoginDialog instance = new LoginDialog(null, "Login", true);

    public LoginDialog(JFrame parent, String title, boolean isModal) {
        super(parent, title, isModal);
        setLocationRelativeTo(null);
        setResizable(false);

        container = getContentPane();

        container.setLayout(new BorderLayout());

        centerPanel = new JPanel();
        centerPanelBox = Box.createVerticalBox();
        centerPanel.add(centerPanelBox);
        container.add(centerPanel, BorderLayout.CENTER);

        loginField = new JTextField(20);
        setField("Nom d'utilisateur", loginField);

        passwordField = new JPasswordField(20);
        setField("Mot de passe", passwordField);

        JPanel errorPanel = new JPanel();
        centerPanelBox.add(errorPanel);
        errorLabel = new JLabel("Les 2 champs sont obligatoires");
        errorLabel.setForeground(new Color(0, 0, 0, 0));
        errorPanel.add(errorLabel);

        JPanel bottomPanel = new JPanel();
        container.add(bottomPanel, BorderLayout.SOUTH);

        btnConnect = new JButton("Connectez-vous");
        bottomPanel.add(btnConnect);
        btnConnect.addActionListener((e) -> {
            login = loginField.getText();
            password = String.valueOf(passwordField.getPassword());
            if (login.isBlank() || password.isBlank()) {
                instance.errorLabel.setForeground(Color.red);
                System.out.println("ok");
            } else {
                response = CONNECT_OPTION;
                setVisible(false);
            }
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowDeactivated(WindowEvent e) {
                response = CANCEL_OPTION;
            }
        });
        pack();
    }

    private void setField(String label, JTextField field) {
        JPanel rowPanel = new JPanel();
        FlowLayout layout = new FlowLayout();
        layout.setAlignment(FlowLayout.RIGHT);
        rowPanel.setLayout(layout);

        centerPanelBox.add(rowPanel);

        JLabel fieldLabel = new JLabel(label);
        fieldLabel.setHorizontalAlignment(SwingConstants.LEFT);
        rowPanel.add(fieldLabel);

        JPanel fieldPanel = new JPanel();
        rowPanel.add(fieldPanel);
        fieldPanel.add(field);
    }

    public static int showDialog() {
        response = CANCEL_OPTION;
        instance.loginField.setText("");
        instance.passwordField.setText("");
        login = null;
        password = null;
        instance.errorLabel.setForeground(new Color(0, 0, 0, 0));
        instance.setVisible(true);
        return response;
    }

    public static LoginDialog getInstance() {
        return instance;
    }

    public static String getLogin() {
        return login;
    }

    public static String getPassword() {
        return password;
    }
}

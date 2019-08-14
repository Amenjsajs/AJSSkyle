package com.ajs.ui;

import com.ajs.components.DirectoriesPath;
import com.ajs.components.ImageResizer;
import com.ajs.fileChooser.FilePreviewer;
import com.ajs.fileChooser.MonFilter;
import com.ajs.model.User;
import com.ajs.test.FullDemo;
import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class RegistrationDialog extends JDialog {
    private static User user;
    private Map<String, JComponent> fieldMap;
    private Map<JComponent, JPanel> componentJPanelMap;
    private static final String REGISTRARTION_TITLE = "Enrégistrement";
    private static final String MODIFY_REGISTRARTION_TITLE = "Modification";
    private static RegistrationDialog instance = new RegistrationDialog(null, REGISTRARTION_TITLE, true);
    private JButton okBtn;

    public static final int OK_OPTION = 0;
    public static final int CANCEL_OPTION = -1;
    private static int response;

    private static final String FIRST_NAME = "Nom";
    private static final String LAST_NAME = "Prénoms";
    private static final String BIRTH_DATE = "Date de naissance";
    private static final String EMAIL = "Email";
    private static final String PASSWORD = "Password";
    private static final String LEGEND = "Légende";
    private static AvatarPane avatarPane;

    private static Color validFieldPaneBgColor;
    private static Color invalidFieldPaneBgColor;

    public RegistrationDialog(JFrame parent, String title, boolean modal) {
        super(parent, title, modal);
        setIconImage(ImageResizer.scaleImage(new ImageIcon("images/icon/register-icon.jpg").getImage(), 20, 20));
        setResizable(false);
        validFieldPaneBgColor = Color.GREEN;
        invalidFieldPaneBgColor = Color.RED;
        Container container = getContentPane();

        fieldMap = new LinkedHashMap<>();
        componentJPanelMap = new LinkedHashMap<>();
        fieldMap.put(FIRST_NAME, new JTextField(30));
        fieldMap.put(LAST_NAME, new JTextField(30));

        URL dateImageURL = FullDemo.class.getResource("/images/datepickerbutton1.png");
        Image dateExampleImage = Toolkit.getDefaultToolkit().getImage(dateImageURL);
        ImageIcon dateExampleIcon = new ImageIcon(dateExampleImage);
        // Create the date picker, and apply the image icon.

        DatePickerSettings dateSettings = new DatePickerSettings();
        dateSettings.setAllowKeyboardEditing(false);
        dateSettings.setAllowEmptyDates(false);
        fieldMap.put(BIRTH_DATE, new DatePicker(dateSettings));
        LocalDate today = LocalDate.now();
        LocalDate startDate = LocalDate.of(today.getYear() - 5, Month.JANUARY, 1);
        dateSettings.setDateRangeLimits(startDate.minusDays(100 * 356), startDate.plusDays(0));

        startDate = LocalDate.of(today.getYear() - 6, Month.JANUARY, 1);
        ((DatePicker) fieldMap.get(BIRTH_DATE)).setDate(startDate);

        fieldMap.get(BIRTH_DATE).setPreferredSize(new Dimension(310, 40));

        JButton datePickerButton = ((DatePicker) fieldMap.get(BIRTH_DATE)).getComponentToggleCalendarButton();
        datePickerButton.setText("");
        datePickerButton.setIcon(dateExampleIcon);

        fieldMap.put(EMAIL, new JTextField(30));
        fieldMap.put(PASSWORD, new JPasswordField(30));
        fieldMap.put(LEGEND, new JTextField(30));

        JPanel center = new JPanel();
        container.add(center, BorderLayout.CENTER);

        BoxLayout boxLayout = new BoxLayout(center, BoxLayout.Y_AXIS);
        center.setLayout(boxLayout);

        avatarPane = new AvatarPane();
        FlowLayout fl = new FlowLayout();
        fl.setAlignment(FlowLayout.RIGHT);
        JPanel avatarPanel = new JPanel(fl);
        avatarPanel.add(avatarPane);
        center.add(avatarPanel);

        fieldMap.forEach((key, val) -> {
            center.add(createFieldContainer(key, val));
        });

        JLabel label = new JLabel("(*) champ obligatoire");
        JPanel panel = new JPanel();
        panel.add(label);
        center.add(panel);

        label = new JLabel("Champ valide");
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setPreferredSize(new Dimension(235, 40));
        label.setOpaque(true);
        label.setForeground(Color.white);
        label.setBackground(validFieldPaneBgColor);

        JLabel label1 = new JLabel("Champ invalide");
        label1.setHorizontalAlignment(JLabel.CENTER);
        label1.setPreferredSize(new Dimension(235, 40));
        label1.setOpaque(true);
        label1.setForeground(Color.white);
        label1.setBackground(invalidFieldPaneBgColor);

        panel = new JPanel();
        panel.add(label);
        panel.add(label1);

        center.add(panel);

        JPanel bottom = new JPanel();
        container.add(bottom, BorderLayout.SOUTH);
        okBtn = new JButton("Enregistrer");
        bottom.add(okBtn);

        okBtn.addActionListener(new OkAction("Ok Action"));

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                DatePicker datePicker = (DatePicker) fieldMap.get(BIRTH_DATE);
                datePicker.closePopup();
            }
        });

        pack();
    }

    private JPanel createFieldContainer(String labelText, JComponent field) {
        field.setPreferredSize(new Dimension(310, 40));
        JPanel validPanel = new JPanel();
        componentJPanelMap.put(field, validPanel);
        validPanel.setPreferredSize(new Dimension(5, 36));
        FlowLayout fl = new FlowLayout();
        fl.setAlignment(FlowLayout.LEFT);
        JPanel panel = new JPanel();
        JLabel label = new JLabel(!LEGEND.equals(labelText) ? "* " + labelText : labelText);
        label.setOpaque(true);

        label.setHorizontalAlignment(JLabel.LEFT);
        label.setLabelFor(field);
        label.setPreferredSize(new Dimension(150, 40));
        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                field.requestFocusInWindow();
            }
        });
        if (!labelText.equals(BIRTH_DATE)) {
            field.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    super.focusGained(e);
                }

                @Override
                public void focusLost(FocusEvent e) {
                    String text = ((JTextField) field).getText();
                    if (text.isBlank()) {
                        validPanel.setBackground(invalidFieldPaneBgColor);
                    } else {
                        validPanel.setBackground(validFieldPaneBgColor);
                    }
                }
            });
            handleFieldDocumentListener((JTextField) field, validPanel);
        }

        panel.add(label);

        if (!labelText.equals(PASSWORD)) {
            panel.add(field);
        } else {
            final boolean[] isPasswordShow = {false};
            JPanel panel1 = new JPanel(null);
            panel1.setPreferredSize(new Dimension(310, 40));
            Insets insets = panel1.getInsets();

            JTextField field1 = new JTextField(30);
            field1.setPreferredSize(new Dimension(310, 40));
            field1.setBounds(insets.left, insets.top, 310, 40);
            field1.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    ((JTextField) field).setText(field1.getText());
                }
            });
            handleFieldDocumentListener(field1, validPanel);

            field.setBounds(insets.left, insets.top, 310, 40);
            field.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    field1.setText(((JTextField) field).getText());
                }
            });

            JButton btn = new JButton("");
            Icon showIcon = new ImageIcon(ImageResizer.scaleImage(new ImageIcon(DirectoriesPath.getIconPath()+"/show-password-icon.jpg").getImage(), 30, 30));
            Icon hideIcon = new ImageIcon(ImageResizer.scaleImage(new ImageIcon(DirectoriesPath.getIconPath()+"/hide-password-icon.jpg").getImage(), 30, 30));
            btn.setIcon(showIcon);
            btn.setPreferredSize(new Dimension(40, 40));
            btn.setBounds((310 + insets.left) - 40, insets.top, 40, 40);
            btn.addActionListener((e) -> {
                isPasswordShow[0] = !isPasswordShow[0];
                if (isPasswordShow[0]) {
                    btn.setIcon(hideIcon);
                    field1.setText(((JTextField) field).getText());
                    field.setVisible(false);
                    field1.setVisible(true);

                } else {
                    btn.setIcon(showIcon);
                    field1.setText(((JTextField) field).getText());
                    field.setVisible(true);
                    field1.setVisible(false);
                }
            });

            panel1.add(btn);
            panel1.add(field);
            panel1.add(field1);
            panel.add(panel1);
        }

        panel.add(validPanel);
        return panel;
    }

    private void handleFieldDocumentListener(JTextField field, JPanel panel) {
        field.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                String text = field.getText();
                if (text.isBlank()) {
                    panel.setBackground(invalidFieldPaneBgColor);
                } else {
                    panel.setBackground(validFieldPaneBgColor);
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                String text = field.getText();
                if (text.isBlank()) {
                    panel.setBackground(invalidFieldPaneBgColor);
                } else {
                    panel.setBackground(validFieldPaneBgColor);
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }
        });
    }

    public static int showRegisterDialog() {
        user = new User();
        response = CANCEL_OPTION;
        reset(false);
        instance.setVisible(true);
        return response;
    }

    public static int showEditDialog(User user) {
        RegistrationDialog.user = user;
        response = CANCEL_OPTION;
        reset(true);
        instance.setVisible(true);
        return response;
    }

    private static void reset(boolean isEdit) {
        if (!isEdit) {
            instance.setTitle(REGISTRARTION_TITLE);
            instance.fieldMap.forEach((key, val) -> {
                if (!key.equals(BIRTH_DATE)) {
                    ((JTextField) val).setText("");
                }
            });
            instance.componentJPanelMap.forEach((key, val) -> {
                val.setBackground((new JPanel()).getBackground());
            });
        } else {
            instance.setTitle(MODIFY_REGISTRARTION_TITLE);
            avatarPane.setImage(user.getAvatar());
            ((JTextField) instance.fieldMap.get(FIRST_NAME)).setText(user.getFirstName());
            ((JTextField) instance.fieldMap.get(LAST_NAME)).setText(user.getLastName());

            LocalDate localDate = user.getBirthDate().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            ((DatePicker) instance.fieldMap.get(BIRTH_DATE)).setDate(localDate);
            ((JTextField) instance.fieldMap.get(EMAIL)).setText(user.getEmail());
            ((JPasswordField) instance.fieldMap.get(PASSWORD)).setText(user.getPassword());
            ((JTextField) instance.fieldMap.get(LEGEND)).setText(user.getLegend());

            instance.componentJPanelMap.forEach((key, val) -> {
                val.setBackground(validFieldPaneBgColor);
            });
        }
    }

    private static class AvatarPane extends JPanel {
        private Image image;
        private int width = 150;
        private int height = 200;
        private String placeholder = "Image de profile";
        private String avatarPath;

        AvatarPane() {
            setPreferredSize(new Dimension(width, height));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            JFileChooser fileChooser;
            fileChooser = new JFileChooser();
            fileChooser.setMultiSelectionEnabled(false);
            fileChooser.setAcceptAllFileFilterUsed(false);
            String[] suffixesImages = {"jpeg", "jpg", "png"};
            MonFilter mfi = new MonFilter(suffixesImages, "les fichiers image (*.png, *.jpeg");
            FilePreviewer previewer = new FilePreviewer(fileChooser, null);
            fileChooser.setAccessory(previewer);
            fileChooser.addChoosableFileFilter(mfi);

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    int rep = fileChooser.showOpenDialog(null);
                    if (rep == JFileChooser.APPROVE_OPTION) {
                        try {
                            File file = fileChooser.getSelectedFile();
                            avatarPath = file.getPath();
                            image = ImageIO.read(file.getAbsoluteFile());
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.white);
            g.fillRoundRect(0, 0, width, height, 20, 20);
            g.setColor(Color.BLACK);
            FontMetrics fm = g.getFontMetrics();
            g.drawString(placeholder, (width - fm.stringWidth(placeholder)) / 2, (height - fm.getHeight()) / 2);
            if (image != null) {
                g.drawImage(image, 5, 5, width - 10, height - 10, null);
            }
        }

        public String getAvatarPath() {
            return avatarPath;
        }

        private void setImage(Image image){
            this.image = image;
            repaint();
        }

        private Image getImage(){
            return image;
        }
    }

    public static RegistrationDialog getInstance() {
        return instance;
    }

    public static User getUser() {
        return user;
    }

    private class OkAction extends AbstractAction {
        OkAction(String name) {
            super(name);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            response = OK_OPTION;
            final boolean[] isOk = {true};
            User user1 = new User();
            user1.setAvatar(avatarPane.getImage());

            fieldMap.forEach((key, val) -> {
                if (BIRTH_DATE.equals(key)) {
                    Date date = java.sql.Date.valueOf(((DatePicker) val).getDate());
                    user1.setBirthDate(date);
                } else if (PASSWORD.equals(key)) {
                    JPasswordField passwordField = (JPasswordField) val;
                    String pass = String.valueOf(passwordField.getPassword());
                    JPanel panel = componentJPanelMap.get(val);
                    if (!pass.isBlank()) {
                        user1.setPassword(pass);
                        panel.setBackground(validFieldPaneBgColor);
                    } else {
                        isOk[0] = false;
                        panel.setBackground(invalidFieldPaneBgColor);
                    }
                } else {
                    JTextField field = (JTextField) val;
                    String text = field.getText();
                    JPanel panel = componentJPanelMap.get(val);
                    if (!text.isBlank()) {
                        panel.setBackground(validFieldPaneBgColor);
                    } else {
                        isOk[0] = false;
                        panel.setBackground(invalidFieldPaneBgColor);
                    }
                    switch (key) {
                        case FIRST_NAME:
                            user1.setFirstName(field.getText());
                            break;
                        case LAST_NAME:
                            user1.setLastName(field.getText());
                            break;
                        case EMAIL:
                            user1.setEmail(field.getText());
                            break;
                        case LEGEND:
                            user1.setLegend(field.getText());
                            break;
                    }
                }
            });

            if (isOk[0]) {
                RegistrationDialog.user.setAvatar(user1.getAvatar());
                RegistrationDialog.user.setFirstName(user1.getFirstName());
                RegistrationDialog.user.setLastName(user1.getLastName());
                RegistrationDialog.user.setBirthDate(user1.getBirthDate());
                RegistrationDialog.user.setEmail(user1.getEmail());
                RegistrationDialog.user.setPassword(user1.getPassword());
                RegistrationDialog.user.setLegend(user1.getLegend());
                RegistrationDialog.user.setAvatarPath(avatarPane.avatarPath);
                instance.setVisible(false);
            }
        }
    }
}

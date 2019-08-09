package com.ajs.test;

import com.ajs.UI.RegistrationDialog;
import com.ajs.model.User;

import javax.swing.*;
import java.awt.*;
import java.time.Instant;
import java.util.Date;

public class RegistrationDialogTest {
    public static void main(String[] args) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        }catch (Exception e){}

        JFrame frame = new JFrame();
        frame.setSize(new Dimension(400,300));
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JButton btn = new JButton("Click");
        btn.addActionListener((e)->{
            User user = new User();
            user.setAvatar(new ImageIcon("sauvegarde/amenjs.png").getImage());
            user.setFirstName("Djè");
            user.setLastName("Bi Tizié Ange Eric");
            user.setBirthDate(new Date(Instant.parse("1992-12-17T10:12:35Z").toEpochMilli()));
            user.setEmail("amenjsajs@gmail.com");
            user.setPassword("123123");
            user.setLegend("L'amour plus fort que la haine");
            //RegistrationDialog.showEditDialog(user);
            RegistrationDialog.showRegisterDialog();
//            System.out.println(RegistrationDialog.getUser());
        });
        frame.add(btn,BorderLayout.SOUTH);
        frame.setVisible(true);
    }
}

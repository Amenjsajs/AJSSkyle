package com.ajs.test;

import com.ajs.components.MessageShowPane;
import com.ajs.components.UserPresenceStatusPane;
import com.ajs.model.Message;
import com.ajs.model.User;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

public class Fenetre extends JFrame {
    Fenetre(){
        super("Essai");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(new Dimension(900,500));
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        getContentPane().add(panel);

        Image avatar = new ImageIcon("images/amenjs.png").getImage();

        User user = new User();
        user.setAvatar(avatar);
        user.setFirstName("DJE");
        user.setLastName("Bi Tizié Ange Eric");
        UserPresenceStatusPane userActivePane = new UserPresenceStatusPane(user);
        UserPresenceStatusPane userActivePane1 = new UserPresenceStatusPane(user);

        User user1 = new User();
        user1.setFirstName("Amen");
        user1.setLastName("JS");
        UserPresenceStatusPane userActivePane2 = new UserPresenceStatusPane(user1);
        User user2 = new User();
        user2.setFirstName("DJE");
        user2.setLastName("Zoumian");
        UserPresenceStatusPane userActivePane3 = new UserPresenceStatusPane(user2);
        panel.add(userActivePane);
        panel.add(userActivePane1);
        panel.add(userActivePane2);
        panel.add(userActivePane3);

        String msg = "Nous sommes des enfants de Dieu par Jésus-Christ notre Sauveur";

        Message message = new Message();
        message.setSender(user1);
        message.setReceiver(user2);
        message.setContent(msg);
        message.setMsgHeight(32);
        message.setCreatedDate(new Date());

        Calendar calendar = Calendar.getInstance();
        MessageShowPane messageShowPane = new MessageShowPane(message,FlowLayout.RIGHT);

        //MessageShowPane messageShowPane1 = new MessageShowPane(user1,"Hello",16,FlowLayout.LEFT);

        panel.add(messageShowPane);
        //panel.add(messageShowPane1);

//        int i=0;
//        do {
//            i+=32;
//            BufferedImage trans = new BufferedImage(i, i/2, BufferedImage.TYPE_INT_ARGB);
//            Graphics2D g = (Graphics2D) trans.getGraphics();
//            g.setColor(Color.white);
//            g.fillRect(0,0,i,i);
//            saveImage(trans,"bg/bg"+i,"png");
//        }while (i<=700);

        setVisible(true);
    }

    private void saveImage(BufferedImage image, String imageName, String ext) {
        File f = new File(String.format("images/%s.%s", imageName, ext));
        try {
            if (ImageIO.write(image, ext, f)) {
                System.out.println("Avatar Ok");
            } else {
                System.out.println("Avatar Not Ok");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Fenetre();
    }
}

package com.ajs.components;

import com.ajs.model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class UserAvatarPane extends JPanel {
    private final int SIZE = 50;
    private User user;

    public UserAvatarPane(User user) {
        this.user = user;
        setBackground(new Color(0, 0, 0, 0));
        setPreferredSize(new Dimension(SIZE, SIZE));
        createUserProfileAvatar();
    }

    private void createUserProfileAvatar() {
        int width = 250;
        int height = 250;
        Image cadre = new ImageIcon(DirectoriesPath.getImagesPath()+"/empty_round.png").getImage();
        cadre = ImageResizer.scaleImage(cadre, width, height);
        BufferedImage trans = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D) trans.getGraphics();
        g.drawImage(cadre, 0, 0, null);

        BufferedImage avatarTrans = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D avatarG2d = (Graphics2D) avatarTrans.getGraphics();
        if (user.getAvatar() != null) {
            Image avatar = user.getAvatar();
            avatar = ImageResizer.scaleImage(avatar, width, height);
            avatarG2d.drawImage(avatar, 0, 0, null);
        } else {
            avatarG2d.setColor(new Color(80, 80, 80));
            avatarG2d.fillRect(0, 0, width, height);
            avatarG2d.setColor(Color.white);
            avatarG2d.setFont(new Font("Arial", Font.BOLD, 80));
            FontMetrics fm = avatarG2d.getFontMetrics();
            String str = user.getFirstName().substring(0, 1) + user.getLastName().substring(0, 1);
            avatarG2d.drawString(str, (width - fm.stringWidth(str)) / 2, (height + fm.getHeight()) / 2);
        }

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (trans.getRGB(i, j) != 0) {
                    avatarTrans.setRGB(i, j, 0x00000000);
                }
            }
        }
        user.setAvatar(avatarTrans);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(user.getAvatar(), 0, 0, SIZE, SIZE, null);
        g.dispose();
    }
}

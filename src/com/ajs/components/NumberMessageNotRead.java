package com.ajs.components;

import javax.swing.*;
import java.awt.*;

public class NumberMessageNotRead extends JPanel {
    private int number = 0;
    private int width = 20;
    private int height = 20;
    private Image bg;

    public NumberMessageNotRead() {
        setBackground(new Color(0,0,0,0));
        setPreferredSize(new Dimension(width, height));
        bg = new ImageIcon("images/red_round.png").getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (number == 0) {
            return;
        }

        FontMetrics fm = g.getFontMetrics();
        int numberWidth = fm.stringWidth(number + "");
        int numberHeight = fm.getHeight();

        if (numberWidth > width) {
            width = numberWidth + 5;
        }

        g.drawImage(bg, 0, 0, width, height, null);
        g.setColor(Color.white);
        g.drawString(number + "", (width - numberWidth) / 2, (height + numberHeight / 2) / 2);
    }

    public void increaseNumber() {
        number++;
        repaint();
    }

    public void decreaseNumber() {
        if (number > 0) {
            number--;
            repaint();
        }
    }

    public void reset() {
        number = 0;
        repaint();
    }
}

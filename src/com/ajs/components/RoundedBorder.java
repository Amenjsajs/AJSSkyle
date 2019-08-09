package com.ajs.components;

import javax.swing.border.AbstractBorder;
import java.awt.*;

public class RoundedBorder extends AbstractBorder {

    public Insets getBorderInsets(Component c, Insets insets) {
        insets.left = insets.top = insets.right = insets.bottom = 5;
        return insets;
    }

    public void paintBorder(Component c, Graphics g, int x, int y,
                            int width, int height) {
        int w = width;
        int h = height;

        g.translate(x, y);
        g.setColor(c.getBackground().darker());
        g.drawRoundRect(0, 0, w - 1, h - 1, 8, 8);
        g.translate(-x, -y);
    }

    public boolean isBorderOpaque() {
        return true;
    }
}
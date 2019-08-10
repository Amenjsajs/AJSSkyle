package com.ajs.components;

import javax.swing.*;
import java.awt.*;

public class DotAnimated extends JPanel {
    private int size;
    private long delay;
    private int margin = 0;
    private Dot[] dots;
    private int panelWidth;
    private Image image;
    private int nbDot;
    private boolean show;

    public DotAnimated(int nbDot, int size, long delay, int margin) {
        this.size = size;
        this.delay = delay;
        this.dots = new Dot[nbDot];
        this.nbDot = nbDot;
        this.margin = margin;
        this.image = new ImageIcon("images/gray_round.png").getImage();

        panelWidth = nbDot * (size + margin) + margin;
        setPreferredSize(new Dimension(panelWidth, size));
        setOpaque(false);
        for (int i = 0; i < nbDot; i++) {
            dots[i] = new Dot();
        }
        positionDot();
    }

    public DotAnimated(int nbDot, int size, long delay) {
        this(nbDot, size, delay, 0);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (show) {
            for (Dot dot : dots) {
                g.drawImage(image, dot.getX(), dot.getY(), dot.getSize(), dot.getSize(), null);
            }
        }
        repaint();
    }

    private void animate() {
        Thread t = new Thread(() -> {
            try {
                while (show) {
                    int i = 0;
                    for (Dot dot : dots) {
                        if (dot.isIncrease()) {
                            dot.setSize(dot.getSize() + 1);
                            if (dot.getSize() >= size) {
                                dot.setIncrease(false);
                            }
                        } else {
                            dot.setSize(dot.getSize() - 1);
                            if (dot.getSize() <= 0) {
                                dot.setIncrease(true);
                            }
                        }
                        Point p = getCoords(i++, dot.getSize());
                        dot.setX(p.x);
                        dot.setY(p.y);
                    }
                    Thread.sleep(delay);
                }
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        });
        t.start();
    }

    private void positionDot() {
        for (int i = 0; i < nbDot; i++) {
            double factor = (i + 1) / (double) nbDot;
            int dsize = (int) (size * factor);

            Point p = getCoords(i, dsize);
            Dot dot = dots[i];
            dot.setX(p.x);
            dot.setY(p.y);
            dot.setSize(dsize);
        }
    }

    private Point getCoords(int dotIndex, int dotSize) {
        int x = margin + (size + margin) * dotIndex + (size - dotSize) / 2;
        int y = (size - dotSize) / 2;
        return new Point(x, y);
    }

    public void start() {
        if (!show) {
            show = true;
            positionDot();
            animate();
        }
    }

    public void stop() {
        Thread t = new Thread(() -> {
            try {
                int i = 0;
                while (i++ < 1) {
                    Thread.sleep(3000);
                }
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            } finally {
                show = false;
            }
        });
        t.start();
    }

    private final class Dot {
        private int x;
        private int y;
        private int size;
        private boolean increase;

        Dot() {
        }

        Dot(int x, int y, int size) {
            this.x = x;
            this.y = y;
            this.size = size;
            this.increase = false;
        }

        int getX() {
            return x;
        }

        void setX(int x) {
            this.x = x;
        }

        int getY() {
            return y;
        }

        void setY(int y) {
            this.y = y;
        }

        int getSize() {
            return size;
        }

        void setSize(int size) {
            this.size = size;
        }

        boolean isIncrease() {
            return increase;
        }

        void setIncrease(boolean increase) {
            this.increase = increase;
        }

        @Override
        public String toString() {
            return String.format("%s[%s, %d, %d]", Dot.class.getName(), x, y, size);
        }
    }
}
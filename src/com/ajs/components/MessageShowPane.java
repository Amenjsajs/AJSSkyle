package com.ajs.components;

import com.ajs.model.Message;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Calendar;

public class MessageShowPane extends JPanel {
    private UserAvatarPane userAvatarPane;
    private Message message;
    public MessageShowPane(Message message, int alignment){
        userAvatarPane = new UserAvatarPane(message.getReceiver());
        FlowLayout fl = new FlowLayout();
        fl.setAlignment(alignment);
        fl.setAlignOnBaseline(true);
        setLayout(fl);

        JTextArea msgArea = new JTextArea();
        msgArea.setOpaque(false);
        msgArea.setEditable(false);
        msgArea.setText(message.getContent());
        msgArea.setMargin(new Insets(5,5,5,5));
        Dimension dim = msgArea.getPreferredSize();
        if (dim.width >= 700) {
            msgArea.setLineWrap(true);
            dim.setSize(700, message.getMsgHeight() + 16);
        }
        msgArea.setPreferredSize(dim);

        BorderLayout bl = new BorderLayout();
        JPanel panel = new JPanel(bl);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(message.getCreatedDate());
        String date = String.format("%d:%d",calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE));

        JLabel userFirstName;
        if(alignment == FlowLayout.LEFT) {
            userFirstName = new JLabel(String.format("%s, %s",message.getReceiver().getFirstName(), date));
            JPanel userAvatarPanePanel = new JPanel();
            userAvatarPanePanel.add(userAvatarPane);
            panel.add(userAvatarPanePanel, BorderLayout.WEST);
        }else{
            userFirstName = new JLabel(date,JLabel.RIGHT);
        }
        bl = new BorderLayout();
        JPanel areaPanel = new JPanel(bl);
        areaPanel.add(userFirstName,BorderLayout.NORTH);

//        BorderRound borderRound = new BorderRound(dim.width,dim.height);
//        borderRound.add(msgArea);
        JPanel borderRoundedPanel = new JPanel();
        borderRoundedPanel.setBorder(BorderFactory.createCompoundBorder(new RoundedBorder(), BorderFactory.createEmptyBorder()));
        borderRoundedPanel.add(msgArea);
        areaPanel.add(borderRoundedPanel,BorderLayout.CENTER);
        panel.add(areaPanel);
        add(panel);
    }

    private class BorderRound extends JPanel{
        private Image bg;
        private int width;
        private int height;
        BorderRound(int width, int height){
            this.width = width+10;
            this.height = height+10;
            BufferedImage trans = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = (Graphics2D) trans.getGraphics();
            g.setColor(Color.white);
            g.fillRoundRect(0,0,width,height,20,20);
            bg = trans;
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(bg,0,0,width,height,null);
        }
    }
}

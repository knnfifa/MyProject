package com.project.components;

import javax.swing.JButton;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Color;
import java.awt.geom.RoundRectangle2D;

public class RoundedButton extends JButton {
    private int arcSize = 20;  // ขนาดของการโค้งมน

    public RoundedButton(String text) {
        super(text);  // ให้ข้อความแสดงในปุ่ม
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);  // ทำให้ขอบนุ่มนวล

        // วาดกรอบโค้งมน
        g2.setColor(getBackground());
        g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), arcSize, arcSize));  // ขนาดกรอบโค้งมน

        super.paintComponent(g);  // วาดข้อความในปุ่ม
        g2.dispose();  // ปล่อยทรัพยากร
    }

    @Override
    public void setBackground(Color bg) {
        super.setBackground(bg);  // กำหนดสีพื้นหลัง
    }
}

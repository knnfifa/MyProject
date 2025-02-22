package com.project;

import javax.swing.*;
import java.awt.*;

public class WeatherAppGui extends JFrame {
    public WeatherAppGui() {
        super("Weather App");

        // ปิดโปรแกรมเมื่อกดปิด
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // ตั้งค่าขนาด GUI เป็นแนวนอน
        setSize(800, 450); // Landscape mode

        // โหลด GUI ไว้ที่กึ่งกลางหน้าจอ
        setLocationRelativeTo(null);

        // ใช้ BorderLayout หรือ GridLayout เพื่อจัดให้อยู่ตรงกลาง
        setLayout(new BorderLayout());

        // ป้องกันการ Resize
        setResizable(false);

        addGuiComponents();
    }

    private void addGuiComponents() {
        // สร้าง JPanel หลักที่เป็นกล่อง
        JPanel boxPanel = new JPanel();
        boxPanel.setPreferredSize(new Dimension(600, 300)); // กำหนดขนาดกล่อง
        boxPanel.setBackground(Color.LIGHT_GRAY); // ตั้งค่าสีของกล่อง

        // จัดให้อยู่ตรงกลาง
        add(boxPanel, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            WeatherAppGui app = new WeatherAppGui();
            app.setVisible(true);
        });
    }
}

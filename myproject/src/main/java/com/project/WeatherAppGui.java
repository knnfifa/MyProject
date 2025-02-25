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

        // ใช้ BorderLayout เพื่อจัดให้อยู่ตรงกลาง
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
        boxPanel.setLayout(null); // กำหนด Layout เป็น null เพื่อใช้ setBounds

        // สร้าง JTextField สำหรับค้นหาสถานที่ (ย้ายไปฝั่งขวา)
        JTextField searchTextField = new JTextField();
        searchTextField.setBounds(380, 15, 250, 45); // จัดตำแหน่งไปฝั่งขวา
        searchTextField.setFont(new Font("Dialog", Font.PLAIN, 14));

        // สร้างปุ่มแว่นขยาย 🔍
        JButton searchButton = new JButton("🔍");
        searchButton.setBounds(640, 15, 50, 45); // ปุ่มอยู่ขวาของกล่องค้นหา
        searchButton.setFont(new Font("Dialog", Font.PLAIN, 18));
        searchButton.setFocusPainted(false); // เอาเส้นโฟกัสออก
        searchButton.setBackground(Color.WHITE); // ตั้งค่าพื้นหลังปุ่ม
        searchButton.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1)); // เพิ่มเส้นขอบ

        // ✅ เพิ่ม JTextField และปุ่มลงใน JPanel
        boxPanel.add(searchTextField);
        boxPanel.add(searchButton);

        // ✅ เพิ่ม JPanel ลงใน JFrame
        add(boxPanel, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            WeatherAppGui app = new WeatherAppGui();
            app.setVisible(true);
        });
    }
}

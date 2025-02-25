package com.project;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WeatherAppGui extends JFrame {
    private JLabel locationLabel; // ✅ ตัวแปรสำหรับแสดงชื่อเมือง
    private JLabel weatherInfoLabel; // ✅ ตัวแปรสำหรับแสดงอุณหภูมิและสภาพอากาศ

    public WeatherAppGui() {
        super("Weather App");

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 450);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setResizable(false);

        addGuiComponents();
    }

    private void addGuiComponents() {
        JPanel boxPanel = new JPanel();
        boxPanel.setPreferredSize(new Dimension(600, 300));
        boxPanel.setBackground(Color.LIGHT_GRAY);
        boxPanel.setLayout(null);

        // ✅ โหลดรูปเมฆ
        String imagePath = "C:/Users/11/MyProject/myproject/src/main/assets/weatherapp_images/cloudy.png";
        ImageIcon icon = loadTransparentImage(imagePath, 150, 150);

        // ✅ แสดงรูปภาพที่ฝั่งซ้าย
        JLabel weatherConditionImage = new JLabel(icon);
        weatherConditionImage.setBounds(100, 80, 150, 150);
        weatherConditionImage.setOpaque(false);
        boxPanel.add(weatherConditionImage);

        // ✅ เพิ่ม JLabel สำหรับแสดงชื่อเมือง (อยู่เหนือรูปภาพ)
        locationLabel = new JLabel("Enter a city name", SwingConstants.CENTER);
        locationLabel.setBounds(50, 40, 250, 30);
        locationLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
        locationLabel.setForeground(Color.BLACK);
        boxPanel.add(locationLabel);

        // ✅ เพิ่ม JLabel สำหรับแสดงอุณหภูมิและสภาพอากาศ (อยู่ใต้รูปภาพ)
        weatherInfoLabel = new JLabel("25°C | Cloudy", SwingConstants.CENTER);
        weatherInfoLabel.setBounds(100, 240, 150, 30); // ✅ จัดให้อยู่ตรงกลางใต้รูป
        weatherInfoLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
        weatherInfoLabel.setForeground(Color.BLACK);
        boxPanel.add(weatherInfoLabel); // ✅ ไม่มีกรอบหรือพื้นหลัง

        // ✅ เพิ่ม JTextField สำหรับพิมพ์ชื่อเมือง
        JTextField searchTextField = new JTextField();
        searchTextField.setBounds(380, 15, 250, 45);
        searchTextField.setFont(new Font("Arial", Font.PLAIN, 14));
        boxPanel.add(searchTextField);

        // ✅ โหลดรูปแว่นขยาย
        String searchIconPath = "C:/Users/11/MyProject/myproject/src/main/assets/weatherapp_images/search.png";
        ImageIcon searchIcon = loadTransparentImage(searchIconPath, 30, 30);

        // ✅ ปุ่มค้นหา 🔍
        JButton searchButton = new JButton(searchIcon);
        searchButton.setBounds(640, 15, 50, 45);
        searchButton.setFocusPainted(false);
        searchButton.setBackground(Color.WHITE);
        searchButton.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        boxPanel.add(searchButton);

        // ✅ Event: กดปุ่ม 🔍 แล้วอัปเดตชื่อเมืองและอุณหภูมิ
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String location = searchTextField.getText();
                if (!location.trim().isEmpty()) {
                    locationLabel.setText(location);
                    weatherInfoLabel.setText("25°C | Cloudy"); // ✅ สามารถเปลี่ยนได้ตาม API จริง
                } else {
                    locationLabel.setText("Enter a city name");
                    weatherInfoLabel.setText("---");
                }
            }
        });

        add(boxPanel, BorderLayout.CENTER);
    }

    /** ✅ โหลดรูปและปรับขนาด พร้อมรองรับพื้นหลังโปร่งใส **/
    private ImageIcon loadTransparentImage(String path, int width, int height) {
        try {
            BufferedImage img = ImageIO.read(new File(path));
            Image scaledImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImg);
        } catch (IOException e) {
            System.out.println("❌ Image not found at: " + path);
            return null;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            WeatherAppGui app = new WeatherAppGui();
            app.setVisible(true);
        });
    }
}

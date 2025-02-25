package com.project;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class WeatherAppGui extends JFrame {
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

        //ใช้ BufferedImage เพื่อรองรับความโปร่งใส
        String imagePath = "C:/Users/11/MyProject/myproject/src/main/assets/weatherapp_images/cloudy.png";
        ImageIcon icon = loadTransparentImage(imagePath, 150, 150);

        //แสดงรูปภาพที่ฝั่งซ้าย
        JLabel weatherConditionImage = new JLabel(icon);
        weatherConditionImage.setBounds(100, 100, 150, 150);
        weatherConditionImage.setOpaque(false); // ✅ ทำให้ JLabel ไม่มีพื้นหลัง
        boxPanel.add(weatherConditionImage);

        //เพิ่ม JTextField
        JTextField searchTextField = new JTextField();
        searchTextField.setBounds(380, 15, 250, 45);
        searchTextField.setFont(new Font("Dialog", Font.PLAIN, 14));
        boxPanel.add(searchTextField);

        //เพิ่มปุ่มแว่นขยาย 
        JButton searchButton = new JButton("🔍");
        searchButton.setBounds(640, 15, 50, 45);
        searchButton.setFont(new Font("Dialog", Font.PLAIN, 18));
        searchButton.setFocusPainted(false);
        searchButton.setBackground(Color.WHITE);
        searchButton.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        boxPanel.add(searchButton);

        add(boxPanel, BorderLayout.CENTER);
    }

    /**โหลดรูปและปรับขนาด พร้อมรองรับพื้นหลังโปร่งใส**/
    private ImageIcon loadTransparentImage(String path, int width, int height) {
        try {
            BufferedImage img = ImageIO.read(new File(path));
            Image scaledImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImg);
        } catch (IOException e) {
            System.out.println("❌ ไม่พบไฟล์รูปภาพที่: " + path);
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

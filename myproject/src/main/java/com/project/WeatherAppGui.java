package com.project;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.project.models.WeatherInfo;


public class WeatherAppGui extends JFrame {
    private JLabel locationLabel;
    private JLabel weatherInfoLabel;
    private JLabel weatherConditionImage;
    private JTextField searchTextField;
    private JButton searchButton;

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

        // โหลดรูปเมฆเริ่มต้น
        String defaultImagePath = "C:/Users/11/MyProject/myproject/src/main/assets/weatherapp_images/cloudy.png";
        ImageIcon defaultIcon = loadTransparentImage(defaultImagePath, 150, 150);

        // แสดงรูปภาพสภาพอากาศ
        weatherConditionImage = new JLabel(defaultIcon);
        weatherConditionImage.setBounds(100, 80, 150, 150);
        boxPanel.add(weatherConditionImage);

        // เพิ่ม JLabel สำหรับแสดงชื่อเมือง
        locationLabel = new JLabel("Enter a city name", SwingConstants.CENTER);
        locationLabel.setBounds(50, 40, 250, 30);
        locationLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
        locationLabel.setForeground(Color.BLACK);
        boxPanel.add(locationLabel);

        // เพิ่ม JLabel สำหรับแสดงข้อมูลอากาศ
        weatherInfoLabel = new JLabel("---", SwingConstants.CENTER);
        weatherInfoLabel.setBounds(100, 240, 150, 30);
        weatherInfoLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
        weatherInfoLabel.setForeground(Color.BLACK);
        boxPanel.add(weatherInfoLabel);

        // เพิ่ม JTextField สำหรับพิมพ์ชื่อเมือง
        searchTextField = new JTextField();
        searchTextField.setBounds(380, 15, 250, 45);
        searchTextField.setFont(new Font("Arial", Font.PLAIN, 14));
        boxPanel.add(searchTextField);

        // โหลดรูปแว่นขยาย
        String searchIconPath = "C:/Users/11/MyProject/myproject/src/main/assets/weatherapp_images/search.png";
        ImageIcon searchIcon = loadTransparentImage(searchIconPath, 30, 30);

        // ปุ่มค้นหา
        searchButton = new JButton(searchIcon);
        searchButton.setBounds(640, 15, 50, 45);
        searchButton.setFocusPainted(false);
        searchButton.setBackground(Color.WHITE);
        searchButton.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        boxPanel.add(searchButton);

        // Event Listener ปุ่มค้นหา
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fetchWeatherData();
            }
        });

        add(boxPanel, BorderLayout.CENTER);
    }

    /** โหลดรูปและปรับขนาด พร้อมรองรับพื้นหลังโปร่งใส **/
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

    /** ดึงข้อมูลอากาศจาก API และอัปเดต GUI **/
    private void fetchWeatherData() {
        String location = searchTextField.getText().trim();
        if (location.isEmpty()) {
            locationLabel.setText("Enter a city name");
            weatherInfoLabel.setText("---");
            return;
        }

        try {
            WeatherInfo weatherData = WeatherService.getWeatherData(location);
            locationLabel.setText(weatherData.getCity());
            weatherInfoLabel.setText(String.format("%.1f°C | %s", weatherData.getTemperature(), weatherData.getWeatherCondition()));

            // เปลี่ยนรูปสภาพอากาศตามข้อมูลจริง
            updateWeatherIcon(weatherData.getWeatherCondition());
        } catch (Exception e) {
            locationLabel.setText("City not found");
            weatherInfoLabel.setText("---");
            System.out.println("❌ Error fetching weather data: " + e.getMessage());
        }
    }

    /** อัปเดตรูปสภาพอากาศ **/
    private void updateWeatherIcon(String weatherCondition) {
        String basePath = "C:/Users/11/MyProject/myproject/src/main/assets/weatherapp_images/";
        String imagePath = basePath + "cloudy.png"; // ค่าเริ่มต้น

        if (weatherCondition.equalsIgnoreCase("Clear")) {
            imagePath = basePath + "clear.png";
        } else if (weatherCondition.equalsIgnoreCase("Cloudy")) {
            imagePath = basePath + "cloudy.png";
        } else if (weatherCondition.equalsIgnoreCase("Rain")) {
            imagePath = basePath + "rain.png";
        } else if (weatherCondition.equalsIgnoreCase("Snow")) {
            imagePath = basePath + "snow.png";
        }

        weatherConditionImage.setIcon(loadTransparentImage(imagePath, 150, 150));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            WeatherAppGui app = new WeatherAppGui();
            app.setVisible(true);
        });
    }
}

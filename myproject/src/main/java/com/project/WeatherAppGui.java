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
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;



public class WeatherAppGui extends JFrame {
    private JLabel locationLabel;
    private JLabel weatherInfoLabel;
    private JLabel weatherConditionImage;
    private JTextField searchTextField;
    private JButton searchButton;
    private JLabel humidityIconLabel;
    private JLabel windIconLabel;
    private JLabel humidityLabel;
    private JLabel windSpeedLabel;
    private JLabel currentTimeLabel;  // เพิ่ม JLabel สำหรับแสดงเวลา
    

    public WeatherAppGui() {
        super("Weather App");

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500, 700);
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
        String defaultImagePath = "myproject/src/main/assets/weatherapp_images/cloudy.png";
        ImageIcon defaultIcon = loadTransparentImage(defaultImagePath, 150, 150);

        // แสดงรูปภาพสภาพอากาศ
        weatherConditionImage = new JLabel(defaultIcon);
        weatherConditionImage.setBounds(170, 200, 150, 150);
        boxPanel.add(weatherConditionImage);

        // เพิ่ม JLabel สำหรับแสดงชื่อเมือง
        locationLabel = new JLabel("Enter a city name", SwingConstants.CENTER);
        locationLabel.setBounds(110, 100, 250, 30);
        locationLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
        locationLabel.setForeground(Color.BLACK);
        boxPanel.add(locationLabel);

        // เพิ่ม JLabel สำหรับแสดงข้อมูลอากาศ
        weatherInfoLabel = new JLabel("---", SwingConstants.CENTER);
        weatherInfoLabel.setBounds(160, 400, 150, 30);
        weatherInfoLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
        weatherInfoLabel.setForeground(Color.BLACK);
        boxPanel.add(weatherInfoLabel);

        // เพิ่ม JLabel สำหรับแสดงเวลา
        currentTimeLabel = new JLabel("Last updated: --:--", SwingConstants.CENTER); // ป้ายเวลาปัจจุบัน
        currentTimeLabel.setBounds(90, 130, 300, 30);  // ตำแหน่งใต้ชื่อเมือง
        currentTimeLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        currentTimeLabel.setForeground(Color.BLACK);
        boxPanel.add(currentTimeLabel);

        // เพิ่ม JTextField สำหรับพิมพ์ชื่อเมือง
        searchTextField = new JTextField();
        searchTextField.setBounds(100, 15, 250, 45);
        searchTextField.setFont(new Font("Arial", Font.PLAIN, 14));
        boxPanel.add(searchTextField);

        // โหลดรูปแว่นขยาย
        String searchIconPath = "myproject/src/main/assets/weatherapp_images/search.png";
        ImageIcon searchIcon = loadTransparentImage(searchIconPath, 30, 30);

        // ปุ่มค้นหา
        searchButton = new JButton(searchIcon);
        searchButton.setBounds(350, 15, 50, 45);
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

        // โหลดรูปภาพสำหรับความชื้น
        String humidityIconPath = "myproject/src/main/assets/weatherapp_images/humidity.png";
        ImageIcon humidityIcon = loadTransparentImage(humidityIconPath, 30, 30);

        // โหลดรูปภาพสำหรับลม
        String windIconPath = "myproject/src/main/assets/weatherapp_images/wind.png";
        ImageIcon windIcon = loadTransparentImage(windIconPath, 30, 30);

        humidityIconLabel = new JLabel(humidityIcon);
        humidityIconLabel.setBounds(50, 500, 30, 30); //ไอคอนความชื้น
        boxPanel.add(humidityIconLabel);

        // สร้าง JLabel สำหรับข้อมูลความชื้น
        humidityLabel = new JLabel("Humidity: --%", SwingConstants.CENTER);
        humidityLabel.setBounds(35, 500, 250, 30); //// ข้อความความชื้น 
        humidityLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
        humidityLabel.setForeground(Color.BLACK);
        boxPanel.add(humidityLabel);

        // สร้าง JLabel สำหรับไอคอนลม
        windIconLabel = new JLabel(windIcon);
        windIconLabel.setBounds(50, 550, 30, 30); // ไอคอนลม
        boxPanel.add(windIconLabel);

        // สร้าง JLabel สำหรับข้อมูลความเร็วลม
        windSpeedLabel = new JLabel("Wind Speed: -- km/h", SwingConstants.CENTER);
        windSpeedLabel.setBounds(65, 550, 250, 30);  //ข้อความลม
        windSpeedLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
        windSpeedLabel.setForeground(Color.BLACK);
        boxPanel.add(windSpeedLabel);

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
            humidityLabel.setText("---");
            windSpeedLabel.setText("---");
            return;
        }

        try {
            WeatherInfo weatherData = WeatherService.getWeatherData(location);

            if (weatherData == null) {
                locationLabel.setText("City not found");
                weatherInfoLabel.setText("---");
                humidityLabel.setText("---");
                windSpeedLabel.setText("---");
                return;
            }

            locationLabel.setText(weatherData.getCity());
            weatherInfoLabel.setText(String.format("%.1f°C | %s", weatherData.getTemperature(), weatherData.getWeatherCondition()));

            // เพิ่มข้อมูลความชื้นและลม
            humidityLabel.setText(String.format("Humidity: %.1f%%", weatherData.getHumidity()));
            windSpeedLabel.setText(String.format("Wind Speed: %.1f km/h", weatherData.getWindSpeed()));

            // เพิ่มเวลาปัจจุบันตาม timezone ของเมืองที่ค้นหา
            updateTimeLabel(weatherData.getTimezone());  // ใช้ข้อมูล timezone จาก API

            // ปรับไอคอนสภาพอากาศ
            updateWeatherIcon(weatherData.getWeatherCondition());

        } catch (Exception e) {
            locationLabel.setText("Error fetching data");
            weatherInfoLabel.setText("---");
            humidityLabel.setText("---");
            windSpeedLabel.setText("---");
            System.err.println("❌ Error: " + e.getMessage());
        }
    }

    // ฟังก์ชั่นสำหรับอัปเดตเวลาปัจจุบัน
    private void updateTimeLabel(String timezone) {
        try {
            if (timezone == null || timezone.isEmpty()) {
                currentTimeLabel.setText("Timezone not available");
                return;
            }
    
            ZoneId zoneId = ZoneId.of(timezone); // ✅ ใช้ timezone ที่ได้จาก API
            ZonedDateTime zonedDateTime = ZonedDateTime.now(zoneId);
    
            // รูปแบบวันที่และเวลา
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a");
            String formattedTime = zonedDateTime.format(formatter);
    
            currentTimeLabel.setText("Last updated: " + formattedTime);
        } catch (Exception e) {
            currentTimeLabel.setText("Error updating time");
            System.err.println("❌ Error: " + e.getMessage());
        }
    }
    

    /** อัปเดตรูปสภาพอากาศ **/
    private void updateWeatherIcon(String weatherCondition) {
        String basePath = "myproject/src/main/assets/weatherapp_images/";
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
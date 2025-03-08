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
    private JPanel humidityCard, windSpeedCard, pm2_5Card, sunriseCard, sunsetCard, visibilityCard;
    private JPanel cardsPanel;
    private ImageIcon humidityIcon, windIcon, pm2_5Icon, sunriseIcon, sunsetIcon, visibilityIcon;
    

    public WeatherAppGui() {
        super("Weather App");

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setResizable(false);

        addGuiComponents();
    }

    private void loadIcons() {
        humidityIcon = loadTransparentImage("myproject/src/main/assets/weatherapp_images/humidity.png", 40, 40);
        windIcon = loadTransparentImage("myproject/src/main/assets/weatherapp_images/wind.png", 40, 40);
        pm2_5Icon = loadTransparentImage("myproject/src/main/assets/weatherapp_images/pm2_5.png", 40, 40);
        sunriseIcon = loadTransparentImage("myproject/src/main/assets/weatherapp_images/sunrise.png", 40, 40);
        sunsetIcon = loadTransparentImage("myproject/src/main/assets/weatherapp_images/sunset.png", 40, 40);
        visibilityIcon = loadTransparentImage("myproject/src/main/assets/weatherapp_images/visibility.png", 40, 40);
    }

    private void addGuiComponents() {
        JPanel boxPanel = new JPanel();
        boxPanel.setPreferredSize(new Dimension(600, 300));
        boxPanel.setBackground(Color.LIGHT_GRAY);
        boxPanel.setLayout(null);
        loadIcons();

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
        weatherInfoLabel.setHorizontalAlignment(SwingConstants.CENTER); // จัดกึ่งกลางแนวนอน
        weatherInfoLabel.setVerticalAlignment(SwingConstants.CENTER);   // จัดกึ่งกลางแนวตั้ง
        weatherInfoLabel.setBounds(120, 400, 250, 30);
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

         // 🌟 **สร้าง Panel สำหรับ Card UI** 🌟
         cardsPanel = new JPanel();
         cardsPanel.setLayout(new GridLayout(2, 3, 10, 10));
         cardsPanel.setBounds(30, 450, 440, 200); 
         cardsPanel.setBackground(Color.LIGHT_GRAY);

        humidityCard = createCard("Humidity", "--%", humidityIcon);
        windSpeedCard = createCard("Wind", "-- km/h", windIcon);
        pm2_5Card = createCard("PM2.5", "-- µg/m³", pm2_5Icon);
        sunriseCard = createCard("Sunrise", "--:-- AM", sunriseIcon);
        sunsetCard = createCard("Sunset", "--:-- PM", sunsetIcon);
        visibilityCard = createCard("Visibility", "-- km", visibilityIcon);
         
         

        cardsPanel.add(humidityCard);
        cardsPanel.add(windSpeedCard);
        cardsPanel.add(pm2_5Card);
        cardsPanel.add(sunriseCard);
        cardsPanel.add(sunsetCard);
        cardsPanel.add(visibilityCard);

        boxPanel.add(cardsPanel);
        add(boxPanel, BorderLayout.CENTER);

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

    /** 🔹 ฟังก์ชันสร้าง Card **/
    private JPanel createCard(String title, String value, ImageIcon icon) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setPreferredSize(new Dimension(130, 100));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
    
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
    
        JLabel textLabel = new JLabel("<html><center><b>" + title + "</b><br>" + value + "</center></html>", SwingConstants.CENTER);
        textLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
    
        card.add(iconLabel, BorderLayout.NORTH);
        card.add(textLabel, BorderLayout.CENTER);
    
        return card;
    }
    
    private void updateCards(WeatherInfo weatherData) {
        setCardText(humidityCard, "Humidity", formatCard("Humidity", weatherData.getHumidity() + "%"));
        setCardText(windSpeedCard, "Wind", formatCard("Wind", weatherData.getWindSpeed() + " km/h"));
        setCardText(pm2_5Card, "PM2.5", formatCard("PM2.5", weatherData.getPm2_5() + " µg/m³"));
        setCardText(sunriseCard, "Sunrise", formatCard("Sunrise", weatherData.getSunrise()));
        setCardText(sunsetCard, "Sunset", formatCard("Sunset", weatherData.getSunset()));
        setCardText(visibilityCard, "Visibility", formatCard("Visibility", weatherData.getVisibility() + " km"));
    
        cardsPanel.revalidate();
        cardsPanel.repaint();
    }
    
    
    private void setCardText(JPanel card, String title, String value) {
        JLabel textLabel = (JLabel) card.getComponent(1); // ดึง JLabel ตัวที่สอง (index 1)
        textLabel.setText("<html><center><b>" + title + "</b><br>" + value + "</center></html>");
    }
    
    

    private String formatCard(String title, String value) {
        return "<html><center>" + value + "</center></html>";
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
        // รับค่าจากช่องค้นหา
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
    
            // ✅ อัปเดตค่าของการ์ด
            updateCards(weatherData);
    
            // ✅ อัปเดตเวลาล่าสุด
            updateTimeLabel(weatherData.getTimezone());
    
            // ✅ ปรับไอคอนสภาพอากาศ
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
        } else if (weatherCondition.equalsIgnoreCase("Snow")) {
            imagePath = basePath + "snow.png";
        } else if (weatherCondition.toLowerCase().contains("rain") || weatherCondition.toLowerCase().contains("shower")) {
            // ถ้า weatherCondition มีคำว่า "rain" หรือ "shower" ให้ใช้รูปฝน
            imagePath = basePath + "rain.png";
        } else if (weatherCondition.toLowerCase().contains("thunderstorm")) {
            // ถ้ามีพายุฝนฟ้าคะนอง ใช้รูป thunderstorm
            imagePath = basePath + "thunderstorm.png";
        } else if (weatherCondition.toLowerCase().contains("fog") || weatherCondition.toLowerCase().contains("mist")) {
            // ถ้ามีหมอกหรือหมอกควัน ใช้รูป fog
            imagePath = basePath + "fog.png";
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
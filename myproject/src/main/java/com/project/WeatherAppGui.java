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
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class WeatherAppGui extends JFrame {
    private JLabel locationLabel, weatherInfoLabel, weatherConditionImage, currentTimeLabel;
    private JTextField searchTextField;
    private JButton searchButton;
    private JPanel humidityCard, windSpeedCard, pm2_5Card, sunriseCard, sunsetCard, visibilityCard;
    private JPanel cardsPanel;
    private ImageIcon humidityIcon, windIcon, pm2_5Icon, sunriseIcon, sunsetIcon, visibilityIcon;

    public WeatherAppGui() {
        super("Weather App");

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500, 750);
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
    
        // Card Panel
        cardsPanel = new JPanel(new GridLayout(2, 3, 20, 20));
        cardsPanel.setBounds(30, 450, 440, 250);
        cardsPanel.setBackground(Color.LIGHT_GRAY);

        humidityCard = createCard("Humidity", "---", humidityIcon);
        windSpeedCard = createCard("Wind", "---", windIcon);
        pm2_5Card = createCard("PM2.5", "---", pm2_5Icon);
        sunriseCard = createCard("Sunrise", "---", sunriseIcon);
        sunsetCard = createCard("Sunset", "---", sunsetIcon);
        visibilityCard = createCard("Visibility", "---", visibilityIcon);

        cardsPanel.add(humidityCard);
        cardsPanel.add(windSpeedCard);
        cardsPanel.add(pm2_5Card);
        cardsPanel.add(sunriseCard);
        cardsPanel.add(sunsetCard);
        cardsPanel.add(visibilityCard);

        boxPanel.add(cardsPanel);
        add(boxPanel, BorderLayout.CENTER);
    }

    /** 🔹 ฟังก์ชันสร้าง Card **/ 
    private JPanel createCard(String title, String value, ImageIcon icon) {
        JPanel card = new JPanel(new GridBagLayout());
        card.setPreferredSize(new Dimension(130, 130));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(3, 3, 3, 3);
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel iconLabel = new JLabel(icon);
        gbc.gridy = 0;
        card.add(iconLabel, gbc);

        JLabel titleLabel = new JLabel("<html><center><b>" + title + "</b></center></html>");
        gbc.gridy = 1;
        card.add(titleLabel, gbc);

        JLabel valueLabel = new JLabel("<html><center>" + value + "</center></html>");
        gbc.gridy = 2;
        card.add(valueLabel, gbc);

        return card;
    }
    
   
    private void updateCards(WeatherInfo weatherData) {
        // ✅ ใช้ formatTime() เพื่อแสดง AM/PM แทน "ก่อนเที่ยง/หลังเที่ยง"
        String sunriseTime = formatTime(weatherData.getSunrise());
        String sunsetTime = formatTime(weatherData.getSunset());
    
        setCardText(humidityCard, weatherData.getHumidity() + "%");
        setCardText(windSpeedCard, weatherData.getWindSpeed() + " km/h");
        setCardText(pm2_5Card, weatherData.getPm2_5() + " µg/m³");
        setCardText(sunriseCard, sunriseTime);
        setCardText(sunsetCard, sunsetTime);
        setCardText(visibilityCard, weatherData.getVisibility() + " km");
    
        cardsPanel.revalidate();
        cardsPanel.repaint();
    }
    


    private void setCardText(JPanel card, String value) {
        Component[] components = card.getComponents();
        if (components.length > 2 && components[2] instanceof JLabel) {
            ((JLabel) components[2]).setText("<html><center>" + value + "</center></html>");
        }
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


    private String formatTime(String timeStr) {
        try {
            if (timeStr == null || timeStr.isEmpty()) {
                return "N/A";
            }
    
            // ✅ แปลงจาก "2025-03-11T06:27" เป็น LocalTime (ตัดวันที่ออก)
            LocalTime time = LocalTime.parse(timeStr.substring(11)); // ตัด "T" และใช้แค่ HH:mm
    
            // ✅ ใช้ Locale.ENGLISH เพื่อบังคับให้แสดง AM / PM เป็นภาษาอังกฤษ
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH);
    
            return time.format(formatter);
        } catch (Exception e) {
            System.err.println("❌ Error formatting time: " + timeStr);
            return "Invalid Time";
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
            if (weatherData == null) {
                locationLabel.setText("City not found");
                return;
            }

            locationLabel.setText(weatherData.getCity());
            weatherInfoLabel.setText(String.format("%.1f°C | %s", weatherData.getTemperature(), weatherData.getWeatherCondition()));
            updateCards(weatherData);
            updateTimeLabel(weatherData.getTimezone());
            updateWeatherIcon(weatherData.getWeatherCondition());
        } catch (Exception e) {
            locationLabel.setText("Error fetching data");
            System.err.println("❌ Error: " + e.getMessage());
        }
    }
    // ฟังก์ชั่นสำหรับอัปเดตเวลาปัจจุบัน
    private void updateTimeLabel(String timezone) {
        try {
            if (timezone == null || timezone.isEmpty()) {
                currentTimeLabel.setText("Timezone unavailable");
                return;
            }
    
            ZoneId zoneId;
            try {
                zoneId = ZoneId.of(timezone);
            } catch (Exception e) {
                System.err.println("❌ Invalid timezone: " + timezone);
                currentTimeLabel.setText("Invalid timezone");
                return;
            }
    
            ZonedDateTime zonedDateTime = ZonedDateTime.now(zoneId);
    
            // ✅ ใช้ "hh:mm a" เพื่อแสดง AM/PM แทน "ก่อนเที่ยง/หลังเที่ยง"
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a", java.util.Locale.ENGLISH);
            String formattedTime = zonedDateTime.format(formatter);
    
            currentTimeLabel.setText("Last updated: " + formattedTime);
    
        } catch (Exception e) {
            System.err.println("❌ Error updating time: " + e.getMessage());
            currentTimeLabel.setText("Error updating time");
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

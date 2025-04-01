package com.project;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.project.models.FavoriteCityManager;
import com.project.models.WeatherInfo;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.LocalTime;
import java.util.Locale;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import com.project.components.RoundedButton;
import com.project.components.RoundedTextField;



public class WeatherAppGui extends JFrame {
    private JLabel locationLabel, weatherInfoLabel, weatherConditionImage, currentTimeLabel;
    private JTextField searchTextField;
    private JPanel humidityCard, windSpeedCard, pm2_5Card, sunriseCard, sunsetCard, visibilityCard;
    private JPanel cardsPanel;
    private ImageIcon humidityIcon, windIcon, pm2_5Icon, sunriseIcon, sunsetIcon, visibilityIcon;
    private JPanel boxPanel;
    private FavoriteCityManager favoriteManager = new FavoriteCityManager(); //สำหรับจัดการเมืองโปรด
    ;

    

    public WeatherAppGui() {
        super("Weather App");

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(505, 770);
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
        this.boxPanel = new JPanel(); 
        boxPanel.setPreferredSize(new Dimension(600, 300));
        boxPanel.setBackground(Color.WHITE);
        boxPanel.setLayout(null);
        loadIcons();
        getContentPane().add(boxPanel, BorderLayout.CENTER);
        

        cardsPanel = new JPanel();
        cardsPanel.setLayout(new GridLayout(2, 3, 20, 20));
        boxPanel.add(cardsPanel); 
        
        // โหลดรูปเมฆเริ่มต้น
        String defaultImagePath = "myproject/src/main/assets/weatherapp_images/cloudy.png";
        ImageIcon defaultIcon = loadTransparentImage(defaultImagePath, 150, 150);
    
        // แสดงรูปภาพสภาพอากาศ
        weatherConditionImage = new JLabel(defaultIcon);
        weatherConditionImage.setBounds(170, 220, 150, 150);
        boxPanel.add(weatherConditionImage);
    
        // เพิ่ม JLabel สำหรับแสดงชื่อเมือง
        locationLabel = new JLabel("Enter a city name", SwingConstants.CENTER);
        locationLabel.setBounds(110, 140, 250, 30);
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
        currentTimeLabel.setBounds(90, 170, 300, 30);  // ตำแหน่งใต้ชื่อเมือง
        currentTimeLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        currentTimeLabel.setForeground(Color.BLACK);
        boxPanel.add(currentTimeLabel);
    
        // เพิ่ม JTextField สำหรับพิมพ์ชื่อเมือง
        searchTextField = new RoundedTextField(20);
        searchTextField.setBounds(100, 15, 250, 45);
        searchTextField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        searchTextField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 180), 2), // ขอบสีเทาอ่อน
            BorderFactory.createEmptyBorder(8, 10, 8, 10) // padding ด้านใน
        ));
        searchTextField.setBackground(Color.WHITE);
        searchTextField.setForeground(Color.BLACK);
        searchTextField.setCaretColor(Color.BLACK); // สีของเคอร์เซอร์
        
        boxPanel.add(searchTextField);
        
    
     
        // โหลดไอคอนแว่นขยาย
        String searchIconPath = "myproject/src/main/assets/weatherapp_images/search.png";
        ImageIcon searchIcon = loadTransparentImage(searchIconPath, 30, 30);

        // สร้างปุ่มค้นหาด้วย RoundedButton
        RoundedButton searchButton = new RoundedButton(""); // ปรับให้ใช้ RoundedButton แทน JButton
        searchButton.setIcon(searchIcon);  // ใส่ไอคอนแว่นขยาย
        searchButton.setBounds(355, 15, 50, 45);  // กำหนดขนาดและตำแหน่ง
        searchButton.setContentAreaFilled(false);  // ไม่มีพื้นหลัง
        searchButton.setBorderPainted(false);     // ไม่มีขอบ
        searchButton.setFocusPainted(false);      // ไม่มีกรอบเมื่อโฟกัส
        searchButton.setCursor(new Cursor(Cursor.HAND_CURSOR));  // เปลี่ยนเมาส์เป็นมือ
        boxPanel.add(searchButton);

    
        searchTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    // เมื่อกด Enter จะเรียกใช้ฟังก์ชันค้นหา
                    fetchWeatherData();  // หรือใช้ searchButton.doClick() ถ้าต้องการให้เหมือนการกดปุ่ม
                }
            }
        });

        // Event Listener ปุ่มค้นหา
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fetchWeatherData();
            }
        });

        ImageIcon favoriteIcon = loadTransparentImage("myproject/src/main/assets/weatherapp_images/star.png", 30, 30);
        RoundedButton favoriteButton = new RoundedButton("");  // ใช้ RoundedButton
        favoriteButton.setIcon(favoriteIcon);  // ใส่ไอคอน
        favoriteButton.setBounds(410, 15, 45, 45);  // เลื่อนปุ่มออกไปจากช่องค้นหามากขึ้น
        favoriteButton.setContentAreaFilled(false);  // ไม่มีพื้นหลัง
        favoriteButton.setBorderPainted(false);     // ไม่มีขอบ
        favoriteButton.setFocusPainted(false);      // ไม่มีกรอบเมื่อโฟกัส
        favoriteButton.setCursor(new Cursor(Cursor.HAND_CURSOR));  // เปลี่ยนเมาส์เป็นมือ
        boxPanel.add(favoriteButton);

        // เมื่อกดปุ่มนี้ จะบันทึกเมืองปัจจุบัน
        favoriteButton.addActionListener(e -> {
            String city = searchTextField.getText().trim();
            if (!city.isEmpty()) {
                favoriteManager.addCity(city);
                JOptionPane.showMessageDialog(this, city + " added to favorites!");
            }
        });

        RoundedButton viewFavoritesButton = new RoundedButton("FavoriteCity");  // ใช้ RoundedButton
        viewFavoritesButton.setBounds(180, 75, 120, 35);  // ปรับขนาดให้พอดี
        viewFavoritesButton.setFocusPainted(false);  // ไม่มีการแสดงเส้นโฟกัส
        viewFavoritesButton.setContentAreaFilled(true);  // ทำให้มีพื้นหลัง
        viewFavoritesButton.setBackground(new Color(100, 149, 237));  // พื้นหลังสีฟ้า
        viewFavoritesButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));  // ฟอนต์
        viewFavoritesButton.setForeground(Color.WHITE);  // สีข้อความเป็นขาว

        // เพิ่มปุ่มใน boxPanel
        boxPanel.add(viewFavoritesButton);

        viewFavoritesButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                viewFavoritesButton.setBackground(new Color(70, 130, 180));  // สีเข้มขึ้นเมื่อ hover
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                viewFavoritesButton.setBackground(new Color(100, 149, 237));  // คืนสีพื้นหลังเดิม
            }
        });

        viewFavoritesButton.addActionListener(e -> {
            showFavoritesDialog();
        });


        // Card Panel
        cardsPanel = new JPanel(new GridLayout(2, 3, 20, 20));
        cardsPanel.setBounds(30, 450, 440, 250);
        cardsPanel.setBackground(Color.WHITE);

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
    
    private void showFavoritesDialog() {
        List<String> cities = favoriteManager.getFavorites();
        if (cities.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No favorite cities saved yet.");
            return;
        }
    
        JDialog dialog = new JDialog(this, "รายการเมืองโปรด", true);
        dialog.setSize(300, 300);
        dialog.setLayout(new BorderLayout());
    
        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (String city : cities) {
            listModel.addElement(city);
        }
    
        JList<String> cityList = new JList<>(listModel);
        dialog.add(new JScrollPane(cityList), BorderLayout.CENTER);
    
        JPanel buttonPanel = new JPanel();
        JButton deleteButton = new JButton("Delete ");
        JButton viewButton = new JButton("Weather");
    
        //ปุ่มลบ
        deleteButton.addActionListener(e -> {
            String selected = cityList.getSelectedValue();
            if (selected != null) {
                favoriteManager.removeCity(selected);
                listModel.removeElement(selected);
            }
        });
    
        //ปุ่มดู
        viewButton.addActionListener(e -> {
            String selected = cityList.getSelectedValue();
            if (selected != null) {
                searchTextField.setText(selected);
                fetchWeatherData();
                dialog.dispose();
            }
        });
    
        buttonPanel.add(viewButton);
        buttonPanel.add(deleteButton);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
    
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
    

    /**ฟังก์ชันสร้าง Card **/ 
    private JPanel createCard(String title, String value, ImageIcon icon) {
        JPanel card = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    
                // เพิ่มเงาให้ดูนูน
                g2.setColor(new Color(0, 0, 0, 50)); // เงาสีดำจาง
                g2.fillRoundRect(3, 5, getWidth() - 6, getHeight() - 6, 20, 20);
    
                // พื้นหลังการ์ด
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth() - 6, getHeight() - 6, 20, 20);
            }
        };
    
        card.setPreferredSize(new Dimension(130, 130));
        card.setOpaque(false); //ทำให้การ์ดไม่มีพื้นหลังที่ทับกัน
        card.setBackground(new Color(255, 204, 102)); //สีเหลืองพาสเทล
        card.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); //ลบเส้นขอบออก
    
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(3, 3, 3, 3);
        gbc.anchor = GridBagConstraints.CENTER;
    
        JLabel iconLabel = new JLabel(icon);
        gbc.gridy = 0;
        card.add(iconLabel, gbc);
    
        JLabel titleLabel = new JLabel("<html><center><b>" + title + "</b></center></html>");
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
        titleLabel.setForeground(Color.BLACK);
        gbc.gridy = 1;
        card.add(titleLabel, gbc);
    
        JLabel valueLabel = new JLabel("<html><center>" + value + "</center></html>");
        valueLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
        valueLabel.setForeground(Color.BLACK);
        gbc.gridy = 2;
        card.add(valueLabel, gbc);
    
        return card;
    }
    
    private void updateCards(WeatherInfo weatherData) {
        // ใช้ formatTime() เพื่อแสดง AM/PM แทน "ก่อนเที่ยง/หลังเที่ยง"
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
    
            // แปลงจาก "2025-03-11T06:27" เป็น LocalTime (ตัดวันที่ออก)
            LocalTime time = LocalTime.parse(timeStr.substring(11)); // ตัด "T" และใช้แค่ HH:mm
    
            // ใช้ Locale.ENGLISH เพื่อบังคับให้แสดง AM / PM เป็นภาษาอังกฤษ
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
    
            String weatherCondition = weatherData.getWeatherCondition(); // ดึงค่า Weather Condition จริง
    
            locationLabel.setText(weatherData.getCity());
            weatherInfoLabel.setText(String.format("%.1f°C | %s", weatherData.getTemperature(), weatherCondition));
    
            //Debug ดูค่าที่ส่งเข้า updateWeatherIcon()
            System.out.println("🌤 Weather Condition: " + weatherCondition);
    
            updateCards(weatherData);
            updateTimeLabel(weatherData.getTimezone());
    
            // ใช้ค่าจริงของ weatherCondition ใน updateWeatherIcon()
            updateWeatherIcon(weatherCondition);
    
            // ส่ง weatherCondition เข้าไปใน updateBackground()
            if (boxPanel != null) {
                updateBackground(weatherData.getSunrise(), weatherData.getSunset(), weatherData.getTimezone(), weatherCondition);
            } else {
                System.err.println("❌ boxPanel is NULL! Background will not update.");
            }
    
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
    
            //ใช้ "hh:mm a" เพื่อแสดง AM/PM แทน "ก่อนเที่ยง/หลังเที่ยง"
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

    private void updateBackground(String sunrise, String sunset, String timezone, String weatherCondition) {
        try {
            if (sunrise == null || sunset == null || sunrise.isEmpty() || sunset.isEmpty()) {
                System.err.println("❌ Missing sunrise or sunset data!");
                return;
            }
    
            if (timezone == null || timezone.isEmpty()) {
                System.err.println("❌ Missing timezone data!");
                return;
            }
    
            // ใช้โซนเวลาของประเทศที่ค้นหา
            ZoneId zoneId = ZoneId.of(timezone);
            ZonedDateTime now = ZonedDateTime.now(zoneId); //เวลาปัจจุบันของประเทศนั้น
    
            // แปลง sunrise/sunset เป็น LocalTime
            LocalTime sunriseTime = LocalTime.parse(sunrise.substring(11)); 
            LocalTime sunsetTime = LocalTime.parse(sunset.substring(11));
    
            // Debug ดูค่าเวลาที่ใช้เปรียบเทียบ
            System.out.println("🌅 Sunrise: " + sunriseTime + " | 🌇 Sunset: " + sunsetTime + " | ⏰ Now: " + now.toLocalTime());
    
            Color backgroundColor;
            Color textColor;
            Color cardColor;
            Color cardTextColor;
    
            if (now.toLocalTime().isAfter(sunriseTime) && now.toLocalTime().isBefore(sunsetTime)) {
                backgroundColor = new Color(200, 230, 255); //กลางวัน - ฟ้าอ่อน
                textColor = Color.BLACK; // ตัวหนังสือหลักเป็นสีดำ
                cardColor = new Color(255, 223, 120); // การ์ดเป็นสีส้มเหลืองอ่อน
                cardTextColor = Color.BLACK; //ตัวหนังสือในการ์ดเป็นสีดำ
            } else {
                backgroundColor = new Color(0, 51, 102); // สีกรมท่า
                textColor = Color.WHITE;
                cardColor = new Color(75, 46, 130); // สีม่วงเข้ม
                cardTextColor = Color.WHITE;
            }
    
            // Debug ดูว่าพื้นหลังเปลี่ยนหรือไม่
            System.out.println("🎨 Changing background to: " + (now.toLocalTime().isAfter(sunriseTime) && now.toLocalTime().isBefore(sunsetTime) ? "Day" : "Night"));
    
            // อัปเดต UI
            SwingUtilities.invokeLater(() -> {
                getContentPane().setBackground(backgroundColor);
                boxPanel.setBackground(backgroundColor);
                cardsPanel.setBackground(new Color(0, 0, 0, 0)); // โปร่งใส
                cardsPanel.setOpaque(false);
    
                // เปลี่ยนสีของตัวหนังสือใน Title และ Time
                locationLabel.setForeground(textColor);
                weatherInfoLabel.setForeground(textColor);
                currentTimeLabel.setForeground(textColor);
    
                // อัปเดตการ์ดทั้งหมดให้พื้นหลังและตัวหนังสือเปลี่ยนสี
                for (Component comp : cardsPanel.getComponents()) {
                    if (comp instanceof JPanel) {
                        JPanel card = (JPanel) comp;
                        card.setBackground(cardColor); // เปลี่ยนพื้นหลังของการ์ด
    
                        for (Component innerComp : card.getComponents()) {
                            if (innerComp instanceof JLabel) {
                                ((JLabel) innerComp).setForeground(cardTextColor); // ตัวหนังสือภายในการ์ดเปลี่ยนสีตามกลางวัน/กลางคืน
                            }
                        }
                    }
                }
    
                // รีโหลดไอคอนของสภาพอากาศ
                updateWeatherIcon(weatherCondition);
    
                revalidate();
                repaint();
            });
    
        } catch (Exception e) {
            System.err.println("❌ Error updating background: " + e.getMessage());
        }
    }
    
    

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            WeatherAppGui app = new WeatherAppGui();
            app.setVisible(true);
        });
    }
}

package com.project.models;

public class WeatherInfo {
    private String city;
    private double temperature;
    private String weatherCondition;
    private double snowfall;
    private double humidity;
    private double windSpeed;
    private double pm2_5;  // ✅ เพิ่มตัวแปร PM2.5
    private String sunrise;
    private String sunset;
    private double visibility;
    private String timezone;

    // ✅ Constructor
    public WeatherInfo(String city, double temperature, String weatherCondition, double snowfall, double humidity, 
                       double windSpeed, double pm2_5, String sunrise, String sunset, double visibility, String timezone) {
        this.city = city;
        this.temperature = temperature;
        this.weatherCondition = weatherCondition;
        this.snowfall = snowfall;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
        this.pm2_5 = pm2_5; // ✅ เก็บค่าฝุ่น PM2.5
        this.sunrise = sunrise;
        this.sunset = sunset;
        this.visibility = visibility;
        this.timezone = timezone;
    }

    // ✅ Getter Methods
    public String getCity() { return city; }
    public double getTemperature() { return temperature; }
    public String getWeatherCondition() { return weatherCondition; }
    public double getSnowfall() { return snowfall; }
    public double getHumidity() { return humidity; }
    public double getWindSpeed() { return windSpeed; }
    public double getPm2_5() { return pm2_5; } // ✅ เพิ่ม Getter สำหรับ PM2.5
    public String getSunrise() { return sunrise; }
    public String getSunset() { return sunset; }
    public double getVisibility() { return visibility; }
    public String getTimezone() { return timezone; }

    // ✅ Override toString() เพื่อแสดงข้อมูล
    @Override
    public String toString() {
        return String.format(
            "📍 เมือง: %s\n🌡 อุณหภูมิ: %.2f°C\n⛅ สภาพอากาศ: %s\n❄ หิมะตก: %.1f mm\n💧 ความชื้น: %.1f%%\n💨 ความเร็วลม: %.1f km/h\n🌫 PM2.5: %.1f µg/m³",
            city, temperature, weatherCondition, snowfall, humidity, windSpeed, pm2_5
        );
    }
}

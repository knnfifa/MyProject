package com.project.models;

public class WeatherInfo {
    private String city;
    private double temperature;
    private String weatherCondition;
    private double snowfall;
    private double humidity;
    private double windSpeed;

    public WeatherInfo(String city, double temperature, String weatherCondition, double snowfall, double humidity, double windSpeed) {
        this.city = city;
        this.temperature = temperature;
        this.weatherCondition = weatherCondition;
        this.snowfall = snowfall;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
    }

    public String getCity() { return city; }
    public double getTemperature() { return temperature; }
    public String getWeatherCondition() { return weatherCondition; }
    public double getSnowfall() { return snowfall; }
    public double getHumidity() { return humidity; }
    public double getWindSpeed() { return windSpeed; }
    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    @Override
    public String toString() {
        return String.format(
            "📍 เมือง: %s\n🌡 อุณหภูมิ: %.2f°C\n⛅ สภาพอากาศ: %s\n❄ หิมะตก: %.1f mm\n💧 ความชื้น: %.1f%%\n💨 ความเร็วลม: %.1f km/h",
            city, temperature, weatherCondition, snowfall, humidity, windSpeed
        );
    }
}

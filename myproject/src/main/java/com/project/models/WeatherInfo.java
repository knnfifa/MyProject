package com.project.models;

public class WeatherInfo {
    private String city;
    private double temperature;
    private String weatherCondition;

    public WeatherInfo(String city, double temperature, String weatherCondition) {
        this.city = city;
        this.temperature = temperature;
        this.weatherCondition = weatherCondition;
    }

    public String getCity() { return city; }
    public double getTemperature() { return temperature; }
    public String getWeatherCondition() { return weatherCondition; }

    @Override
    public String toString() {
        return String.format(
            "📍 เมือง: %s\n🌡 อุณหภูมิ: %.2f°C\n⛅ สภาพอากาศ: %s",
            city, temperature, weatherCondition
        );
    }
}

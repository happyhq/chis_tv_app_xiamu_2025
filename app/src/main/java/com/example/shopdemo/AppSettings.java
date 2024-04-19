package com.example.shopdemo;

public class AppSettings {
    private static AppSettings instance;
    private String ipAddress = "";

    // Private constructor to prevent external instantiation
    private AppSettings() {}

    // Get the singleton instance of AppSettings
    public static synchronized AppSettings getInstance() {
        if (instance == null) {
            instance = new AppSettings();
        }
        return instance;
    }

    // Getters and setters for ipAddress
    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
}


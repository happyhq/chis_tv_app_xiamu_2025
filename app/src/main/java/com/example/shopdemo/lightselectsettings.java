package com.example.shopdemo;

public class lightselectsettings {

    private static lightselectsettings instance;
    private String lightmacAddress = "";

    // Private constructor to prevent external instantiation
    private lightselectsettings() {}

    // Get the singleton instance of AppSettings
    public static synchronized lightselectsettings getInstance() {
        if (instance == null) {
            instance = new lightselectsettings();
        }
        return instance;
    }

    // Getters and setters for ipAddress
    public String getlightmacAddress() {
        return lightmacAddress;
    }

    public void setlightmacAddress(String macAddress) {
        this.lightmacAddress = macAddress;
    }
}

package com.example.user.myapplication;

public class WifiInfot {
    private String ssid;

    public WifiInfot(String ssid){
        super();
        this.ssid = ssid;
    }

    public String getSsid(){
        return ssid;
    }

    public void setSsid(String ssid){
        this.ssid = ssid;
    }
}

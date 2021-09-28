package com.team01.web.virtualwallet.storage;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("storage")
public class StorageProperties {

    private String location = "D:\\TelerikProjects\\Repos\\virtual-wallet\\VirtualWallet\\src\\main\\resources\\images\\";

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

}
package com.example.home.guesthouse;

/**
 * Created by home on 1/3/2017.
 */

public class GuestData {
    private String name ;
    private String roomNum ;
    private String address ;
    private String healthStatus ;

    public GuestData(){



    }

    public GuestData(String name, String roomNum, String address, String healthStatus) {
        this.name = name;
        this.roomNum = roomNum;
        this.address = address;
        this.healthStatus = healthStatus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoomNum() {
        return roomNum;
    }

    public void setRoomNum(String roomNum) {
        this.roomNum = roomNum;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getHealthStatus() {
        return healthStatus;
    }

    public void setHealthStatus(String healthStatus) {
        this.healthStatus = healthStatus;
    }
}

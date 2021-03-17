package com.example.mediscan;

public class Hospitals {
    String name,address,phone;
    int distance;

    public Hospitals(String name, String address, String phone, int distance) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.distance = distance;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public int getDistance() {
        return distance;
    }
}

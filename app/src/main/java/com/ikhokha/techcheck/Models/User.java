package com.ikhokha.techcheck.Models;

import com.google.firebase.firestore.Exclude;
import java.util.HashMap;
import java.util.Map;

public class User {
    private String name;
    private int number;
    private double price;
    public User(String name, int number, double price) {
        this.name = name;
        this.number = number;
        this.price = price;
    }
    @Exclude
    public Map<String, Object> toMap(){
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("number", number);
        params.put("price", price);
        return  params;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getNumber() {
        return number;
    }
}

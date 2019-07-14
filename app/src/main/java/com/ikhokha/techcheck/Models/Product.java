package com.ikhokha.techcheck.Models;

import android.graphics.Bitmap;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Product {
    private String description, date, image;
    private double price;
    private Bitmap bitmap;
    private int quantity;
    public Product(){}
    public Product(String description,  double price, String image, int quantity, String date, Bitmap bitmap){
        this.description = description;
        this.price = price;
        this.image = image;
        this.quantity = quantity;
        this.date = date;
        this.bitmap = bitmap;
    }
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("description", description);
        result.put("price", price);
        result.put("image", image);
        result.put("quantity", quantity);
        result.put("date", date);
        result.put("bitmap", bitmap);
        return result;
    }
    public String getDescription(){ return description; }
    public double getPrice(){return price;}
    public String getImage(){
        return image;
    }
    public int getQuantity(){return quantity;}
    public String getDate(){ return date; }

    public Bitmap getBitmap() {
        return bitmap;
    }
}

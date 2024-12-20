package com.example.ajai_kamaraj_application2;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public class Product implements Serializable {
    private String name;
    private String description;
    private String imageUrl;



    private String additionalDetails;
    private String directImageUrl;
    private String key;  // added this
    private int quantity = 1; // Added for maintaining quantity

    private double price;


        public Product(String productName, String productDescription, String productImage, String additionalDetails, double productPrice) {
            this.name = productName;
            this.description = productDescription;
            this.imageUrl = productImage;
            this.additionalDetails = additionalDetails;
            this.price = productPrice; // Initialize price with the given productPrice
        }


    // getter and setter for key
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setDirectImageUrl(String directImageUrl) {
        this.directImageUrl = directImageUrl;
    }

    public Product() {}

    public Product(String name, String description, String imageUrl, String additionalDetails) {
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.additionalDetails = additionalDetails;
        double price = this.price;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
    public String getImageUrl() {
        return imageUrl;
    }

    public String getDirectImageUrl() {
        return directImageUrl;
    }

    public String getAdditionalDetails() {
        return additionalDetails;
    }
    public void setAdditionalDetails(String additionalDetails) {
        this.additionalDetails = additionalDetails;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("description", description);
        result.put("imageUrl", imageUrl);
        result.put("additionalDetails", additionalDetails);
        result.put("directImageUrl", directImageUrl);
        result.put("key", key);  // updated
        result.put("quantity", quantity);

        return result;
    }
}

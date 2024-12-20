package com.example.ajai_kamaraj_application2;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private final List<Product> products;

    public Cart() {
        products = new ArrayList<>();
    }

    public List<Product> getProducts() {
        return products;
    }

    public void addToCart(Product product) {
        products.add(product);
    }

}

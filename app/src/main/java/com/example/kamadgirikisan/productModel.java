package com.example.kamadgirikisan;



public class productModel {

    private String product_name;
    private String product_image;

    public String getImageUrl() {
        return product_image;
    }

    public void setImageUrl(String product_image) {
        this.product_image = product_image;
    }

    public String getName() {
        return product_name;
    }

    public void setName(String product_name) {
        this.product_name = product_name;

    }
}


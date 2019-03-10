/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans;

/**
 *
 * @author omkarkamate
 */
public class SoldProductItem {
    String productId;
    String productName;
    double productPrice;
    String productSaleDate;
    String MOP;

    public SoldProductItem(String productId, String productName, double productPrice, String productSaleDate, String MOP) {
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productSaleDate = productSaleDate;
        this.MOP = MOP;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductSaleDate() {
        return productSaleDate;
    }

    public void setProductSaleDate(String productSaleDate) {
        this.productSaleDate = productSaleDate;
    }

    public String getMOP() {
        return MOP;
    }

    public void setMOP(String MOP) {
        this.MOP = MOP;
    }

    
    
}

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

    public SoldProductItem(String productId, String productName, double productPrice, String productSaleDate) {
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productSaleDate = productSaleDate;
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
    
    
    
}

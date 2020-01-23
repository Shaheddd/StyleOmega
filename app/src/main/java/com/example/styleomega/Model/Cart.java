package com.example.styleomega.Model;

public class Cart
{
    private String ProductID, ProductName, Price, Quantity, Discount;

    public Cart()
    {

    }

    public Cart(String productID, String productName, String price, String quantity, String discount)
    {
        ProductID = productID;
        ProductName = productName;
        Price = price;
        Quantity = quantity;
        Discount = discount;
    }

    public String getProductID() {
        return ProductID;
    }

    public void setProductID(String productID) {
        ProductID = productID;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String discount) {
        Discount = discount;
    }
}

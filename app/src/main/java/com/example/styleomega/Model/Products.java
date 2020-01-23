package com.example.styleomega.Model;

public class Products
{
    private String ProductID, Date, Time, Description, Image, Category, Price, Name;

    public Products()
    {

    }

    public Products(String productID, String date, String time, String description, String image, String category, String price, String name)
    {
        ProductID = productID;
        Date = date;
        Time = time;
        Description = description;
        Image = image;
        Category = category;
        Price = price;
        Name = name;
    }

    public String getProductID() {
        return ProductID;
    }

    public void setProductID(String productID) {
        ProductID = productID;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}

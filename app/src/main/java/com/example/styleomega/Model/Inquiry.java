package com.example.styleomega.Model;

public class Inquiry
{
    String productName, productSubject, productMessage;

    public Inquiry()
    {

    }

    public Inquiry(String productName, String productSubject, String productMessage)
    {
        this.productName = productName;
        this.productSubject = productSubject;
        this.productMessage = productMessage;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductSubject() {
        return productSubject;
    }

    public void setProductSubject(String productSubject) {
        this.productSubject = productSubject;
    }

    public String getProductMessage() {
        return productMessage;
    }

    public void setProductMessage(String productMessage) {
        this.productMessage = productMessage;
    }
}

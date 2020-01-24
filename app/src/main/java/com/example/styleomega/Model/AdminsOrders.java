package com.example.styleomega.Model;

public class AdminsOrders
{
    private String Address, City, Date, Email, PhoneNumber, State, Time, TotalAmount, Username;

    public AdminsOrders()
    {

    }

    public AdminsOrders(String address, String city, String date, String email, String phoneNumber, String state, String time, String totalAmount, String username) {
        Address = address;
        City = city;
        Date = date;
        Email = email;
        PhoneNumber = phoneNumber;
        State = state;
        Time = time;
        TotalAmount = totalAmount;
        Username = username;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        TotalAmount = totalAmount;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }
}

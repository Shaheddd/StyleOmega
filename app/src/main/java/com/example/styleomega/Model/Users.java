package com.example.styleomega.Model;

public class Users
{
    private String Email, Password, PhoneNumber, Username, Image, Address;

    public Users()
    {

    }

    public Users(String email, String password, String phoneNumber, String username, String image, String address) {
        Email = email;
        Password = password;
        PhoneNumber = phoneNumber;
        Username = username;
        Image = image;
        Address = address;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }
}

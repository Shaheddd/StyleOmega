package com.example.styleomega.Model;

public class Users
{
    private String Email, Password, PhoneNumber, Username;

    public Users()
    {

    }

    public Users(String email, String password, String phoneNumber, String username) {
        Email = email;
        Password = password;
        PhoneNumber = phoneNumber;
        Username = username;
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
}

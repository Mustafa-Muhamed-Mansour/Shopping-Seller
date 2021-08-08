package com.shoppingseller.model;

public class UserModel
{
    String user_id, email, name, phone, country, city;

    public UserModel()
    {
    }

    public UserModel(String user_id, String email, String name, String phone, String country, String city)
    {
        this.user_id = user_id;
        this.email = email;
        this.name = name;
        this.phone = phone;
        this.country = country;
        this.city = city;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}

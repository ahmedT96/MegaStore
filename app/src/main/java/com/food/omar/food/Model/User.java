package com.food.omar.food.Model;

public class User {
    private String phone;
    private String name;
    private String birthdate;
    private String address;
    private String image;
    private String eror_msg;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getEror_msg() {
        return eror_msg;
    }

    public void setEror_msg(String eror_msg) {
        this.eror_msg = eror_msg;
    }
}

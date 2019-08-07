package com.food.omar.food.Model;

public class Token {
  public String   Phone,token,IsServerToken;

    public Token() {
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getIsServerToken() {
        return IsServerToken;
    }

    public void setIsServerToken(String isServerToken) {
        IsServerToken = isServerToken;
    }
}

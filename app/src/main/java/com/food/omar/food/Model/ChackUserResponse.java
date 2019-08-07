package com.food.omar.food.Model;

public class ChackUserResponse {

    private boolean existes;
    private String error_msg;
    public ChackUserResponse(){}
    public ChackUserResponse(boolean existes, String error_msg) {
        this.existes = existes;
        this.error_msg = error_msg;
    }

    public boolean isExistes() {
        return existes;
    }

    public void setExistes(boolean existes) {
        this.existes = existes;
    }

    public String getError_msg() {
        return error_msg;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }
}

package com.example.orderapp;



public class UserData {
    private String email,name,phone,imageUrl;
    private boolean provider;

    public UserData(String email, String name, String phone, String imageUrl, boolean provider) {
        this.email = email;
        this.name = name;
        this.phone = phone;
        this.imageUrl = imageUrl;
        this.provider = provider;
    }

    public UserData() {
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isProvider() {
        return provider;
    }

    public void setProvider(boolean provider) {
        this.provider = provider;
    }

    @Override
    public String toString() {
        return "UserData{" +
                "email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", provider=" + provider +
                '}';
    }
}

package com.example.bloodbanknaut.DataModels;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Donor {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("phoneNum")
    @Expose
    private String phoneNum;
    @SerializedName("city")
    @Expose
    private String city;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String number) {
        this.phoneNum = number;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }


}
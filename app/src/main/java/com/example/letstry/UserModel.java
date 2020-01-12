package com.example.letstry;

import java.util.ArrayList;

public class UserModel {
    private String phone_number,token;
    private ArrayList<FirModel> arrayListOfFir;

    public UserModel() {
    }


    public UserModel(String phone_number, ArrayList<FirModel> arrayListOfFir, String token) {
        this.phone_number = phone_number;
        this.arrayListOfFir = arrayListOfFir;
        this.token=token;
    }

    public String getToken() {
        return token;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public ArrayList<FirModel> getArrayListOfFir() {
        return arrayListOfFir;
    }
}



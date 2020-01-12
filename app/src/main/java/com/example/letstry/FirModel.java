package com.example.letstry;

public class FirModel {
    private String location;
    private String singleFir;
    private String status;
    private String imgfirebaseuri;


    public FirModel() {
    }

    public FirModel(String location, String singleFir,String status,String imgfirebaseuri) {
        this.location = location;
        this.singleFir = singleFir;
        this.status=status;
        this.imgfirebaseuri=imgfirebaseuri;
    }

    public String getLocation() {
        return location;
    }

    public String getImgfirebaseuri() {
        return imgfirebaseuri;
    }

    public String getSingleFir() {
        return singleFir;
    }

    public String getStatus() {
        return status;
    }
}



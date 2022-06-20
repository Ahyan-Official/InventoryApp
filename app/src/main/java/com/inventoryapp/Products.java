package com.inventoryapp;

public class Products {
    //Products View Holder class
    private String itemname;
    private String itemcategory;
    private String itemprice;
    private String itembarcode;
    private String itemstock;
    private String itemimage;


    public Products() {

    }

    public Products(String itemname, String itemcategory, String itemprice, String itembarcode, String itemstock, String itemimage) {
        this.itemname = itemname;
        this.itemcategory = itemcategory;
        this.itemprice = itemprice;
        this.itembarcode = itembarcode;
        this.itemstock = itemstock;
        this.itemimage = itemimage;
    }

    public String getItemimage() {
        return itemimage;
    }

    public void setItemimage(String itemimage) {
        this.itemimage = itemimage;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public String getItemcategory() {
        return itemcategory;
    }

    public void setItemcategory(String itemcategory) {
        this.itemcategory = itemcategory;
    }

    public String getItemprice() {
        return itemprice;
    }

    public void setItemprice(String itemprice) {
        this.itemprice = itemprice;
    }

    public String getItembarcode() {
        return itembarcode;
    }

    public void setItembarcode(String itembarcode) {
        this.itembarcode = itembarcode;
    }

    public String getItemstock() {
        return itemstock;
    }

    public void setItemstock(String itemstock) {
        this.itemstock = itemstock;
    }
}


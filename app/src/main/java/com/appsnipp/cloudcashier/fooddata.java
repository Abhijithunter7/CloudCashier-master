package com.appsnipp.cloudcashier;

public class fooddata {
    private String title;
    private String selectedOption;
    private double price;
    private String note;

    public fooddata() {
        // Default constructor required for Firebase
    }

    public fooddata(String title, String selectedOption, double price, String note) {
        this.title = title;
        this.selectedOption = selectedOption;
        this.price = price;
        this.note = note;
    }

    // Getter methods (required for Firebase)
    public String getTitle() {
        return title;
    }

    public String getselectedOption() {
        return selectedOption;
    }

    public double getPrice() {
        return price;
    }

    public String getNote() {
        return note;
    }
}
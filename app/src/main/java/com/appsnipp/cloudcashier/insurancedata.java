package com.appsnipp.cloudcashier;

public class insurancedata {
    private String title;
    private String options;
    private double price;
    private String note;

    public insurancedata() {
        // Default constructor required for Firebase
    }

    public insurancedata(String title, String options, double price, String note) {
        this.title = title;
        this.options = options;
        this.price = price;
        this.note = note;
    }

    // Getter methods (required for Firebase)
    public String getTitle() {
        return title;
    }

    public String getSelectedOption() {
        return options;
    }

    public double getPrice() {
        return price;
    }

    public String getNote() {
        return note;
    }
}

package com.appsnipp.cloudcashier;

public class home_maintenance_data {
    private String title;
    private String options;
    private double price;
    private String note;

    public home_maintenance_data() {
        // Default constructor required for Firebase
    }

    public home_maintenance_data(String title, String options, double price, String note) {
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

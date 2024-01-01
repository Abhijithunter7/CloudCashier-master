package com.appsnipp.cloudcashier;

public class Mainmodel {

    String title, selectedOptions, note;

    double price;

    Mainmodel()
    {

    }

    public Mainmodel(String title, String selectedOptions, String note, double price) {
        this.title = title;
        this.selectedOptions = selectedOptions;
        this.note = note;
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSelectedOptions() {
        return selectedOptions;
    }

    public void setSelectedOptions(String selectedOptions) {
        this.selectedOptions = selectedOptions;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}

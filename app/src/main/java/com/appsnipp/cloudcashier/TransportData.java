package com.appsnipp.cloudcashier;

public class TransportData {
    private String title;
    private String selectedOption;
    private double price;
    private String note;

    private String selectedDate;

    public TransportData() {
        // Default constructor required for Firebase
    }

    public TransportData(String title, String selectedOption, double price, String note, String selectedDate) {
        this.title = title;
        this.selectedOption = selectedOption;
        this.price = price;
        this.note = note;
        this.selectedDate = selectedDate;
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

    public String getselectedDate() {
        return selectedDate;
    }
}


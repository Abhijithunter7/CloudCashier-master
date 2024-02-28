package com.appsnipp.cloudcashier;

public class profiledata {
    private String name;
    private String email;
    private String dob;
    private String emergencyContact;
    private String address;
    private String bloodGroup;
    private String gender;

    // Required default constructor for Firebase
    public profiledata() {
        // Default constructor required for calls to DataSnapshot.getValue(profiledata.class)
    }

    public profiledata(String name, String email, String dob, String emergencyContact, String address, String bloodGroup, String gender) {
        this.name = name;
        this.email = email;
        this.dob = dob;
        this.emergencyContact = emergencyContact;
        this.address = address;
        this.bloodGroup = bloodGroup;
        this.gender = gender;
    }

    // Getters and Setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getEmergencyContact() {
        return emergencyContact;
    }

    public void setEmergencyContact(String emergencyContact) {
        this.emergencyContact = emergencyContact;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}

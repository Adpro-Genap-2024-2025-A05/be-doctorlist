package id.ac.ui.cs.advprog.beprofile.model;

public class Doctor {
    private String id;
    private String name;
    private String practiceAddress;
    private String workSchedule;
    private String email;
    private String phoneNumber;
    private double rating;


    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getPracticeAddress() {
        return practiceAddress;
    }
    public void setPracticeAddress(String practiceAddress) {
        this.practiceAddress = practiceAddress;
    }

    public String getWorkSchedule() {
        return workSchedule;
    }
    public void setWorkSchedule(String workSchedule) {
        this.workSchedule = workSchedule;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public double getRating() {
        return rating;
    }
    public void setRating(double rating) {
        this.rating = rating;
    }
}
package com.example.home_window;

public class Generate_plans {
    String state, category, accommodation, travel;
    float rating;

    public Generate_plans()
    {

    }
    public Generate_plans(String state, String category, float rating, String accommodation, String travel)
    {
        this.state = state;
        this.category = category;
        this.rating = rating;
        this.accommodation = accommodation;
        this.travel = travel;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAccommodation() {
        return accommodation;
    }

    public void setAccommodation(String accommodation) {
        this.accommodation = accommodation;
    }

    public String getTravel() {
        return travel;
    }

    public void setTravel(String travel) {
        this.travel = travel;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}

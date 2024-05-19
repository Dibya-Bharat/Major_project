package com.example.home_window;

public class Generate_plans {
    public String state;
    public String user_name;
    public String category;
    public String rating;
    public String accommodation;
    public String travel_mode;
    public String place1;
    public String place2;
    public String place3;

    public Generate_plans() {

    }

    public Generate_plans(String user_name, String state, String category, String rating, String accommodation, String travel_mode, String place1, String place2, String place3) {
        this.user_name = user_name;
        this.state = state;
        this.category = category;
        this.rating = rating;
        this.accommodation = accommodation;
        this.travel_mode = travel_mode;
        this.place1 = place1;
        this.place2 = place2;
        this.place3 = place3;
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

    public String getTravelMode() {
        return travel_mode;
    }

    public void setTravelMode(String travel_mode) {
        this.travel_mode = travel_mode;
    }

    public String getRating() {
        return rating;
    }

    public void setRating() {this.rating =rating;}

    public String getPlace1(){return place1;};
    public void setPlace1(String Place1){this.place1 = place1;};

    public String getPlace2(){return place2;};
    public void setPlace2(String Place3){this.place2 = place2;};
    public String getPlace3(){return place3;};
    public void setPlace3(String Place3){this.place3 = place3;};


}

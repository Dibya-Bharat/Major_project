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



    public String getCategory() {
        return category;
    }


    public String getAccommodation() {
        return accommodation;
    }

    public String getTravelMode() {
        return travel_mode;
    }


    public String getRating() {
        return rating;
    }

    public String getPlace1(){return place1;}

    public String getPlace2(){return place2;}

    public String getPlace3(){return place3;}


}

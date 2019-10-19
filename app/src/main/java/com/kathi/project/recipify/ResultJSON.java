package com.kathi.project.recipify;

import java.util.ArrayList;

public class ResultJSON {

    private String prediction;
    private Double calories;
    private Long fat;
    private ArrayList<String> recepies;
    private ArrayList<String> ingredients;
    private ArrayList<String> names;

    public ResultJSON(){}

    public ResultJSON(Double calories, Long fat, ArrayList<String> recepies, String prediction,
                      ArrayList<String> ingredients, ArrayList<String> names){

        this.prediction = prediction;
        this.calories = calories;
        this.fat = fat;
        this.recepies = recepies;
        this.ingredients = ingredients;
        this.names = names;
    }

    public String getPrediction() {
        return prediction;
    }

    public void setPrediction(String prediction) {
        this.prediction = prediction;
    }

    public Double getCalories() {
        return calories;
    }

    public void setCalories(Double calories) {
        this.calories = calories;
    }

    public Long getFat() {
        return fat;
    }

    public void setFat(Long fat) {
        this.fat = fat;
    }

    public ArrayList<String> getRecepies() {
        return recepies;
    }

    public void setRecepies(ArrayList<String> recepies) {
        this.recepies = recepies;
    }

    public ArrayList<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<String> ingredients) {
        this.ingredients = ingredients;
    }

    public ArrayList<String> getNames() {
        return names;
    }

    public void setNames(ArrayList<String> names) {
        this.names = names;
    }



}

package com.emadomarah.food.model;

public class LogmealItemModel {
    String mealName ;
    double mealCal , mealAmount;

    public LogmealItemModel(String mealName, double mealCal, double mealAmount) {
        this.mealName = mealName;
        this.mealCal = mealCal;
        this.mealAmount = mealAmount;
    }

    public String getMealName() {
        return mealName;
    }

    public void setMealName(String mealName) {
        this.mealName = mealName;
    }

    public double getMealCal() {
        return mealCal;
    }

    public void setMealCal(double mealCal) {
        this.mealCal = mealCal;
    }

    public double getMealAmount() {
        return mealAmount;
    }

    public void setMealAmount(double mealAmount) {
        this.mealAmount = mealAmount;
    }
}

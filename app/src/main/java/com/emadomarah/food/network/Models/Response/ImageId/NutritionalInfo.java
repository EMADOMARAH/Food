package com.emadomarah.food.network.Models.Response.ImageId;

import com.google.gson.annotations.SerializedName;

public class NutritionalInfo{

	@SerializedName("dailyIntakeReference")
	private DailyIntakeReference dailyIntakeReference;

	@SerializedName("calories")
	private double calories;

	@SerializedName("totalNutrients")
	private TotalNutrients totalNutrients;

	public DailyIntakeReference getDailyIntakeReference(){
		return dailyIntakeReference;
	}

	public double getCalories(){
		return calories;
	}

	public TotalNutrients getTotalNutrients(){
		return totalNutrients;
	}
}
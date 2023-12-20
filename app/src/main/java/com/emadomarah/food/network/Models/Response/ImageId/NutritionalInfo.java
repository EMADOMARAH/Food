package com.emadomarah.food.network.Models.Response.ImageId;

import com.google.gson.annotations.SerializedName;

public class NutritionalInfo{

	@SerializedName("dailyIntakeReference")
	private DailyIntakeReference dailyIntakeReference;

	@SerializedName("calories")
	private Object calories;

	@SerializedName("totalNutrients")
	private TotalNutrients totalNutrients;

	public DailyIntakeReference getDailyIntakeReference(){
		return dailyIntakeReference;
	}

	public Object getCalories(){
		return calories;
	}

	public TotalNutrients getTotalNutrients(){
		return totalNutrients;
	}
}
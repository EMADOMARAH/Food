package com.emadomarah.food.network.Models.Response.ImageId;

import com.google.gson.annotations.SerializedName;

public class NutritionalInfoPerItemItem{

	@SerializedName("nutritional_info")
	private NutritionalInfo nutritionalInfo;

	@SerializedName("serving_size")
	private double servingSize;

	@SerializedName("food_item_position")
	private int foodItemPosition;

	@SerializedName("id")
	private int id;

	@SerializedName("hasNutritionalInfo")
	private boolean hasNutritionalInfo;

	public NutritionalInfo getNutritionalInfo(){
		return nutritionalInfo;
	}

	public double getServingSize(){
		return servingSize;
	}

	public int getFoodItemPosition(){
		return foodItemPosition;
	}

	public int getId(){
		return id;
	}

	public boolean isHasNutritionalInfo(){
		return hasNutritionalInfo;
	}
}
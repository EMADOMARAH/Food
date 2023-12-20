package com.emadomarah.food.network.Models.Response.Image;

import com.google.gson.annotations.SerializedName;

public class FoodTypeItem{

	@SerializedName("name")
	private String name;

	@SerializedName("id")
	private int id;

	public String getName(){
		return name;
	}

	public int getId(){
		return id;
	}
}
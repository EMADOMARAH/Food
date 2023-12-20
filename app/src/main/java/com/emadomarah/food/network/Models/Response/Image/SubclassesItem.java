package com.emadomarah.food.network.Models.Response.Image;

import java.util.List;

public class SubclassesItem{
	private Object prob;
	private FoodType foodType;
	private String name;
	private int id;
	private List<FoodFamilyItem> foodFamily;

	public Object getProb(){
		return prob;
	}

	public FoodType getFoodType(){
		return foodType;
	}

	public String getName(){
		return name;
	}

	public int getId(){
		return id;
	}

	public List<FoodFamilyItem> getFoodFamily(){
		return foodFamily;
	}
}
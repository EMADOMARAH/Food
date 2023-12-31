package com.emadomarah.food.network.Models.Response.ImageId;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ImageIdResponse {

	@SerializedName("nutritional_info")
	private NutritionalInfo nutritionalInfo;

	@SerializedName("foodName")
	private List<String> foodName;

	@SerializedName("imageId")
	private int imageId;

	@SerializedName("serving_size")
	private double servingSize;

	@SerializedName("ids")
	private List<Integer> ids;

	@SerializedName("nutritional_info_per_item")
	private List<NutritionalInfoPerItemItem> nutritionalInfoPerItem;

	@SerializedName("hasNutritionalInfo")
	private boolean hasNutritionalInfo;

	public NutritionalInfo getNutritionalInfo(){
		return nutritionalInfo;
	}

	public List<String> getFoodName(){
		return foodName;
	}

	public int getImageId(){
		return imageId;
	}

	public double getServingSize(){
		return servingSize;
	}

	public List<Integer> getIds(){
		return ids;
	}

	public List<NutritionalInfoPerItemItem> getNutritionalInfoPerItem(){
		return nutritionalInfoPerItem;
	}

	public boolean isHasNutritionalInfo(){
		return hasNutritionalInfo;
	}
}
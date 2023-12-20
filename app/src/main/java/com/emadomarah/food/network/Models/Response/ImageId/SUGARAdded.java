package com.emadomarah.food.network.Models.Response.ImageId;

import com.google.gson.annotations.SerializedName;

public class SUGARAdded{

	@SerializedName("unit")
	private String unit;

	@SerializedName("quantity")
	private int quantity;

	@SerializedName("label")
	private String label;

	@SerializedName("level")
	private String level;

	@SerializedName("percent")
	private int percent;

	public String getUnit(){
		return unit;
	}

	public int getQuantity(){
		return quantity;
	}

	public String getLabel(){
		return label;
	}

	public String getLevel(){
		return level;
	}

	public int getPercent(){
		return percent;
	}
}
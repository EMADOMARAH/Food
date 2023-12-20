package com.emadomarah.food.network.Models.Response.ImageId;

import com.google.gson.annotations.SerializedName;

public class NA{

	@SerializedName("unit")
	private String unit;

	@SerializedName("quantity")
	private Object quantity;

	@SerializedName("label")
	private String label;

	@SerializedName("level")
	private String level;

	@SerializedName("percent")
	private Object percent;

	public String getUnit(){
		return unit;
	}

	public Object getQuantity(){
		return quantity;
	}

	public String getLabel(){
		return label;
	}

	public String getLevel(){
		return level;
	}

	public Object getPercent(){
		return percent;
	}
}
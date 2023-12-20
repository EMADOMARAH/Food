package com.emadomarah.food.network.Models.Response.ImageId;

import com.google.gson.annotations.SerializedName;

public class FE{

	@SerializedName("unit")
	private String unit;

	@SerializedName("quantity")
	private Object quantity;

	@SerializedName("label")
	private String label;

	public String getUnit(){
		return unit;
	}

	public Object getQuantity(){
		return quantity;
	}

	public String getLabel(){
		return label;
	}
}
package com.emadomarah.food.network.Models.Response.ImageId;

import com.google.gson.annotations.SerializedName;

public class K{

	@SerializedName("unit")
	private String unit;

	@SerializedName("quantity")
	private double quantity;

	@SerializedName("label")
	private String label;

	public String getUnit(){
		return unit;
	}

	public double getQuantity(){
		return quantity;
	}

	public String getLabel(){
		return label;
	}
}
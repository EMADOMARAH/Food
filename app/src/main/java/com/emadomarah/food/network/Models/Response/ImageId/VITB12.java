package com.emadomarah.food.network.Models.Response.ImageId;

import com.google.gson.annotations.SerializedName;

public class VITB12{

	@SerializedName("unit")
	private String unit;

	@SerializedName("quantity")
	private int quantity;

	@SerializedName("label")
	private String label;

	public String getUnit(){
		return unit;
	}

	public int getQuantity(){
		return quantity;
	}

	public String getLabel(){
		return label;
	}
}
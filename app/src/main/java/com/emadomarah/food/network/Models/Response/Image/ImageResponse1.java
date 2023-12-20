package com.emadomarah.food.network.Models.Response.Image;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ImageResponse1 {

	@SerializedName("imageId")
	private int imageId;

	@SerializedName("foodType")
	private List<FoodTypeItem> foodType;

	@SerializedName("recognition_results")
	private List<RecognitionResultsItem> recognitionResults;

	@SerializedName("foodFamily")
	private List<FoodFamilyItem> foodFamily;

	@SerializedName("model_versions")
	private ModelVersions modelVersions;

	public int getImageId(){
		return imageId;
	}

	public List<FoodTypeItem> getFoodType(){
		return foodType;
	}

	public List<RecognitionResultsItem> getRecognitionResults(){
		return recognitionResults;
	}

	public List<FoodFamilyItem> getFoodFamily(){
		return foodFamily;
	}

	public ModelVersions getModelVersions(){
		return modelVersions;
	}
}
package com.emadomarah.food.network.Models.Response.Image;

import java.util.List;

public class ImageResponse{
	private String occasion;
	private int imageId;
	private OccasionInfo occasionInfo;
	private ProcessedImageSize processedImageSize;
	private FoodType foodType;
	private List<SegmentationResultsItem> segmentationResults;
	private List<FoodFamilyItem> foodFamily;
	private ModelVersions modelVersions;

	public String getOccasion(){
		return occasion;
	}

	public int getImageId(){
		return imageId;
	}

	public OccasionInfo getOccasionInfo(){
		return occasionInfo;
	}

	public ProcessedImageSize getProcessedImageSize(){
		return processedImageSize;
	}

	public FoodType getFoodType(){
		return foodType;
	}

	public List<SegmentationResultsItem> getSegmentationResults(){
		return segmentationResults;
	}

	public List<FoodFamilyItem> getFoodFamily(){
		return foodFamily;
	}

	public ModelVersions getModelVersions(){
		return modelVersions;
	}
}
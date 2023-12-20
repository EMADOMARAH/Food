package com.emadomarah.food.network.Models.Response.Image;

import java.util.List;

public class SegmentationResultsItem{
	private ContainedBbox containedBbox;
	private List<Integer> polygon;
	private Center center;
	private List<RecognitionResultsItem> recognitionResults;
	private int foodItemPosition;

	public ContainedBbox getContainedBbox(){
		return containedBbox;
	}

	public List<Integer> getPolygon(){
		return polygon;
	}

	public Center getCenter(){
		return center;
	}

	public List<RecognitionResultsItem> getRecognitionResults(){
		return recognitionResults;
	}

	public int getFoodItemPosition(){
		return foodItemPosition;
	}
}
package com.emadomarah.food.network.Models.Response.Image;

public class ModelVersions{
	private String foodrec;
	private String foodType;
	private String drinks;
	private String ingredients;
	private String segmentation;
	private String foodgroups;
	private String ontology;

	public String getFoodrec(){
		return foodrec;
	}

	public String getFoodType(){
		return foodType;
	}

	public String getDrinks(){
		return drinks;
	}

	public String getIngredients(){
		return ingredients;
	}

	public String getSegmentation(){
		return segmentation;
	}

	public String getFoodgroups(){
		return foodgroups;
	}

	public String getOntology(){
		return ontology;
	}
}

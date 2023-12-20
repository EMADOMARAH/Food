package com.emadomarah.food.network.Models.Response.ImageId;

import com.google.gson.annotations.SerializedName;

public class DailyIntakeReference{

	@SerializedName("PROCNT")
	private PROCNT pROCNT;

	@SerializedName("ENERC_KCAL")
	private ENERCKCAL eNERCKCAL;

	@SerializedName("FASAT")
	private FASAT fASAT;

	@SerializedName("NA")
	private NA nA;

	@SerializedName("FAT")
	private FAT fAT;

	@SerializedName("CHOCDF")
	private CHOCDF cHOCDF;

	@SerializedName("SUGAR.added")
	private SUGARAdded sUGARAdded;

	@SerializedName("SUGAR")
	private SUGAR sUGAR;

	public PROCNT getPROCNT(){
		return pROCNT;
	}

	public ENERCKCAL getENERCKCAL(){
		return eNERCKCAL;
	}

	public FASAT getFASAT(){
		return fASAT;
	}

	public NA getNA(){
		return nA;
	}

	public FAT getFAT(){
		return fAT;
	}

	public CHOCDF getCHOCDF(){
		return cHOCDF;
	}

	public SUGARAdded getSUGARAdded(){
		return sUGARAdded;
	}

	public SUGAR getSUGAR(){
		return sUGAR;
	}
}
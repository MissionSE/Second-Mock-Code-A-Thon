package com.missionse.arcticthunder.model;

import java.io.Serializable;

public class AssetObject implements Serializable  {

	private static final long serialVersionUID = -2020039213442656557L;
	
	private long latitude;
	private long longitude;
	
	private AssetType type;
	private int uid;
	
	
	public AssetObject(long latitude, long longitude, AssetType type, int uid){
		this.latitude = latitude;
		this.longitude = longitude;
		this.type = type;
		this.uid = uid;
		
	}


	public long getLatitude() {
		return latitude;
	}


	public void setLatitude(long latitude) {
		this.latitude = latitude;
	}


	public long getLongitude() {
		return longitude;
	}


	public void setLongitude(long longitude) {
		this.longitude = longitude;
	}


	public AssetType getType() {
		return type;
	}


	public void setType(AssetType type) {
		this.type = type;
	}


	public int getUid() {
		return uid;
	}


	public void setUid(int uid) {
		this.uid = uid;
	}
	
	

}

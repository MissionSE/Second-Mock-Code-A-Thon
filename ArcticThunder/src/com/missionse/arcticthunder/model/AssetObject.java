package com.missionse.arcticthunder.model;

import java.io.Serializable;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

public class AssetObject implements Serializable  {

	private static int currentID = 0;
	private static final long serialVersionUID = -2020039213442656557L;

	private double latitude;
	private double longitude;

	private AssetType type;
	private int uid;

	public AssetObject(final double latitude, final double longitude, final AssetType type) {
		this.latitude = latitude;
		this.longitude = longitude;
		this.type = type;
		this.uid = currentID++;
	}
	
	public AssetObject(Location location, AssetType type){
		latitude = location.getLatitude();
		longitude = location.getLongitude();
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(final double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(final double longitude) {
		this.longitude = longitude;
	}

	public AssetType getType() {
		return type;
	}

	public void setType(final AssetType type) {
		this.type = type;
	}

	public int getUid() {
		return uid;
	}

	public LatLng getLatLng() {
		return new LatLng(latitude, longitude);
	}
}

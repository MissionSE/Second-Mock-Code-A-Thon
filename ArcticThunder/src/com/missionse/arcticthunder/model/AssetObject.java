package com.missionse.arcticthunder.model;

import java.io.Serializable;

import com.google.android.gms.maps.model.LatLng;

public class AssetObject implements Serializable  {

	private static int currentID = 0;
	private static final long serialVersionUID = -2020039213442656557L;

	private long latitude;
	private long longitude;

	private AssetType type;
	private int uid;

	public AssetObject(final long latitude, final long longitude, final AssetType type) {
		this.latitude = latitude;
		this.longitude = longitude;
		this.type = type;
		this.uid = currentID++;
	}

	public long getLatitude() {
		return latitude;
	}

	public void setLatitude(final long latitude) {
		this.latitude = latitude;
	}

	public long getLongitude() {
		return longitude;
	}

	public void setLongitude(final long longitude) {
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

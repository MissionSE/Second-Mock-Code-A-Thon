package com.missionse.arcticthunder.model;

import android.graphics.Color;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.missionse.arcticthunder.R;

public enum AssetType {

	FRIEND (0, Color.BLUE, BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher)),
	ENEMY_VEHICLE (0, Color.BLUE, BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher)),
	ENEMY_BUILDING (0, Color.BLUE, BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher)),
	ENEMY_WATCH_STAND (0, Color.BLUE, BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher)),
	ENEMY_ROAMING_TROOP (0, Color.BLUE, BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher)),
	TOWN_CHURCH (0, Color.BLUE, BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher)),
	TOWN_SCHOOL (0, Color.BLUE, BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher)),
	TOWN_MALL (0, Color.BLUE, BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher)),
	PHOTO (0, Color.BLUE, BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher)),
	VIDEO (0, Color.BLUE, BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher)),
	WIFI (0, Color.BLUE, BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher)),
	POSSIBLE_THREAT (0, Color.BLUE, BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher));

	private double radius;
	private BitmapDescriptor bitmap;
	private int color;

	AssetType(final double r, final int c, final BitmapDescriptor image) {
		radius = r;
		color = c;
		bitmap = image;
	}

	public double getRadius() {
		return radius;
	}

	public BitmapDescriptor getBitmap() {
		return bitmap;
	}

	public int getColor() {
		return color;
	}
}

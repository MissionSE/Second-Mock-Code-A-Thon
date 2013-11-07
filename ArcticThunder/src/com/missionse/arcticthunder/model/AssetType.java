package com.missionse.arcticthunder.model;

import android.graphics.Color;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.missionse.arcticthunder.R;

public enum AssetType {
	FRIEND (1, Color.argb(50, 30, 220, 60), R.drawable.friendly_icon, BitmapDescriptorFactory.fromResource(R.drawable.friendly_icon), "Friend"),
	ENEMY_VEHICLE (2, Color.argb(50, 220, 20, 20), R.drawable.enemy_icon, BitmapDescriptorFactory.fromResource(R.drawable.enemy_icon), "Enemy Vehicle"),
	ENEMY_BUILDING (0, Color.argb(50, 220, 20, 20), R.drawable.enemy_icon, BitmapDescriptorFactory.fromResource(R.drawable.enemy_icon), "Enemy Building"),
	ENEMY_WATCH_STAND (0, Color.argb(50, 220, 20, 20), R.drawable.enemy_icon, BitmapDescriptorFactory.fromResource(R.drawable.enemy_icon), "Enemy Watch Stand"),
	ENEMY_ROAMING_TROOP (10, Color.argb(50, 220, 20, 20), R.drawable.enemy_icon, BitmapDescriptorFactory.fromResource(R.drawable.enemy_icon), "Enemy Roaming Troop"),
	TOWN_CHURCH (0, Color.argb(50, 0, 100, 255), R.drawable.church_icon, BitmapDescriptorFactory.fromResource(R.drawable.church_icon), "Town Church"),
	TOWN_SCHOOL (0, Color.argb(50, 0, 100, 255), R.drawable.school_icon, BitmapDescriptorFactory.fromResource(R.drawable.school_icon), "Town School"),
	TOWN_MALL (0, Color.argb(50, 0, 100, 255), R.drawable.mall_icon, BitmapDescriptorFactory.fromResource(R.drawable.mall_icon), "Town Mall"),
	PHOTO (0, Color.argb(50, 255, 150, 0), R.drawable.camera_icon, BitmapDescriptorFactory.fromResource(R.drawable.camera_icon), "Photo"),
	VIDEO (0, Color.argb(50, 255, 150, 0), R.drawable.video_icon, BitmapDescriptorFactory.fromResource(R.drawable.video_icon), "Video"),
	WIFI (50, Color.argb(50, 20, 220, 220), R.drawable.wifi_icon, BitmapDescriptorFactory.fromResource(R.drawable.wifi_icon), "Wifi Hotspot"),
	POSSIBLE_THREAT (50, Color.argb(50, 255, 255, 0), R.drawable.ic_launcher, BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher), "Possible Threat");

	private double radius;
	private BitmapDescriptor bitmap;
	private int color;
	private int resource;
	private String text;

	AssetType(final double r, final int c, final int resourceID, final BitmapDescriptor image, final String description) {
		radius = r;
		color = c;
		resource = resourceID;
		bitmap = image;
		text = description;
	}

	public double getRadius() {
		return radius;
	}

	public int getResourceId() {
		return resource;
	}

	public BitmapDescriptor getBitmap() {
		return bitmap;
	}

	public int getColor() {
		return color;
	}

	public String toString() {
		return text;
	}

	public static CharSequence[] valuesAsCharSequence() {
		CharSequence[] value = new CharSequence[values().length];
		AssetType[] types = values();
		for (int i = 0; i < types.length; i++) {
			value[i] = types[i].toString();
		}
		return value;
	}
}

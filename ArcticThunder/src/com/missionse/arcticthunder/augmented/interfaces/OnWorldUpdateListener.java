package com.missionse.arcticthunder.augmented.interfaces;

import gl.GLCamera;

import java.util.List;

import markerDetection.MarkerObjectMap;
import worldData.World;
import android.app.Activity;

import com.missionse.arcticthunder.model.AssetObject;

public interface OnWorldUpdateListener {
	
	public void onWorldUpdate(Activity activity, GLCamera camera, World world, MarkerObjectMap markerMap);
	
	public List<AssetObject> getAssetObjectsForWorld();

}

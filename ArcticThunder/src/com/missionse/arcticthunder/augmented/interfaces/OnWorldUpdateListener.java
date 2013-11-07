package com.missionse.arcticthunder.augmented.interfaces;

import gl.GLCamera;
import markerDetection.MarkerObjectMap;
import worldData.World;
import android.app.Activity;

public interface OnWorldUpdateListener {
	
	public void onWorldUpdate(Activity activity, GLCamera camera, World world, MarkerObjectMap markerMap);

}

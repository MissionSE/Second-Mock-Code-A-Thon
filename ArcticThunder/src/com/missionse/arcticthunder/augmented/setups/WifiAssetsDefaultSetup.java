package com.missionse.arcticthunder.augmented.setups;

import geo.GeoObj;
import gl.GLCamera;
import gl.GLFactory;
import gl.scenegraph.MeshComponent;
import gui.GuiSetup;

import java.util.List;

import markerDetection.MarkerObjectMap;
import v2.simpleUi.util.IO;
import worldData.Obj;
import worldData.World;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.missionse.arcticthunder.ArcticThunderActivity;
import com.missionse.arcticthunder.augmented.components.MeshComponentFactory;
import com.missionse.arcticthunder.augmented.interfaces.OnWifiProximityListener;
import com.missionse.arcticthunder.model.AssetObject;
import com.missionse.arcticthunder.model.AssetType;
import commands.Command;
import components.ProximitySensor;

public class WifiAssetsDefaultSetup extends DefaultSetup {
	
	private List<AssetObject> defaultAssets;
	private OnWifiProximityListener[] listeners;
	private ArcticThunderActivity parentActivity;
	
	public WifiAssetsDefaultSetup(ArcticThunderActivity a, List<AssetObject> assets, OnWifiProximityListener... l){
		super(true);
		defaultAssets = assets;
		listeners = l;
		parentActivity = a;
	}

	@Override
	public void _x_addDefaultMarkers(MarkerObjectMap markerObjectMap) {
	}

	@Override
	public void _y_addDefaultObjects(World world) {
		
		for(AssetObject asset : defaultAssets){
			logInfo("Creating an marker at " + asset.getLatitude() + " / " + asset.getLongitude());
			
			if(asset.getType() == AssetType.WIFI){
				MeshComponent m = MeshComponentFactory.createArrowWithCircle();
				GeoObj o = new GeoObj();
				o.setComp(new ProximitySensor(getCamera(), 3f) {
					@Override
					public void onObjectIsCloseToCamera(GLCamera myCamera2, Obj obj,
							MeshComponent m, float currentDistance) {
						notifyWifiProxyListeners();
					}
				});
				
				o.setComp(m);
				o.setMyLatitude(asset.getLatitude());
				o.setMyLongitude(asset.getLongitude());
				
				world.add(o);
			}else{
				Bitmap b = IO.loadBitmapFromId(getActivity(), asset.getType().getResourceId());
				Bitmap scaled = createScaledBitmap(b, 5, true);
				b.recycle();
				MeshComponent m = GLFactory.getInstance().newTexturedSquare(
						Integer.valueOf(asset.getUid()).toString(),
						scaled,
						asset.getType().getResourceId());
				
				GeoObj o = new GeoObj();
				o.setComp(m);
				o.setMyLatitude(asset.getLatitude());
				o.setMyLongitude(asset.getLongitude());
					
				world.add(o);
			}
		}
	}
	
	
	 public static Bitmap createScaledBitmap(Bitmap src, float scale, boolean filtering)
	 {
		    int width = (int)( src.getWidth() * scale + 0.5f);
		    int height = (int)( src.getHeight() * scale + 0.5f);
		    return Bitmap.createScaledBitmap(src, width, height, filtering);
	}
	
	public void notifyWifiProxyListeners(){
		logInfo("NOTIFING LISTENERS PROXY REACHED");
		for(OnWifiProximityListener l : listeners){
			l.onWifiProximityReached(getActivity());
		}
	}
	
	public void logInfo(String msg){
		Log.i("ASSETS SETUP", msg);
	}

	@Override
	public void _e3_addElementsToUi(GuiSetup guiSetup, Activity activity) {
		guiSetup.addButtonToBottomView(new Command() {
						@Override
						public boolean execute() {
							logInfo("CREATING ENEMY MARK");
//							Vec rayPosition = new Vec();
//							Vec rayDirection = new Vec();
//							getCamera().getPickingRay(rayPosition, rayDirection,
//									GLRenderer.halfWidth, GLRenderer.halfHeight);
//							
//							System.out.println("rayPosition=" + rayPosition);
//							Systet.println("rayDirection=" + rayDirection);
//			
//							rayDirection.setLength(5);
//							// mesh1.setPosition(rayPosition.add(rayDirection));
//							MeshComponent mesh4 = new Shape();
//							mesh4.addChild(GLFactory.getInstance().newDiamond(Color.battleshipGrey()));
//			
							GeoObj o = new GeoObj();
//							o.setComp(mesh4);
//							//getWorld().add(mesh4);
//			
//							//o.setPosition(rayPosition.add(rayDirection));
//							o.setMyPosition(rayPosition.add(rayDirection));
							o.setMyLatitude(getCamera().getGPSLocation().getLatitude());
							o.setMyLongitude(getCamera().getGPSLocation().getLongitude());
							logInfo("Creating new mark at " + o.getLatitude() + " / " + o.getLongitude());
							if(parentActivity != null)
								parentActivity.createAsset(o.getLatitude(), o.getLongitude(),getActivity());
							
							return false;
						}
					}, "Mark");
	}
	
	
	

}

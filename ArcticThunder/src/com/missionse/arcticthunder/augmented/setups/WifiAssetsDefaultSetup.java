package com.missionse.arcticthunder.augmented.setups;

import geo.GeoObj;
import gl.GLCamera;
import gl.scenegraph.MeshComponent;
import gui.GuiSetup;

import java.util.List;

import markerDetection.MarkerObjectMap;
import worldData.Obj;
import worldData.World;
import android.app.Activity;
import android.util.Log;

import com.missionse.arcticthunder.augmented.components.MeshComponentFactory;
import com.missionse.arcticthunder.augmented.interfaces.OnWifiProximityListener;
import com.missionse.arcticthunder.model.AssetObject;
import com.missionse.arcticthunder.model.AssetType;
import commands.Command;
import components.ProximitySensor;

public class WifiAssetsDefaultSetup extends DefaultSetup {
	
	private List<AssetObject> defaultAssets;
	private OnWifiProximityListener[] listeners;
	
	public WifiAssetsDefaultSetup(List<AssetObject> assets, OnWifiProximityListener... l){
		super(true);
		defaultAssets = assets;
		listeners = l;
	}

	@Override
	public void _x_addDefaultMarkers(MarkerObjectMap markerObjectMap) {
	}

	@Override
	public void _y_addDefaultObjects(World world) {
		
		for(AssetObject asset : defaultAssets){
			if(asset.getType() == AssetType.WIFI){
				
				logInfo("Creating an marker at " + asset.getLatitude() + " / " + asset.getLongitude());
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
			}
		}
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
//							System.out.println("rayDirection=" + rayDirection);
//			
//							rayDirection.setLength(5);
//							// mesh1.setPosition(rayPosition.add(rayDirection));
//							MeshComponent mesh4 = new Shape();
//							mesh4.addChild(GLFactory.getInstance().newArrow());
//			
//							Obj o = new Obj();
//							o.setComp(mesh4);
//							getWorld().add(mesh4);
//			
//							mesh4.setPosition(rayPosition.add(rayDirection));
//			
//							mesh4.get
							
							return false;
						}
					}, "Mark Enemy");
		guiSetup.addButtonToBottomView(new Command() {
			@Override
			public boolean execute() {
				logInfo("CREATING SPY MARK");
//				Vec rayPosition = new Vec();
//				Vec rayDirection = new Vec();
//				getCamera().getPickingRay(rayPosition, rayDirection,
//						GLRenderer.halfWidth, GLRenderer.halfHeight);
//				
//				System.out.println("rayPosition=" + rayPosition);
//				System.out.println("rayDirection=" + rayDirection);
//
//				rayDirection.setLength(5);
//				// mesh1.setPosition(rayPosition.add(rayDirection));
//				MeshComponent mesh4 = new Shape();
//				mesh4.addChild(GLFactory.getInstance().newArrow());
//
//				Obj o = new Obj();
//				o.setComp(mesh4);
//				getWorld().add(mesh4);
//
//				mesh4.setPosition(rayPosition.add(rayDirection));
//
//				mesh4.get
				
				return false;
			}
		}, "Mark Spy");
		guiSetup.addButtonToBottomView(new Command() {
			@Override
			public boolean execute() {
				logInfo("CREATING ENEMY FRIEND");
//				Vec rayPosition = new Vec();
//				Vec rayDirection = new Vec();
//				getCamera().getPickingRay(rayPosition, rayDirection,
//						GLRenderer.halfWidth, GLRenderer.halfHeight);
//				
//				System.out.println("rayPosition=" + rayPosition);
//				System.out.println("rayDirection=" + rayDirection);
//
//				rayDirection.setLength(5);
//				// mesh1.setPosition(rayPosition.add(rayDirection));
//				MeshComponent mesh4 = new Shape();
//				mesh4.addChild(GLFactory.getInstance().newArrow());
//
//				Obj o = new Obj();
//				o.setComp(mesh4);
//				getWorld().add(mesh4);
//
//				mesh4.setPosition(rayPosition.add(rayDirection));
//
//				mesh4.get
				
				return false;
			}
		}, "Mark Friend");
	}
	
	
	

}

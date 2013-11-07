package com.missionse.arcticthunder.map;

import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_HYBRID;

import java.util.HashMap;
import java.util.Map.Entry;

import android.app.Fragment;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.missionse.arcticthunder.ArcticThunderActivity;
import com.missionse.arcticthunder.R;
import com.missionse.arcticthunder.model.AssetObject;
import com.missionse.arcticthunder.model.AssetType;

public class MapsFragment extends Fragment implements ConnectionCallbacks, OnConnectionFailedListener,
LocationListener, OnMyLocationButtonClickListener, OnMapClickListener, OnMapLongClickListener {

	private static final LatLng MSE = new LatLng(39.974552, -74.976844);
	private static final LatLng ZONE_A = new LatLng(39.974074, -74.977462);
	private static final LatLng ZONE_B = new LatLng(39.975233, -74.977328);
	private static final LatLng ZONE_C = new LatLng(39.975085, -74.976164);

	private HashMap<AssetObject, AssetMarker> mAssetMarkers;

	private HashMap<AssetType, Boolean> mAssetTypeVisibility;

	private GoogleMap mMap;

	private LocationClient mLocationClient;

	private boolean mFirstLocationChange = true;

	private static View view;

	// These settings are the same as the settings for the map. They will in fact give you updates
	// at the maximal rates currently possible.
	private static final LocationRequest REQUEST = LocationRequest.create().setInterval(5000) // 5 seconds
			.setFastestInterval(16) // 16ms = 60fps
			.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

	private class AssetMarker {

		private Marker mCenterMarker;
		private Circle mCircle;

		AssetMarker(final AssetObject asset) {
			
			mCenterMarker = mMap.addMarker(new MarkerOptions()
				.icon(asset.getType().getBitmap())
				.title(asset.getType().toString())
				.position(asset.getLatLng()));
			mCircle = mMap.addCircle(new CircleOptions()
				.center(asset.getLatLng())
				.radius(asset.getType().getRadius())
				.fillColor(asset.getType().getColor())
				.strokeWidth(0));
		}

		public void setVisible(final boolean visible) {
			mCenterMarker.setVisible(visible);
			mCircle.setVisible(visible);
		}
	}

	public MapsFragment() {
		mAssetMarkers = new HashMap<AssetObject, AssetMarker>();
		mAssetTypeVisibility = new HashMap<AssetType, Boolean>();
	}

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {

		if (view != null) {
			ViewGroup parent = (ViewGroup) view.getParent();
			if (parent != null) {
				parent.removeView(view);
			}
		}
		try {
			view = inflater.inflate(R.layout.fragment_map, container, false);
		} catch (InflateException e) {
			// Map already exists.
		}

		return view;
	}

	@Override
	public boolean onMyLocationButtonClick() {
		return false;
	}

	@Override
	public void onLocationChanged(final Location arg0) {
		if (mFirstLocationChange) {
			mMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(MSE, 17.5f, 0, 27)));
			mFirstLocationChange = false;
		}
	}

	@Override
	public void onConnectionFailed(final ConnectionResult arg0) {
		// Do nothing.
	}

	@Override
	public void onConnected(final Bundle arg0) {
		mLocationClient.requestLocationUpdates(REQUEST, this); // LocationListener
	}

	@Override
	public void onDisconnected() {
		// Do nothing.
	}

	@Override
	public void onResume() {
		super.onResume();
		setUpMapIfNeeded();
		setUpLocationClientIfNeeded();
		mLocationClient.connect();
	}

	@Override
	public void onPause() {
		super.onPause();
		if (mLocationClient != null) {
			mLocationClient.disconnect();
		}
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		switch (item.getItemId()) {
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void setUpMapIfNeeded() {
		// Do a null check to confirm that we have not already instantiated the map.
		if (mMap == null) {
			// Try to obtain the map from the SupportMapFragment.
			mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
			// Check if we were successful in obtaining the map.
			if (mMap != null) {
				setUpMap();
			}
		}
	}

	private void setUpLocationClientIfNeeded() {
		if (mLocationClient == null) {
			mLocationClient = new LocationClient(getActivity(), this, // ConnectionCallbacks
					this); // OnConnectionFailedListener
		}
	}

	private void setUpMap() {
		mMap.setMyLocationEnabled(true);
		mMap.setOnMyLocationButtonClickListener(this);
		mMap.setOnMapClickListener(this);
		mMap.setOnMapLongClickListener(this);
		mMap.setBuildingsEnabled(true);
		mMap.setMapType(MAP_TYPE_HYBRID);
		((ArcticThunderActivity)getActivity()).createWifiAssets();
		for (AssetType type: AssetType.values()) {
			mAssetTypeVisibility.put(type, true);
		}
	}

	public void addAsset(final AssetObject asset) {

		AssetMarker assetMarker = new AssetMarker(asset);
		mAssetMarkers.put(asset, assetMarker);
	}

	@Override
	public void onMapLongClick(final LatLng point) {
		((ArcticThunderActivity) getActivity()).createAsset(point.latitude, point.longitude, getActivity());
	}

	@Override
	public void onMapClick(final LatLng point) {
		Log.e("MapsFragment", "onMapClick: " + point);
	}

	public void setAssetShown(final AssetType type, final boolean visible) {
		mAssetTypeVisibility.put(type, Boolean.valueOf(visible));

		for (Entry<AssetObject, AssetMarker> entry : mAssetMarkers.entrySet()) {
			if (entry.getKey().getType() == type) {
				entry.getValue().setVisible(visible);
			}
		}
	}

	public boolean isAssetShown(final AssetType type) {
		Boolean visible = mAssetTypeVisibility.get(type);
		if (visible != null) {
			return visible.booleanValue();
		}
		return false;
	}
}

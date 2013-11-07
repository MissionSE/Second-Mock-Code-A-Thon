package com.missionse.arcticthunder;

import java.util.LinkedList;
import java.util.List;

import system.ArActivity;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.missionse.arcticthunder.augmented.interfaces.OnWifiProximityListener;
import com.missionse.arcticthunder.augmented.setups.WifiAssetsDefaultSetup;
import com.missionse.arcticthunder.map.MapsFragment;
import com.missionse.arcticthunder.model.AssetObject;
import com.missionse.arcticthunder.model.AssetType;
import com.missionse.arcticthunder.modelviewer.ModelViewerFragment;
import com.missionse.arcticthunder.modelviewer.ModelViewerFragmentFactory;
import com.missionse.arcticthunder.modelviewer.ObjectLoadedListener;
import com.missionse.arcticthunder.videoviewer.VideoFragment;
import com.missionse.arcticthunder.videoviewer.VideoFragmentFactory;
import com.missionse.arcticthunder.wifidirect.PeerDetailFragment;
import com.missionse.arcticthunder.wifidirect.PeersListFragment;
import com.missionse.arcticthunder.wifidirect.connector.ConnectionInitiationListener;
import com.missionse.arcticthunder.wifidirect.connector.DisconnectionListener;
import com.missionse.arcticthunder.wifidirect.connector.DiscoverPeersListener;
import com.missionse.arcticthunder.wifidirect.connector.P2pStateChangeHandler;
import com.missionse.arcticthunder.wifidirect.connector.WifiDirectConnector;

public class ArcticThunderActivity extends Activity implements ObjectLoadedListener, OnWifiProximityListener {

	private final WifiDirectConnector wifiDirectConnector = new WifiDirectConnector();

	private SlidingMenu navigationDrawer;
	private SlidingMenu filterDrawer;

	private MapsFragment mapsFragment;
	private ModelViewerFragment modelViewerFragment;
	private List<AssetObject> assets = new LinkedList<AssetObject>();
	private VideoFragment videoFragment;

	private PeerDetailFragment peerDetailFragment;
	private PeersListFragment peersListFragment;
	//private ModelControllerClient modelClient;
	//private ModelControllerServer modelServer;
	private WifiP2pDevice targetDevice;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		wifiDirectConnector.onCreate(this);

		mapsFragment = new MapsFragment();
		modelViewerFragment = ModelViewerFragmentFactory.createObjModelFragment(R.raw.lobby_obj);
		modelViewerFragment.registerObjectLoadedListener(this);

		videoFragment = VideoFragmentFactory.createVideoFragment(R.raw.security_video);
		peerDetailFragment = new PeerDetailFragment();
		peersListFragment = new PeersListFragment();

		createNavigationMenu();

		createFilterMenu();

		showMap();
	}

	private void createNavigationMenu() {
		navigationDrawer = new SlidingMenu(this);
		navigationDrawer.setMode(SlidingMenu.LEFT);
		navigationDrawer.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		navigationDrawer.setShadowWidthRes(R.dimen.drawer_shadow_width);
		navigationDrawer.setShadowDrawable(R.drawable.shadow_left);
		navigationDrawer.setBehindWidthRes(R.dimen.nav_drawer_width);
		navigationDrawer.attachToActivity(this, SlidingMenu.SLIDING_WINDOW);
		navigationDrawer.setMenu(R.layout.nav_drawer);

		Fragment leftDrawerFragment;
		FragmentTransaction transaction = this.getFragmentManager().beginTransaction();
		leftDrawerFragment = new NavigationDrawerFragment();
		transaction.replace(R.id.nav_drawer, leftDrawerFragment);
		transaction.commit();
	}

	private void createFilterMenu() {
		filterDrawer = new SlidingMenu(this);
		filterDrawer.setMode(SlidingMenu.RIGHT);
		filterDrawer.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		filterDrawer.setShadowWidthRes(R.dimen.drawer_shadow_width);
		filterDrawer.setShadowDrawable(R.drawable.shadow_right);
		filterDrawer.setBehindWidthRes(R.dimen.filter_drawer_width);
		filterDrawer.attachToActivity(this, SlidingMenu.SLIDING_WINDOW);
		filterDrawer.setMenu(R.layout.filter_drawer);

		Fragment rightDrawerFragment;
		FragmentTransaction transaction = this.getFragmentManager().beginTransaction();
		rightDrawerFragment = new FilterDrawerFragment();
		transaction.replace(R.id.filter_drawer, rightDrawerFragment);
		transaction.commit();
	}

	@Override
	public void onResume() {
		super.onResume();
		wifiDirectConnector.onResume(this);
		wifiDirectConnector.addStateChangeHandler(new P2pStateChangeHandler() {
			@Override
			public void onPeersAvailable(final WifiP2pDeviceList peers) {
				// Peer discovery has finished, and we have a list of peers.
				peersListFragment.setAvailablePeers(peers);

				// If we got a list of 0 peers, we've either disconnected, or there are no peers to be found after
				// the timeout.
				if (peers.getDeviceList().size() == 0) {
					// Ensure that the detail fragment is made aware of a potential disconnect.
					peerDetailFragment.setTargetDevice(null);
					peerDetailFragment.setConnectionSuccessfulInformation(null);

					FragmentManager fragmentManager = getFragmentManager();
					fragmentManager.beginTransaction().replace(R.id.content, peersListFragment).commit();

					fragmentManager.executePendingTransactions();
				}
			}

			@Override
			public void onConnectionInfoAvailable(final WifiP2pInfo connectionInfo) {
				// We have made a connection, and the PeerDetail fragment should receive the connection information.
				peerDetailFragment.setConnectionSuccessfulInformation(connectionInfo);

				showToast("Connection successful.");

				// Start the server thread to listen for incoming model state changes.
				//modelServer = new ModelControllerServer();
				//modelServer.execute(modelFragment);

				//modelClient.onConnectionSuccessful(connectionInfo, targetDevice);
			}

			@Override
			public void onDeviceChanged(final WifiP2pDevice thisDevice) {
				// Our own device has changed.
				peersListFragment.setThisDeviceInfo(thisDevice);

				peersListFragment.refresh();
			}
		});
	}

	@Override
	public void onPause() {
		super.onPause();
		wifiDirectConnector.onPause(this);
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/**
	 * HOOKS FROM MENUS
	 */

	public void showMap() {
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction().replace(R.id.content, mapsFragment).commit();
	}

	public void showAR() {
		WifiAssetsDefaultSetup s = 
				new WifiAssetsDefaultSetup(
						this,
						getAssetList(),
						(OnWifiProximityListener)this);
		ArActivity.startWithSetup(this, s);

	}

	public void showCamera() {
		showWifiDirect();
	}

	public void showChat() {
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction().replace(R.id.content, modelViewerFragment).commit();

	}

	public void showModelViewer() {
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction().replace(R.id.content, modelViewerFragment).commit();
	}

	public void showVideo() {
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction().replace(R.id.content, videoFragment).commit();

	}

	/**
	 * Networking callbacks
	 */

	public void showWifiDirect() {
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction().replace(R.id.content, peersListFragment).addToBackStack("PeersList")
				.commit();

		fragmentManager.executePendingTransactions();

		wifiDirectConnector.discoverPeers(new DiscoverPeersListener() {
			@Override
			public void onP2pNotEnabled() {
				showToast("You must enable P2P first!");
			}

			@Override
			public void onDiscoverPeersSuccess() {
				showToast("Discovery Initiated");
				peersListFragment.discoveryInitiated();
			}

			@Override
			public void onDiscoverPeersFailure(final int reasonCode) {
				showToast("Discovery Failed");
			}
		});
	}

	public void sendDataOverWifi() {

	}

	public void connect() {
		// Called by the PeerDetail fragment when the Connect button is pressed.
		WifiP2pConfig config = new WifiP2pConfig();
		config.deviceAddress = targetDevice.deviceAddress;
		config.wps.setup = WpsInfo.PBC;
		config.groupOwnerIntent = 0;

		wifiDirectConnector.connect(config, new ConnectionInitiationListener() {
			@Override
			public void onConnectionInitiationSuccess() {
				showToast("Initiating connection...");
			}

			@Override
			public void onConnectionInitiationFailure() {
				showToast("Connection failed. Retry.");
			}
		});
	}

	public void disconnect() {
		// Called by the PeerDetail fragment when the Disconnect button is pressed.
		wifiDirectConnector.disconnect(new DisconnectionListener() {
			@Override
			public void onDisconnectionSuccess() {
				peerDetailFragment.setTargetDevice(null);
				peerDetailFragment.setConnectionSuccessfulInformation(null);
				//peerDetailFragment.refresh();

				// On disconnect, stop the client from sending, and shut down the server thread.
				//modelClient.onDisconnect();

				//modelServer.cancel(true);
				//modelServer = null;

				targetDevice = null;
			}

			@Override
			public void onDisconnectionFailure() {
				showToast("Disconnection failed. Try again.");
			}
		});
	}

	public void showPeerDetails(final WifiP2pDevice device) {
		// This is called by the PeerList fragment, when an item is selected. Save off the target device for
		// the server connection, give it to the PeerDetail fragment, and switch the ViewPager automatically.
		targetDevice = device;
		peerDetailFragment.setTargetDevice(device);

		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction().replace(R.id.content, peerDetailFragment).addToBackStack("PeerDetail")
				.commit();

		fragmentManager.executePendingTransactions();

		peerDetailFragment.refresh();
	}

	/**
	 * CALLBACKS
	 */

	@Override
	public void onObjectLoaded() {
		modelViewerFragment.getController().scale(0.00025f);
		modelViewerFragment.getAnimator().scaleTo(0.045f, 1000);
		modelViewerFragment.getAnimator().rotateTo(-45f, 225f, 0f, 1000);
	}

	/**
	 * HOOKS FOR MANIUPLATING ASSETS
	 */

	public void setAssetShown(final AssetType type, final boolean visible) {
		mapsFragment.setAssetShown(type, visible);
	}

	public boolean isAssetShown(final AssetType type) {
		return mapsFragment.isAssetShown(type);
	}

	public void createAsset(final double lat, final double log) {
		// TODO:
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.identify_asset).setItems(AssetType.valuesAsCharSequence(),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(final DialogInterface dialog, final int which) {
						//AssetType assetType = AssetType.values()[which];
						//AssetObject asset = new AssetObject(
						//		mCurrentAssetLatLng.latitude,
						//		mCurrentAssetLatLng.longitude, assetType);
						//AssetMarker assetMarker = new AssetMarker(asset);
						//mAssetMarkers.add(assetMarker);
						//TODO:
					}
				});
		builder.create();
		builder.show();
	}

	public List<AssetObject> getAssetList() {
		return assets;
	}

	private void showToast(final String message) {
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onWifiProximityReached(Activity activity) {
		// TODO Auto-generated method stub
		
	}

}

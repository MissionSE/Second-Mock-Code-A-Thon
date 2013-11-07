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
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.missionse.arcticthunder.augmented.interfaces.OnWifiProximityListener;
import com.missionse.arcticthunder.augmented.setups.WifiAssetsDefaultSetup;
import com.missionse.arcticthunder.camera.PictureFragment;
import com.missionse.arcticthunder.imageviewer.ImageFragment;
import com.missionse.arcticthunder.map.MapsFragment;
import com.missionse.arcticthunder.model.AssetObject;
import com.missionse.arcticthunder.model.AssetType;
import com.missionse.arcticthunder.modelviewer.ModelViewerFragment;
import com.missionse.arcticthunder.modelviewer.ModelViewerFragmentFactory;
import com.missionse.arcticthunder.modelviewer.ObjectLoadedListener;
import com.missionse.arcticthunder.nfc.NfcConnectionListener;
import com.missionse.arcticthunder.nfc.NfcConnector;
import com.missionse.arcticthunder.videoviewer.VideoFragment;
import com.missionse.arcticthunder.videoviewer.VideoFragmentFactory;
import com.missionse.arcticthunder.wifidirect.PeerDetailFragment;
import com.missionse.arcticthunder.wifidirect.PeersListFragment;
import com.missionse.arcticthunder.wifidirect.connector.ConnectionInitiationListener;
import com.missionse.arcticthunder.wifidirect.connector.DisconnectionListener;
import com.missionse.arcticthunder.wifidirect.connector.DiscoverPeersListener;
import com.missionse.arcticthunder.wifidirect.connector.P2pStateChangeHandler;
import com.missionse.arcticthunder.wifidirect.connector.WifiDirectConnector;
import com.missionse.arcticthunder.wifidirect.network.Client;
import com.missionse.arcticthunder.wifidirect.network.Server;

public class ArcticThunderActivity extends Activity implements ObjectLoadedListener, OnWifiProximityListener {

	private static int NFC_COUNT = 0;

	static final int TAKE_SECURITY_PICTURE = 1234;

	private final WifiDirectConnector wifiDirectConnector = new WifiDirectConnector();
	private final NfcConnector nfcConnector = new NfcConnector();

	private WifiP2pInfo connectionInfo;

	public static boolean COMMANDER_MODE = false;

	private SlidingMenu navigationDrawer;
	private SlidingMenu filterDrawer;

	private MapsFragment mapsFragment;
	private ModelViewerFragment modelViewerFragment;
	private List<AssetObject> assets = new LinkedList<AssetObject>();
	private VideoFragment videoFragment;
	private ImageFragment imageFragment;
	private PictureFragment pictureFragment;

	private PeerDetailFragment peerDetailFragment;
	private PeersListFragment peersListFragment;
	private Client client;
	private Server server;
	private WifiP2pDevice targetDevice;
	private boolean populate = false;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		client = new Client(this);

		wifiDirectConnector.onCreate(this);
		nfcConnector.onCreate(this);

		mapsFragment = new MapsFragment();
		modelViewerFragment = ModelViewerFragmentFactory.createObjModelFragment(R.raw.lobby_obj);
		modelViewerFragment.registerObjectLoadedListener(this);

		videoFragment = VideoFragmentFactory.createVideoFragment(R.raw.security_video);
		imageFragment = new ImageFragment();

		peerDetailFragment = new PeerDetailFragment();
		peersListFragment = new PeersListFragment();

		pictureFragment = new PictureFragment();

		createNavigationMenu();

		createFilterMenu();

		showMap();

	}

	@Override
	public void onNewIntent(final Intent intent) {
		Log.e("something", "got nfc");
		setIntent(intent);
		NfcConnector.parseIntent(getIntent(), new NfcConnectionListener() {
			@Override
			public void onNfcConnection() {
				Log.e("something", "got nfc");

				ArcticThunderActivity.this.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						NFC_COUNT++;
						showToast("saw NFC: " + NFC_COUNT);
					}

				});

			}
		});
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

		NfcConnector.parseIntent(getIntent(), new NfcConnectionListener() {
			@Override
			public void onNfcConnection() {
				Log.e("something", "got nfc");

				ArcticThunderActivity.this.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						NFC_COUNT++;
						showToast("saw NFC: " + NFC_COUNT);
					}

				});

			}
		});

		wifiDirectConnector.onResume(this);
		wifiDirectConnector.addStateChangeHandler(new P2pStateChangeHandler() {
			@Override
			public void onPeersAvailable(final WifiP2pDeviceList peers) {
				// Peer discovery has finished, and we have a list of peers.
				if (peersListFragment.isResumed()) {
					peersListFragment.setAvailablePeers(peers);
				}

				// If we got a list of 0 peers, we've either disconnected, or
				// there are no peers to be found after
				// the timeout.
				if (peers.getDeviceList().size() == 0) {
					// Ensure that the detail fragment is made aware of a
					// potential disconnect.
					peerDetailFragment.setTargetDevice(null);
					peerDetailFragment.setConnectionSuccessfulInformation(null);

					FragmentManager fragmentManager = getFragmentManager();
					fragmentManager.beginTransaction().replace(R.id.content, peersListFragment).commit();

					fragmentManager.executePendingTransactions();

					peersListFragment.refresh();
				}
			}

			@Override
			public void onConnectionInfoAvailable(final WifiP2pInfo connectionInfo) {
				// We have made a connection, and the PeerDetail fragment should
				// receive the connection information.
				peerDetailFragment.setConnectionSuccessfulInformation(connectionInfo);

				if (peerDetailFragment.isResumed()) {
					peerDetailFragment.refresh();
				}

				Log.e("something", "client saving connect info and device");
				client.setConnectionSuccessful(connectionInfo, targetDevice);

				// Start the server thread to listen for incoming changes.
				server = new Server();
				server.execute(ArcticThunderActivity.this);

				showToast("Connection successful.");

				if (COMMANDER_MODE) {
					//SHOW ALL SPY ASSETS
				}
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

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_commander_mode:
				item.setChecked(!item.isChecked());
				COMMANDER_MODE = item.isChecked();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * HOOKS FROM MENUS
	 */

	public void showMap() {
		navigationDrawer.showContent();
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction().replace(R.id.content, mapsFragment).commit();

	}

	public void showAR() {
		WifiAssetsDefaultSetup s = new WifiAssetsDefaultSetup(this, getAssetList(), (OnWifiProximityListener) this);
		ArActivity.startWithSetup(this, s);
	}

	public void showChat() {
		navigationDrawer.showContent();
		showWifiDirect();
	}

	public void showModelViewer() {
		navigationDrawer.showContent();
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction().replace(R.id.content, modelViewerFragment).addToBackStack("ModelViewer")
				.commit();
	}

	public void showVideo() {
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction().replace(R.id.content, videoFragment).addToBackStack("Video").commit();

	}

	public void showPhoto() {
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction().replace(R.id.content, imageFragment).addToBackStack("Image").commit();
	}

	/**
	 * Networking callbacks
	 */

	public void showWifiDirect() {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
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

		});

	}

	public void sendDataOverWifi(final AssetObject asset) {
		if (connectionInfo != null && targetDevice != null) {
			String data = asset.getType().ordinal() + "&" + asset.getLatitude() + "&" + asset.getLongitude();
			Log.e("somthing", "sending:" + data);
			client.sendChanges(data);
		}
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
		// Called by the PeerDetail fragment when the Disconnect button is
		// pressed.
		wifiDirectConnector.disconnect(new DisconnectionListener() {
			@Override
			public void onDisconnectionSuccess() {
				peerDetailFragment.setTargetDevice(null);
				peerDetailFragment.setConnectionSuccessfulInformation(null);
				// peerDetailFragment.refresh();

				// On disconnect, stop the client from sending, and shut down
				// the server thread.
				client.onDisconnect();

				server.cancel(true);
				server = null;

				targetDevice = null;
				connectionInfo = null;
			}

			@Override
			public void onDisconnectionFailure() {
				showToast("Disconnection failed. Try again.");
			}
		});
	}

	public void showPeerDetails(final WifiP2pDevice device) {
		// This is called by the PeerList fragment, when an item is selected.
		// Save off the target device for
		// the server connection, give it to the PeerDetail fragment, and switch
		// the ViewPager automatically.
		Log.e("something", "saving peer detail");
		targetDevice = device;
		peerDetailFragment.setTargetDevice(device);

		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction().replace(R.id.content, peerDetailFragment).addToBackStack("PeerDetail")
				.commit();

		fragmentManager.executePendingTransactions();

		peerDetailFragment.refresh();
	}

	public void processReceivedData(final String receivedData) {
		if (COMMANDER_MODE) {
			String[] parsed = receivedData.split("&");
			//String data = asset.getType().name() + "&" + asset.getLatitude() + "&" + asset.getLongitude();
			Log.e("something", "got: " + receivedData);
			AssetObject asset = new AssetObject(Double.valueOf(parsed[1]), Double.valueOf(parsed[2]),
					AssetType.values()[Integer.valueOf(parsed[0]).intValue()]);

			assets.add(asset);
			mapsFragment.addAsset(asset);

		}
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

	public void createAsset(final double lat, final double log, final Activity a) {
		AlertDialog.Builder builder = new AlertDialog.Builder(a);
		builder.setTitle(R.string.identify_asset).setItems(AssetType.valuesAsCharSequence(),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(final DialogInterface dialog, final int which) {
						AssetType assetType = AssetType.values()[which];
						AssetObject asset = new AssetObject(lat, log, assetType);
						ArcticThunderActivity.this.sendDataOverWifi(asset);
						assets.add(asset);
						mapsFragment.addAsset(asset);
						if (assetType == AssetType.PHOTO) {
							Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
							startActivityForResult(takePictureIntent, TAKE_SECURITY_PICTURE);
						}
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
	public void onWifiProximityReached(final Activity activity) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
		// Check which request we're responding to
		if (requestCode == TAKE_SECURITY_PICTURE) {
			// Make sure the request was successful
			if (resultCode == RESULT_OK) {

				Bundle extras = data.getExtras();
				Bitmap mImageBitmap = (Bitmap) extras.get("data");

				FragmentTransaction transaction = getFragmentManager().beginTransaction();
				transaction.replace(R.id.content, pictureFragment).addToBackStack("camera");
				transaction.commit();

				// mapFragmentShowing = false;

				getFragmentManager().executePendingTransactions();

				pictureFragment.setImageBitmap(mImageBitmap);
			}
		}
	}

	public void createWifiAssets() {
		AssetObject asset;

		asset = new AssetObject(39.974222, -74.976639, AssetType.WIFI);
		assets.add(asset);
		mapsFragment.addAsset(asset);

		asset = new AssetObject(39.9743332, -74.975547, AssetType.WIFI);
		assets.add(asset);
		mapsFragment.addAsset(asset);

		asset = new AssetObject(39.9757139, -74.977589, AssetType.WIFI);
		assets.add(asset);
		mapsFragment.addAsset(asset);
	}

	public void createSpyAssets(final int index) {
		AssetObject asset;

		switch (index) {
			case 1:
				asset = new AssetObject(39.973806, -74.979490, AssetType.ENEMY_WATCH_STAND);
				assets.add(asset);
				mapsFragment.addAsset(asset);

				asset = new AssetObject(39.973181, -74.980155, AssetType.ENEMY_VEHICLE);
				assets.add(asset);
				mapsFragment.addAsset(asset);

				asset = new AssetObject(39.971786, -74.978396, AssetType.ENEMY_BUILDING);
				assets.add(asset);
				mapsFragment.addAsset(asset);

				break;
			case 2:
				asset = new AssetObject(39.973342, -74.975182, AssetType.ENEMY_BUILDING);
				assets.add(asset);
				mapsFragment.addAsset(asset);

				asset = new AssetObject(39.973342, -74.975182, AssetType.ENEMY_ROAMING_TROOP);
				assets.add(asset);
				mapsFragment.addAsset(asset);

				break;
		}
	}

}

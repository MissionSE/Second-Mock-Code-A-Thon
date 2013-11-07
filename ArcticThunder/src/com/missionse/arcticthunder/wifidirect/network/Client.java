package com.missionse.arcticthunder.wifidirect.network;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pInfo;
import android.util.Log;

import com.missionse.arcticthunder.wifidirect.connector.WifiUtilities;

public class Client {

	private Context context;

	private WifiP2pInfo connectionInfo;
	private WifiP2pDevice targetDevice;

	public Client(final Context context) {
		this.context = context;
	}

	public void setConnectionSuccessful(final WifiP2pInfo p2pInfo, final WifiP2pDevice p2pDevice) {
		connectionInfo = p2pInfo;
		targetDevice = p2pDevice;
	}

	public void onDisconnect() {
		connectionInfo = null;
		targetDevice = null;
	}

	public void sendChanges(final String data) {
		Log.e("something", "checking null status");
		if (connectionInfo != null && targetDevice != null) {
			String address = "";
			if (connectionInfo.isGroupOwner) {
				address = WifiUtilities.getIPAddressFromMacAddress(targetDevice.deviceAddress);
			} else {
				address = connectionInfo.groupOwnerAddress.getHostAddress();
			}

			Intent modelStatusIntent = new Intent(context, ClientIntentService.class);
			modelStatusIntent.setAction(ClientIntentService.ACTION_SEND_UPDATE);

			modelStatusIntent.putExtra(ClientIntentService.EXTRAS_DATA, data);
			modelStatusIntent.putExtra(ClientIntentService.EXTRAS_HOST, address);
			modelStatusIntent.putExtra(ClientIntentService.EXTRAS_PORT, Server.PORT);

			Log.e("Client", "sending some data");

			context.startService(modelStatusIntent);
		}
	}
}

package com.missionse.arcticthunder.nfc;

import android.app.Activity;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.util.Log;

public class NfcConnector {

	private NfcAdapter nfcAdapter;

	public boolean onCreate(final Activity activity) {
		nfcAdapter = NfcAdapter.getDefaultAdapter(activity);
		if (nfcAdapter == null) {
			return false;
		}
		return true;
	}

	public boolean isReady() {
		return nfcAdapter.isEnabled();
	}

	public static void parseIntent(final Intent intent, final NfcConnectionListener listener) {
		Log.e("something", "listening for nfc");
		String action = intent.getAction();
		if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action) || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
				|| NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
			Log.e("something", "this is nfc");
			listener.onNfcConnection();
		}
	}

}

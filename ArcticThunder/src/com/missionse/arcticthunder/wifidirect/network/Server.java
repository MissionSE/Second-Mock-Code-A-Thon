package com.missionse.arcticthunder.wifidirect.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import android.os.AsyncTask;
import android.util.Log;

import com.missionse.arcticthunder.ArcticThunderActivity;

public class Server extends AsyncTask<ArcticThunderActivity, Void, String> {

	// public static final String TAG = ModelControllerServer.class.getSimpleName();

	public static final int PORT = 3456;

	public Server() {
	}

	@Override
	protected String doInBackground(final ArcticThunderActivity... params) {
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(PORT);
			ArcticThunderActivity activity = params[0];
			while (true) {
				Socket connectingClient = serverSocket.accept();

				BufferedReader reader = new BufferedReader(new InputStreamReader(connectingClient.getInputStream()));
				final String receivedData = reader.readLine();

				Log.e("Server", "received this: " + receivedData);

				activity.processReceivedData(receivedData);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (serverSocket != null) {
					serverSocket.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}

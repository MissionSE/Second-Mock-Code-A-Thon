package com.missionse.arcticthunder.wifidirect.connector;

public interface ConnectionInitiationListener {
	
	/**
	 * Called when connect() ha successfully begun initiation.
	 */
	public void onConnectionInitiationSuccess();
	
	/**
	 * Called when connect() has failed.
	 */
	public void onConnectionInitiationFailure();
}

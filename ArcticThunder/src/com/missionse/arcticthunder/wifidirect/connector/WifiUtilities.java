package com.missionse.arcticthunder.wifidirect.connector;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class WifiUtilities {
	private final static String p2pIndicator = "p2p-p2p0";

	public static String getIPAddressFromMacAddress(final String macAddress) {
		// The MAC address provided is different than that which will be found
		// in the /proc/net/arp file. So ignoring the 8th bit, or more simply,
		// just ignoring the 5th offset of the MAC address.

		String[] splitMacAddress = macAddress.split(":");

		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader("/proc/net/arp"));
			String line;
			while ((line = br.readLine()) != null) {
				String[] entry = line.split(" +");
				if (entry != null) {
					// Basic sanity check
					String device = entry[5];
					if (device.matches(".*" + p2pIndicator + ".*")) {
						String entryMacAddress = entry[3];
						String[] splitEntryMacAddress = entryMacAddress.split(":");

						boolean matching = true;
						if (splitMacAddress.length == splitEntryMacAddress.length) {
							for (int index = 0; index < splitMacAddress.length; ++index) {
								// As per note above, ignoring some bits.
								if (index != 4) {
									if (!splitMacAddress[index].equals(splitEntryMacAddress[index])) {
										matching = false;
									}
								}
							}
						}
						if (matching) {
							return entry[0];
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
			}
		}
		return "";
	}
}

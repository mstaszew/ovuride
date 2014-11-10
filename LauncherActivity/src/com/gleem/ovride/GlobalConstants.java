package com.gleem.ovride;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class GlobalConstants {
	private static GlobalConstants instance;
	// all variables have default values
	public Elements usage = null;
	/** username and password should be always obtained from the settings.json file
	 * and not hard-coded here
	 */
	// TODO: remove usernames and passwords from this source code using PersistentSettings class
	public String username = "ANNBALDERAMA";
	public String password = "bellx2573";  
	public String providerLogOnUrl = "http://ovuride.co.nf/mybell.ca/?USER="+username+"&PASSWORD="+password;
	public final String getUrl = "https://mybell.bell.ca/Login";
	public final String postUrl = "https://mybell.bell.ca/Login";
	public final String postLoginUrl = "https://mybell.bell.ca/MyServices";
	// TODO: use constructor to build the constants
	public Element lastBill;
	public Element paymReceived;
	public Element currBalance;
	// TODO: this should call PersistentSettings getInstance() and load values from there
	public static synchronized GlobalConstants getInstance() {
		if (instance == null) {
			instance = new GlobalConstants();
		}
		return instance;
	}


	// Restrict the constructor from being instantiated
	private GlobalConstants() {
	}

	public String getPassword() {
		return password;
	}

	public String getProviderLogOnUrl() {
		return providerLogOnUrl;
	}

	public Elements getUsage() {
		return usage;
	}

	public String getUsername() {
		return username;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}

	public void setProviderLogOnUrl(String providerLogOnUrl) {
		this.providerLogOnUrl = providerLogOnUrl;
	}
	
	public void setUsage(Elements usage) {
		this.usage = usage;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
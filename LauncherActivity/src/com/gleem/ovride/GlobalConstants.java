package com.gleem.ovride;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class GlobalConstants {
	private static GlobalConstants instance;
	public Elements usage = null;
	// TODO: remove usernames and passwords from this source code using PersistentSettings class
	public String username = "ANNBALDERAMA";
	public String password = "bellx2573";  
	/* public String providerLogOnUrl = "file://assets/_ovuride/log_in_to_mybell.php?USER="+username+"&PASSWORD="+password; */
	public String providerLogOnUrl = null;
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
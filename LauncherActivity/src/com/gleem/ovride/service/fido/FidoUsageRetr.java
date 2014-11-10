package com.gleem.ovride.service.fido;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class FidoUsageRetr {
	public static final float SUCCESS = Float.NEGATIVE_INFINITY;
	public static final float FAILURE = Float.POSITIVE_INFINITY;
	public float getDataUsage() {
		URL fidoLogin;
		HttpsURLConnection conn = null;
		try {
			fidoLogin = new URL(
					"https://www.fido.ca/web/page/portal/Fido/Ecare_Standalone");
			conn = (HttpsURLConnection) fidoLogin.openConnection();
		} catch (MalformedURLException e) {

			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Acts like a browser
		conn.setUseCaches(false);
		try {
			conn.setRequestMethod("POST");
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		conn.setRequestProperty("Host", "www.fido.ca");
		// conn.setRequestProperty("User-Agent", USER_AGENT);
		// conn.setRequestProperty("Accept",
		// "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		// conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		// for (String cookie : this.cookies) {
		// conn.addRequestProperty("Cookie", cookie.split(";", 1)[0]);
		// }
		// conn.setRequestProperty("Connection", "keep-alive");
		// conn.setRequestProperty("Referer",
		// "https://accounts.google.com/ServiceLoginAuth");
		// conn.setRequestProperty("Content-Type",
		// "application/x-www-form-urlencoded");
		// conn.setRequestProperty("Content-Length",
		// Integer.toString(postParams.length()));
		//
		// conn.setDoOutput(true);
		// conn.setDoInput(true);
		try {
			conn.connect();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (conn.getDoInput() || conn.getDoOutput()) {
			conn.disconnect();
			return SUCCESS;
		}
		conn.disconnect();
		return FAILURE;
	}
}
package com.gleem.ovride.service.bell;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.os.Environment;
import android.view.View;

public class HTTPCommWorkerBell {

	private List<String> cookies;
	private HttpsURLConnection conn;
	private View v1 = null;
	
	private final String USER_AGENT = "Mozilla/5.0";
	private final static String USERNAME = "ANNBALDERAMA";
	private final static String PASSWORD = "bell1passxx";

	public static String main(String[] args) throws Exception {
		return (new HTTPCommWorkerBell(null).login());
	}

	public HTTPCommWorkerBell(View v1) {
		super();
		this.v1 = v1;
	}
	
	private String login() throws Exception {
		
		String url = "https://mybell.bell.ca/Login";
		String myServices = "https://mybell.bell.ca/MyServices";

		HTTPCommWorkerBell http = this;

		// make sure cookies is turn on
		CookieHandler.setDefault(new CookieManager());

		// 1. Send a "GET" request, so that you can extract the form's data.
		String page = http.GetPageContent(url);
		String postParams = http.getFormParams(page, USERNAME, PASSWORD);
		{
			File sdCard = Environment.getExternalStorageDirectory();
			File dir = new File(sdCard.getAbsolutePath() + "/ovuride");
			dir.mkdirs();
			File file = new File(dir, "postParams.txt");

			FileOutputStream f = new FileOutputStream(file);
			f.write(postParams.getBytes());
			f.close();
		}
		// 2. Construct above post's content and then send a POST request for
		// authentication
		http.sendPost(url, postParams);

		// 3. success then go to gmail.
		String result = http.GetPageContent(myServices);
		{
			File sdCard = Environment.getExternalStorageDirectory();
			File dir = new File(sdCard.getAbsolutePath() + "/ovuride");
			dir.mkdirs();
			File file = new File(dir, "after_login.txt");

			FileOutputStream f = new FileOutputStream(file);
			f.write(result.getBytes());
			f.close();
		}

		return result;
		
	}

	private void sendPost(String url, String postParams) throws Exception {

		URL obj = new URL(url);
		conn = (HttpsURLConnection) obj.openConnection();

		// Acts like a browser
		conn.setUseCaches(false);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Host", "mybell.bell.ca");
		conn.setRequestProperty("User-Agent", USER_AGENT);
		conn.setRequestProperty("Accept",
				"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		for (String cookie : this.cookies) {
			conn.addRequestProperty("Cookie", cookie.split(";", 1)[0]);
		}
		conn.setRequestProperty("Connection", "keep-alive");
		conn.setRequestProperty("Referer", "https://mybell.bell.ca/Login");
		conn.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded");
		conn.setRequestProperty("Content-Length",
				Integer.toString(postParams.length()));

		conn.setDoOutput(true);
		conn.setDoInput(true);

		// Send post request
		DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
		wr.writeBytes(postParams);
		wr.flush();
		wr.close();

		int responseCode = conn.getResponseCode();
		{
			String result = "\nSending 'POST' request to URL : " + url
					+ "\n Post parameters: " + postParams
					+ "\n Response Code: " + responseCode + "\n";
			File sdCard = Environment.getExternalStorageDirectory();
			File dir = new File(sdCard.getAbsolutePath() + "/ovuride");
			dir.mkdirs();
			File file = new File(dir, "send_post.txt");

			FileOutputStream f = new FileOutputStream(file);
			f.write(result.getBytes());
			f.close();
		}
		System.out.println("\nSending 'POST' request to URL : " + url);
		System.out.println("Post parameters : " + postParams);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(new InputStreamReader(
				conn.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		// System.out.println(response.toString()); - how is the response send?
		// at in.close?

	}

	private String GetPageContent(String url) throws Exception {

		URL obj = new URL(url);
		conn = (HttpsURLConnection) obj.openConnection();

		// default is GET
		conn.setRequestMethod("GET");

		conn.setUseCaches(false);

		// act like a browser
		conn.setRequestProperty("User-Agent", USER_AGENT);
		conn.setRequestProperty("Accept",
				"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		if (cookies != null) {
			for (String cookie : this.cookies) {
				conn.addRequestProperty("Cookie", cookie.split(";", 1)[0]);
			}
		}
		int responseCode = conn.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(new InputStreamReader(
				conn.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		// Get the response cookies
		setCookies(conn.getHeaderFields().get("Set-Cookie"));

		return response.toString();

	}

	public String getFormParams(String html, String username, String password)
			throws UnsupportedEncodingException {

		System.out.println("Extracting form's data...");

		Document doc = Jsoup.parse(html);

		// Bell form id
		Element loginform = doc.getElementById("LoginForm");
		Elements inputElements = loginform.getElementsByTag("input");
		List<String> paramList = new ArrayList<String>();
		for (Element inputElement : inputElements) {
			String key = inputElement.attr("name");
			String value = inputElement.attr("value");

			if (key.equals("USER"))
				value = username;
			else if (key.equals("PASSWORD"))
				value = password;
			paramList.add(key + "=" + URLEncoder.encode(value, "UTF-8"));
		}

		// build parameters list
		StringBuilder result = new StringBuilder();
		for (String param : paramList) {
			if (result.length() == 0) {
				result.append(param);
			} else {
				result.append("&" + param);
			}
		}
		return result.toString();
	}

	public List<String> getCookies() {
		return cookies;
	}

	public void setCookies(List<String> cookies) {
		this.cookies = cookies;
	}

}

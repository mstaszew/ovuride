package com.gleem.ovride.service.bell;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
/** currently this class is only calling the Worker*/
public class HTTPCommThreadBell extends Thread {
	private static final String USERNAME = null;
	private static final String PASSWORD = null;
	private View v;


	public HTTPCommThreadBell(View v) {
		super();
		this.v = v;
	}
	
	// After call for background.start this run method call
	public void run() {
		try {
			/* the idea is to (replace google with bell):
			 *  Send an HTTP �GET� request to Google login form � https://accounts.google.com/ServiceLoginAuth
    			Analyze the form data via Google Chrome�s �Network� feature. Alternatively, you can view the HTML source code.
    			Use jSoup library to extract all visible and hidden form�s data, replace with your username and password.
    			Send a HTTP �POST� request back to login form, along with the constructed parameters
    			After user authenticated, send another HTTP �GET� request to Gmail page. https://mail.google.com/mail/
			 */
//			CookieHandler.setDefault(new CookieManager());
//			final HttpClient Client = new DefaultHttpClient();
//			String responseServerString = "";
//			HttpGet httpget = new HttpGet(getUrl);
//			ResponseHandler<String> responseHandler = new BasicResponseHandler();
//			responseServerString = Client.execute(httpget, responseHandler);
//			Element filledForm = fillTheForm(responseServerString);
//			responseServerString = null; // for GC
//			responseHandler = null; // for GC

			/* now use the GET data (input elements) for the POST request - login */
//			HttpPost httppost = new HttpPost(postUrl);
			/* httppost.addHeader("add_all_the_cookies", "here"); */ // handled by the CookieManager
//			httppost.addHeader("and_all_the_form_input_fields", "here");
//			responseHandler = new BasicResponseHandler();
//			responseServerString = Client.execute(httppost,responseHandler);
			/* GET again to retrieve usage */
			threadMsg(HTTPCommWorkerBell.main(null));
		} catch (Throwable t) {
			// just end the background thread
			Log.i(this.getClass().getCanonicalName(), "Thread  exception " + t);
		}
	}
	// returns login form element (filled)
/*	private Element fillTheForm(String responseServerString) {
		// EXTRACT FORM DATA
		Document doc = Jsoup.parse(responseServerString);
		
		// Fill Bell Login form username and pass
		Element loginForm = doc.getElementById("LoginForm");
		Elements inputElements = loginForm.getElementsByTag("input");
		for (Element inputElement : inputElements) {
			
			String key = inputElement.attr("name");
			String value = inputElement.attr("value");
			
			// discard all non-relevant (not username or password) input fields			
				if (key != null && (key.equals("USER") || key.equals("PASSWORD"))) { 
					if (key.equals("USER")) {
						value = this.USERNAME;
						inputElement.val(value);
					} else  for sure the key is PASSWORD  {
						value = this.PASSWORD;
						inputElement.val(value);
					}
				}
		}
		
		return loginForm;
	}*/

	private void threadMsg(String msg) {
		if (!msg.equals(null) && !msg.equals("")) { // check if msg is empty
			Message msgObj = handler.obtainMessage();
			Bundle b = new Bundle();
			b.putString("message", msg);
			msgObj.setData(b);
			handler.sendMessage(msgObj);
		}
	}

	// Define the Handler that receives messages from the thread and update the
	// progress
	private final Handler handler = new Handler() {

		public void handleMessage(Message msg) {
			// TODO: add handler code, remove always return!
			if (true) return;
			
			String aResponse = msg.getData().getString("message");

			if ((null != aResponse)) {
				// EXTRACT FORM DATA
				Document doc = Jsoup.parse(aResponse);
				
				// Google form id
				Elements inputElements = doc.getElementsByTag("input");
				List<String> commonParamList = new ArrayList<String>();
				List<String> userParamList = new ArrayList<String>();
				for (Element inputElement : inputElements) {
					// discard all non-relevant (not username or password) input fields
					String key;
					System.out.println(key = inputElement.attr("name"));
					String value = inputElement.attr("value");
					
					/* Log.d("jsoup", inputElement.toString()); no reason to output this ... */ /* all wrong here ... */
					
					try {
						if (key != null && (key.equals("USER") || key.equals("PASSWORD"))) 
							userParamList.add(key + "=" + URLEncoder.encode(value, "UTF-8"));
						else
							commonParamList.add(key + "=" + URLEncoder.encode(value, "UTF-8"));
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				}				
				
				// ALERT MESSAGE
				Toast.makeText(v.getRootView().getContext(),
						"Server Response (username/pass): " + userParamList.toString(), Toast.LENGTH_SHORT)
						.show();
			} else {

				// ALERT MESSAGE
				Toast.makeText(v.getRootView().getContext(),
						"Not Got Response From Server.", Toast.LENGTH_SHORT)
						.show();
			}

		}
	};

}
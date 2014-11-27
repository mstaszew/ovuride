package com.gleem.ovride;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.ObjectInputStream.GetField;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.Arrays;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.gleem.ovride.service.OvrideService;
import com.gleem.ovride.webserver.WebServer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.webkit.JavascriptInterface;
import android.webkit.MimeTypeMap;
import android.webkit.WebView;
import android.webkit.WebViewClient;

//TODO: Start this activity hidden instead of hiding after init. Change name to reflect it is not showing graphics
@SuppressLint("SetJavaScriptEnabled")
public class MainActivity extends Activity {
	final static String HTMLLOGFILE = "/htmlbell.html";
	final static String PRICETAGNAME = "priceTag";
	final static boolean VERBOSE = true;
	
	ServiceConnection servConn = null;
	WebView myWebView = null;
	Elements usage = null;
	JSHtmlOutInterface jshoi = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (GlobalConstants.getInstance().getProviderLogOnUrl() == null) {
				GlobalConstants.getInstance().setProviderLogOnUrl("http://192.168.1.1/index.php:8888");
		}
		String rawHtml = "";
		try {
		BufferedReader bufferedReader = new BufferedReader(new java.io.FileReader(getFilesDir()+"/index.html"));
		String line;
		while ((line = bufferedReader.readLine()) != null) {
			rawHtml += line + "\r\n";
		}
		bufferedReader.close();
		} catch (IOException exception) {
			exception.printStackTrace();
		}
		myWebView = new WebView(getApplicationContext());
		myWebView.getSettings().setJavaScriptEnabled(true);
		myWebView.addJavascriptInterface(
				jshoi = new JSHtmlOutInterface(this.getApplicationContext(),
						this), "HTMLOUT");
		myWebView.setWebViewClient(new WebViewClient() {
			public void onPageFinished(WebView view, String url) {
				// extract usage, save, show
				/*
				 * This call inject JavaScript into the page which just finished
				 * loading. Note that logon-on process itself involves processing of
				 * custom JS through the Ovuride website 
				 */
				myWebView
						.loadUrl("javascript:setTimeout(function(){window.HTMLOUT.processHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>')},120000);"); 
				
				// give 2m for the webpage to load at most
			}
		});
		myWebView.loadDataWithBaseURL("https://mybell.bell.ca/Login", rawHtml, "text/html", "utf-8", null);
		//myWebView.loadUrl(GlobalConstants.getInstance().getProviderLogOnUrl()); 
				/*loadDataWithBaseURL("index.html", rawHtml, "text/html", "utf-8", null);*/
				/* .loadUrl(GlobalConstants.getInstance().getProviderLogOnUrl()); */ // this log on url will never point directly to the mobile operator. this way we have an on-server pre-processing option.
		Intent i = new Intent(this,OvrideService.class);
		{ // namespace division
			servConn = new ServiceConnection() {
				@Override
				public void onServiceDisconnected(ComponentName name) { }

				@Override
				public void onServiceConnected(ComponentName name,	IBinder service) { }
			};
			bindService(i, servConn, Context.BIND_AUTO_CREATE);
		}
		this.startService(i);
		
		Thread webServer = new Thread(new Runnable() {
			
			@Override
			public void run() {
				BufferedReader br = new BufferedReader(new InputStreamReader(getResources().openRawResource(R.raw.index)));
				char[] readVal = new char[4096];
				StringBuffer sb = new StringBuffer();
				try {
					while (br.read(readVal) != -1) {
						sb.append(readVal);
					}
				} catch (IOException e) {	e.printStackTrace();	}
				String rawHtml = sb.toString();				
				WebServer webServer = new WebServer(rawHtml);
				webServer.startServer();
			}
		});
		webServer.start();
		
		///////////////////////////////////
		Intent pickIntent = new Intent(AppWidgetManager.ACTION_APPWIDGET_PICK);
	    pickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, "com.gleem.ovride.ui.widget.LockScreenWidget");
	    startActivity(pickIntent);
	    ///////////////////////////////////
		Intent intent = new Intent(Intent.ACTION_MAIN);
	    intent.addCategory(Intent.CATEGORY_HOME);
	    startActivity(intent); // close the window (show the home screen)
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (servConn != null)
			unbindService(servConn);
	}

	@Override
	protected void onStart() {
		super.onStart();
		this.setVisible(false);
	}

	public static void updateUsage() {
		
	}

}

/* An instance of this class will be registered as a JavaScript interface */
class JSHtmlOutInterface {
	Context context = null;
	Elements priceTags = null;
	MainActivity parent = null;

	public JSHtmlOutInterface(Context applicationContext,
			MainActivity parent_arg) {
		parent = parent_arg;
		context = applicationContext;
	}

	// extracts the price tags, this is called from WebView JS, logs the whole received HTML 
	@JavascriptInterface
	public void processHTML(String html) throws Exception {

		Document docHtml = org.jsoup.Jsoup.parse(html);
		priceTags = docHtml.getElementsByClass(MainActivity.PRICETAGNAME);
		GlobalConstants gcs = GlobalConstants.getInstance();
		gcs.lastBill = priceTags.get(0);
		gcs.paymReceived = priceTags.get(1);
		gcs.currBalance = priceTags.get(2);
		parent.usage = priceTags;
		FileOutputStream os = new FileOutputStream(context.getFilesDir() + MainActivity.HTMLLOGFILE);
		os.write(html.getBytes());
		os.close();
		if (MainActivity.VERBOSE) 
			System.out.println(priceTags);
		GlobalConstants.getInstance().setUsage(priceTags);
	}

}


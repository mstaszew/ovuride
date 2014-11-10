package com.gleem.ovride;

import java.io.FileOutputStream;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.webkit.JavascriptInterface;
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
						.loadUrl("javascript:setTimeout(function(){window.HTMLOUT.processHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>')},50000);"); // give 50s for the webpage to load at most
			}
		});
		myWebView
				.loadUrl(GlobalConstants.getInstance().getProviderLogOnUrl()); // this log on url will never point directly to the mobile operator. this way we have an on-server pre-processing option.
		Intent i = new Intent();
		i.setClassName("com.gleem.ovride", "com.gleem.ovride.OvrideService");
		{ // namespace division
			// 'dummy' ServiceConnection interface implementation due to
			// requirements of API 19
			// for ServiceConnection bindService parameter for the servicec not
			// to be null
			servConn = new ServiceConnection() {
				@Override
				public void onServiceDisconnected(ComponentName name) {
					// nothing to do
				}

				@Override
				public void onServiceConnected(ComponentName name,
						IBinder service) {
					// nothing to do
				}
			};
			bindService(i, servConn, Context.BIND_AUTO_CREATE);
		}
		if (VERBOSE) { // try to get usage every 500ms and log to the console, ONLY for testing
			Runnable tempRun = new Runnable() {
				@Override
				public void run() {
					while (usage == null)
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							System.out.println(usage);
						}
					});
				}
			};
			(new Thread(tempRun)).start();
		}
		this.startService(i);
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
		assert(true);
		this.setVisible(false);
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
		FileOutputStream os = new FileOutputStream(context.getFilesDir() + MainActivity.HTMLLOGFILE);
		os.write(html.getBytes());
		os.close();
		parent.usage = priceTags;
		if (MainActivity.VERBOSE) 
			System.out.println(priceTags);
		GlobalConstants.getInstance().setUsage(priceTags);
	}

}


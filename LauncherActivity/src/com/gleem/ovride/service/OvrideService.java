package com.gleem.ovride.service;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.IBinder;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.gleem.ovride.ui.HUDView;
import com.gleem.ovride.ui.OVUrideTLis;

public class OvrideService extends Service {
	HUDView mView;
	protected static View rootView;
	public static Rect ovrRectangle = null;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	// add the ovuride button
	@Override
	public int onStartCommand(Intent theIntent, int flags, int startId) {
		super.onStartCommand(theIntent, flags, startId);
		WindowManager.LayoutParams params = new WindowManager.LayoutParams(24,
				80, WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
						| WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
				/* | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH */,
				PixelFormat.TRANSLUCENT);
		params.gravity = Gravity.CENTER_VERTICAL | Gravity.RIGHT;
		params.x = 0;
		params.y = 0;		
		final WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
		rootView = new HUDView(getBaseContext(), this);
		
		OVUrideTLis touchListenerOvuride = new OVUrideTLis(theIntent);
		touchListenerOvuride.setServiceRootView(getRootView());
		touchListenerOvuride.setServiceWindowManager(wm);
		touchListenerOvuride.setServiceLayoutParams(params);
		
		rootView.setOnTouchListener(touchListenerOvuride);	

		// Add layout to window manager
		wm.addView(rootView, params);
		return START_STICKY;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Toast.makeText(getBaseContext(), "onDestroy", Toast.LENGTH_LONG).show();
		if (mView != null) {
			((WindowManager) getSystemService(WINDOW_SERVICE))
					.removeView(mView);
			mView = null;
		}
	}

	public static View getRootView() {
		return rootView;
	}
}




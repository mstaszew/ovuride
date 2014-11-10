package com.gleem.ovride.ui;

import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

public	class OVUrideTLis  implements OnTouchListener {

		View serviceRootView = null;
		WindowManager serviceWindowManager = null;
		public OVUrideTLis(Intent theIntent) {
		}
		// listener on the ovuride button. should open the bubble menu.
		// very simple implementation now, stripped 95% from before 'wincopy' version
		@Override
		public boolean onTouch(final View v, MotionEvent event) {
			Intent newIntent = new Intent(v.getContext(),BubbleDialogActivity.class);
			newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			v.getContext().startActivity(newIntent);
			// true means event has not been consumed 
			return true;
		}
		public View getServiceRootView() {
			return serviceRootView;
		}
		public void setServiceRootView(View serviceRootView) {
			this.serviceRootView = serviceRootView;
		}
		public WindowManager getServiceWindowManager() {
			return serviceWindowManager;
		}
		public void setServiceWindowManager(WindowManager serviceWindowManager) {
			this.serviceWindowManager = serviceWindowManager;
		}
		public void setServiceLayoutParams(LayoutParams params) {
		}
	
	}
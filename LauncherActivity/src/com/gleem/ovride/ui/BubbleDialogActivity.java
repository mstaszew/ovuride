package com.gleem.ovride.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.R.color;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.gleem.ovride.R;

/** This is left only to test if Ovuride button works, much code stripped compared to versions
 * before 'wincopy'
 * 
 * @author Michael Staszewski
 */
public class BubbleDialogActivity extends Activity {
	
	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a {@link FragmentPagerAdapter}
	 * derivative, which will keep every loaded fragment in memory. If this
	 * becomes too memory intensive, it may be best to switch to a
	 * {@link android.support.v13.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bubble_dialog);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the activity.
		mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.bubble_dialog, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onStart() {
		super.onStart();
		LayoutParams lp = this.getWindow().getAttributes();
		lp.width = lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		this.getWindow().setAttributes(lp);
		this.setTitle("");
	}
	
	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a PlaceholderFragmentProcesses (defined as a static inner class
			// below).
			if (position % 2 == 0)
				return PlaceholderFragmentProcesses.newInstance(position + 1);
			else
				return PlaceholderFragment.newInstance(position + 1);
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_section2).toUpperCase(l);
			case 2:
				return getString(R.string.title_section3).toUpperCase(l);
			}
			return null;
		}
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";

		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static PlaceholderFragment newInstance(int sectionNumber) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.usage_fragment,
					container, false);
			return rootView;
		}
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragmentProcesses extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";

		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static PlaceholderFragmentProcesses newInstance(int sectionNumber) {
			PlaceholderFragmentProcesses fragment = new PlaceholderFragmentProcesses();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public PlaceholderFragmentProcesses() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.task_manager_fragment,
					container, false);
			/*
			  rootView = getAllChildren(rootView).get(1);
			  // get children of ScrollView, not rootView directly
			*/
			for (View v:getAllChildren(rootView)){
				System.out.println(v);
				TableLayout tl = (TableLayout) v;
				ActivityManager am = null;
				List<RunningAppProcessInfo> runningAppProcesses = (am=(ActivityManager) this.getActivity().getSystemService(ACTIVITY_SERVICE)).getRunningAppProcesses();
				for (RunningAppProcessInfo pi:runningAppProcesses) {
					TableRow tr = new TableRow(tl.getContext());
					TextView process = new TextView(tr.getContext());
					process.setText(pi.processName);
					tr.addView(process);
					// add stop & start buttons
					ImageButton stopButton = new ImageButton(tr.getContext());
					stopButton.setImageResource(R.drawable.stop);
					stopButton.setMaxHeight(32); stopButton.setMaxWidth(32);
					stopButton.setScaleType(ScaleType.FIT_XY);
					stopButton.setBackgroundColor(color.transparent);
					stopButton.setOnClickListener(new ButtonListenerStop(pi,am));
					tr.addView(stopButton,32,32);
					ImageButton startButton = new ImageButton(tr.getContext());
					startButton.setImageResource(R.drawable.start);
					startButton.setMaxHeight(32); startButton.setMaxWidth(32);
					startButton.setScaleType(ScaleType.FIT_XY);
					startButton.setBackgroundColor(color.transparent);
					startButton.setOnClickListener(new ButtonListenerStart(pi,am));
					tr.addView(startButton,32,32);					
					ImageView iv = new ImageView(tr.getContext());
					tr.addView(iv);
					tl.addView(tr);					
					System.out.println(pi);	
				}
				break;
			}
			
		
			return rootView;
		}
		class ButtonListenerStart implements OnClickListener {
			RunningAppProcessInfo pi;
			ActivityManager am;
			public ButtonListenerStart(RunningAppProcessInfo pi, ActivityManager am) {
				this.pi = pi; this.am = am;
			}
			@Override
			public void onClick(View v) {

			}

		}
		class ButtonListenerStop implements OnClickListener {
			RunningAppProcessInfo pi;
			ActivityManager am;
			public ButtonListenerStop(RunningAppProcessInfo pi, ActivityManager am) {
				this.pi = pi; this.am = am;
			}			
			@Override
			public void onClick(View v) {
				android.os.Process.killProcess(pi.pid);
			}

		}		
		// get all viewgroup children
		private ArrayList<View> getAllChildren(View v) {

		    if (!(v instanceof ViewGroup)) {
		        ArrayList<View> viewArrayList = new ArrayList<View>();
		        viewArrayList.add(v);
		        return viewArrayList;
		    }

		    ArrayList<View> result = new ArrayList<View>();

		    ViewGroup vg = (ViewGroup) v;
		    for (int i = 0; i < vg.getChildCount(); i++) {

		        View child = vg.getChildAt(i);

		        ArrayList<View> viewArrayList = new ArrayList<View>();
		        viewArrayList.add(v);
		        viewArrayList.addAll(getAllChildren(child));

		        result.addAll(viewArrayList);
		    }
		    return result;
		}
	}
}


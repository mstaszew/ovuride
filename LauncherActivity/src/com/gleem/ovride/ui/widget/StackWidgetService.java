package com.gleem.ovride.ui.widget;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.gleem.ovride.GlobalConstants;
import com.gleem.ovride.R;

public class StackWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new StackRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}

class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private List<WidgetItem> mWidgetItems = new ArrayList<WidgetItem>();
    private Context mContext;
    private static int counter = 0;

    public StackRemoteViewsFactory(Context context, Intent intent) {
        mContext = context;
        /* mAppWidgetId = */ 
        	/* intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
               AppWidgetManager.INVALID_APPWIDGET_ID); */
    }

    public void onCreate() {
            mWidgetItems.add(new WidgetItem());
    }

    public void onDestroy() { mWidgetItems.clear(); }

    public int getCount() { return 0; }

    public RemoteViews getViewAt(int pagenumber) {

    	RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);
    	
        Bundle extras = new Bundle();
        extras.putInt(StackWidgetProvider.EXTRA_ITEM, 0);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        
        rv.setOnClickFillInIntent(R.id.widget_item, fillInIntent);
        rv.setTextViewText(R.id.widget_item, "USAGE");
        
		while (GlobalConstants.getInstance().currBalance == null || GlobalConstants.getInstance().lastBill == null) {
			System.err.println("Waiting for currBalance and lastBill");
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} 
		rv.setTextViewText(R.id.balance_view_text, "Balance ("+(Integer.toString(counter++))+"): ");
		NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.CANADA);
		Integer balance = null;
		Integer bill = null;
		try {
			balance = nf.parse(GlobalConstants.getInstance().currBalance.text()).intValue();
			bill = nf.parse(GlobalConstants.getInstance().lastBill.text()).intValue();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		rv.setTextViewText(R.id.balance_value_view, Integer.toString(balance));
		rv.setTextViewText(R.id.lastbill_value_view, Integer.toString(bill));
        
        return rv;
    }

    public RemoteViews getLoadingView() { return null; }

    public int getViewTypeCount() { return 1; }

    public long getItemId(int position) { return position; }

    public boolean hasStableIds() { return true; }

    public void onDataSetChanged() { }
}

/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gleem.ovride.ui.widget;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.gleem.ovride.GlobalConstants;
import com.gleem.ovride.MainActivity;
import com.gleem.ovride.R;

public class StackWidgetProvider extends AppWidgetProvider {
	public static final String TOAST_ACTION = "com.example.android.stackwidget.TOAST_ACTION";
	public static final String EXTRA_ITEM = "com.example.android.stackwidget.EXTRA_ITEM";

	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {super.onDeleted(context, appWidgetIds);	}

	@Override
	public void onDisabled(Context context) {super.onDisabled(context);	}

	@Override
	public void onEnabled(Context context) {super.onEnabled(context); }

	@Override
	public void onReceive(Context context, Intent intent) {	super.onReceive(context, intent); }

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		/*
		Intent intent = new Intent(context, StackWidgetService.class);
		intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[0]);
		intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
		RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
		rv.setRemoteAdapter(appWidgetIds[0], R.id.stack_view, intent);

		//rv.setEmptyView(R.id.stack_view, R.id.balance_value_view);

		Intent toastIntent = new Intent(context, StackWidgetProvider.class);
		toastIntent.setAction(StackWidgetProvider.TOAST_ACTION);
		toastIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[0]);
		intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
		PendingIntent toastPendingIntent = PendingIntent.getBroadcast(context, 0, toastIntent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		rv.setPendingIntentTemplate(R.id.stack_view, toastPendingIntent);

		appWidgetManager.updateAppWidget(appWidgetIds[0], rv);
		*/
	}
}
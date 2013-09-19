//Alex Behannon
//09-19-2013
//MDF3 Week 3

package com.behannon.quoter;

import org.json.JSONObject;
import com.behannon.libs.FileSaving;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;

public class WidgetProvider extends AppWidgetProvider {

	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {

		System.out.println("ATTEMPTING WIDGET UPDATE");

		RemoteViews rv = new RemoteViews(context.getPackageName(),
				R.layout.widget_layout);

		try {

			String read = FileSaving
					.readStringFile(context, "quoteData", false);
			System.out.println("WIDGE READ TEST: " + read);
			try {

				JSONObject json;

				// create json from the file loaded
				json = new JSONObject(read);
				String quote = json.get("quote").toString();
				String author = "Author: " + json.get("author").toString() + "   ";

				// Set the text views to show data loaded
				rv.setTextViewText(R.id.widgetQuoteText, quote);
				rv.setTextViewText(R.id.widgetAuthorText, author);

			} catch (Exception e) {
				e.printStackTrace();
				rv.setTextViewText(R.id.widgetQuoteText, "Error loading quote");
				rv.setTextViewText(R.id.widgetAuthorText, "");
			} finally {

			}
		} finally {
			appWidgetManager.updateAppWidget(appWidgetIds, rv);
		}
	}
}
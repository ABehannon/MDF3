//Alex Behannon
//09-19-2013
//MDF3 Week 3

package com.behannon.quoter;

import org.json.JSONException;
import org.json.JSONObject;

import com.behannon.libs.FileSaving;
import com.behannon.libs.QuoteProvider;
import com.behannon.libs.WebCheck;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

public class WidgetConfig extends Activity implements OnClickListener {
	
	final String URI = "http://quotesondesign.com/";
	Boolean _connected = false;
	static String author;
	static String quote;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.widget_config);
		
		Button button = (Button) this.findViewById(R.id.doneButton);
		button.setOnClickListener(this);
		
		getQuote();
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
		Bundle extras = getIntent().getExtras();
		
		if(extras != null){
			
			int widgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
			
			if(widgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
				
				RemoteViews rv = new RemoteViews(this.getPackageName(), R.layout.widget_layout);
				rv.setTextViewText(R.id.widgetAuthorText, author);
				rv.setTextViewText(R.id.widgetQuoteText, quote);
				
				Intent buttonIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(this.URI));
				
				PendingIntent pi = PendingIntent.getActivity(this,0,buttonIntent,0);
				
				rv.setOnClickPendingIntent(R.id.doneButton, pi);
				
				AppWidgetManager.getInstance(this).updateAppWidget(widgetId, rv);
				
				Intent resultValue = new Intent();
				resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
				resultValue.putExtra("quote", quote);
				resultValue.putExtra("author", author);
				setResult(RESULT_OK, resultValue);
				finish();
			}
			
		}
	}
	
	// Called when the quote of the moment listview item is clicked
	@SuppressLint("HandlerLeak")
	public void getQuote() {

		Button button = (Button) this.findViewById(R.id.doneButton);
		final TextView tv1 = (TextView) this.findViewById(R.id.configQuoteText);
		tv1.setText("Attempting to retreive Quote");
		
		button.setEnabled(false);
		
		_connected = WebCheck.getConnectionStatus(getApplicationContext());

		Handler serviceHandler = new Handler() {

			public void handleMessage(Message message) {

				if (message.arg1 == RESULT_OK && message.obj != null) {

					String workingURL = message.obj.toString();
					Log.i("workingURL: ", workingURL);

					try {

						// Get JSON from API
						JSONObject json = new JSONObject(workingURL);
						System.out.println(json);

						quote = json.get("quote").toString();
						author = json.get("author").toString();

						System.out.println("Quote: " + quote);
						System.out.println("Author: " + author);

						Cursor cursor = getContentResolver().query(
								QuoteProvider.QuoteData.CONTENT_URI, null,
								null, null, null);
						displayQuoteData(cursor);
					} catch (JSONException e) {

						Log.e("JSON Error in OnClick:", e.toString());
						tv1.setText("Error retreiving quote");
						
					}
				}
			}
		};

		// Create messenger
		Messenger serviceMessenger = new Messenger(serviceHandler);

		Intent intent1 = new Intent(getApplicationContext(), DataService.class);
		intent1.putExtra("messenger", serviceMessenger);
		intent1.putExtra("filename", "quoteData");

		// Start intent for data
		startService(intent1);

	}

	public void displayQuoteData(Cursor cursor) {

		String read = FileSaving.readStringFile(this, "quoteData", false);
		Button button = (Button) this.findViewById(R.id.doneButton);
		final TextView tv1 = (TextView) this.findViewById(R.id.configQuoteText);
		final TextView tv2 = (TextView) this.findViewById(R.id.configAuthorText);
		
		// Init variables
		JSONObject json;

		try {

			// create json from the file loaded
			json = new JSONObject(read);
			quote = json.get("quote").toString();
			author = json.get("author").toString();
			
			tv1.setText(quote);
			tv2.setText(author);
			
			button.setEnabled(true);
		} catch (JSONException e) {

			e.printStackTrace();

		}
	}
	
	// Called to test internet when button pressed or app started
	public void testConnection() {

		if (_connected) {

			Log.i("Network connection: ", WebCheck.getConnectionType(getApplicationContext()));
			System.out.println("ONLINE");

		} else {

			Toast.makeText(
					getApplicationContext(),
					"You are not currently connected to the internet. Searching will not work, but you may still be able to load your last saved definition.",
					Toast.LENGTH_LONG).show();
			System.out.println("OFFLINE");

			String file = FileSaving.readStringFile(getApplicationContext(), "quoteData",
					false);

			if (!file.isEmpty()) {

				Cursor cursor = getContentResolver().query(
						QuoteProvider.QuoteData.CONTENT_URI, null, null, null,
						null);
				displayQuoteData(cursor);

			} else {

				System.out.println("EMPTY FILE ERROR");
				final TextView tv1 = (TextView) this.findViewById(R.id.configQuoteText);
				tv1.setText("Error Retreiving Quote");
			}
		}
	}
}
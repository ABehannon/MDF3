//Alex Behannon
//09-19-2013
//MDF3 Week 3

package com.behannon.quoter;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import org.json.JSONException;
import org.json.JSONObject;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.behannon.libs.QuoteProvider;
import com.behannon.libs.Singleton;
import com.behannon.libs.WebCheck;

public class MainActivity extends Activity {

	// initial variables
	Context _context;
	Boolean _connected = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Sets context to the main activity
		_context = this;
		_connected = WebCheck.getConnectionStatus(_context);

		// Tests connection upon being loaded.
		testConnection();

		final ListView fragmentList = (ListView) findViewById(R.id.fragmentList);

		fragmentList
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int position, long arg3) {
						System.out.println("POS: " + position);

						switch (position) {

						// Quote of the Moment
						case 0:

							if (_connected) {
								quoteButton();
							} else {
								Toast.makeText(
										getApplicationContext(),
										"You are not currently connected to the internet. Searching will not work, but you may still be able to load your last saved definition.",
										Toast.LENGTH_LONG).show();
							}
							break;

						// Last saved file & offline view
						case 1:
							favoriteButton();
							break;

						// Website visit
						case 2:
							intentButton();
							break;
						}

					}

				});

	}

	// Called when the website listview item is clicked
	public void intentButton() {

		String url = Singleton.webURL();
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setData(Uri.parse(url));
		startActivity(i);

	}

	// Called when the favorite button from listview is clicked
	public void favoriteButton() {

		// Show third activity.
		Intent intent = new Intent(MainActivity.this, ThirdActivity.class);
		startActivity(intent);
	}

	// Called when the quote of the moment listview item is clicked
	public void quoteButton() {

		_connected = WebCheck.getConnectionStatus(_context);

		// Tests connection upon being clicked
		testConnection();

		Handler serviceHandler = new Handler() {

			public void handleMessage(Message message) {

				if (message.arg1 == RESULT_OK && message.obj != null) {

					String workingURL = message.obj.toString();
					Log.i("workingURL: ", workingURL);

					try {

						// Get JSON from API
						JSONObject json = new JSONObject(workingURL);
						System.out.println(json);

						String quote = json.get("quote").toString();
						String author = json.get("author").toString();

						System.out.println("Quote: " + quote);
						System.out.println("Author: " + author);

						Cursor cursor = getContentResolver().query(
								QuoteProvider.QuoteData.CONTENT_URI, null,
								null, null, null);

						// Show second activity.
						Intent intent = new Intent(MainActivity.this,
								SecondActivity.class);

						startActivity(intent);

					} catch (JSONException e) {

						Log.e("JSON Error in OnClick:", e.toString());

					}
				}
			}
		};

		// Create messenger
		Messenger serviceMessenger = new Messenger(serviceHandler);

		Intent intent1 = new Intent(_context, DataService.class);
		intent1.putExtra("messenger", serviceMessenger);
		intent1.putExtra("filename", "quoteData");

		// Start intent for data
		startService(intent1);

	}

	// Called to test internet when button pressed or app started
	public void testConnection() {

		if (_connected) {

			Log.i("Network connection: ", WebCheck.getConnectionType(_context));
			System.out.println("ONLINE");

		} else {

			Toast.makeText(
					getApplicationContext(),
					"You are not currently connected to the internet. Searching will not work, but you may still be able to load your last saved definition.",
					Toast.LENGTH_LONG).show();
			System.out.println("OFFLINE");

		}
	}

}
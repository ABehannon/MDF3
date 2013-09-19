//Alex Behannon
//09-19-2013
//MDF3 Week 3

package com.behannon.quoter;

import android.net.Uri;
import android.os.Bundle;
import android.annotation.SuppressLint;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.behannon.libs.FileSaving;
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

		// Run the call to get initial quote online
		getQuote();

		// Button for saving info
		Button saveButton = (Button) findViewById(R.id.saveButton);
		saveButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				System.out.println("CLICKED!");
				TextView authorText = (TextView) findViewById(R.id.widgetAuthorText);
				TextView quoteText = (TextView) findViewById(R.id.widgetQuoteText);
				String authorData = authorText.getText().toString()
						.replace("Author:", "");
				String quoteData = quoteText.getText().toString();
				String MixedData = authorData + ":" + quoteData;

				System.out.println("Save Data: " + MixedData);
				FileSaving.storeStringFile(getApplicationContext(),
						"favoriteData", MixedData, false);

				Toast.makeText(getApplicationContext(),
						"The requested quote has been saved.",
						Toast.LENGTH_LONG).show();

			}
		});

	}

	// Main menu addition
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		menu.getItem(2).setVisible(false);
		return true;
	}

	// Main menu on select
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		boolean ret = false;
		if (item.getItemId() == R.id.action_mainmenu) {
			ret = true;
		} else if (item.getItemId() == R.id.action_favorite) {
			favoriteButton();
			ret = true;
		} else if (item.getItemId() == R.id.action_website) {
			intentButton();
			ret = true;
		} else {
			ret = super.onOptionsItemSelected(item);
		}
		return ret;
	}

	// Called when attempting to open the second activity
	public void favoriteButton() {
		Intent intent = new Intent(MainActivity.this, SecondActivity.class);
		startActivity(intent);
	}

	// Called when the website listview item is clicked
	public void intentButton() {

		String url = Singleton.webURL();
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setData(Uri.parse(url));
		startActivity(i);

	}

	// Called when the quote of the moment listview item is clicked
	@SuppressLint("HandlerLeak")
	public void getQuote() {

		_connected = WebCheck.getConnectionStatus(_context);

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
						displayQuoteData(cursor);

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

	public void displayQuoteData(Cursor cursor) {

		String read = FileSaving.readStringFile(this, "quoteData", false);

		// Init variables
		JSONObject json;

		try {

			// create json from the file loaded
			json = new JSONObject(read);
			String quote = json.get("quote").toString();
			String author = json.get("author").toString();

			// Set the text views to show data loaded
			((TextView) findViewById(R.id.widgetQuoteText)).setText(quote);
			((TextView) findViewById(R.id.widgetAuthorText)).setText("Author: "
					+ author);

		} catch (JSONException e) {

			testConnection();
			e.printStackTrace();

		}
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

			String file = FileSaving.readStringFile(_context, "quoteData",
					false);

			if (!file.isEmpty()) {

				Cursor cursor = getContentResolver().query(
						QuoteProvider.QuoteData.CONTENT_URI, null, null, null,
						null);
				displayQuoteData(cursor);

			} else {

				System.out.println("EMPTY FILE ERROR");

			}
		}
	}

}
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
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class SecondActivity extends Activity {

	// initial variables
	Context _context;
	Boolean _connected = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_page);

		// Button for going back
		Button menuButton = (Button) findViewById(R.id.backButton);
		menuButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				onBack();
			}
		});

		// Button for saving info
		Button saveButton = (Button) findViewById(R.id.saveButton);
		saveButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				System.out.println("CLICKED!");
				TextView authorText = (TextView) findViewById(R.id.authorText);
				TextView quoteText = (TextView) findViewById(R.id.quoteText);
				String authorData = authorText.getText().toString()
						.replace("Author:", "");
				String quoteData = quoteText.getText().toString();
				String MixedData = authorData + ":" + quoteData;

				System.out.println("Save Data: " + MixedData);
				FileSaving.storeStringFile(getApplicationContext(),
						"favoriteData", MixedData, false);

				Toast.makeText(getApplicationContext(),
						"The requested quote has been marked as a favorite.",
						Toast.LENGTH_LONG).show();

			}
		});

		Cursor cursor = getContentResolver().query(
				QuoteProvider.QuoteData.CONTENT_URI, null, null, null, null);

		//display data call to show current json info
		displayQuoteData(cursor);
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
			((TextView) findViewById(R.id.quoteText)).setText(quote);
			((TextView) findViewById(R.id.authorText)).setText("Author: "
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

	//Called when back button is pressed
	public void onBack() {
		// TODO Auto-generated method stub
		finish();
	}

}

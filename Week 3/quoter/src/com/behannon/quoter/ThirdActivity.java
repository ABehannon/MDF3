//Alex Behannon
//09-19-2013
//MDF3 Week 3

package com.behannon.quoter;

import com.behannon.libs.FileSaving;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

@SuppressLint("HandlerLeak")
public class ThirdActivity extends Activity {

	// initial variables
	Context _context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.favorite_page);

		// Button for going back
		Button menuButton = (Button) findViewById(R.id.backButton2);
		menuButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				onBack();
			}
		});

		displayQuoteData2();
	}

	// Attempts to display the favorite quote
	public void displayQuoteData2() {

		String read = FileSaving.readStringFile(getApplicationContext(),
				"favoriteData", false);

		try {

			// splits the string from the file loaded
			String splitter[] = read.split(":");
			String quote = splitter[1];
			String author = splitter[0];

			// Set the text views to show data loaded
			((TextView) findViewById(R.id.quoteText2)).setText(quote);
			((TextView) findViewById(R.id.authorText2)).setText("Author:"
					+ author);

		} catch (Exception e) {
			e.printStackTrace();
			((TextView) findViewById(R.id.quoteText2))
					.setText("No favorite has been saved.");
			((TextView) findViewById(R.id.authorText2)).setText("");
		}
	}

	// called when back button is pressed
	public void onBack() {
		// TODO Auto-generated method stub
		finish();
	}
}

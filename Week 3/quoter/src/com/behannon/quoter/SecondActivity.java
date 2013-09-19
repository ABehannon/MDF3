//Alex Behannon
//09-19-2013
//MDF3 Week 3

package com.behannon.quoter;

import org.json.JSONException;
import org.json.JSONObject;
import com.behannon.libs.FileSaving;
import com.behannon.libs.QuoteProvider;
import com.behannon.libs.Singleton;
import com.behannon.libs.WebCheck;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class SecondActivity extends Activity {

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
		
		// Main menu addition
		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
			getMenuInflater().inflate(R.menu.main, menu);
			menu.getItem(0).setVisible(false);
			return true;
		}
		
		// Main menu on select
		@Override
		public boolean onOptionsItemSelected(MenuItem item) {

			boolean ret = false;
			if (item.getItemId() == R.id.action_mainmenu) {
				onBack();
				ret = true;
			} else if (item.getItemId() == R.id.action_favorite) {
				ret = true;
			} else if (item.getItemId() == R.id.action_website) {
				intentButton();
				ret = true;
			} else {
				ret = super.onOptionsItemSelected(item);
			}
			return ret;
		}
		
		// Called when the website listview item is clicked
		public void intentButton() {

			String url = Singleton.webURL();
			Intent i = new Intent(Intent.ACTION_VIEW);
			i.setData(Uri.parse(url));
			startActivity(i);

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

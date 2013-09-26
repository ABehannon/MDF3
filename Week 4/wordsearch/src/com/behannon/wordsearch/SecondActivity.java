//Alex Behannon
//MDF3 Week 4
//09-26-2013

package com.behannon.wordsearch;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import org.json.JSONException;
import org.json.JSONObject;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SecondActivity extends Activity {

	// initial setup for variables an such
	Context _context;
	Boolean _connected;
	Boolean internetConnection = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.secondactivity);

		xmlLayoutSetup();
		
		Button backButton = (Button)findViewById(R.id.backButton);
		backButton.setOnClickListener(new View.OnClickListener() {
		    public void onClick(View v) {
		        onBack();
		    }
		});
	}

	// Layout test
	private void xmlLayoutSetup() {

		_context = this;

		testConnection();
		try {
			if (internetConnection = true) {
				String keyword = MainActivity.keyword;

				TextView resultsText = (TextView) findViewById(R.id.resultsText);
				TextView keywordText = (TextView) findViewById(R.id.keywordText);

				resultsText.setText("Searching...");
				keywordText.setText(MainActivity.keyword);

				getDefinition(keyword);
			} else {
				Toast.makeText(getApplicationContext(),
						"You are not currently connected to the internet.",
						Toast.LENGTH_LONG).show();
			}
		} finally {

		}

	}

	// Called to test internet when button pressed or app started
	private void testConnection() {

		_connected = Web.getConnectionStatus(_context);
		if (_connected) {
			internetConnection = true;
			System.out.println("ONLINE");

		} else {
			Toast.makeText(
					getApplicationContext(),
					"You are not currently connected to the internet. Searching will not work, but you may still be able to load your last saved definition.",
					Toast.LENGTH_LONG).show();
			internetConnection = false;
			System.out.println("OFFLINE");
		}
	}

	// Get the definition of the word requested
	@SuppressWarnings("unused")
	private void getDefinition(String keyword) {

		// Create init variables and fix URL info
		String URL = "http://api.wordreference.com/0.8/e105d/json/enfr/";
		String baseURL = keyword.replaceAll("\\s", "+");
		String moddedURL = URL + baseURL;
		String encodeURL;

		try {
			encodeURL = URLEncoder.encode(baseURL, "UTF-8");
		} catch (Exception e) {
			Log.e("Encoding Failure", "Bad URL");
			encodeURL = "";
		}

		URL finalURL;
		try {
			finalURL = new URL(moddedURL);
			definitionRequest newRequest = new definitionRequest();
			newRequest.execute(finalURL);
			System.out.println("Modded URL: " + moddedURL);
		} catch (MalformedURLException e) {
			Log.e("Bad URL", "Malformed URL");
			finalURL = null;
		}

	}
	
	//Go back button
	public void onBack() {
		// TODO Auto-generated method stub
		finish();
	}

	// Background tasks going on when using tool
	private class definitionRequest extends AsyncTask<URL, Void, String> {

		@Override
		protected String doInBackground(URL... urls) {
			String response = "";
			for (URL url : urls) {
				response = Web.getURLStringResponse(url);

			}
			return response;
		}

		@Override
		protected void onPostExecute(String result) {

			// call to xml
			TextView resultsText = (TextView) findViewById(R.id.resultsText);
			TextView keywordText = (TextView) findViewById(R.id.keywordText);

			// Get JSON info
			try {
				JSONObject json = new JSONObject(result);
				JSONObject results = json.getJSONObject("term0")
						.getJSONObject("PrincipalTranslations")
						.getJSONObject("0").getJSONObject("OriginalTerm");
				String definition = results.getString("sense");
				keywordText.setText(MainActivity.keyword);
				resultsText.setText(definition);
				System.out.println("JSON SUCCESSFUL");
			} catch (JSONException e) {
				Log.e("JSON", "JSON OBJECT EXCEPTION");
				keywordText.setText(MainActivity.keyword);
				resultsText.setText("Definition not found.");
			}

		}

	}

}

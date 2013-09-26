//Alex Behannon
//MDF3 Week 4
//09-26-2013

package com.behannon.wordsearch;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.IOException;
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
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SecondActivity extends Activity {
	
	//initial setup for variables an such
	Context _context;
	Boolean _connected;
	Boolean internetConnection = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.secondactivity);
	
		xmlLayoutSetup();
	}
	

	//Layout test
	private void xmlLayoutSetup(){
		
		_context = this;
		
		//Set up buttons for app
		Button searchButton = (Button) findViewById(R.id.searchButton);
		Button saveButton = (Button) findViewById(R.id.saveButton);
		Button loadButton = (Button) findViewById(R.id.loadButton);
		
    	//On click handler for search
    	searchButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				EditText searchBox = (EditText) findViewById(R.id.searchBox);
				
				testConnection();
				
				if(internetConnection = true){
					String keyword = searchBox.getText().toString();
					getDefinition(keyword);
				}else {
					Toast.makeText(getApplicationContext(), "You are not currently connected to the internet. Searching will not work, but you may still be able to load your last saved definition.", Toast.LENGTH_LONG).show();	
				}
			}
		});
    	
    	//On click handler for save
    	saveButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					saveDefinition();
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
    	
    	//On click handler for load
    	loadButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				loadDefinition();
			}
		});
    	
    	
	}
		
		//Called to test internet when button pressed or app started
		private void testConnection() {
			
			_connected = Web.getConnectionStatus(_context);
	        if (_connected) {
	            internetConnection = true;
	            System.out.println("ONLINE");
	            
	        } else {
	        	Toast.makeText(getApplicationContext(), "You are not currently connected to the internet. Searching will not work, but you may still be able to load your last saved definition.", Toast.LENGTH_LONG).show();
	        	internetConnection = false;
	        	System.out.println("OFFLINE");
	        }
		}
		
		//Save current definition to cache
		private void saveDefinition() throws IOException{
			
			//call to xml
			TextView resultsText = (TextView) findViewById(R.id.resultsText);
			EditText searchBox = (EditText) findViewById(R.id.searchBox);
			
			//init strings
			String text = resultsText.getText().toString();
			String text2 = searchBox.getText().toString();
			String def = text2 + ":" + text;
			String error1 = "Definition not found.";
			String error2 = "";
			String FILENAME = "def_file.txt";
			
			if (text != error1 && text != error2){

				    //Write string
				    DataOutputStream out;
					try {
						out = new DataOutputStream(openFileOutput(FILENAME, Context.MODE_PRIVATE));
						
						out.writeUTF(def);
					    out.close();
					    System.out.println("SAVE WORKED");
					    
					    Toast.makeText(getApplicationContext(), "The definition has been saved.", Toast.LENGTH_LONG).show();
			        	
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						System.out.println("SAVE FAILED");
					}
				} else {
				Toast.makeText(getApplicationContext(), "Please complete a successful search before attempting to save.", Toast.LENGTH_LONG).show();
			}
		}
		
		//Load cached definition
		private void loadDefinition(){
			
			//call to xml
			TextView resultsText = (TextView) findViewById(R.id.resultsText);
			EditText searchBox = (EditText) findViewById(R.id.searchBox);
		
			String FILENAME = "def_file.txt";
			String extracted;
			String[] separated;
			String part1;
			String part2;
			
			try{
			    // Read data
			    DataInputStream in = new DataInputStream(openFileInput(FILENAME));
			    
			    try {
			        for (;;) {
			        	extracted = in.readUTF();
			        	separated = extracted.split(":");
			        	part1 = separated[0];
			        	part2 = separated[1];
			        	
			        	searchBox.setText(part1);
			        	resultsText.setText(part2);

			        	Log.i("Data Input Sample", in.readUTF());
			          
			        }
			    } catch (EOFException e) {
			        Log.i("Data Input Sample", "End of file reached");
			    }
			    in.close();
			} catch (IOException e) {
			    Log.i("Data Input Sample", "I/O Error");
				Toast.makeText(getApplicationContext(), "An error occured attemting to load your previous definition. Please try again.", Toast.LENGTH_LONG).show();

			}
			
		}
		
		
		//Get the definition of the word requested
		@SuppressWarnings("unused")
		private void getDefinition(String keyword){
			
			//Create init variables and fix URL info
			String URL = "http://api.wordreference.com/0.8/e105d/json/enfr/";
			String baseURL = keyword.replaceAll("\\s", "+");
			String moddedURL = URL + baseURL;
			String encodeURL;
			
			try{
				encodeURL = URLEncoder.encode(baseURL, "UTF-8");
			}catch (Exception e){
				Log.e("Encoding Failure", "Bad URL");
				encodeURL = "";
			}
			
			URL finalURL;
			try{
				finalURL = new URL(moddedURL);
				definitionRequest newRequest = new definitionRequest();
				newRequest.execute(finalURL);
				System.out.println("Modded URL: "+ moddedURL);
			}catch (MalformedURLException e){
				Log.e("Bad URL", "Malformed URL");
				finalURL = null;
			}
			
		}
		
		//Background tasks going on when using tool
		private class definitionRequest extends AsyncTask<URL, Void, String> {
			
			@Override
			protected String doInBackground(URL... urls) {
				String response = "";
				for (URL url:urls) {
					response = Web.getURLStringResponse(url);
					
				}
				return response;
			}
			
			@Override
			protected void onPostExecute(String result){
				
				//call to xml
				TextView resultsText = (TextView) findViewById(R.id.resultsText);
				
				//Get JSON info
				try{
					JSONObject json = new JSONObject(result);
					JSONObject results = json.getJSONObject("term0").getJSONObject("PrincipalTranslations").getJSONObject("0").getJSONObject("OriginalTerm");
					String definition = results.getString("sense");
					resultsText.setText(definition);
					System.out.println("JSON SUCCESSFUL");
				}catch (JSONException e){
					Log.e("JSON", "JSON OBJECT EXCEPTION");
					resultsText.setText("Definition not found.");
				}
				
			}
			
		}
		
		
}

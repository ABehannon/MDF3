//Alex Behannon
//MDF3 Week 4
//09-26-2013

package com.behannon.wordsearch;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;

public class MainActivity extends Activity {

	static Context _context;
	Button searchButton;
	static String keyword;

	@SuppressLint("JavascriptInterface")
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		_context = this;
		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview);

		// Setting up the main web view.
		WebView mainWebView = (WebView) findViewById(R.id.webView);

		// Connecting to the settings of the web view
		WebSettings webSettings = mainWebView.getSettings();
		webSettings.setJavaScriptEnabled(true);

		// Connecting buttons using javascript interface
		mainWebView.addJavascriptInterface(new Object() {
			public void performClick() {
				System.out.println("COW");
				keyword = "cow";
				System.out.println("Keyword: " + keyword);
				nextActivity();
			}
		}, "cowButton");
		
		mainWebView.addJavascriptInterface(new Object() {
			public void performClick() {
				System.out.println("money");
				keyword = "money";
				System.out.println("Keyword: " + keyword);
				nextActivity();
			}
		}, "moneyButton");
		
		mainWebView.addJavascriptInterface(new Object() {
			public void performClick() {
				System.out.println("Red");
				keyword = "red";
				System.out.println("Keyword: " + keyword);
				nextActivity();
			}
		}, "redButton");
		
		mainWebView.addJavascriptInterface(new Object() {
			public void performClick() {
				System.out.println("triangle");
				keyword = "triangle";
				System.out.println("Keyword: " + keyword);
				nextActivity();
			}
		}, "triangleButton");

		// Display web view after settings
		mainWebView.loadUrl("file:///android_asset/main.html");

	}

	// Called when attempting to open the second activity
	public void nextActivity() {
		Intent myIntent = new Intent(MainActivity.this, SecondActivity.class);
		MainActivity.this.startActivity(myIntent);
	}
}

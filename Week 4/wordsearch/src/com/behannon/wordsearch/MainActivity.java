//Alex Behannon
//MDF3 Week 4
//09-26-2013

package com.behannon.wordsearch;

import java.io.File;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;


public class MainActivity extends Activity {
	
	Context _context;
	Button searchButton;
	
	@SuppressLint("JavascriptInterface")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview);
	
		//Setting up the main web view.
		WebView mainWebView = (WebView)findViewById(R.id.webView);
		
		//Connecting to the settings of the web view
		WebSettings webSettings = mainWebView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		
		//Connecting button using javascript interface
		mainWebView.addJavascriptInterface(new Object() {
			public void performClick(){
				System.out.println("TEST!");
			}
		}, "searchButton");
		
		//Display web view after settings
		mainWebView.loadUrl("file:///android_asset/main.html");
		
		
	}
	
}

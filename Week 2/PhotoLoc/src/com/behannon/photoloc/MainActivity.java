//Alex Behannon
//09-12-2013
//MDF3 Week 2

package com.behannon.photoloc;

import android.os.BatteryManager;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

	//Initial variables created below
	TextView latitude;		//latitude from text view
	TextView longitude;		//longitude from text view
	Button cameraButton;	//open camera button
	Button batteryButton;	//batter power popup button
	Context context;		//Set context
	String batlife;			//string for bat life call
	
	private static final int CAMERA_PIC_REQUEST = 2500;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE); //Removes title from window
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//Call for battery life receiver information
		this.registerReceiver(this.batteryInfoReceiver,	new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
		
		//set context
		context = this;
		
		//Call variables from layout
  		cameraButton = (Button) findViewById(R.id.cameraButton);
  		batteryButton = (Button) findViewById(R.id.batteryButton);
  		latitude = (TextView) findViewById(R.id.latitude);
  		longitude = (TextView) findViewById(R.id.longitude);
        	
      	//camera button call
      	cameraButton.setOnClickListener(new View.OnClickListener() {
      		public void onClick(View v) {
      			
      			Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
      			startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
      			
      	    }
      	});
      	
      	//battery power button call
      	batteryButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				batteryManage();
			}
		});
      	
    }
	
	private BroadcastReceiver batteryInfoReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			int  level= intent.getIntExtra(BatteryManager.EXTRA_LEVEL,0);
			batlife = level + "%";
			System.out.println("BAT LIFE: " + batlife);
		}
	};
	
	//Battery power management and information
	private void batteryManage() {
		
		//Use alertdialog to pop up current battery life
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage("Current battery life is " + batlife)
	       .setCancelable(false)
	       .setPositiveButton("OK", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	                
	           }
	       });
		AlertDialog alert = builder.create();
		alert.show();
		
	}
}

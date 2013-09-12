//Alex Behannon
//09-12-2013
//MDF3 Week 2

package com.behannon.photoloc;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	//Initial variables created below
	String latitude;		//latitude string
	String longitude;		//longitude string
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
  		TextView currentLat = (TextView) findViewById(R.id.latitude);
 	    TextView currentLong = (TextView) findViewById(R.id.longitude);
        	
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
      	
      	//Setting up for GPS location
    	LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE); 
    	Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    	
    	//Checks if the current location is available or not.
    	if(location != null) {
    		System.out.println("GPS location is NOT null");
    		double latitude = location.getLatitude();
    	    double longitude = location.getLongitude();
    	    
    	    System.out.println("LAT: " + latitude);
    	    System.out.println("LONG: " + longitude);
    	    currentLat.setText("LAT: " + latitude);
    	    currentLong.setText("LONG: " + longitude);
    	    
    	} else {
    		System.out.println("GPS location is null");
    		currentLat.setText("LAT: " + "Unavailable");
    	    currentLong.setText("LONG: " + "Unavailable");
    	    Toast.makeText(getApplicationContext(), "GPS Coordinates unavailable. If on emulator, please be sure to feed in test coords.", Toast.LENGTH_LONG).show();
    	}

    	final LocationListener locationListener = new LocationListener() {
    	    public void onLocationChanged(Location location) {
    	        showCurrentAddress(location);
    	    }

    	    public void onProviderDisabled(String arg0) {

    	    	Toast.makeText(getApplicationContext(), "GPS is disabled.", Toast.LENGTH_LONG).show();

    	    }

    	    public void onProviderEnabled(String arg0) {
    	        
    	    	Toast.makeText(getApplicationContext(), "GPS is enabled.", Toast.LENGTH_LONG).show();

    	    }

    	    public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
    	        // TODO Auto-generated method stub

    	    }
    	};

    	lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, locationListener);
    }
	
	//Battery info broadcast for power level
	private BroadcastReceiver batteryInfoReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			int level= intent.getIntExtra(BatteryManager.EXTRA_LEVEL,0);
			batlife = String.valueOf(level);
			System.out.println("BAT LIFE: " + batlife + "%");
		}
	};
	
	//Display image result from camera
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

		ImageView CameraResult = (ImageView) findViewById(R.id.cameraImage);
		
		super.onActivityResult(requestCode, resultCode, intent);

		if (resultCode == RESULT_OK){
			Bundle extras = intent.getExtras();
			Bitmap bmp = (Bitmap) extras.get("data");
			CameraResult.setImageBitmap(bmp);
			
		}
		
	}
	//Battery power management and information
	private void batteryManage() {
		
		//Use alertdialog to pop up current battery life
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage("Current battery life is " + batlife + "%")
	       .setCancelable(false)
	       .setPositiveButton("OK", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	                
	           }
	       });
		AlertDialog alert = builder.create();
		alert.show();
		
	}
	
	//GPS location method
	private void showCurrentAddress(Location location) {
		
		TextView currentLoc = (TextView) findViewById(R.id.latitude);
		
	    double latitude = location.getLatitude();
	    double longitude = location.getLongitude();
	    Geocoder myLocation = new Geocoder(getApplicationContext(), Locale.getDefault());   
	    List<Address> myList;
	    try {
	        myList = myLocation.getFromLocation(latitude, longitude, 1);
	        if(myList.size() == 1) {
	            currentLoc.setText(myList.get(0).toString());             
	        }
	    } catch (IOException e1) {
	        // TODO Auto-generated catch block
	        e1.printStackTrace();
	    }
	}
}

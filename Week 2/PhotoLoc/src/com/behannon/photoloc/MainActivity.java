//Alex Behannon
//09-12-2013
//MDF3 Week 2

package com.behannon.photoloc;

import android.os.BatteryManager;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity implements SensorEventListener {

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
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
        
		//Call for battery life receiver information
		this.registerReceiver(this.batteryInfoReceiver,	new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
		
		//set context
		context = this;
		
		//Call variables from layout
  		cameraButton = (Button) findViewById(R.id.cameraButton);
  		batteryButton = (Button) findViewById(R.id.batteryButton);
  		TextView currentAcc = (TextView) findViewById(R.id.accelerometer);
        	
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
      	
    	//Accelerometer Setup
    	SensorManager manager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
    	Sensor accelerometer = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    	if(!manager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME)){
    		currentAcc.setText("Error, could not register sensor listener");
    	}

    }
	
	//---------------------------
	//Sensor information
	//---------------------------
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		
	}
	
	@Override
	public void onSensorChanged(SensorEvent event) {
		
  		TextView currentAcc = (TextView) findViewById(R.id.accelerometer);
  		
		//Init string
		StringBuilder builder = new StringBuilder();
		
		builder.setLength(0);
		builder.append("X " + event.values[0] + "\nY " + event.values[1] + "\nZ " + event.values[2]);
		currentAcc.setText(builder.toString());
		
	}
	
	//---------------------------
	//Battery info broadcast
	//for power level
	//---------------------------
	private BroadcastReceiver batteryInfoReceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			int level= intent.getIntExtra(BatteryManager.EXTRA_LEVEL,0);

			batlife = String.valueOf(level);
			System.out.println("BAT LIFE: " + batlife + "%");
		}
	};
	
	//---------------------------
	//Battery power management
	//and information
	//---------------------------
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
	
	//---------------------------
	//Display image result
	//from camera
	//---------------------------
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

		ImageView CameraResult = (ImageView) findViewById(R.id.cameraImage);
		
		super.onActivityResult(requestCode, resultCode, intent);

		if (resultCode == RESULT_OK){
			Bundle extras = intent.getExtras();
			Bitmap bmp = (Bitmap) extras.get("data");
			CameraResult.setImageBitmap(bmp);
			
			showNotification();
			
		}
		
	}
	
	//---------------------------
	//Notification setup, called
	//when picture is taken.
	//---------------------------
	private void showNotification() {

		Intent intent = new Intent(this, Notification.class);
		PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);

		// Build notification
		Notification noti = new Notification.Builder(this)
		        .setContentTitle("Picture Taken!")
		        .setContentText("PhotoLoc")
		        .setSmallIcon(R.drawable.ic_launcher)
		        .setContentIntent(pIntent).build();
		    
		NotificationManager notificationManager = 
		  (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

		// Hide the notification after its selected
		noti.flags |= Notification.FLAG_AUTO_CANCEL;

		notificationManager.notify(0, noti); 
	}

}

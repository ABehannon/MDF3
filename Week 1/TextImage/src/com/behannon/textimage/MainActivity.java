package com.behannon.textimage;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	//Initial variables created below
	TextView phoneNumber;	//Phone number from text view
	TextView messageText;	//message text from text view
	Button sendButton;		//Send button to text
	Uri photoURI;			//URI for photo
	ImageView cameraImage;	//Image from camera
	Context context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		//Removes title from window
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//Call variables from layout
		phoneNumber = (TextView) findViewById(R.id.phoneNumber);
		messageText = (TextView) findViewById(R.id.textMessage);
		sendButton = (Button) findViewById(R.id.sendButton);
		cameraImage = (ImageView) findViewById(R.id.savedPhoto);
		
		
		//Send button call
		sendButton.setOnClickListener(new View.OnClickListener() {
	        public void onClick(View v) {
	        	sendText();
	        }
	    });
		
		//Checks to see if the app was ran on its own, or through the camera / image send
		Intent intent = getIntent();
		String action = intent.getAction();
		String type = intent.getType();
		
		if (Intent.ACTION_SEND.equals(action) && type != null) {
            if (type.startsWith("image/")) {
            	getImage(intent);
            	
            	//Enables send button
            	sendButton.setEnabled(true);
            }
        } else {
			//Send error letting user know not to open app.
        	Toast.makeText(getApplicationContext(), "This application is not to be run on its own, but rather through the Camera/Image applications.", Toast.LENGTH_LONG).show();
			
        	//Disables send button
        	sendButton.setEnabled(false);
        }
		  
	}
	
	//Called when pressing the "Send" button on main layout
	void sendText(){
		
		//Set context
		context = this;
		
		//Set up initial strings
		String number = phoneNumber.getText().toString();
		String message = messageText.getText().toString();
		
		//Setting up URI for sending information to text app
		Intent it = new Intent(Intent.ACTION_SEND);
		it.putExtra("address", number);
		it.putExtra("sms_body", message);   
		it.putExtra(Intent.EXTRA_STREAM, photoURI);
		it.setType("image/png");
		startActivity(it);
		
	}
	
	//get image from camera/library
	void getImage(Intent intent) {
		
		//Set view for camera image
		cameraImage = (ImageView) findViewById(R.id.savedPhoto);
		
		//set up URI for intent
		photoURI = intent.getParcelableExtra(Intent.EXTRA_STREAM);
		
		//If the URI isnt blank, run next part
		if(photoURI != null) {
			System.out.println("URI OF PHOTO: " + photoURI);
			
			//Set image from camera
			cameraImage.setImageURI(photoURI);
			
		}
	}

}

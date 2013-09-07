package com.behannon.textimage;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

	//Initial variables created below
	TextView phoneNumber;	//Phone number from text view
	TextView messageText;	//message text from text view
	Button sendButton;		//Send button to text
	Uri photoURI;			//URI for photo
	Context context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//Call variables from layout
		phoneNumber = (TextView) findViewById(R.id.phoneNumber);
		messageText = (TextView) findViewById(R.id.textMessage);
		sendButton = (Button) findViewById(R.id.sendButton);
		
		sendButton.setOnClickListener(new View.OnClickListener() {
	        public void onClick(View v) {
	        	sendText();
	        }
	    });
		  
	}
	
	void sendText(){
		
		//Set context
		context = this;
		
		//Set up initial strings
		String number = phoneNumber.getText().toString();
		String message = messageText.getText().toString();
		
		//Setting up URI for sending information to text app
		Uri uri = Uri.parse("smsto:" + number);   
		Intent it = new Intent(Intent.ACTION_SENDTO, uri);   
		it.putExtra("sms_body", message);   
		startActivity(it);
		
	}

}

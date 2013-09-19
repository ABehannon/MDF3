//Alex Behannon
//08-29-2013
//Java Week 4

package com.behannon.quoter;

import java.net.MalformedURLException;
import java.net.URL;
import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import com.behannon.libs.*;

public class DataService extends IntentService {

	//initial setup for variables an such
	URL usableURL = null;
	String response = null;
	String category = null;
	String filename = null;

	public DataService(){
		super("DataService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		System.out.println("DATA SERVICE STARTED");
		Bundle extras = intent.getExtras();

		if(extras != null){
			category = (String) extras.get("category");
			filename = (String) extras.get("filename");
		}

		//Create init variables and fix URL info
		String apiURL = "http://quotesondesign.com/api/3.0/api-3.0.json";
		String queryString = "";

		try {
			usableURL = new URL(apiURL);
			System.out.println("URL: " + usableURL);
			
			response = WebCheck.getURLStringResponse(usableURL);
			
			Log.i("Query String: ", queryString.toString());
			Log.i("Response data: ", response.toString());
			
			System.out.println("SAVE RESPONSE: " + response);
			FileSaving.storeStringFile(this, filename, response, false);
		} catch(MalformedURLException e) {
			Log.e("Error: ", "MalformedURLException");
			usableURL = null;
		}

		Messenger messenger = (Messenger) extras.get("messenger");

		Message message = Message.obtain();
		if (message != null){
			message.arg1 = Activity.RESULT_OK;
			message.obj = response;
		}

		try {
			messenger.send(message);
		} catch (RemoteException e) {
			Log.e("onHandleIntent", e.getMessage().toString());
		}
	}

}
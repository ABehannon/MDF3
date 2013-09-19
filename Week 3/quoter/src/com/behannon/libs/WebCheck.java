//Alex Behannon
//08-29-2013
//Java Week 4

package com.behannon.libs;

import java.io.BufferedInputStream;
import java.net.URL;
import java.net.URLConnection;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class WebCheck {

	static Boolean _connectionStatus = false;
	static String _connectionType = "Unavailable";

	public static String getConnectionType(Context context){
		
		nInfo(context);
		return _connectionType;
		
	}

	public static Boolean getConnectionStatus(Context context){
		
		_connectionStatus = false;
		nInfo(context);

		return _connectionStatus;
		
	}

	private static void nInfo(Context context){
		
		ConnectivityManager manageConnection = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = manageConnection.getActiveNetworkInfo();
		
		if(netInfo != null){
			
			if(netInfo.isConnected()){
				
				_connectionType = netInfo.getTypeName();
				_connectionStatus = true;
				
			}
		}
	}

	public static String getURLStringResponse(URL url){

		String response = "";

		try{
			
			URLConnection connect = url.openConnection();
			BufferedInputStream bin = new BufferedInputStream(connect.getInputStream());

			byte[] contentBytes = new byte[1024];
			int bytesRead = 0;

			StringBuilder responseBuffer = new StringBuilder();

			while ((bytesRead = bin.read(contentBytes)) != -1){
				
				response = new String(contentBytes, 0, bytesRead);
				responseBuffer.append(response);
				
			}
			
			return responseBuffer.toString();
			
		} catch(Exception e) {
			
			Log.e("URL Response Error", e.toString());
			
		}
		
		return response;
	}

}
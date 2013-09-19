//Alex Behannon
//08-29-2013
//Java Week 4

package com.behannon.libs;

import android.content.Intent;

public class Singleton extends Intent{
	
	//Custom values
	static String URL;
	
	//Default Singleton setup
	private static Singleton instance = null;

	private Singleton(){
		
	}
	
	public static Singleton getInstance(){
		if (instance == null){
			instance = new Singleton();
		}
		return instance;
	}
	
	//BEGIN CUSTOM METHODS HERE
	
	//URL for Website Intent
	public static String webURL(){
		URL = "http://quotesondesign.com/";
		return URL;
	}
	
	
}
//Alex Behannon
//08-29-2013
//Java Week 4

package com.behannon.libs;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import android.content.Context;
import android.util.Log;

public class FileSaving {

	@SuppressWarnings("resource")
	public static Boolean storeStringFile(Context context, String filename, String content, Boolean locExt){
		
		try{
			
			File file;
			FileOutputStream fos;
			
			if(locExt){
				
				file = new File(context.getExternalFilesDir(null), filename);
				fos = new FileOutputStream(file);
				
			} else {
				
				fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
			
			}
			
			fos.write(content.getBytes());
			
		} catch (IOException e){
			
			Log.e("Write Error: ", filename);
			
		}
		
		return true;
		
	}

	@SuppressWarnings("resource")
	public static Boolean storeObjectFile(Context context, String filename, Object content, Boolean locExt){
		
		try {
			
			File file;
			FileOutputStream fos;
			ObjectOutputStream oos;
			
			if(locExt){
				
				file = new File(context.getExternalFilesDir(null), filename);
				fos = new FileOutputStream(file);
				
			} else {
				
				fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
				
			}
			
			oos = new ObjectOutputStream(fos);
			oos.writeObject(content);
			oos.close();
			fos.close();
			
		} catch(IOException e){
			
			Log.e("Write Error: ", filename);
			
		}
		
		return true;
	}

	@SuppressWarnings("resource")
	public static String readStringFile(Context context, String filename, Boolean locExt){
		
		String content = "";
		
		try{
			
			File file;
			FileInputStream fis;
			
			if(locExt){
				
				file = new File(context.getFileStreamPath(null), filename);
				fis = new FileInputStream(file);
				
			} else {
				
				file = new File(filename);
				fis = context.openFileInput(filename);
				
			}
			
			BufferedInputStream bin = new BufferedInputStream(fis);
			byte[] contentBytes = new byte[1024];
			int bytesRead = 0;
			StringBuffer contentBuffer = new StringBuffer();
			
			while((bytesRead = bin.read(contentBytes)) != -1){
				
				content = new String(contentBytes, 0, bytesRead);
				contentBuffer.append(content);
				
			}
			
			content = contentBuffer.toString();
			
		} catch(FileNotFoundException e){
			
			Log.e("Read Error: ", "File not found " + filename);
			
		} catch (IOException e){
			
			Log.e("Read Error: ", "I/O Error");
			
		}
		
		return content;
	}

	@SuppressWarnings("resource")
	public static Object readObjectFile(Context context, String filename, Boolean locExt){
		
		Object content = new Object();
		
		try{
			
			File file;
			FileInputStream fis;
			
			if(locExt){
				
				file = new File(context.getFileStreamPath(null), filename);
				fis = new FileInputStream(file);
				
			} else {
				
				file = new File(filename);
				fis = context.openFileInput(filename);
				
			}
			
			ObjectInputStream objectInput = new ObjectInputStream(fis);
			
			try{
				
				content = objectInput.readObject();
				
			} catch(ClassNotFoundException e) {
				
				Log.e("Read Error: ", "Invalid Java OBject File");
				
			}
			
			objectInput.close();
			fis.close();
			
		} catch(FileNotFoundException e){
			
			Log.e("Read Error: ", "File not found " + filename);
			
		} catch (IOException e){
			
			Log.e("Read Error: ", "I/O Error");
			
		}
		
		return content;
		
	}
}
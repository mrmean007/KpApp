package com.example.mrmean.hakomataaa;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.zip.Inflater;

import org.json.JSONArray;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageContainer;
import com.android.volley.toolbox.ImageLoader.ImageListener;

//import com.example.socialtourism.R;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;

public class Universal {
//	static String linkip = "http://tourbeem.com/app_api";
	
	static String linkip = "http://192.168.10.5/userphotoupload.php";
	//
//	static String linkip = "http://192.168.8.104";
//	static String linkip = "http://172.16.10.129";
//	static String linkip = "http://192.168.1.8";
	static Bitmap bitmap=null;
	public String getuser_id(Context context) {///
		String user_id;
		SharedPreferences preferences;
		preferences = context.getSharedPreferences("loginuser",	Context.MODE_PRIVATE);
		user_id = preferences.getString("loginuserid", "1");
		return user_id;
	}

	public void setuser_id(Context context, String user_id) {
		SharedPreferences preferences;
		preferences = context.getSharedPreferences("loginuser",	Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString("loginuserid", user_id);
		editor.commit();
	}
	

	public String getuser_type(Context context) {
		String user_type;
		SharedPreferences preferences;
		preferences = context.getSharedPreferences("loginuser",	Context.MODE_PRIVATE);
		preferences = context.getSharedPreferences("loginuser",	Context.MODE_PRIVATE);
		user_type = preferences.getString("loginusertype", "1");
		return user_type;
	}

	public void setuser_type(Context context, String user_type) {
		SharedPreferences preferences;
		preferences = context.getSharedPreferences("loginuser",	Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString("loginusertype", user_type);
		editor.commit();
	}
	
	


	public Uri getImageUri(Context inContext, Bitmap inImage) {
	    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
	    inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
	    String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
	    return Uri.parse(path);
	}
	private void shareImage(Context context,Bitmap bitmap,String message) {
		String imagePath = Environment.getExternalStorageDirectory()
				+ "/myImage.png";
//
		File imageFileToShare = new File(imagePath);
//
		Uri uri = Uri.fromFile(imageFileToShare);
		
		 File filePath = context.getFileStreamPath("shareimage.jpg");  //optional //internal storage
	     Intent shareIntent = new Intent();
	     shareIntent.setAction(Intent.ACTION_SEND);
	     shareIntent.putExtra(Intent.EXTRA_TEXT, message);
	     shareIntent.putExtra(Intent.EXTRA_STREAM, getImageUri(context, bitmap));  //optional//use this when you want to send an image
	     shareIntent.setType("image/jpeg");
	     shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
	     context.startActivity(Intent.createChooser(shareIntent, "send"));
	}
	public void showToast(Context context,String message){
		
		
		LayoutInflater inflater = ((Activity) context).getLayoutInflater();
	 
//		View layout =inflater.inflate(R.layout.toast_layout2, null);
//		TextView textView=(TextView) layout.findViewById(R.id.ttext);
//		textView.setText(message);
//		Toast toast = new Toast(context);
//		toast.setDuration(Toast.LENGTH_LONG);
//		toast.setView(layout);
//		toast.show();
	  
	}
	
	 public boolean chechConnectivity(Context context){
		 boolean haveConnectedWifi = false;
		    boolean haveConnectedMobile = false;
	        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	        NetworkInfo[] netInfo = connectivity.getAllNetworkInfo();
	        for (NetworkInfo ni : netInfo) {
	            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
	                if (ni.isConnected())
	                    haveConnectedWifi = true;
	            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
	                if (ni.isConnected())
	                    haveConnectedMobile = true;
	        }
	        return haveConnectedWifi || haveConnectedMobile;
	    }
	public String getuser_typeTemp(Context context) {
		String user_type;
		SharedPreferences preferences;
		preferences = context.getSharedPreferences("loginuser",	Context.MODE_PRIVATE);
		user_type = preferences.getString("loginusertypetemp", "1");
		return user_type;
	}

	public void setuser_typeTemp(Context context, String user_type) {
		SharedPreferences preferences;
		preferences = context.getSharedPreferences("loginuser",	Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString("loginusertypetemp", user_type);
		editor.commit();
	}
	
	 
	
	
	public String getuser_id_Visit(Context context) {
		String user_type;
		SharedPreferences preferences;
		preferences = context.getSharedPreferences("loginuser",	Context.MODE_PRIVATE);
		user_type = preferences.getString("loginuservisituser", "0");
		return user_type;
	}

	public void setuser_id_Visit(Context context, String user_type) {
		SharedPreferences preferences;
		preferences = context.getSharedPreferences("loginuser",	Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString("loginuservisituser", user_type);
		editor.commit();
	}

	public File storeImage(Context context, Bitmap image) {
		File pictureFile = getOutputMediaFile(context);
		if (pictureFile == null) {
			Log.d("saving",
					"Error creating media file, check storage permissions: ");// e.getMessage());
			return null;
		}
		try {
			FileOutputStream fos = new FileOutputStream(pictureFile);
			image.compress(Bitmap.CompressFormat.PNG, 90, fos);
			fos.close();
		} catch (FileNotFoundException e) {
			Log.d("saving", "File not found: " + e.getMessage());
		} catch (IOException e) {
			Log.d("saving", "Error accessing file: " + e.getMessage());
		}
		return pictureFile;
	}

	public String genrate(Context context) {//
		String user_id = getuser_id(context);
		Calendar calendar = Calendar.getInstance();
		String year = new SimpleDateFormat("yyyy").format(calendar
				.getTimeInMillis());
		String month = new SimpleDateFormat("MM").format(calendar
				.getTimeInMillis());
		String token = "";
		char[] array1 = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
				'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V',
				'W', 'X', 'Y', 'Z' };
		char[] array = { 'z', 'y', 'x', 'w', 'v', 'u', 't', 's', 'r', 'q', 'p',
				'o', 'n', 'm', 'l', 'k', 'j', 'i', 'h', 'g', 'f', 'e', 'd',
				'c', 'b', 'a' };
		char[] randomarray = { 'e', 'u', 'c', 'r', 'p', 's', 'w', 'x', 'e',
				'p', 'd', 'h', 'l', 'p', 'l', 'h', 't', 'u', 'x', 'x', 'r',
				'l', 'b', 'h', 'l', 'p' };
		for (int i = 0; i < year.length(); i++) {
			token += randomarray[(Integer.parseInt(year.charAt(i) + "") + Integer
					.parseInt(year.charAt(i) + ""))]
					+ ""
					+ year.charAt(i)
					+ ""
					+ user_id
					+ array1[Integer.parseInt(year.charAt(i) + "")]
					+ "" + array[Integer.parseInt(year.charAt(i) + "")] + "";
		}
//		Log.e("id", user_id);
//		Log.e("Token", token);
		return token;
	}

	private File getOutputMediaFile(Context context) {
		File mediaStorageDir = new File(
				Environment.getExternalStorageDirectory() + "/Android/data/"
						+ context.getPackageName() + "/Files");
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				return null;
			}
		}
		String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm")
				.format(new Date());
		File mediaFile;
		String mImageName = "MI_" + timeStamp + ".jpg";
		mediaFile = new File(mediaStorageDir.getPath() + File.separator
				+ mImageName);
		return mediaFile;
	}

	public void imageLoader(final Context context, final ImageView v,
			String url, final int wedth, final int hieght) {
		if (url.equals(null))
			url = "http://img1.wikia.nocookie.net/__cb20130903220615/spiderman/images/0/02/Image_not_available.gif";
		ImageLoader imageLoader = AppController.getInstance().getImageLoader();

		imageLoader.get(url, new ImageListener() {
			@Override
			public void onErrorResponse(VolleyError arg0) {
				imageLoader(
						context,
						v,
						"http://img1.wikia.nocookie.net/__cb20130903220615/spiderman/images/0/02/Image_not_available.gif",
						wedth, hieght);
			}

			@Override
			public void onResponse(ImageContainer arg0, boolean arg1) {
				if (arg0.getBitmap() != null)
					v.setImageBitmap(getResizedBitmap(context,
							arg0.getBitmap(), wedth, hieght));//
				v.setScaleType(ScaleType.FIT_XY);
			}
		});
//		imageLoader.get(url, ImageLoader.getImageListener(
//				new ImageView(context), R.drawable.loading_image,
//				R.drawable.dialog_warning));
	}
	
	
	public Bitmap imageLoaderForShare(final Context context, 
			String url,final String message) {
		  
		if (url.equals(null))
			url = "http://img1.wikia.nocookie.net/__cb20130903220615/spiderman/images/0/02/Image_not_available.gif";
		ImageLoader imageLoader = AppController.getInstance().getImageLoader();

		imageLoader.get(url, new ImageListener() {
			@Override
			public void onErrorResponse(VolleyError arg0) {
				imageLoaderForShare(
						context,

						"http://img1.wikia.nocookie.net/__cb20130903220615/spiderman/images/0/02/Image_not_available.gif",
						message);
			}

			@Override
			public void onResponse(ImageContainer arg0, boolean arg1) {
				if (arg0.getBitmap() != null){
					bitmap=arg0.getBitmap();
					shareImage(context, bitmap, message);
					 
					
				}
			}
		});
//		imageLoader.get(url, ImageLoader.getImageListener(
//				new ImageView(context), R.drawable.loading_image,
//				R.drawable.dialog_warning));
		return bitmap;
	}

	public Bitmap getResizedBitmap(Context context, Bitmap bm, int newWidth,
			int newHeight) {
		int width = bm.getWidth();
		int height = bm.getHeight();
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height,
				matrix, false);
		return resizedBitmap;
	}

	public int uploadFile(final Context context, String sourceFileUri,
			String finalimageName, String URL) {
		int serverResponseCode = 0;
		String upLoadServerUri = this.linkip + URL;
		String fileName = sourceFileUri;
		HttpURLConnection conn = null;
		DataOutputStream dos = null;
		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";
		int bytesRead, bytesAvailable, bufferSize;
		byte[] buffer;
		int maxBufferSize = 1 * 1024 * 1024;
		File sourceFile = new File(sourceFileUri);
		if (!sourceFile.isFile()) {
			Log.e("uploadFile", "Source File Does not exist");
			return 0;
		}
		try {
			FileInputStream fileInputStream = new FileInputStream(sourceFile);
			URL url = new URL(upLoadServerUri);
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoInput(true); // Allow Inputs
			conn.setDoOutput(true); // Allow Outputs
			conn.setUseCaches(false); // Don't use a Cached Copy
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("ENCTYPE", "multipart/form-data");
			conn.setRequestProperty("Content-Type",
					"multipart/form-data;boundary=" + boundary);
			conn.setRequestProperty("uploaded_file", fileName);
			dos = new DataOutputStream(conn.getOutputStream());
			dos.writeBytes(twoHyphens + boundary + lineEnd);
			fileName = finalimageName;
			dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
					+ fileName + "\"" + lineEnd);
			dos.writeBytes(lineEnd);
			bytesAvailable = fileInputStream.available(); // create a buffer of
			bufferSize = Math.min(bytesAvailable, maxBufferSize);
			buffer = new byte[bufferSize];
			bytesRead = fileInputStream.read(buffer, 0, bufferSize);
			while (bytesRead > 0) {
				dos.write(buffer, 0, bufferSize);
				bytesAvailable = fileInputStream.available();
				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				bytesRead = fileInputStream.read(buffer, 0, bufferSize);
			}
			dos.writeBytes(lineEnd);
			dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
			serverResponseCode = conn.getResponseCode();
			String serverResponseMessage = conn.getResponseMessage();
			Log.i("uploadFile", "HTTP Response is : " + serverResponseMessage
					+ ": " + serverResponseCode);
			if (serverResponseCode == 200) {
				Toast.makeText(context, "File Upload Complete.",
						Toast.LENGTH_SHORT).show();
			}
			fileInputStream.close();
			dos.flush();
			dos.close();
		} catch (MalformedURLException ex) {
			ex.printStackTrace();
			Toast.makeText(context, "MalformedURLException", Toast.LENGTH_SHORT)
					.show();
			Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(context, "Exception : " + e.getMessage(),
					Toast.LENGTH_SHORT).show();
			Log.e("Upload file to server Exception",
					"Exception : " + e.getMessage(), e);
		}
		return serverResponseCode;
	}
	
	public void  ShowProgressDailog(Context context,String title) {
		
		
		
		
		
	}

}

package com.example.mrmean.hakomataaa;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;

public class GPSTracker extends Service implements LocationListener{
	private final Context context;
	boolean isGpsEnabled = false;
	boolean canGetlocation = false;
	boolean isNetworkEnabled = false;
	LocationManager locationManger;
	Location location;

	double latitude;
	double logitude;
	double altitude;

	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0;
	private static final long MIN_TIME_BW_UPDATES = 60;

	protected LocationManager locationManager;

	public GPSTracker(Context context){
		this.context = context;
		getLocation();
	}

	public void checkLocation(){
		isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		if(isGpsEnabled || isNetworkEnabled)
			this.canGetlocation = true;
		else
			this.canGetlocation = false;
	}
	public Location getLocation(){
		try{
			locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
			checkLocation();
		//	if( !isNetworkEnabled && !isGpsEnabled){

		//	}
		//	else {
			if( isNetworkEnabled || isGpsEnabled){

				if(isGpsEnabled){
					if(location==null){
						locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,MIN_DISTANCE_CHANGE_FOR_UPDATES, MIN_TIME_BW_UPDATES, this);

						if(locationManager != null){
							location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

							if(location != null) {
								latitude = location.getLatitude();
								logitude = location.getLongitude();

							}
						}
					}
				}

				else if(isNetworkEnabled) {
					locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_DISTANCE_CHANGE_FOR_UPDATES, MIN_TIME_BW_UPDATES, this);

				if(locationManager != null){
					location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
					
					if(location != null){
						latitude = location.getLatitude();
						logitude= location.getLongitude();
					}
				  }
				}

			}
			//}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return location;
	}
	
	public void stopUsingGPS(){
		if(locationManager != null)
			locationManager.removeUpdates(GPSTracker.this);
		
	}
	public double getLatitude(){
		if(location != null){
			latitude = location.getLatitude();
		}else
		getLocation();
	return latitude;
	}
	public double getLongitude(){
		if(location != null){
		logitude = location.getLongitude();
	}
		else 
			getLocation();
	return logitude;
	}
	
	public double getAltitude(){
		if(location != null){
			altitude = location.getAltitude();
		}
		return altitude;
	}
	public boolean canGetLocation(){
		checkLocation();
		return this.canGetlocation;
	}
	
	public void showSettingsAlert(){

		checkLocation();
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
		
		alertDialog.setTitle("Settings");
		alertDialog.setMessage("Enable Your Internet Connection And Location");
		
		alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Settings.ACTION_SETTINGS);
				context.startActivity(intent);
			}
		});
		
		alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
		 dialog.cancel();	
			}
		});
		alertDialog.show();
	}
	@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}

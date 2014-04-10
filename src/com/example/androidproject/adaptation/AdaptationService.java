package com.example.androidproject.adaptation;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.Toast;

import com.example.androidproject.R;
import com.example.androidproject.adaptation.bundle.time.TimeBroadcast;
import com.example.androidproject.adaptation.bundle.time.TimeBundle;
import com.example.androidproject.util.XMLUtil;
import com.example.androidproject.util.XMLUtil.ConfigBuilder;

public class AdaptationService extends Service {

	private boolean mQuit;	
	private LocationManager locationManager;
	private String provider;
	
	private AdaptationContorller adaptationContorller;
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		
		provider = locationManager.getBestProvider(new Criteria(), true);
		
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		mQuit = true;
		/*
		 * 
		 */
		ConfigurationManager.getInstance().removeAllBroadcast();
	
		Toast.makeText(AdaptationService.this, "service end", Toast.LENGTH_SHORT).show();
		
		super.onDestroy();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		mQuit = false;

		locationManager.requestLocationUpdates(provider, 10 * 1000, 10, locationListener);

		// ConfigurationManager.getInstance().installBundle(R.raw.timebundle);
		ConfigurationManager.getInstance().startBundle(TimeBundle.class.getName());
		
		adaptationContorller = new AdaptationContorller(this, locationManager, provider, mHandler);
		
		new AdaptationThread().start();
		
		return START_STICKY;
	}
	
	private class AdaptationThread extends Thread {

		@Override
		public void run() {
			while (!mQuit) {
				
				adaptationContorller.execute();
				
				try { Thread.sleep(5 * 1000); } catch (InterruptedException e) { }
			}
		}
	}
	
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0) {
				
				String data = (String) msg.obj;
				
				Toast.makeText(AdaptationService.this, data, Toast.LENGTH_SHORT).show();
			}
		}
	};
	
	private LocationListener locationListener = new LocationListener() {
		
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			
		}
		
		@Override
		public void onProviderEnabled(String provider) {
			System.out.println("onProviderEnabled");
		}
		
		@Override
		public void onProviderDisabled(String provider) {
			System.out.println("onProviderDisabled");
		}
		
		@Override
		public void onLocationChanged(Location location) {
			String text = "[locationListener] : getLatitude : " + location.getLatitude() + " getLongitude : " + location.getLongitude();
			
			System.out.println(text);
		}
	};
}

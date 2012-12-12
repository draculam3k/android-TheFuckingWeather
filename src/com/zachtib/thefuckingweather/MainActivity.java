package com.zachtib.thefuckingweather;

import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		displayTheFuckingWeather();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	public void displayTheFuckingWeather() {
		LocationManager lmngr = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);
		Geocoder geocoder = new Geocoder(this, Locale.getDefault());
		Location location = lmngr
				.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

		Address address = null;

		try {
			address = geocoder.getFromLocation(location.getLatitude(),
					location.getLongitude(), 1).get(0);
			String zip = address.getPostalCode();
			GetTheFuckingWeatherTask task = new GetTheFuckingWeatherTask();
			task.execute(zip);
			
			TheFuckingWeather tfw = task.get();
			
			setContentView(R.layout.activity_result);
			
			((TextView) findViewById(R.id.degrees)).setText(tfw.getTemperature() + "\u00B0!?");
			((TextView) findViewById(R.id.remark)).setText(tfw.getRemark());
			((TextView) findViewById(R.id.flavor)).setText(tfw.getFlavor());
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}

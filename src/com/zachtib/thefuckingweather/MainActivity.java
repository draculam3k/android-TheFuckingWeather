package com.zachtib.thefuckingweather;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Locale;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	protected class GetTheFuckingWeatherTask extends
			AsyncTask<String, Void, TheFuckingWeather> {

		private static final String TAG = "TFW.GetTheFuckingWeatherTask";
		private static final String THE_FUCKING_URL = "http://thefuckingweather.com/?where=";

		@Override
		protected TheFuckingWeather doInBackground(String... params) {
			String zip = params[0];
			Log.d(TAG, "Got a request for " + zip);

			TheFuckingWeather result = new TheFuckingWeather(null, null, null);

			HttpClient client = new DefaultHttpClient();
			HttpGet get = new HttpGet(THE_FUCKING_URL + zip);
			HttpResponse response = null;
			try {
				response = client.execute(get);

				BufferedReader in = new BufferedReader(new InputStreamReader(
						response.getEntity().getContent()));
				StringBuffer sb = new StringBuffer("");
				String line = "";
				String NL = System.getProperty("line.separator");

				while ((line = in.readLine()) != null) {
					sb.append(line + NL);
				}

				String html = sb.toString();

				Document doc = Jsoup.parse(html);

				String temp = doc.getElementsByClass("temperature").first()
						.html();
				String remark = doc.getElementsByClass("remark").first().html();
				String flavor = doc.getElementsByClass("flavor").first().html();

				result = new TheFuckingWeather(temp, remark, flavor);
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return result;
		}

		@Override
		protected void onPostExecute(TheFuckingWeather result) {
			showTheFuckingWeather(result);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onStart() {
		super.onStart();
		getTheFuckingWeather();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	public void getTheFuckingWeather() {
		setContentView(R.layout.activity_main);

		LocationManager lmngr = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);
		Geocoder geocoder = new Geocoder(this, Locale.getDefault());
		Location location = lmngr
				.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

		if (location == null) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(R.string.couldnt_determine_location)
					.setTitle(R.string.dialog_alert).setNeutralButton(R.string.dialog_alert,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();

						}
					});
			AlertDialog dialog = builder.create();
			dialog.show();
			return;
		}

		Address address = null;

		try {
			address = geocoder.getFromLocation(location.getLatitude(),
					location.getLongitude(), 1).get(0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String zip = address.getPostalCode();
		(new GetTheFuckingWeatherTask()).execute(zip);

	}

	public void showTheFuckingWeather(TheFuckingWeather weather) {
		setContentView(R.layout.activity_result);

		((TextView) findViewById(R.id.degrees)).setText(Html.fromHtml("<i>"
				+ weather.getTemperature() + "\u00B0?!</i>"));
		((TextView) findViewById(R.id.remark)).setText(Html.fromHtml(weather
				.getRemark()));
		((TextView) findViewById(R.id.flavor)).setText(Html.fromHtml(weather
				.getFlavor()));
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_refresh:
			getTheFuckingWeather();
			return true;
		case R.id.menu_settings:
			Toast.makeText(getApplicationContext(), "Not yet implemented",
					Toast.LENGTH_SHORT).show();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}

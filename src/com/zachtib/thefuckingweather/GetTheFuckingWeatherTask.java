package com.zachtib.thefuckingweather;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class GetTheFuckingWeatherTask extends
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
			
			String temp = doc.getElementsByClass("temperature").first().html();
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

}

package net.esperia.application;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import net.esperia.application.backend.AlarmReceiver;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class VoteActivity extends Activity {

	WebView mWebView = null;
	Context context = null;
	private final boolean IS_DEBUG = false;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.vote);

		//CookieSyncManager.createInstance(context);

		this.context = this;

		mWebView = (WebView) findViewById(R.id.voteWebView);

		mWebView.setWebViewClient(new MyWevView());
		mWebView.getSettings().setJavaScriptEnabled(true);


		mWebView.clearHistory();
		mWebView.clearFormData();
		mWebView.clearCache(true);
		mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);



		mWebView.addJavascriptInterface(new JavaScriptInterface(), "Android");

		Map<String, String> extraHeaders = new HashMap<String, String>();
		extraHeaders.put("referer", getString(R.string.referer));

		mWebView.loadUrl(getString(R.string.voteUrl),extraHeaders);

	}



	@Override
	protected void onResume() {
		//CookieSyncManager.getInstance().startSync();
		super.onResume();
	}
	@Override
	protected void onPause() {
		//CookieSyncManager.getInstance().stopSync();
		super.onPause();
	}



	private class MyWevView extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			Map<String, String> extraHeaders = new HashMap<String, String>();
			extraHeaders.put("referer", view.getUrl());
			view.loadUrl(url, extraHeaders);
			return true;
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			if(url.equalsIgnoreCase(getString(R.string.voteUrl))) {
				view.loadUrl("javascript:window.scrollTo(300,860);");//déplace
				if(!IS_DEBUG)view.loadUrl("javascript:if(document.getElementById('topsite').innerHTML.indexOf('Merci !')=>0)Android.validateVote();");//si le vote est validé, on fait une action.
				else view.loadUrl("javascript:if(true)Android.validateVote();");
			}
			//CookieSyncManager.getInstance().sync();
			super.onPageFinished(view, url);
		}
	}
	public class JavaScriptInterface {

		/** Instantiate the interface and set the context */
		JavaScriptInterface() {
		}


		/**
		 * Appelé quand le vote s'est bien effectué, pour relancer le timer.
		 */
		public void validateVote() {

			// get a Calendar object with current time
			Calendar cal = Calendar.getInstance();
			// add 5 minutes to the calendar object
			if(!IS_DEBUG)cal.add(Calendar.HOUR, 24);
			else cal.add(Calendar.MINUTE, 1);
			Intent intent = new Intent(context, AlarmReceiver.class);
			// In reality, you would want to have a static variable for the request code instead of 192837
			PendingIntent sender = PendingIntent.getBroadcast(context, 192837, intent, PendingIntent.FLAG_UPDATE_CURRENT);

			// Get the AlarmManager service
			AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
			am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), sender);

			SharedPreferences settings = getPreferences(MODE_PRIVATE);
			SharedPreferences.Editor editor = settings.edit();
			editor.putLong("nextVoteTime", cal.getTimeInMillis());
			editor.commit();


			if(IS_DEBUG)Log.d("Vote","Timer lancé !");
		}
	}
}

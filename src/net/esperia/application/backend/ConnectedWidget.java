package net.esperia.application.backend;

import org.json.JSONException;

import net.esperia.application.R;
import net.esperia.application.listConnected.ServerStatusObject;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

public class ConnectedWidget extends AppWidgetProvider {

	public static String ACTION_WIDGET_RECEIVER = "ActionReceiverWidget";
	private final String TAG = "EsperiaWidget";

	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		final int N = appWidgetIds.length;


		// Perform this loop procedure for each App Widget that belongs to this provider
		for (int i=0; i<N; i++) {
			int appWidgetId = appWidgetIds[i];

			Log.d(TAG, "onUpdateonUpdateonUpdateonUpdate"+appWidgetId);
			/*
			// Create an Intent to launch ExampleActivity
			Intent intent = new Intent(context, EsperiaActivity.class);
			PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

			// Get the layout for the App Widget and attach an on-click listener
			// to the button
			RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.connectedwidget);
			views.setOnClickPendingIntent(R.id.widget, pendingIntent);
			 */
			RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.connectedwidget);
			Intent active = new Intent(context, ConnectedWidget.class);
			active.setAction(ACTION_WIDGET_RECEIVER);
			active.putExtra("widgetId", appWidgetId);
			PendingIntent actionPendingIntent = PendingIntent.getBroadcast(context, appWidgetId, active, PendingIntent.FLAG_UPDATE_CURRENT);

			/*Intent intent = new Intent(context, ConnectedWidget.class);
			PendingIntent actionPendingIntent = PendingIntent.getActivity(context, appWidgetId, intent, PendingIntent.FLAG_UPDATE_CURRENT );
			 */
			views.setOnClickPendingIntent(R.id.widget, actionPendingIntent);

			// Tell the AppWidgetManager to perform an update on the current app widget
			updateWidget(context, appWidgetId);
			appWidgetManager.updateAppWidget(appWidgetId, views);
			//updateWidget(context, appWidgetId);
		}
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d(TAG,"Message reçu par ici ! : "+intent.getAction());
		// v1.5 fix that doesn't call onDelete Action
		final String action = intent.getAction();
		if (AppWidgetManager.ACTION_APPWIDGET_DELETED.equals(action)) {
			final int appWidgetId = intent.getExtras().getInt(
					AppWidgetManager.EXTRA_APPWIDGET_ID,
					AppWidgetManager.INVALID_APPWIDGET_ID);
			if (appWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
				this.onDeleted(context, new int[] { appWidgetId });
			}
		} else {
			// check, if our Action was called
			if (intent.getAction().equals(ACTION_WIDGET_RECEIVER)) {
				int appWidgetId = -1;
				try {
					appWidgetId = intent.getIntExtra("widgetId",-1);
				} catch (NullPointerException e) {
					Log.e(TAG, "msg = null");
				}
				Log.d(TAG, "lalalalalalalalalala : "+appWidgetId);
				/*Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
				PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, 0);
				NotificationManager notificationManager =
						(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
				Notification noty = new Notification(R.drawable.icone, "Button 1 clicked",
						System.currentTimeMillis());
				noty.setLatestEventInfo(context, "Notice", "aaaaaaaaaaa", contentIntent);
				notificationManager.notify(1, noty);*/

				/*RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.connectedwidget);
				views.setTextViewText(R.id.textView, String.valueOf(getConnectedNumber()));
				views.setViewVisibility(R.id.textView, View.VISIBLE);
				views.setViewVisibility(R.id.progressBar, View.GONE);

				ConnectedWidget.mAppWidManager.updateAppWidget(appWidgetId, views);*/
				updateWidget(context, appWidgetId);
			}
			super.onReceive(context, intent);
		}
	}

	private void updateWidget(Context context, int appWidgetId) {
		RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.connectedwidget);

		AppWidgetManager mAppWidManager = AppWidgetManager.getInstance(context);

		views.setViewVisibility(R.id.textView, View.GONE);
		views.setViewVisibility(R.id.progressBar, View.VISIBLE);
		mAppWidManager.updateAppWidget(appWidgetId, views);
		//maintenant on mets à jour le nombre et on réaffichera après
		
		String connected = "";
		int connectedNumber = getConnectedNumber();
		if(connectedNumber < 0) {
			connected = "OFF";
		} else {
			connected = String.valueOf(connectedNumber);
		}
		
		views.setTextViewText(R.id.textView, connected);
		views.setViewVisibility(R.id.textView, View.VISIBLE);
		views.setViewVisibility(R.id.progressBar, View.GONE);

		mAppWidManager.updateAppWidget(appWidgetId, views);
	}

	private int getConnectedNumber() {

		/*String page = executeHttpGet();


		//offline ?
		Pattern offlinePattern = Pattern.compile("\\<em class=\\\"offline\\\"\\>");
		Matcher offlineMatcher = offlinePattern.matcher(page);
		if(offlineMatcher.matches()) {
			return -1;
		} else {

			int connected = -1;
			String pattern = ".*avec <strong>([0-9]*)</strong> joueurs.*";
			// compilation de la regex
			Pattern p = Pattern.compile(".*avec <strong>(\\d*)</strong>.*");
			// création d’un moteur de recherche
			//Matcher m = p.matcher("Le serveur est <em class=\"online\">en ligne</em><span title=\"Herodion, gametm, rorocola, xJija, MistaJim, loulou68, Vico74, Diabl, Tyren72\">, avec <strong>9</strong> joueurs connectés</span>.");
			Matcher m = p.matcher(page);
			// lancement de la recherche de toutes les occurrences
			boolean b = m.matches();
			// si recherche fructueuse
			if(b) {
				try {
					connected = Integer.parseInt(m.group(1));
				} catch (NumberFormatException e) {
					connected = -1;
				}
			} else {
				Log.d(TAG, "pattern = "+pattern);
				Log.d(TAG,"page = "+page);
			}*/

		int connected = -1;
		ServerStatusObject status;
		try {
			status = new ServerStatusObject();
			if(status != null && status.isOnline())
				connected = status.getConnectedNumber();
		} catch (JSONException e) {
			
		}

		return connected;//(int)Math.round(Math.random()*100);
	}


	/*private String executeHttpGet() {
		BufferedReader in = null;
		String result = "";
		try {
			HttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet();
			request.setURI(new URI("http://www.esperia-rp.net/server_status_ajax.php"));
			HttpResponse response = client.execute(request);
			in = new BufferedReader
					(new InputStreamReader(response.getEntity().getContent()));
			StringBuffer sb = new StringBuffer("");
			String line = "";
			String NL = System.getProperty("line.separator");
			while ((line = in.readLine()) != null) {
				//sb.append(line + NL);
				sb.append(line);
			}
			in.close();
			String page = sb.toString();
			result = page;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return result;
	}*/
}

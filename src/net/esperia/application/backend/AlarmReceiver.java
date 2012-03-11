package net.esperia.application.backend;

import net.esperia.application.R;
import net.esperia.application.VoteActivity;
import net.esperia.application.R.drawable;
import net.esperia.application.R.string;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {

	private static final int NOTIFICATION_ID = 1;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		showAppNotification(context);
	}

	/**
	 * The notification is the icon and associated expanded entry in the
	 * status bar.
	 */
	void showAppNotification(Context context) {
		// look up the notification manager service
		NotificationManager nm = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

		// The details of our fake message
		CharSequence from = context.getString(R.string.notification_title);
		CharSequence message = context.getString(R.string.notification_message);


		// The PendingIntent to launch our activity if the user selects this
		// notification.  Note the use of FLAG_CANCEL_CURRENT so that, if there
		// is already an active matching pending intent, cancel it and replace
		// it with the new array of Intents.
		//PendingIntent contentIntent = PendingIntent.getActivities(this, 0, makeMessageIntentStack(this, from, message), PendingIntent.FLAG_CANCEL_CURRENT);
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0, new Intent(context, VoteActivity.class), PendingIntent.FLAG_CANCEL_CURRENT);


		// The ticker text, this uses a formatted string so our message could be localized
		String tickerText = context.getString(R.string.notification_initial_message);

		// construct the Notification object.
		Notification notif = new Notification(R.drawable.notification_vote, tickerText,
				System.currentTimeMillis());

		// Set the info for the views that show in the notification panel.
		notif.setLatestEventInfo(context, from, message, contentIntent);

		// We'll have this notification do the default sound, vibration, and led.
		// Note that if you want any of these behaviors, you should always have
		// a preference for the user to turn them off.
		notif.defaults = Notification.DEFAULT_ALL;


		notif.flags |= Notification.FLAG_AUTO_CANCEL;

		// Note that we use R.layout.incoming_message_panel as the ID for
		// the notification.  It could be any integer you want, but we use
		// the convention of using a resource id for a string related to
		// the notification.  It will always be a unique number within your
		// application.
		nm.notify(NOTIFICATION_ID, notif);
	}

}

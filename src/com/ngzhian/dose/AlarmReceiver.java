package com.ngzhian.dose;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		StringBuilder sb = new StringBuilder();
		sb.append(" Take " + intent.getIntExtra("quantity", 1) + " "
				+ intent.getStringExtra("unit") + " of "
				+ intent.getStringExtra("name") + ".");

		NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
		builder.setContentTitle("Time to for a Dose!");
		builder.setContentText(sb);
		builder.setSmallIcon(R.drawable.ic_launcher);
		//builder.setSmallIcon(android.R.drawable.ic_lock_lock);
		Intent resultIntent = new Intent(context, MainActivity.class);
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
		// Adds the back stack for the Intent (but not the Intent itself)
		stackBuilder.addParentStack(MainActivity.class);
		// Adds the Intent that starts the Activity to the top of the stack
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent =
		        stackBuilder.getPendingIntent(
		            0,
		            PendingIntent.FLAG_UPDATE_CURRENT
		        );
		builder.setContentIntent(resultPendingIntent);
		Notification notification = builder.build();
		NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		nm.notify(intent.getIntExtra("id", 0), notification);
		Log.d("dose", "Fired: " + intent.getIntExtra("quantity", 1)
				+ intent.getStringExtra("unit") + " of "
				+ intent.getStringExtra("name") + ".");
	}
}

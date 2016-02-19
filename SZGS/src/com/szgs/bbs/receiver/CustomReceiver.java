package com.szgs.bbs.receiver;

import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore.Audio;
import android.support.v4.app.NotificationCompat;

import com.avos.avoscloud.AVOSCloud;
import com.szgs.bbs.R;
import com.szgs.bbs.common.util.SharedPranceUtils;
import com.szgs.bbs.mine.NotificationActivity;

public class CustomReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		try {
			 if (intent.getAction().equals("com.szgs.answer")) {
			JSONObject json = new JSONObject(intent.getExtras().getString(
					"com.avos.avoscloud.Data"));
			final String message = json.getString("alert");
			Intent resultIntent = new Intent(AVOSCloud.applicationContext,
					NotificationActivity.class);
			PendingIntent pendingIntent = PendingIntent.getActivity(
					AVOSCloud.applicationContext, 0, resultIntent,
					PendingIntent.FLAG_UPDATE_CURRENT);
			NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
					AVOSCloud.applicationContext)
					.setSmallIcon(R.drawable.app_icon)
					.setDefaults(Notification.DEFAULT_SOUND)
					.setContentTitle(
							AVOSCloud.applicationContext.getResources()
									.getString(R.string.app_name))
					.setContentText(message).setTicker(message);
			mBuilder.setContentIntent(pendingIntent);
			mBuilder.setAutoCancel(true);
			int mNotificationId = 10086;
			NotificationManager mNotifyMgr = (NotificationManager) AVOSCloud.applicationContext
					.getSystemService(Context.NOTIFICATION_SERVICE);
			mNotifyMgr.notify(mNotificationId, mBuilder.build());
			SharedPranceUtils.SaveBolDate(context, true, "hasnewmsg");
			Intent intent1 = new Intent();
		    intent1.setAction("action.refresh");   
		       context.sendBroadcast(intent1); 
			 }
		} catch (Exception e) {

		}
	}
}
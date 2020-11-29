package com.gtech.fishbangla.FIREBASE;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.gtech.fishbangla.Activity.Cart_Activity;
import com.gtech.fishbangla.Activity.Home_Page;
import com.gtech.fishbangla.Library.KeyWord;
import com.gtech.fishbangla.Library.Utility;
import com.gtech.fishbangla.R;

import java.net.URL;
import java.util.Map;

public class FISHBANGLA_FIREBASE extends FirebaseMessagingService {

    private static final String TAG = "FISHBANGLA";

    Utility utility = new Utility(this);

    NotificationManager notificationManager;


    NotificationUtils notificationUtils = new NotificationUtils(this);

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages
        // are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data
        // messages are the type
        // traditionally used with GCM. Notification messages are only received here in
        // onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated
        // notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages
        // containing both notification
        // and data payloads are treated as notification messages. The Firebase console always
        // sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            Map<String, String> data = remoteMessage.getData();
            String title = data.get("title");
            String body = data.get("body");
            String image_url = data.get("image_url");
            String big_text = data.get("big_text");
            String metadataBrowse = data.get("metadataBrowse");
            String metadata = data.get("metadata");
            String small_image = data.get("icon_path");
            Log.d(TAG, "From: " + remoteMessage.getFrom());

            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            //sendNotification(title, msg, value1, value2);
            //createNotification(title);
            if (small_image != null && !TextUtils.isEmpty(small_image)) {
                notificationUtils.setNotification(title, body,metadataBrowse,metadata, small_image);
            } else {
                SHOW_NOTIFICATION(title, body, image_url, big_text, metadataBrowse, metadata);
            }
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
        //sendNotification("FISHBANGLA");
        //sendNotification(title, msg, value1, value2);
    }
    // [END receive_message]


    // [START on_new_token]

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    @Override
    public void onNewToken(String token) {

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        // If you want to send messages to this application instance or
                        // manage this apps subscriptions on the server side, send the
                        // Instance ID token to your app server.
                        sendRegistrationToServer(token);
                    }
                });
        Log.d(TAG, "Refreshed token: " + token);
    }

    // [END on_new_token]
    private void sendRegistrationToServer(String token) {
        utility.setFirebaseToken(token);
    }

    public void SHOW_NOTIFICATION(String title, String body, String image_url, String big_text, String metadataBrowse, String metadata) {
        try {
            createNotificationChannel();
            int notifyId = (int) System.currentTimeMillis();
            Intent intent = new Intent(this, Home_Page.class);
            if (metadataBrowse != null && !TextUtils.isEmpty(metadataBrowse) && metadata != null && !TextUtils.isEmpty(metadata)) {
                utility.logger("paisi");
                intent.putExtra("NOTIFICATION", "yes");
                intent.putExtra("metadataBrowse", metadataBrowse);
                intent.putExtra("metadata", metadata);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, notifyId, intent, 0);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, KeyWord.NOTIFICATION_CHANNEL_NAME)
                    .setContentTitle(title)
                    .setSmallIcon(R.drawable.ic_logo_png)
                    .setContentText(body)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setTicker(title)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_logo_png))
                    /*.setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(body))*/
                    /*.setStyle(new NotificationCompat.BigPictureStyle()
                            .bigPicture(*//*BitmapFactory.decodeResource(getResources(), R.drawable.ribbon)*//*Glide
                                    .with(this)
                                    .asBitmap()
                                    .load("http://116.212.109.34:9090/content/resources/images/banner/3/20200318_9.jpg")
                                    .submit()
                                    .get())
                            .bigLargeIcon(null))*/
                    .setPriority(Notification.PRIORITY_MAX);
            if (image_url != null && !TextUtils.isEmpty(image_url)) {
                builder.setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(Glide.with(this).asBitmap().load(image_url).submit().get()));
            } else if (big_text != null && !TextUtils.isEmpty(big_text) && big_text.equalsIgnoreCase(KeyWord.YES)) {
                builder.setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(body));
            }
            notificationManager.notify(notifyId, builder.build());

        } catch (Exception e) {
            Log.d("Error Line Number", Log.getStackTraceString(e));
        }
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.app_name);
            String description = getString(R.string.app_name);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(KeyWord.NOTIFICATION_CHANNEL_NAME, name, importance);
            channel.setDescription(description);
            channel.enableVibration(true);
            channel.setLightColor(getColor(R.color.app_red));
            channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            notificationManager.createNotificationChannel(channel);
        }
    }
}

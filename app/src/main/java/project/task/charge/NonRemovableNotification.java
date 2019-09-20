
package project.task.charge;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class NonRemovableNotification extends Activity {
    Context context;
    final int NOTIFICATION_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = NonRemovableNotification.this;
        // create notification builder object
        Notification.Builder builder = new Notification.Builder(context);
        builder.setContentText("there is a demo message").setContentTitle(
                context.getString(R.string.app_name));
        builder.setSmallIcon(R.drawable.ico_completed);
        // make the intent object
        Intent secondActivityIntent = new Intent(NonRemovableNotification.this,
                tasks.class);
        // pending intent
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                secondActivityIntent, 0);

        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(false);
        Notification notification = builder.build();
        // this is the main thing to do to make a non removable notification
        notification.flags |= Notification.FLAG_NO_CLEAR;
        NotificationManager manager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(NOTIFICATION_ID, notification);

    }
}
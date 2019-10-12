package project.task.charge.Notification;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.IBinder;

import androidx.annotation.Nullable;

import java.util.concurrent.atomic.AtomicBoolean;

import project.task.charge.MainActivity;

public class notification extends Service {

    private static final int NOTIFICATION_ID = 1;
    public static final String NOTIFICATION_CHANNEL_ID = "wigle_notification_1";

    private GuardThread guardThread;
    private final AtomicBoolean done = new AtomicBoolean( false );
    private Bitmap largeIcon = null;
    // Binder given to clients
//    private final IBinder wigleServiceBinder = new WigleServiceBinder();

    private class GuardThread extends Thread {
        GuardThread() {
        }

        @Override
        public void run() {
            Thread.currentThread().setName( "GuardThread-" + Thread.currentThread().getName() );
            while ( ! done.get() ) {
//                setupNotification();
            }
        }
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

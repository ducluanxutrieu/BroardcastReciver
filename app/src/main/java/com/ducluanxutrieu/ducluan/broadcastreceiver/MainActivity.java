package com.ducluanxutrieu.ducluan.broadcastreceiver;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    int a = 0;
    TextView textView;
    MyBroadcastReceiver receiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Intent intent = new Intent(this, OnBatteryChange.class);
//        startActivity(intent);

        textView = findViewById(R.id.numberOfScreenOn);

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SCREEN_ON);
        SharedPreferences sharedPreferences = getSharedPreferences("BroadcastReceiverLuan", 0);
        a = Integer.parseInt(Objects.requireNonNull(sharedPreferences.getString("NumberOfScreenOn", "0")));
        sharedPreferences.edit().putString("NumberOfScreenOn", a + "").apply();
        textView.setText(a + "");

        //BroadcastReceiver br = new MyBroadcastReceiver();
        //IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        //filter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        //this.registerReceiver(br, filter);
        //receiver = new MyBroadcastReceiver();
        //receiver.abortBroadcast();
    }

    /*
        @Override
        protected void onStart() {
            super.onStart();
            IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
            this.registerReceiver(onScreen, filter);
        }
    */
    public class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("screen_on", "" + a);
            SharedPreferences sharedPreferences = getSharedPreferences("BroadcastReceiverLuan", 0);
            int a = Integer.parseInt(Objects.requireNonNull(sharedPreferences.getString("NumberOfScreenOn", "1")));
            a += intent.getIntExtra(Intent.ACTION_SCREEN_ON, 0);
            sharedPreferences.edit().putString("NumberOfScreenOn", a + "").apply();
            textView.setText(a + "a");

            Log.i("screen_on", "" + a);

            createNewNotifacation(context, a);
        }

        private void createNewNotifacation(Context context, int numberOfScreenOn) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
            builder.setSmallIcon(R.drawable.icon_notification)
                    .setContentTitle("Có thông báo!")
                    .setContentText("Bạn đã mở máy " + numberOfScreenOn + " lần.");

            Intent resultIntent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            builder.setContentIntent(pendingIntent);

            Uri uri = Uri.parse("android.resource://" + context.getPackageName() + "/" +R.raw.notification);
            builder.setSound(uri);

            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.notify(2411, builder.build());
        }
    }
}

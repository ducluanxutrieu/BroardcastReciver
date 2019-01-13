package com.ducluanxutrieu.ducluan.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
public class OnBatteryChange extends AppCompatActivity {
    private ProgressBar bar= null;
    private ImageView status= null;
    private TextView level= null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_battery_change);
        bar=findViewById(R.id.bar);
        status=findViewById(R.id.status);
        level=findViewById(R.id.level);
    }
    @Override
    public void onStart() {
        super.onStart();
        IntentFilter f=new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        this.registerReceiver(onBattery, f);
    }
    @Override
    public void onStop() {
        this.unregisterReceiver(onBattery);
        super.onStop();
    }
    BroadcastReceiver onBattery=new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            int pct = 100 * intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 1)
                            / intent.getIntExtra(BatteryManager.EXTRA_SCALE, 1);
            bar.setProgress(pct);
            level.setText(String.valueOf(pct));
            switch (intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1)) {
                case BatteryManager.BATTERY_STATUS_CHARGING:
                    status.setImageResource(R.drawable.battery_charging);
                    break;
                case BatteryManager.BATTERY_STATUS_FULL:
                    int plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
                    if (plugged == BatteryManager.BATTERY_PLUGGED_AC
                            || plugged == BatteryManager.BATTERY_PLUGGED_USB) {
                        status.setImageResource(R.drawable.battery_full);
                    }
                    else {
                    status.setImageResource(R.drawable.battery_unplugin);
                }
                break;
                default:
                    status.setImageResource(R.drawable.battery_unplugin);
                    break;
            }
        }
    };
}
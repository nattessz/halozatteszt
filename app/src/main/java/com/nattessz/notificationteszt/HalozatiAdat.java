package com.nattessz.notificationteszt;

import android.net.TrafficStats;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

public class HalozatiAdat extends MainActivity {
    private TextView letolt;
    private TextView feltolt;
    private Handler mHandler = new Handler();
    private final Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            long rxBytes = TrafficStats.getMobileRxBytes();
            int letoltadat = new Integer((int) ((rxBytes/1024)/1024));
            letolt.setText(letoltadat +" gigabyte");
            long txBytes = TrafficStats.getMobileTxBytes();
            int feltoltadat = new Integer((int) (txBytes/1024));
            feltolt.setText(feltoltadat+" megabyte");
            mHandler.postDelayed(mRunnable, 1000);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.halozat);

        letolt = findViewById(R.id.letolt);
        feltolt = findViewById(R.id.feltolt);
        mHandler.postDelayed(mRunnable, 1000);
    }
}

package com.nattessz.notificationteszt;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.StrictMode;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    private Button notificgomb;
    private  Button httpget;
    private TextView adatok;
    private EditText hostcim;
    private Button adatforg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //showNotification();
        adatforg = findViewById(R.id.newintent);
        adatforg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, HalozatiAdat.class));
            }
        });
        httpget = findViewById(R.id.httpget);
        hostcim = findViewById(R.id.hostcim);
        Toast.makeText(this, "Írj be egy érvényes host címet!", Toast.LENGTH_SHORT).show();
        adatok = findViewById(R.id.adatok);
        notificgomb = findViewById(R.id.notific);
        notificgomb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNotification();
                //getBitmapFromURL("http://www.kresz.com/");
            }
        });
        httpget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                socketDemo();
            }
        });

    }


    private void socketDemo(){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Socket socket = null;
        InputStream is = null;
        OutputStream os = null;
        String cim = new String(hostcim.getText().toString());
        try{
            socket = new Socket(String.valueOf(cim),80);
            socket.setSoTimeout(1000);
            //adatok küldése
            os = socket.getOutputStream();
            os.write("GET index.html HTTPS/1.0\n".getBytes());
            os.write("\n".getBytes());
            os.flush();
            //adatok fogadása
            is = socket.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "UTF-8");
            StringBuilder resultBuffer = new StringBuilder();
            int inChar;
            while ((inChar = is.read()) !=-1){resultBuffer.append((char) inChar);}
            final String result = resultBuffer.toString();
            //eredmény megjelenítése
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adatok.setText(result);
                }
            });
        }
        catch (IOException e){
            e.printStackTrace();
        }
        finally {
            if(os != null)
                try {
                    os.close();;
                } catch (IOException e) {e.printStackTrace();}
            if(is != null)
                try {
                    is.close();;
                } catch (IOException e) {e.printStackTrace();}
            if(socket != null)
                try {
                    socket.close();;
                } catch (IOException e) {e.printStackTrace();}
        }

    }

    public void showNotification() {
        PendingIntent pi = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);
        Resources r = getResources();

        Notification notification = new NotificationCompat.Builder(this)
                .setTicker(r.getString(R.string.notification_title))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(r.getString(R.string.notification_title))
                .setContentText((r.getString(R.string.notification_text)))
                .setStyle(new NotificationCompat.BigTextStyle())
                .setColor(Color.WHITE)
                .setContentIntent(pi)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .addAction(R.mipmap.ic_launcher,"Toast",pi)
                .build();


        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);
    }
}

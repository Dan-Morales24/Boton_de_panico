package com.example.botondepanico.CloudNotification;


import static android.content.ContentValues.TAG;

import android.os.Handler;
import android.os.Looper;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.botondepanico.Activities.MainActivity;
import com.example.botondepanico.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class Notifications extends FirebaseMessagingService {

    public static final String TAG ="Alertas";



    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if(remoteMessage.getNotification() !=null){

            String from = remoteMessage.getFrom();
            Log.d(TAG,"Mensaje recibido de: "+from);

            if(remoteMessage.getNotification() !=null){
                Log.d(TAG,"Titulo: "+remoteMessage.getNotification().getTitle());
                Log.d(TAG ,"Cuerpo"+remoteMessage.getNotification().getBody());

                Looper.prepare();
               // Toast.makeText(this, "Hola mundo", Toast.LENGTH_SHORT).show();
                LayoutInflater inflater =(LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
                View customToast = inflater.inflate(R.layout.notification_received,null);
                TextView txt =(TextView) customToast.findViewById(R.id.title);
                txt.setText(remoteMessage.getNotification().getTitle());
                Toast toast = new Toast(Notifications.this);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.setDuration(Toast.LENGTH_LONG);
                toast.setView(customToast);
                toast.show();


                Looper.loop();



            }


        }







    }
}

package com.example.aninterface.task.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;



public class Network {
    public static boolean isConeccted(Context context){
        ConnectivityManager manager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        return info != null && info.isConnectedOrConnecting() ? true : false;
    }
}

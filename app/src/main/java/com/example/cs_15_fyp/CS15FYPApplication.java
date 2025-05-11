package com.example.cs_15_fyp;

import android.app.Application;
import android.util.Log;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

public class CS15FYPApplication extends Application {
    private static final String TAG = "CS15FYPApplication";

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            // Check if Firebase is already initialized
            if (FirebaseApp.getApps(this).isEmpty()) {
                FirebaseApp.initializeApp(this);
                Log.d(TAG, "Firebase initialized successfully");
            } else {
                Log.d(TAG, "Firebase already initialized");
            }
        } catch (Exception e) {
            Log.e(TAG, "Error initializing Firebase", e);
        }
    }
} 
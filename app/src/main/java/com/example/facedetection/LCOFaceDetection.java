package com.example.facedetection;

import android.app.Application;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

public class LCOFaceDetection extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setApiKey("YOUR_API_KEY")
                .setApplicationId("YOUR_APP_ID")
                .build();
        FirebaseApp.initializeApp(this, options);
    }
}

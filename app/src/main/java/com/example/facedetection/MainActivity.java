package com.example.facedetection;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler;
import com.google.firebase.ml.vision.label.FirebaseVisionOnDeviceImageLabelerOptions;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private FirebaseVisionImageLabeler labeler;
    private ImageView imageViewCaptured;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the on-device image labeler here
        FirebaseVisionOnDeviceImageLabelerOptions options =
                new FirebaseVisionOnDeviceImageLabelerOptions.Builder()
                        .setConfidenceThreshold(0.7f)
                        .build();
        labeler = FirebaseVision.getInstance().getOnDeviceImageLabeler(options);

        Button btnOpenCamera = findViewById(R.id.btnOpenCamera);
        imageViewCaptured = findViewById(R.id.imageViewCaptured);

        btnOpenCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open the camera to capture the image
                dispatchTakePictureIntent();
            }
        });
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // Image captured, process the image using Firebase ML Kit
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            // Set the captured image to the ImageView
            imageViewCaptured.setImageBitmap(imageBitmap);

            // Convert the captured bitmap to a FirebaseVisionImage
            FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(imageBitmap);

            // Process the image using the labeler
            labeler.processImage(image)
                    .addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionImageLabel>>() {
                        @Override
                        public void onSuccess(List<FirebaseVisionImageLabel> labels) {
                            if (!labels.isEmpty()) {
                                // Get the label with the highest confidence
                                FirebaseVisionImageLabel label = labels.get(0);
                                String labelText = label.getText();
                                float confidence = label.getConfidence();

                                // Show the Result Dialog with the label text and confidence
                                showResultDialog("Detected Label: " + labelText + "\nConfidence: " + confidence);
                            } else {
                                showResultDialog("No labels detected.");
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Handle any errors that occurred during processing
                            Toast.makeText(MainActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void showResultDialog(String resultText) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        ResultDialog resultDialog = ResultDialog.newInstance(resultText);
        resultDialog.show(fragmentManager, "ResultDialog");
    }
}

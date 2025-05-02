package com.example.cs_15_fyp.utils;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class FirebaseStorageHelper {
    private static final String TAG = "FirebaseStorageHelper";
    private static FirebaseStorage storage;
    private static final String REVIEW_IMAGES_PATH = "review_images/";

    public static void initialize(Context context) {
        if (storage == null) {
            try {
                // Ensure Firebase is initialized
                if (FirebaseApp.getApps(context).isEmpty()) {
                    FirebaseApp.initializeApp(context);
                }
                
                storage = FirebaseStorage.getInstance();
                Log.d(TAG, "FirebaseStorage initialized successfully");
            } catch (Exception e) {
                Log.e(TAG, "Error initializing FirebaseStorage", e);
                throw new IllegalStateException("Failed to initialize FirebaseStorage", e);
            }
        }
    }

    public static CompletableFuture<List<String>> uploadReviewImages(List<Uri> imageUris) {
        if (storage == null) {
            CompletableFuture<List<String>> future = new CompletableFuture<>();
            future.completeExceptionally(new IllegalStateException("FirebaseStorage not initialized. Call initialize() first."));
            return future;
        }

        CompletableFuture<List<String>> future = new CompletableFuture<>();
        List<String> downloadUrls = new ArrayList<>();
        int totalImages = imageUris.size();
        final int[] uploadedCount = {0};

        for (Uri uri : imageUris) {
            String fileName = UUID.randomUUID().toString() + ".jpg";
            StorageReference imageRef = storage.getReference().child(REVIEW_IMAGES_PATH + fileName);

            imageRef.putFile(uri)
                .continueWithTask(task -> {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return imageRef.getDownloadUrl();
                })
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        downloadUrls.add(downloadUri.toString());
                        uploadedCount[0]++;

                        if (uploadedCount[0] == totalImages) {
                            future.complete(downloadUrls);
                        }
                    } else {
                        Log.e(TAG, "Error uploading image", task.getException());
                        future.completeExceptionally(task.getException());
                    }
                });
        }

        return future;
    }
} 
package com.voldy.blackrock.sms;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class SmsNotificationReceiver extends BroadcastReceiver {

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    @Override
    public void onReceive(Context context, Intent intent) {

        init();

        String action = intent.getAction();
        if ("ACTION_TAG_SELECTED".equals(action)) {
            String sender = intent.getStringExtra("sender");
            String messageBody = intent.getStringExtra("messageBody");
            String selectedTag = intent.getStringExtra("selectedTag");

            Toast.makeText(context, "" + selectedTag, Toast.LENGTH_SHORT).show();

            SmsModel smsModel = (SmsModel) intent.getSerializableExtra("smsModel");
            smsModel.setSmsCategory(selectedTag);

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(123);

            Log.d("SMS", "onReceive: " + firebaseAuth.getCurrentUser().getEmail());
            Log.d("SMS", "onReceive: " + smsModel.getSmsID());
            Log.d("SMS", "onReceive: " + smsModel.getSmsCategory());

            addDataToFirebase(smsModel);
        }
    }

    private void init() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    private void addDataToFirebase(SmsModel smsModel) {
        firebaseFirestore
                .collection("userDetails")
                .document(firebaseAuth.getCurrentUser().getEmail())
                .collection(smsModel.getSmsCategory())
                .document(smsModel.getSmsID())
                .set(smsModel)
                .addOnSuccessListener(v -> {
                    Log.d("SMS", "addDataToFirebase: data added" + smsModel.getSmsCategory() );
                    deleteMis(smsModel.getSmsID());
                }).addOnFailureListener(v -> {
                    Log.d("SMS", "addDataToFirebase: failed " + v.getMessage());
                });
    }

    private void deleteMis(String smsID) {
        firebaseFirestore
                .collection("userDetails")
                .document(firebaseAuth.getCurrentUser().getEmail())
                .collection("Miscellaneous")
                .document(smsID)
                .delete();
    }
}

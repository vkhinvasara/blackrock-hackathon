package com.voldy.blackrock.sms;


import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.voldy.blackrock.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SmsReceiver extends BroadcastReceiver {

    private ApiService apiService;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    @Override
    public void onReceive(Context context, Intent intent) {

        init();

        // Handle incoming SMS here
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            Object[] pdus = (Object[]) bundle.get("pdus");
            if (pdus != null) {
                for (Object pdu : pdus) {
                    SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdu);
                    String sender = smsMessage.getDisplayOriginatingAddress();
                    String messageBody = smsMessage.getMessageBody();

                    Log.d("TAG", "onReceive: " + messageBody);

                    SmsModel smsModel = new SmsModel();
                    smsModel.setSmsTimeStamp(System.currentTimeMillis());

                    smsModel.setSmsMerchantName(convertTimestamp(smsModel.getSmsTimeStamp()));

                    SmsData smsData = new SmsData(messageBody);

                    Call<Data> fetch_call = ApiControlllerEmployee
                            .getInstance()
                            .getFetchCurrentOrdersApi()
                            .getTransaction(smsData);


                    fetch_call.enqueue(new Callback<Data>() {
                        @Override
                        public void onResponse(Call<Data> call, Response<Data> response) {
                            Data data = response.body();
                            if (data != null && data.getAtype().equals("ACCOUNT") && data.getTtype().equals("debit")) {
                                if (data.getAmount() != null && !data.getAmount().isEmpty()) {
                                    smsModel.setSmsAmount((int) Double.parseDouble(data.getAmount()));
                                    smsModel.setSmsTransactionType(data.getTtype());
                                    showCustomNotification(context, sender, messageBody, smsModel);
                                } else {
                                    SmsResponse smsResponse = SMSParse.parseSMS(messageBody);
                                    if (smsResponse.amount != null) {
                                        smsModel.setSmsTransactionType(smsResponse.getTransactionType());
                                        smsModel.setSmsAmount((int) Double.parseDouble(smsResponse.getAmount()));
                                        showCustomNotification(context, sender, messageBody, smsModel);
                                    }
                                }
                            }

                            if (data != null && data.getAtype().equals("ACCOUNT") && data.getTtype().equals("credit")) {
                                smsModel.setSmsCategory("Salary");
                                addDataToFirebase(smsModel);
                            }
                        }

                        @Override
                        public void onFailure(Call<Data> call, Throwable t) {
                            Log.e("SmsReceiver", "Failed to fetch transaction data: " + t.getMessage());
                        }
                    });
//                    if(f==1){
//                        smsModel.setSmsAmount((int) Double.parseDouble(smsResponse.getAmount()));
//                    }
                }
            }
        }
    }

    private void init() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    // Method to show custom notification
    private void showCustomNotification(Context context, String sender, String messageBody, SmsModel smsModel) {
        Log.d("SMS", "onResponse: in notification");

        addDataToFirebase(smsModel);

        Intent submitIntent = new Intent(context, SmsNotificationReceiver.class);
        submitIntent.setAction("ACTION_TAG_SELECTED"); // Custom action for tag selection
        submitIntent.putExtra("sender", sender);
        submitIntent.putExtra("messageBody", messageBody);
        submitIntent.putExtra("smsModel", smsModel);

        PendingIntent pendingIntentSalary = PendingIntent.getBroadcast(
                context,
                0,
                submitIntent.putExtra("selectedTag", "Salary"), // Example: Passing the selected tag
                PendingIntent.FLAG_UPDATE_CURRENT | getPendingIntentFlags()
        );

        PendingIntent pendingIntentFoodExpense = PendingIntent.getBroadcast(
                context,
                1,
                submitIntent.putExtra("selectedTag", "Food Expense"), // Example: Passing the selected tag
                PendingIntent.FLAG_UPDATE_CURRENT | getPendingIntentFlags()
        );

        PendingIntent pendingIntentHealthExpense = PendingIntent.getBroadcast(
                context,
                2,
                submitIntent.putExtra("selectedTag", "Health Expense"), // Example: Passing the selected tag
                PendingIntent.FLAG_UPDATE_CURRENT | getPendingIntentFlags()
        );

        PendingIntent pendingIntentLivingExpense = PendingIntent.getBroadcast(
                context,
                3,
                submitIntent.putExtra("selectedTag", "Living Expense"), // Example: Passing the selected tag
                PendingIntent.FLAG_UPDATE_CURRENT | getPendingIntentFlags()
        );

        PendingIntent pendingIntentTransportExpense = PendingIntent.getBroadcast(
                context,
                4,
                submitIntent.putExtra("selectedTag", "Transport Expense"), // Example: Passing the selected tag
                PendingIntent.FLAG_UPDATE_CURRENT | getPendingIntentFlags()
        );

        PendingIntent pendingIntentPayables = PendingIntent.getBroadcast(
                context,
                5,
                submitIntent.putExtra("selectedTag", "Payables"), // Example: Passing the selected tag
                PendingIntent.FLAG_UPDATE_CURRENT | getPendingIntentFlags()
        );

        @SuppressLint("RemoteViewLayout") RemoteViews notificationLayout = new RemoteViews(context.getPackageName(), R.layout.custom_notification);
        notificationLayout.setTextViewText(R.id.notification_title, "New SMS from " + sender);
        notificationLayout.setTextViewText(R.id.notification_text, messageBody);

        // Set PendingIntent for each RadioButton
        notificationLayout.setOnClickPendingIntent(R.id.radioSalary, pendingIntentSalary);
        notificationLayout.setOnClickPendingIntent(R.id.radioFoodExpense, pendingIntentFoodExpense);
        notificationLayout.setOnClickPendingIntent(R.id.radioHealthExpense, pendingIntentHealthExpense);
        notificationLayout.setOnClickPendingIntent(R.id.radioLivingExpense, pendingIntentLivingExpense);
        notificationLayout.setOnClickPendingIntent(R.id.radioTransportExpense, pendingIntentTransportExpense);
        notificationLayout.setOnClickPendingIntent(R.id.radioPayables, pendingIntentPayables);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, createNotificationChannel(context))
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentIntent(pendingIntentSalary) // Use one of the PendingIntents (example: pendingIntentSalary)
                .setCustomContentView(notificationLayout)
                .setCustomBigContentView(notificationLayout)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setAutoCancel(true); // Removes the notification when tapped

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(123, builder.build());


//        new android.os.Handler().postDelayed(() -> {
//            Log.d("TAG", "showCustomNotification: " + System.currentTimeMillis());
//            firebaseFirestore
//                    .collection("userDetails")
//                    .document(firebaseAuth.getCurrentUser().getEmail())
//                    .collection(smsModel.getSmsCategory())
//                    .document(smsModel.getSmsID())
//                    .set(smsModel)
//                    .addOnSuccessListener(v -> {
//                        Log.d("SMS", "addDataToFirebase: data added" + smsModel.getSmsCategory());
//                        notificationManager.cancel(123);
//                    }).addOnFailureListener(v -> {
//                        Log.d("SMS", "addDataToFirebase: failed " + v.getMessage());
//                    });
//        }, 10000);
    }

    private int getPendingIntentFlags() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            return PendingIntent.FLAG_MUTABLE;
        } else {
            return 0;
        }
    }

    private String createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "channel_id";
            String channelName = "Foreground Service Channel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
            return channelId;
        } else {
            return "";
        }
    }

    private void addDataToFirebase(SmsModel smsModel) {

        firebaseFirestore
                .collection("userDetails")
                .document(firebaseAuth.getCurrentUser().getEmail())
                .collection(smsModel.getSmsCategory())
                .document(smsModel.getSmsID())
                .set(smsModel)
                .addOnSuccessListener(v -> {
                    Log.d("SMS", "addDataToFirebase: data added" + smsModel.getSmsCategory());
                }).addOnFailureListener(v -> {
                    Log.d("SMS", "addDataToFirebase: failed " + v.getMessage());
                });
    }


    private String getSelectedCategoryFromPreferences(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        return prefs.getString("selected_category", "Default Category");
    }

    public String convertTimestamp(long timestamp) {
        // Create a Date object from the timestamp
        Date date = new Date(timestamp);

        // Define the desired format
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm a");

        // Format the date into the desired format
        String formattedDate = formatter.format(date);

        // Prepend "Transaction@"
        return "Transaction at :" + formattedDate;
    }

}
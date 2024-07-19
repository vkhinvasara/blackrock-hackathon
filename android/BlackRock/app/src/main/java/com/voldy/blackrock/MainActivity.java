package com.voldy.blackrock;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.voldy.blackrock.databinding.ActivityMainBinding;
import com.voldy.blackrock.sms.ApiControlllerEmployee;
import com.voldy.blackrock.sms.Data;
import com.voldy.blackrock.sms.SMSParse;
import com.voldy.blackrock.sms.Sms;
import com.voldy.blackrock.sms.SmsData;
import com.voldy.blackrock.sms.SmsParser;
import com.voldy.blackrock.sms.SmsParserResponse;
import com.voldy.blackrock.sms.SmsResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private static final int PERMISSION_REQUEST_CODE = 100;
    private List<Sms> smsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        settings();

        smsList = new ArrayList<>();

        checkAndRequestPermissions();
    }


    private void checkAndRequestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            boolean foregroundServicePermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.FOREGROUND_SERVICE) == PackageManager.PERMISSION_GRANTED;
            boolean postNotificationsPermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED;
            boolean receiveSmsPermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED;
            boolean readSmsPermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED;

            if (!foregroundServicePermission || !postNotificationsPermission || !receiveSmsPermission || !readSmsPermission) {
                Log.d("SMS", "checkAndRequestPermissions: All not accepted");
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{
                                android.Manifest.permission.FOREGROUND_SERVICE,
                                android.Manifest.permission.POST_NOTIFICATIONS,
                                android.Manifest.permission.RECEIVE_SMS,
                                Manifest.permission.READ_SMS
                        },
                        PERMISSION_REQUEST_CODE
                );
            } else {
                Log.d("SMS", "checkAndRequestPermissions: All accepted");
                new FetchSmsTask().execute();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE) {
            boolean allPermissionsGranted = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allPermissionsGranted = false;
                    break;
                }
            }

            if (!allPermissionsGranted) {
                Log.d("SMS", "checkAndRequestPermissions: All not not accepted");
            } else {
                Log.d("SMS", "checkAndRequestPermissions: All not accepted > accepted");
                new FetchSmsTask().execute();
            }
        }
    }

    private class FetchSmsTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
//            getAllSms();
            return null;
        }

        private void getAllSms() {

            ContentResolver contentResolver = getContentResolver();
            Uri uri = Telephony.Sms.Inbox.CONTENT_URI;
            String sortOrder = Telephony.Sms.DEFAULT_SORT_ORDER;
            Cursor cursor = contentResolver.query(uri, null, null, null, sortOrder);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        String address = cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.ADDRESS));
                        String body = cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.BODY));
                        String date = cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.DATE));

                        if (address != null && body != null && date != null) {
                            smsList.add(new Sms(address, body, date));
                        }
                    } while (cursor.moveToNext());

                    List<Sms> filteredTransactions = smsList.stream()
                            .filter(s -> ((s.getBody().toLowerCase().contains("credited")
                                            || s.getBody().toLowerCase().contains("debited")
                                    )
                                            && (s.getBody().toLowerCase().contains("inr")
                                            || s.getBody().toLowerCase().contains("rs")
                                            || s.getBody().toLowerCase().contains("upi")
                                    )) && (
                                            !s.getBody().toLowerCase().contains("bonus")
                                                    || !s.getBody().toLowerCase().contains("download")
                                                    || !s.getBody().toLowerCase().contains("claim")
                                                    || !s.getBody().toLowerCase().contains("congrats")
                                                    || !s.getBody().toLowerCase().contains("%")
                                    )
                            )
                            .collect(Collectors.toList());

                    for (Sms i : filteredTransactions) {
                        SmsParserResponse smsParserResponse = SmsParser.parseSms(i.getBody());
//                        Log.d("TAG", "getAllSms: " + i.getBody());

                        Call<Data> fetch_call = ApiControlllerEmployee
                                .getInstance()
                                .getFetchCurrentOrdersApi()
                                .getTransaction(new SmsData(i.getBody()));


                        fetch_call.enqueue(new Callback<Data>() {
                            @Override
                            public void onResponse(Call<Data> call, Response<Data> response) {
                                Data data = response.body();
                                if(data!=null){
                                    try {
                                        Log.d("TAG", "onResponse: ");
                                    }catch (Exception e){
                                        Log.d("TAG", "onResponse: " + e.getMessage());
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<Data> call, Throwable t) {
                                Log.e("TAG", "Failed to fetch transaction data: " + t.getMessage());
                            }
                        });

                    }
                } else {
                    Log.d("SMS", "No SMS found.");
                }
                cursor.close();
            } else {
                Log.d("SMS", "Cursor is null.");
            }
        }
    }

    private void settings() {
        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

}
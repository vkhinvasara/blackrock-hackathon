package com.voldy.blackrock.ui.home;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.voldy.blackrock.databinding.ActivityShowTransactionBinding;
import com.voldy.blackrock.sms.SmsModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ShowTransactionsActivity extends AppCompatActivity {

    private ActivityShowTransactionBinding binding;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    private String categoryName;

    private List<SmsModel> smsList;

    private TransactionAdapter adapter;
    private int totalSpend = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityShowTransactionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();

        categoryName = getIntent().getStringExtra("categoryName");

        binding.tvCategory.setText(categoryName);

        Log.d("categoryName", "onCreate: " + categoryName);

        smsList = new ArrayList<>();
        adapter = new TransactionAdapter(this, smsList, getSupportFragmentManager());
        binding.rvData.setLayoutManager(new LinearLayoutManager(this));
        binding.rvData.setAdapter(adapter);

        setupLineChart();
        getData();
        Log.d("TAG", "onCreate: " + Integer.toString(totalSpend));
    }

    private void getData() {
        firebaseFirestore
                .collection("userDetails")
                .document(firebaseAuth.getCurrentUser().getEmail())
                .collection(categoryName)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        SmsModel smsModel = document.toObject(SmsModel.class);
                        totalSpend = totalSpend + smsModel.getSmsAmount();
                        Log.d("TAG", "getData: " + smsModel.getSmsAmount());
                        smsList.add(smsModel);
                    }
                    adapter.notifyDataSetChanged();
                    binding.tvTotalSpend.setText(Integer.toString(totalSpend));
                    loadChartData();
                })
                .addOnFailureListener(e -> {
                    Log.d("SMS", "getData: " + e.getMessage());
                });
    }

    private void setupLineChart() {
        binding.lineChart.getDescription().setEnabled(false);
        binding.lineChart.setDrawGridBackground(false);

        XAxis xAxis = binding.lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new DateValueFormatter());

        YAxis leftAxis = binding.lineChart.getAxisLeft();
        leftAxis.setDrawGridLines(true);

        YAxis rightAxis = binding.lineChart.getAxisRight();
        rightAxis.setEnabled(false);
    }

    private class DateValueFormatter extends ValueFormatter {
        private final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd", Locale.US);

        @Override
        public String getFormattedValue(float value) {
            return dateFormat.format(new Date((long) value));
        }
    }

    private void loadChartData() {
        ArrayList<Entry> entries = new ArrayList<>();
        for (SmsModel sms : smsList) {
            float xValue = sms.getSmsTimeStamp();
            float yValue = sms.getSmsAmount();
            entries.add(new Entry(xValue, yValue));
        }

        LineDataSet dataSet = new LineDataSet(entries, "Transaction Amount");
        dataSet.setColor(Color.BLUE);
        dataSet.setValueTextColor(Color.BLACK);

        LineData lineData = new LineData(dataSet);
        binding.lineChart.setData(lineData);
        binding.lineChart.invalidate();
    }

    private void init() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
    }
}

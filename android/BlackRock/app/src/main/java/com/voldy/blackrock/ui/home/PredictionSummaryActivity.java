package com.voldy.blackrock.ui.home;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.voldy.blackrock.databinding.ActivityPredictionSummaryLayoutBinding;
import com.voldy.blackrock.sms.SmsModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PredictionSummaryActivity extends AppCompatActivity {

    ActivityPredictionSummaryLayoutBinding binding;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    Map<String, List<SmsModel>> mp;

    private int collectionsFetched = 0;
    private static final int TOTAL_COLLECTIONS = 6;

    int income, expense;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPredictionSummaryLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();

        income = 0;
        expense = 0;

        mp = new HashMap<>();
        mp.put("Food Expense", new ArrayList<>());
        mp.put("Health Expense", new ArrayList<>());
        mp.put("Living Expense", new ArrayList<>());
        mp.put("Transport Expense", new ArrayList<>());
        mp.put("Payables", new ArrayList<>());
        mp.put("Miscellaneous", new ArrayList<>());
        mp.put("Salary", new ArrayList<>());

        setupPieChart();
        fetchDataAndLoadPieChart();
    }

    private void setupPieChart() {
        PieChart pieChart = binding.pieChart;

        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleRadius(58f);
        pieChart.setHoleRadius(58f);
        pieChart.setDrawEntryLabels(true);
        pieChart.setEntryLabelTextSize(12f);
        pieChart.setEntryLabelColor(Color.BLACK);

        pieChart.getDescription().setEnabled(false);
    }

    private int getSum(String key){
        int sm = 0;
        List<SmsModel> res = mp.get(key);
        if (res != null) {
            for(SmsModel i: res){
                sm += i.getSmsAmount();
            }
        }
        return sm;
    }

    public void getData(String collectionName){
        firebaseFirestore
                .collection("userDetails")
                .document(firebaseAuth.getCurrentUser().getEmail())
                .collection(collectionName)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<SmsModel> result = new ArrayList<>();
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            SmsModel smsModel = document.toObject(SmsModel.class);
                            result.add(smsModel);
                        }

                        Log.d("TAG", "onSuccess: " + collectionName + " " + result.size());
                        mp.put(collectionName, result);
                        collectionsFetched++;

                        if (collectionsFetched == TOTAL_COLLECTIONS) {
                            loadPieChartData();
                        }
                    }
                });
    }

    private void fetchDataAndLoadPieChart() {
        getData("Food Expense");
        getData("Health Expense");
        getData("Living Expense");
        getData("Transport Expense");
        getData("Payables");
        getData("Miscellaneous");
        getData("Salary");
    }

    private void loadPieChartData() {
        List<PieEntry> pieEntries = new ArrayList<>();

        expense += (getSum("Food Expense") + getSum("Health Expense") + getSum("Living Expense") + getSum("Transport Expense") + getSum("Payables") + getSum("Miscellaneous"));
        income = getSum("Salary");
        binding.income.setText("+" +Integer.toString(income));
        binding.expense.setText("-" + Integer.toString(expense));

        pieEntries.add(new PieEntry(getSum("Food Expense"), "Food"));
        pieEntries.add(new PieEntry(getSum("Health Expense"), "Health"));
        pieEntries.add(new PieEntry(getSum("Living Expense"), "Living"));
        pieEntries.add(new PieEntry(getSum("Transport Expense"), "Transport"));
        pieEntries.add(new PieEntry(getSum("Payables"), "Payable"));
        pieEntries.add(new PieEntry(getSum("Miscellaneous"), "Misc"));

        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.rgb(255, 102, 0));
        colors.add(Color.rgb(0, 204, 0));
        colors.add(Color.rgb(0, 102, 255));
        colors.add(ColorTemplate.getHoloBlue()); // Holo Blue
        colors.add(ColorTemplate.COLORFUL_COLORS[2]); // Colorful color 2
        colors.add(ColorTemplate.JOYFUL_COLORS[3]); // Joyful color 3

        PieDataSet dataSet = new PieDataSet(pieEntries, "");
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setDrawValues(true);
        data.setValueTextSize(12f);
        data.setValueTextColor(Color.BLACK);

        binding.pieChart.setData(data);
        binding.pieChart.invalidate();
    }

    private void init() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
    }
}

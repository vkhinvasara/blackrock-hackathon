package com.voldy.blackrock.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.voldy.blackrock.databinding.FragmentHomeBinding;
import com.voldy.blackrock.sms.SmsModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    private List<SmsModel> smsList;

    private TransactionAdapter adapter;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    Map<String, List<SmsModel>> mp;
    private SharedPreferences sharedPreferences;

    private int collectionsFetched = 0;
    private static final int TOTAL_COLLECTIONS = 6;

    int income, expense;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);

        sharedPreferences = requireContext().getSharedPreferences("my_prefs", Context.MODE_PRIVATE);

        income = 0;
        expense = 0;

        init();

        getData();

        mp = new HashMap<>();
        mp.put("Food Expense", new ArrayList<>());
        mp.put("Health Expense", new ArrayList<>());
        mp.put("Living Expense", new ArrayList<>());
        mp.put("Transport Expense", new ArrayList<>());
        mp.put("Payables", new ArrayList<>());
        mp.put("Miscellaneous", new ArrayList<>());
        mp.put("Salary", new ArrayList<>());

        fetchDataAndLoadPieChart();

        Intent intent = new Intent(getActivity(), ShowTransactionsActivity.class);

        binding.llBalanceDetails.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), PredictionSummaryActivity.class));
        });

        binding.llMiscellaneous.setOnClickListener(v -> {
            intent.putExtra("categoryName", "Miscellaneous");
            startActivity(intent);
        });

        binding.llIncome.setOnClickListener(v -> {
            intent.putExtra("categoryName", "Salary");
            startActivity(intent);
        });

        binding.llFoodExpense.setOnClickListener(v -> {
            intent.putExtra("categoryName", "Food Expense");
            startActivity(intent);
        });

        binding.llHealthExpense.setOnClickListener(v -> {
            intent.putExtra("categoryName", "Health Expense");
            startActivity(intent);
        });

        binding.llLivingExpense.setOnClickListener(v -> {
            intent.putExtra("categoryName", "Living Expense");
            startActivity(intent);
        });

        binding.llTransportExpense.setOnClickListener(v -> {
            intent.putExtra("categoryName", "Transport Expense");
            startActivity(intent);
        });

        binding.llPayables.setOnClickListener(v -> {
            intent.putExtra("categoryName", "Payables");
            startActivity(intent);
        });


        return binding.getRoot();
    }


    private int getSum(String key) {
        int sm = 0;
        List<SmsModel> res = mp.get(key);
        if (res != null) {
            for (SmsModel i : res) {
                sm += i.getSmsAmount();
            }
        }
        return sm;
    }

    public void getData2(String collectionName) {
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

                            if(collectionName.equals("Salary")){
                                income += smsModel.getSmsAmount();
                            }else{
                                expense += smsModel.getSmsAmount();
                            }

                        }

                        binding.income.setText(Integer.toString(income));
                        binding.expense.setText(Integer.toString(expense));

                        mp.put(collectionName, result);

                    }
                });
    }

    private void fetchDataAndLoadPieChart() {
        getData2("Food Expense");
        getData2("Health Expense");
        getData2("Living Expense");
        getData2("Transport Expense");
        getData2("Payables");
        getData2("Miscellaneous");
        getData2("Salary");
    }

    private void init() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        smsList = new ArrayList<>();
        binding.rvData.setLayoutManager(new LinearLayoutManager(getContext()));

    }

    private void getData() {
        firebaseFirestore
                .collection("userDetails")
                .document(firebaseAuth.getCurrentUser().getEmail())
                .collection("Miscellaneous")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        SmsModel smsModel = document.toObject(SmsModel.class);
                        smsList.add(smsModel);
                    }
                    TransactionAdapter adapter = new TransactionAdapter(getContext(), smsList, getChildFragmentManager());
                    binding.rvData.setAdapter(adapter);
//                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Log.d("SMS", "getData: " + e.getMessage());
                });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
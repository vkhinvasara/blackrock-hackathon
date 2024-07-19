package com.voldy.blackrock.ui.learn;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.voldy.blackrock.databinding.FragementLearnBinding;
import com.voldy.blackrock.sms.SmsModel;
import com.voldy.blackrock.ui.notifications.NotificationsViewModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class LearnFragment extends Fragment {


    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private PredictionModel predictionModel;

    private FragementLearnBinding binding;

    private static final String BASE_URL = "https://ai-model-six.vercel.app/";

    int predicted, current , a, b, c, d ,e;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragementLearnBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        predictionModel = new PredictionModel();

        predicted = 0;
        current = 0;
        a = b = c = d = e = 0;

        init();

        firebaseFirestore
                .collection("userDetails")
                .document(firebaseAuth.getCurrentUser().getEmail())
                .collection("Food Expense")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            SmsModel smsModel = document.toObject(SmsModel.class);
                            a+=smsModel.getSmsAmount();
                            current += smsModel.getSmsAmount();
                        }

                        firebaseFirestore
                                .collection("userDetails")
                                .document(firebaseAuth.getCurrentUser().getEmail())
                                .collection("Health Expense")
                                .get()
                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                            SmsModel smsModel = document.toObject(SmsModel.class);
                                            b+=smsModel.getSmsAmount();
                                            current += smsModel.getSmsAmount();
                                        }

                                        firebaseFirestore
                                                .collection("userDetails")
                                                .document(firebaseAuth.getCurrentUser().getEmail())
                                                .collection("Living Expense")
                                                .get()
                                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                                            SmsModel smsModel = document.toObject(SmsModel.class);
                                                            c+=smsModel.getSmsAmount();
                                                            current += smsModel.getSmsAmount();
                                                        }

                                                        firebaseFirestore
                                                                .collection("userDetails")
                                                                .document(firebaseAuth.getCurrentUser().getEmail())
                                                                .collection("Miscellaneous")
                                                                .get()
                                                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                                    @Override
                                                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                                                            SmsModel smsModel = document.toObject(SmsModel.class);
                                                                            d+=smsModel.getSmsAmount();
                                                                            current += smsModel.getSmsAmount();
                                                                        }

                                                                        firebaseFirestore
                                                                                .collection("userDetails")
                                                                                .document(firebaseAuth.getCurrentUser().getEmail())
                                                                                .collection("Transport Expense")
                                                                                .get()
                                                                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                                                    @Override
                                                                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                                                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                                                                            SmsModel smsModel = document.toObject(SmsModel.class);
                                                                                            e+=smsModel.getSmsAmount();
                                                                                            current += smsModel.getSmsAmount();
                                                                                        }

                                                                                        binding.textCurrentExpense.setText("Current : " + Integer.toString(current));
                                                                                        binding.tvFoodCurrent.setText(Integer.toString(a));
                                                                                        binding.tvHealthCurrent.setText(Integer.toString(b));
                                                                                        binding.tvLivingCurrent.setText(Integer.toString(c));
                                                                                        binding.tvMiscellaneousCurrent.setText(Integer.toString(d));
                                                                                        binding.tvTransportCurrent.setText(Integer.toString(e));

                                                                                        binding.foodProgressBar.setProgress(a);
                                                                                        binding.healthProgressBar.setProgress(b);
                                                                                        binding.livingProgressBar.setProgress(c);
                                                                                        binding.miscellaneousProgressBar.setProgress(d);
                                                                                        binding.transportProgressBar.setProgress(e);




                                                                                    }
                                                                                });


                                                                    }
                                                                });
                                                    }
                                                });
                                    }
                                });
                    }
                });

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);

        Call<DataResponse> call = apiService.getData();
        call.enqueue(new Callback<DataResponse>() {
            @Override
            public void onResponse(Call<DataResponse> call, Response<DataResponse> response) {
                if (response.isSuccessful()) {
                    DataResponse data = response.body();
                    if (data != null) {
                        Double foodExpense = data.getCategory_Food();
                        Double healthExpense = data.getCategory_Health();
                        Double livingExpense = data.getCategory_Living();
                        Double transportExpense = data.getCategory_Transport();
                        Double miscellaneousExpense = data.getCategory_Miscellaneous();

                        predictionModel.setCategory_Food((int) Math.floor(foodExpense));
                        predictionModel.setCategory_Health((int) Math.floor(healthExpense));
                        predictionModel.setCategory_Living((int) Math.floor(livingExpense));
                        predictionModel.setCategory_Transport((int) Math.floor(transportExpense));
                        predictionModel.setCategory_Miscellaneous((int) Math.floor(miscellaneousExpense));

//                        Toast.makeText(getContext(), data.toString(), Toast.LENGTH_SHORT).show();
                        Log.d("TAG", "onResponse: " + data.toString());

                        predicted += predictionModel.getCategory_Food();
                        predicted += predictionModel.getCategory_Health();
                        predicted += predictionModel.getCategory_Living();
                        predicted += predictionModel.getCategory_Miscellaneous();
                        predicted += predictionModel.getCategory_Transport();

                        binding.textMonthlyPrediction.setText("Predicted : " + Integer.toString(predicted));

                        binding.foodProgressBar.setMax(predictionModel.getCategory_Food());
                        binding.tvFoodPredicted.setText(Integer.toString(predictionModel.getCategory_Food()));

                        binding.healthProgressBar.setMax(predictionModel.getCategory_Health());
                        binding.tvHealthPredicted.setText(Integer.toString(predictionModel.getCategory_Health()));

                        binding.livingProgressBar.setMax(predictionModel.getCategory_Living());
                        binding.tvLivingPredicted.setText(Integer.toString(predictionModel.getCategory_Living()));

                        binding.miscellaneousProgressBar.setMax(predictionModel.getCategory_Miscellaneous());
                        binding.tvMiscellaneousPredicted.setText(Integer.toString(predictionModel.getCategory_Miscellaneous()));

                        binding.transportProgressBar.setMax(predictionModel.getCategory_Transport());
                        binding.tvTransportPredicted.setText(Integer.toString(predictionModel.getCategory_Transport()));

                    }
                } else {
                    Toast.makeText(getContext(), "Failed to fetch data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DataResponse> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
//                Toast.makeText(get, "", Toast.LENGTH_SHORT).show();
                Log.d("TAG", "onFailure: " + t.getMessage());
            }
        });


        return root;
    }

    private void init() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

package com.voldy.blackrock.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.voldy.blackrock.R;
import com.voldy.blackrock.databinding.BottomSheetUntaggedBinding;
import com.voldy.blackrock.sms.SmsModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class UnTaggedBottomSheet extends BottomSheetDialogFragment {

    private static final String ARG_SMS_MODEL = "smsModel";
    private SmsModel smsModel;
    private BottomSheetUntaggedBinding binding;
    private Spinner etCategorySpinner;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    public static UnTaggedBottomSheet newInstance(SmsModel smsModel) {
        UnTaggedBottomSheet fragment = new UnTaggedBottomSheet();
        Bundle args = new Bundle();
        args.putSerializable(ARG_SMS_MODEL, smsModel);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = BottomSheetUntaggedBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        init();

        if (getArguments() != null) {
            smsModel = (SmsModel) getArguments().getSerializable(ARG_SMS_MODEL);
        }else{
            smsModel = new SmsModel();
        }

        String old = smsModel.getSmsCategory();

        etCategorySpinner = view.findViewById(R.id.etCategorySpinner); // Assuming you add an id to your Spinner in XML
        String[] categories = {"Miscellaneous", "Food Expense", "Health Expense", "Living Expense", "Transport Expense", "Payables"}; // Example categories
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        etCategorySpinner.setAdapter(adapter);


        etCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCategory = (String) parent.getItemAtPosition(position);
                smsModel.setSmsCategory(selectedCategory);
                Toast.makeText(requireContext(), "Selected Category: " + selectedCategory, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle no selection if needed
            }
        });

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String formattedDate = sdf.format(new Date(smsModel.getSmsTimeStamp()));
        binding.etDate.setText(formattedDate);

        binding.etAmount.setText(String.valueOf(smsModel.getSmsAmount()));
        binding.etTitle.setText(smsModel.getSmsMerchantName());

        binding.ivDone.setOnClickListener(v -> {

            smsModel.setSmsMerchantName(binding.etTitle.getText().toString());
            smsModel.setSmsTimeStamp(convertDateToMilliseconds(binding.etDate.getText().toString()));
            int amt = 0;
            String s_smt = binding.etAmount.getText().toString();
            if (s_smt != "") {
                amt = Integer.parseInt(s_smt);
            }
            smsModel.setSmsAmount(amt);

            updateSmsModel(smsModel, old);
//            dismiss();
        });

        return view;
    }

    private String formatTimestampToTime(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a", Locale.getDefault());
        Date date = new Date(timestamp);
        return sdf.format(date);
    }

    public long convertDateToMilliseconds(String dateString) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            Date date = sdf.parse(dateString);
            return date.getTime(); // Returns milliseconds since January 1, 1970, 00:00:00 UTC
        } catch (ParseException e) {
            e.printStackTrace();
            return 0; // Handle parse exception or return default value
        }
    }

    private void init() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    public void updateSmsModel(SmsModel smsModel, String old) {

        firebaseFirestore
                .collection("userDetails")
                .document(firebaseAuth.getCurrentUser().getEmail())
                .collection(smsModel.getSmsCategory())
                .document(smsModel.getSmsID())
                .set(smsModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getContext(), "Data Added " + old + " " + smsModel.getSmsCategory(), Toast.LENGTH_SHORT).show();

                        if (old != smsModel.getSmsCategory()) {
                            deleteMis(old, smsModel.getSmsID());
                        }
                    }
                });
    }

    private void deleteMis(String old, String smsID) {

//        Log.d(TAG, "deleteMis: ");
        Toast.makeText(getContext(), "delete Mis" + old, Toast.LENGTH_SHORT).show();

        firebaseFirestore
                .collection("userDetails")
                .document(firebaseAuth.getCurrentUser().getEmail())
                .collection(old)
                .document(smsID)
                .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getContext(), "Duplicate Removed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

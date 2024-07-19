package com.voldy.blackrock.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.voldy.blackrock.databinding.ItemShowTransactionBinding;
import com.voldy.blackrock.sms.SmsModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {

    private final Context context;
    private final List<SmsModel> smsList;
    private final FragmentManager fragmentManager;

    public TransactionAdapter(Context context, List<SmsModel> smsList, FragmentManager fragmentManager) {
        this.context = context;
        this.smsList = smsList;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        ItemShowTransactionBinding binding = ItemShowTransactionBinding.inflate(inflater, parent, false);
        return new TransactionViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        SmsModel smsModel = smsList.get(position);
        holder.bind(smsModel);
    }

    @Override
    public int getItemCount() {
        return smsList.size();
    }

    public class TransactionViewHolder extends RecyclerView.ViewHolder {

        private final ItemShowTransactionBinding binding;

        public TransactionViewHolder(ItemShowTransactionBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(SmsModel smsModel) {
            binding.tvID.setText(smsModel.getSmsID());
            binding.tvCategory.setText(smsModel.getSmsCategory());
            binding.tvAmount.setText(String.format("₹%d", smsModel.getSmsAmount()));
            binding.tvMerchantName.setText(smsModel.getSmsMerchantName());
            binding.tvTransactionType.setText(smsModel.getSmsTransactionType());

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            String formattedDate = sdf.format(new Date(smsModel.getSmsTimeStamp()));
            binding.tvDate.setText(formattedDate);

            binding.tvTime.setText(formatTimestampToTime(smsModel.getSmsTimeStamp()));

            // Set the text color based on transaction type
            if (smsModel.getSmsTransactionType().equalsIgnoreCase("Credit")) {
                binding.tvTransactionType.setTextColor(binding.getRoot().getResources().getColor(android.R.color.holo_green_dark));
            } else {
                binding.tvTransactionType.setTextColor(binding.getRoot().getResources().getColor(android.R.color.holo_red_dark));
            }

            binding.llView.setOnClickListener(v -> {
                UnTaggedBottomSheet unTaggedBottomSheet = UnTaggedBottomSheet.newInstance(smsModel);
                unTaggedBottomSheet.show(fragmentManager, "Un-Tagged Expenses");
            });
        }

        private String formatTimestampToTime(long timestamp) {
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a", Locale.getDefault());
            Date date = new Date(timestamp);
            return sdf.format(date);
        }
    }
}

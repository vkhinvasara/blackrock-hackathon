package com.voldy.blackrock.ui.dashboard;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.voldy.blackrock.R;
import com.voldy.blackrock.databinding.ItemNewsArticleBinding;
import com.voldy.blackrock.sms.SmsNotificationReceiver;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
    private Context context;
    private List<NewsArticleModel> articles;

    public NewsAdapter(Context context, List<NewsArticleModel> articles) {
        this.context = context;
        this.articles = articles;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemNewsArticleBinding binding = ItemNewsArticleBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NewsArticleModel article = articles.get(position);

        holder.binding.textTitle.setText(article.getTitle());
        holder.binding.textAuthor.setText(article.getAuthor());
        holder.binding.textDescription.setText(article.getDescription());

        Glide.with(context)
                .load(article.getUrlToImage())
                .placeholder(R.drawable.loading) // Placeholder image
                .error(R.drawable.error) // Error image if loading fails
                .into(holder.binding.imageNews);

        holder.binding.llLayout.setOnClickListener(v -> {
            Intent intent = new Intent(context, FullNewsActivity.class);
            intent.putExtra("article", article);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ItemNewsArticleBinding binding;

        public ViewHolder(ItemNewsArticleBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}

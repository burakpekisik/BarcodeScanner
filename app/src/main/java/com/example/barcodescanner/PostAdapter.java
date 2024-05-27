package com.example.barcodescanner;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostViewHolder> {
    private final RecyclerViewInterface recyclerViewInterface;
    private List<PostModel> postModelList;

    public PostAdapter(List<PostModel> postModelList, RecyclerViewInterface recyclerViewInterface) {
        this.postModelList = postModelList;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_post, parent, false);
        return new PostViewHolder(view, this.recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(PostViewHolder holder, int position) {
        holder.bindView(postModelList.get(position));
    }

    @Override
    public int getItemCount() {
        return postModelList.size();
    }

    public void clear() {
        postModelList.clear(); // PostModel listesini temizle
        notifyDataSetChanged(); // Değişiklikleri bildir
    }
}

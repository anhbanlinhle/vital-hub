package com.example.vital_hub.pushup;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.vital_hub.R;
import com.example.vital_hub.home_page.HomePagePost;
import com.example.vital_hub.home_page.HpRecyclerAdapter;
import com.example.vital_hub.post_comment.PostCommentActivity;

import java.util.ArrayList;

public class PushupAdapter extends RecyclerView.Adapter<PushupAdapter.ViewHolder> {
    private ArrayList<Integer> arrayList;
    final int VIEW_TYPE_ITEM = 0;
    final int VIEW_TYPE_LOADING = 1;
    final int VIEW_TYPE_DEFAULT = 2;

    public PushupAdapter(ArrayList<Integer> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public PushupAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.pushup_count_result, parent, false);
            return new PushupAdapter.ViewHolder(view, VIEW_TYPE_ITEM);
        } else if (viewType == VIEW_TYPE_LOADING) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.loading_item, parent, false);
            return new PushupAdapter.ViewHolder(view, VIEW_TYPE_LOADING);
        } else {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.pushup_instruction, parent, false);
            return new PushupAdapter.ViewHolder(view, VIEW_TYPE_DEFAULT);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull PushupAdapter.ViewHolder holder, int position) {
        if (holder.itemType == VIEW_TYPE_ITEM) {
            Integer result = arrayList.get(position);
            holder.result.setText(String.valueOf(result));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (arrayList.get(position) == null) {
            return VIEW_TYPE_LOADING;
        } else if (arrayList.get(position) == -1) {
            return VIEW_TYPE_DEFAULT;
        } else {
            return VIEW_TYPE_ITEM;
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView result;
        int itemType;
        public ViewHolder(@NonNull View itemView, int viewType) {
            super(itemView);
            if (viewType == VIEW_TYPE_ITEM) {
                result = itemView.findViewById(R.id.result);
                itemType = VIEW_TYPE_ITEM;
            } else if (viewType == VIEW_TYPE_LOADING) {
                itemType = VIEW_TYPE_LOADING;
            } else {
                itemType = VIEW_TYPE_DEFAULT;
            }
        }
    }
}

package com.example.vital_hub.competition;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.vital_hub.R;
import com.example.vital_hub.friend.ItemClickListener;
import com.example.vital_hub.helper.KeyboardHelper;

public class CompetitionListAdapter extends RecyclerView.Adapter<CompetitionListAdapter.CompetitionViewHolder>{

    private final ArrayList<Competition> competitionList;

    public CompetitionListAdapter(ArrayList<Competition> competitionList) {
        this.competitionList = competitionList;
    }

    @NonNull
    @Override
    public CompetitionListAdapter.CompetitionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.competition_list_item, parent, false);
        return new CompetitionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CompetitionListAdapter.CompetitionViewHolder holder, int position) {
        Glide.with(holder.background.getContext()).load(competitionList.get(position).getBackground()).into(holder.background);
        Glide.with(holder.hostAvatar.getContext()).load(competitionList.get(position).getHostAvatar()).into(holder.hostAvatar);
        holder.title.setText(competitionList.get(position).getTitle());
        holder.hostName.setText(competitionList.get(position).getHostName());
        holder.participantCount.setText(competitionList.get(position).getParticipantCount().toString());
        if (competitionList.get(position).getRemainDay() >= 0) {
            holder.status.setText(competitionList.get(position).getRemainDay() + " days left");
        } else {
            holder.status.setText("Ended");
        }
        holder.joinButton.setOnClickListener(v -> {

        });

        holder.setItemClickListener((view, position1, isLongClick) -> {
            if (isLongClick){

            }
            else{
                KeyboardHelper.hideKeyboard(view);
                Toast.makeText(
                                view.getContext(),
                                "Click: " + competitionList.get(position1).getId(),
                                Toast.LENGTH_SHORT)
                        .show();
            }

        });
    }

    @Override
    public int getItemCount() {
        return competitionList.size();
    }

    public static class CompetitionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView title;
        private final ImageView background, hostAvatar;
        private final TextView hostName;
        private final TextView participantCount;
        private final TextView status;
        private final Button joinButton;
        private ItemClickListener itemClickListener;

        public CompetitionViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.competitionName);
            background = itemView.findViewById(R.id.background);
            hostAvatar = itemView.findViewById(R.id.avatar);
            hostName = itemView.findViewById(R.id.hostName);
            participantCount = itemView.findViewById(R.id.total_mem);
            status = itemView.findViewById(R.id.status);
            joinButton = itemView.findViewById(R.id.joinButton);

            itemView.setOnClickListener(this);
        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }
        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v, getAdapterPosition(), false);
        }
    }
}


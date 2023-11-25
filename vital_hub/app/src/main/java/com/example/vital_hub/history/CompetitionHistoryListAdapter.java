package com.example.vital_hub.history;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.vital_hub.R;
import com.example.vital_hub.client.spring.objects.CompetitionHistoryListResponse;
import com.example.vital_hub.friend.ItemClickListener;
import com.example.vital_hub.helper.KeyboardHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CompetitionHistoryListAdapter extends RecyclerView.Adapter<CompetitionHistoryListAdapter.CompetitionHistoryViewHolder> {

    ArrayList<CompetitionHistoryListResponse> competitionHistoryList;

    public CompetitionHistoryListAdapter(ArrayList<CompetitionHistoryListResponse> competitionHistoryList) {
        this.competitionHistoryList = competitionHistoryList;
    }

    @NonNull
    @Override
    public CompetitionHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.competition_history_list_item, parent, false);
        return new CompetitionHistoryViewHolder(view);
    }




    @Override
    public void onBindViewHolder(@NonNull CompetitionHistoryViewHolder holder, int position) {
        Glide.with(holder.background.getContext()).load(competitionHistoryList.get(position).getBackground()).into(holder.background);
        holder.competitionName.setText(competitionHistoryList.get(position).getTitle());
        holder.participantCount.setText(String.valueOf(competitionHistoryList.get(position).getParticipants()));
        if ((competitionHistoryList.get(position).getPositionStr()).equals("0th")) {
            holder.ranking.setText("N/A");
        } else {
            holder.ranking.setText(competitionHistoryList.get(position).getPositionStr());
        }
        holder.competitionType.setText(competitionHistoryList.get(position).getType());

        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date endDate = inputFormat.parse(competitionHistoryList.get(position).getEndedAt());
            String formattedDate = outputFormat.format(endDate);

            holder.endDate.setText(formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.setItemClickListener((view, listPosition, isLongClick) -> {
            if (isLongClick) {

            } else {
                KeyboardHelper.hideKeyboard(view);
                Intent intent = new Intent(view.getContext(), CompetitionHistoryDetailActivity.class);
                intent.putExtra("competitionHistoryId", competitionHistoryList.get(listPosition).getCompetitionId());
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return competitionHistoryList.size();
    }

    public static class CompetitionHistoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView competitionType;
        TextView ranking;
        ImageView background;
        TextView competitionName;
        TextView participantCount;
        TextView endDate;
        ItemClickListener itemClickListener;

        public CompetitionHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            ranking = itemView.findViewById(R.id.ranking);
            competitionType = itemView.findViewById(R.id.competition_type);
            background = itemView.findViewById(R.id.background);
            competitionName = itemView.findViewById(R.id.competition_name);
            participantCount = itemView.findViewById(R.id.participant_count);
            endDate = itemView.findViewById(R.id.end_date);
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

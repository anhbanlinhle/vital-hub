//package com.example.vital_hub.history;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.bumptech.glide.Glide;
//import com.example.vital_hub.R;
//import com.example.vital_hub.competition.Competition;
//import com.example.vital_hub.competition.CompetitionListAdapter;
//import com.example.vital_hub.friend.ItemClickListener;
//
//import java.util.ArrayList;
//
//public class HistoryListAdapter extends RecyclerView.Adapter<HistoryListAdapter.CompetitionHistoryViewHolder> {
//
//    private final ArrayList<Competition> competitionHistoryList;
//
//    @Override
//    public int getItemCount() {
//            return competitionHistoryList.size();
//    }
//
//    @NonNull
//    @Override
//    public CompetitionHistoryListAdapter.CompetitionHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.competition_list_item, parent, false);
//        return new CompetitionHistoryListAdapter.CompetitionHistoryViewHolder(view);
//    }
//
//    public static class CompetitionHistoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
//
//        private final TextView title;
//        private final ImageView background, hostAvatar;
//        private final TextView hostName;
//        private final TextView participantCount;
//        private final TextView status;
//        private final TextView category;
//        private ItemClickListener itemClickListener;
//
//        public CompetitionHistoryViewHolder(@NonNull View itemView) {
//            super(itemView);
//            title = itemView.findViewById(R.id.competitionName);
//            background = itemView.findViewById(R.id.background);
//            hostAvatar = itemView.findViewById(R.id.avatar);
//            hostName = itemView.findViewById(R.id.hostName);
//            participantCount = itemView.findViewById(R.id.total_mem);
//            status = itemView.findViewById(R.id.status);
//            category = itemView.findViewById(R.id.compe_type_text);
//
//            itemView.setOnClickListener(this);
//        }
//
//        public void setItemClickListener(ItemClickListener itemClickListener) {
//            this.itemClickListener = itemClickListener;
//        }
//        @Override
//        public void onClick(View v) {
//            itemClickListener.onClick(v, getAdapterPosition(), false);
//        }
//    }
//}

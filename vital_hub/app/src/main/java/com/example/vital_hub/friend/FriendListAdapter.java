package com.example.vital_hub.friend;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.vital_hub.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import com.example.vital_hub.helper.KeyboardHelper;
public class FriendListAdapter extends RecyclerView.Adapter<FriendListAdapter.FriendViewHolder> {

    private final ArrayList<Friend> friendList;

    public FriendListAdapter(ArrayList<Friend> friendList) {
        this.friendList = friendList;
    }

    @NonNull
    @Override
    public FriendListAdapter.FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_list_item, parent, false);
        return new FriendViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull FriendListAdapter.FriendViewHolder holder, int position) {

        holder.friendName.setText(friendList.get(position).getName());
        Glide.with(holder.friendAvatar.getContext()).load(friendList.get(position).getAvatar()).into(holder.friendAvatar);

        holder.setItemClickListener((view, position1, isLongClick) -> {
            if (isLongClick){
                KeyboardHelper.hideKeyboard(view);
                showPopupWindow(view, position1);
            }
            else{
                KeyboardHelper.hideKeyboard(view);
                Toast.makeText(
                                view.getContext(),
                                "Click: " + friendList.get(position1).getName(),
                                Toast.LENGTH_SHORT)
                        .show();
            }

        });

        holder.moreButton.setOnClickListener(v -> {
            KeyboardHelper.hideKeyboard(v);
            showPopupWindow(v, position);


        });
    }

    public void showPopupWindow(View v, int position) {

        LayoutInflater inflater = (LayoutInflater) v.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_profile, null);
        popupView.setAnimation(AnimationUtils.loadAnimation(v.getContext(), R.animator.slide_up));
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);

//            popupWindow.setAnimationStyle(R.style.Animation_Design_BottomSheetDialog);
        popupWindow.setElevation(20);

        TextView name = popupView.findViewById(R.id.friendName);
        ImageView avatar = popupView.findViewById(R.id.avatar);

        // 2 actions: View profile, Remove friend
        LinearLayout viewProfile = popupView.findViewById(R.id.item_1);
        LinearLayout removeFriend = popupView.findViewById(R.id.item_4);

        // View profile
        viewProfile.setOnClickListener(v1 -> {
            Toast.makeText(v1.getContext(), "View profile of user: " + friendList.get(position).getId(), Toast.LENGTH_SHORT).show();
            popupWindow.dismiss();
        });

        // Remove friend
        removeFriend.setOnClickListener(v1 -> {
            Toast.makeText(v1.getContext(), "Remove friend: " + friendList.get(position).getId(), Toast.LENGTH_SHORT).show();
            popupWindow.dismiss();
        });


        name.setText(friendList.get(position).getName());
        Glide.with(avatar.getContext()).load(friendList.get(position).getAvatar()).into(avatar);
        dimBehind(popupWindow);

        //Handle when dismiss popup, using slide down animation
        popupWindow.setOnDismissListener(() -> popupView.setAnimation(AnimationUtils.loadAnimation(v.getContext(), R.animator.slide_down)));
    }

    public void dimBehind(PopupWindow popupWindow) {
        View container;
        if (popupWindow.getBackground() == null) {
            container = (View) popupWindow.getContentView().getParent();
        } else {
            container = (View) popupWindow.getContentView().getParent().getParent();
        }
        WindowManager wm = (WindowManager) popupWindow.getContentView().getContext().getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams p = (WindowManager.LayoutParams) container.getLayoutParams();
        p.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        p.dimAmount = (float) 0.5;
        wm.updateViewLayout(container, p);
    }

    @Override
    public int getItemCount() {
        return friendList.size();
    }

    public static class FriendViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private final TextView friendName;
        private final ImageView friendAvatar;
        private final Button moreButton;
        private ItemClickListener itemClickListener;

        public FriendViewHolder(@NonNull View view) {
            super(view);

            friendName = view.findViewById(R.id.friendName);
            friendAvatar = view.findViewById(R.id.avatar);
            moreButton = view.findViewById(R.id.action_button);

            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v, getAdapterPosition(), false);
        }

        @Override
        public boolean onLongClick(View v) {
            itemClickListener.onClick(v, getAdapterPosition(), true);
            return true;
        }
    }
}

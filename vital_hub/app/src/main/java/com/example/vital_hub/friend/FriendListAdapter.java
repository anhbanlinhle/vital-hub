package com.example.vital_hub.friend;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.vital_hub.R;
import com.example.vital_hub.client.spring.controller.Api;
import com.example.vital_hub.helper.KeyboardHelper;
import com.example.vital_hub.profile.OthersProfile;

import java.util.ArrayList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FriendListAdapter extends RecyclerView.Adapter<FriendListAdapter.FriendViewHolder> {
    private FriendActionListener friendActionListener;
    private final ArrayList<Friend> friendList;
    private final Map<String, String> headers = FriendList.headers;
    public FriendListAdapter(ArrayList<Friend> friendList) {
        this.friendList = friendList;
    }

    @NonNull
    @Override
    public FriendListAdapter.FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_list_item, parent, false);
        return new FriendViewHolder(view);
    }

    public void setFriendActionListener(FriendActionListener friendActionListener) {
        this.friendActionListener = friendActionListener;
    }

    @Override
    public void onBindViewHolder(@NonNull FriendListAdapter.FriendViewHolder holder, int position) {

        holder.friendName.setText(friendList.get(position).getName());
        Glide.with(holder.friendAvatar.getContext()).load(friendList.get(position).getAvatar()).into(holder.friendAvatar);
        holder.relation.setText(friendList.get(position).getStatus());
        holder.setItemClickListener((view, position1, isLongClick) -> {
            if (isLongClick) {
                KeyboardHelper.hideKeyboard(view);
                showPopupWindow(view, position1, friendList.get(position1).getRawStatus());
            } else {
                KeyboardHelper.hideKeyboard(view);
                Intent intent = new Intent(view.getContext(),OthersProfile.class);
                intent.putExtra("id", friendList.get(position).getId()+"");
                view.getContext().startActivity(intent);
            }

        });

        holder.moreButton.setOnClickListener(v -> {
            KeyboardHelper.hideKeyboard(v);
            showPopupWindow(v, position, friendList.get(position).getRawStatus());
        });
    }

    public void confirmationPopUp(View view, int position) {
        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.unfriend_confirmation_popup, null);

        // Create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

        Button cancel = popupView.findViewById(R.id.cancel_button);
        Button confirm = popupView.findViewById(R.id.confirm_button);
        TextView confirmText = popupView.findViewById(R.id.confirmation_text);
        confirmText.setText("Are you sure you want to remove \n" + friendList.get(position).getName() + " as your friend?");
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Api.initDeleteFriend(headers, friendList.get(position).getId());
                Api.deleteFriend.clone().enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                        if(response.isSuccessful()) {
                            Toast.makeText(view.getContext(), "Remove friend successfully", Toast.LENGTH_SHORT).show();
                            // Reload friend list
                            friendActionListener.onAction();
                            popupWindow.dismiss();
                        } else {
                            Toast.makeText(view.getContext(), "Error: " + response, Toast.LENGTH_SHORT).show();
                            Log.d("Error", "Error: " + response);
                        }
                    }
                    @Override
                    public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                        Toast.makeText(view.getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        dimBehind(popupWindow);
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
    }
    public void showPopupWindow(View v, int position, String status) {

        LayoutInflater inflater = (LayoutInflater) v.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView;
        switch (status) {
            case "FRIEND":
                popupView = inflater.inflate(R.layout.popup_profile, null);
                break;
            case "PENDING":
                popupView = inflater.inflate(R.layout.popup_profile_pending, null);
                break;
            case "INCOMING":
                popupView = inflater.inflate(R.layout.popup_profile_incoming, null);
                break;
            default:
                popupView = inflater.inflate(R.layout.popup_profile_non_friend, null);
                break;
        }
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
        LinearLayout viewProfile = popupView.findViewById(R.id.profile);
        LinearLayout action = popupView.findViewById(R.id.action);
        // if status is incoming, there is 1 more action: Deny
        if (status.equals("INCOMING")) {
            LinearLayout action_deny = popupView.findViewById(R.id.action_deny);
            // Action deny
            action_deny.setOnClickListener(v1 -> {
                Api.initDenyRequest(headers, friendList.get(position).getId());
                Api.denyRequest.clone().enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                        if(response.isSuccessful()) {
                            friendActionListener.onAction();
                            Toast.makeText(v1.getContext(), "Deny friend request successfully", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(v1.getContext(), "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                        Toast.makeText(v1.getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                popupWindow.dismiss();
            });
        }
        // View profile
        viewProfile.setOnClickListener(v1 -> {
            // TODO: View profile
            Intent intent = new Intent(v1.getContext(),OthersProfile.class);
            intent.putExtra("id", friendList.get(position).getId()+"");
            v1.getContext().startActivity(intent);
            popupWindow.dismiss();
        });

        // Action
        action.setOnClickListener(v1 -> {
            switch (friendList.get(position).getRawStatus()) {
                case "FRIEND":
                    popupWindow.dismiss();
                    confirmationPopUp(v1, position);
                    break;
                case "PENDING":
                    Api.initRevokeRequest(headers, friendList.get(position).getId());
                    Api.revokeRequest.clone().enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                            if(response.isSuccessful()) {
                                Toast.makeText(v1.getContext(), "Revoke friend request successfully", Toast.LENGTH_SHORT).show();
                                friendActionListener.onAction();
                            }
                            else {
                                Toast.makeText(v1.getContext(), "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                            Toast.makeText(v1.getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    popupWindow.dismiss();
                    break;
                case "INCOMING":
                    Api.initAcceptRequest(headers, friendList.get(position).getId());
                    Api.acceptRequest.clone().enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                            if(response.isSuccessful()) {
                                Toast.makeText(v1.getContext(), "Accept friend request successfully", Toast.LENGTH_SHORT).show();
                                friendActionListener.onAction();
                            }
                            else {
                                Toast.makeText(v1.getContext(), "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                            Toast.makeText(v1.getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    popupWindow.dismiss();
                    break;
                default:
                    Api.initAddFriend(headers, friendList.get(position).getId());
                    Api.addFriend.clone().enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                            if(response.isSuccessful()) {
                                Toast.makeText(v1.getContext(), "Send request", Toast.LENGTH_SHORT).show();
                                friendActionListener.onAction();
                            }
                            else {
                                Toast.makeText(v1.getContext(), "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                            Toast.makeText(v1.getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    popupWindow.dismiss();
                    break;
            }
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

        private final TextView friendName, relation;
        private final ImageView friendAvatar;
        private final Button moreButton;
        private ItemClickListener itemClickListener;

        public FriendViewHolder(@NonNull View view) {
            super(view);

            friendName = view.findViewById(R.id.friendName);
            friendAvatar = view.findViewById(R.id.avatar);
            moreButton = view.findViewById(R.id.action_button);
            relation = view.findViewById(R.id.relation);
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

    public interface FriendActionListener {
        void onAction();
    }
}

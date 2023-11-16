package com.example.vital_hub.profile;

import static android.content.Context.MODE_PRIVATE;
import static com.example.vital_hub.authentication.LoginScreen.oneTapClient;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.vital_hub.R;
import com.example.vital_hub.TestPage;
import com.example.vital_hub.authentication.LoginScreen;
import com.example.vital_hub.client.spring.controller.Api;
import com.example.vital_hub.client.spring.objects.CountResponse;
import com.example.vital_hub.client.spring.objects.ProfileDetailResponse;
import com.example.vital_hub.client.spring.objects.ProfileResponse;
import com.example.vital_hub.friend.FriendList;
import com.example.vital_hub.test.TestMain;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {
    Toolbar toolbar;
    ImageView setting;
    View history;
    View statistic;
    View friend;
    Button profileDetailButton;
    SharedPreferences prefs;
    String jwt;
    Map<String, String> headers;
    ProfileResponse profileResponse;
    ProfileDetailResponse profileDetailResponse;
    TextView name;
    TextView description;
    TextView totalFriend;
    ImageView profileImage;
    Button openOthersProfileTest;
    private UserInfo fetchedUserProfile;
    private CountResponse countResponse;
    private UserDetail fetchedUserProfileDetail;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void signOut() {
        oneTapClient.signOut();
        SharedPreferences.Editor editor = getActivity().getSharedPreferences("UserData", MODE_PRIVATE).edit();
        editor.clear();
        editor.apply();
        Intent intent = new Intent(getActivity().getBaseContext(), LoginScreen.class);
        startActivity(intent);
        getActivity().finish();
    }

    private void initHeaderForRequest() {
        prefs = this.getActivity().getSharedPreferences("UserData", MODE_PRIVATE);
        jwt = prefs.getString("jwt", null);
        headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + jwt);
    }

    private void fetchUserProfileDetail() {
        Api.initGetUserProfileDetail(headers);
        Api.getUserProfileDetail.clone().enqueue(new Callback<ProfileDetailResponse>() {
            @Override
            public void onResponse(Call<ProfileDetailResponse> call, Response<ProfileDetailResponse> response) {
                if (response.isSuccessful()) {
                    profileDetailResponse = response.body();
                    assert profileDetailResponse != null;
                    fetchedUserProfileDetail = profileDetailResponse.getData();
                    name.setText(fetchedUserProfileDetail.getName());
                    description.setText(fetchedUserProfileDetail.getUserDetail().getDescription());
                    Glide.with(getActivity().getBaseContext()).load(fetchedUserProfileDetail.getAvatar()).into(profileImage);
                }
            }

            @Override
            public void onFailure(Call<ProfileDetailResponse> call, Throwable t) {
                Toast.makeText(getActivity().getBaseContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchFriends() {
        Api.initGetTotalFriend(headers);
        Api.getTotalFriend.clone().enqueue(new Callback<CountResponse>() {
            @Override
            public void onResponse(Call<CountResponse> call, Response<CountResponse> response) {
                if (response.isSuccessful()) {
                    countResponse = response.body();
                    assert countResponse != null;
                    totalFriend.setText(String.valueOf(countResponse.getData()));
                }
            }

            @Override
            public void onFailure(Call<CountResponse> call, Throwable t) {
                Toast.makeText(getActivity().getBaseContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test_profile, container, false);

        initHeaderForRequest();
        fetchUserProfileDetail();
        fetchFriends();

        profileDetailButton = view.findViewById(R.id.profile_detail_button);
        toolbar = view.findViewById(R.id.toolbar);
        setting = view.findViewById(R.id.setting);
        history = view.findViewById(R.id.history_view);
        statistic = view.findViewById(R.id.statistic_view);
        friend = view.findViewById(R.id.friend_view);
        name = view.findViewById(R.id.username);
        profileImage = view.findViewById(R.id.profile_image);
        openOthersProfileTest = view.findViewById(R.id.others_profile);
        totalFriend = view.findViewById(R.id.friend_counter);
        description = view.findViewById(R.id.description);

        openOthersProfileTest.setVisibility(View.GONE);
        openOthersProfileTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getBaseContext(), OthersProfile.class);
                startActivity(intent);
            }
        });
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getBaseContext(), TestPage.class);
                startActivity(intent);
            }
        });

        statistic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getBaseContext(), TestPage.class);
                startActivity(intent);
            }
        });

        friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getBaseContext(), FriendList.class);
                startActivity(intent);
            }
        });
        profileDetailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getBaseContext(), ProfileDetail.class);
                startActivity(intent);
            }
        });
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(getActivity().getBaseContext(), setting);
                popupMenu.getMenuInflater().inflate(R.menu.setting_menu, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if (menuItem.getItemId() == R.id.test) {
                            Intent intent = new Intent(getActivity().getBaseContext(), TestMain.class);
                            startActivity(intent);
                            return true;
                        } else if (menuItem.getItemId() == R.id.logout) {
                            signOut();
                            return true;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();

        return view;
    }
}
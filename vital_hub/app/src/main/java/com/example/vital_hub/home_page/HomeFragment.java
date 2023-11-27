package com.example.vital_hub.home_page;

import static android.content.Context.MODE_PRIVATE;

import static com.example.vital_hub.client.spring.controller.Api.initGetPostResponse;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.TransitionInflater;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.vital_hub.R;
import com.example.vital_hub.client.spring.controller.Api;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.saadahmedsoft.popupdialog.PopupDialog;
import com.saadahmedsoft.popupdialog.Styles;
import com.saadahmedsoft.popupdialog.listener.OnDialogButtonClickListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    RecyclerView hpRecycler;
    ArrayList<HomePagePost> arrayList;
    boolean isLoading = false;
    HpRecyclerAdapter recyclerAdapter;
    ImageButton logout_button;
    SharedPreferences prefs;
    String jwt;
    Map<String, String> headers;
    public static List<HomePagePost> postResponse;
    int pageNum = 0;
    FloatingActionButton addPostButton;
    public static Boolean refetch = false;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();

        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("res", String.valueOf(refetch));
        if(refetch) {
            refetchPost();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TransitionInflater inflater = TransitionInflater.from(requireContext());
        setEnterTransition(inflater.inflateTransition(R.transition.fade));
        setExitTransition(inflater.inflateTransition(R.transition.fade));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test_home, container, false);
        initHeaderForRequest();
        arrayList = new ArrayList<>();

        hpRecycler = view.findViewById(R.id.home_page_recycler);

        logout_button = view.findViewById(R.id.logout_button);
        addPostButton = view.findViewById(R.id.add_post_button);

        recyclerAdapter = new HpRecyclerAdapter(arrayList);
        hpRecycler.setAdapter(recyclerAdapter);
        hpRecycler.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        getMoreData();

        hpRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) hpRecycler.getLayoutManager();
                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == arrayList.size() - 1) {
                        isLoading = true;
                        getMoreData();
                    }
                }
            }
        });

        addPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), AddPostActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    private void initHeaderForRequest() {
        prefs = this.getActivity().getSharedPreferences("UserData", MODE_PRIVATE);
        jwt = prefs.getString("jwt", null);
        headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + jwt);
    }

    private void fetchPost(int pageNum) {
        initGetPostResponse(headers, pageNum);
        Api.getPostResponse.clone().enqueue(new Callback<List<HomePagePost>>() {
            @Override
            public void onResponse(Call<List<HomePagePost>> call, Response<List<HomePagePost>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    postResponse = response.body();
                    arrayList.addAll(postResponse);
                    recyclerAdapter.notifyItemRangeChanged(arrayList.size() - postResponse.size(), postResponse.size());
                } else {
                    openPopup("Error", "Fail to load post", Styles.FAILED);
                }
            }

            @Override
            public void onFailure(Call<List<HomePagePost>> call, Throwable t) {
                openPopup("Error", "Fail to load post", Styles.FAILED);
            }
        });
    }

    private void openPopup(String heading, String description, Styles styles) {
        PopupDialog.getInstance(requireContext())
                .setStyle(styles)
                .setHeading(heading)
                .setDescription(description)
                .setCancelable(true)
                .showDialog(new OnDialogButtonClickListener() {
                    @Override
                    public void onDismissClicked(Dialog dialog) {
                        super.onDismissClicked(dialog);
                    }
                });
    }

    private void getMoreData() {
        arrayList.add(null);
        // ADD DATA FROM DB
        arrayList.remove(arrayList.size() - 1);
        fetchPost(pageNum);
        pageNum++;
        isLoading = false;
    }

    private void refetchPost() {
        arrayList.clear();
        pageNum = 0;
        getMoreData();
    }
}
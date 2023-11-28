package com.example.vital_hub.history;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vital_hub.R;
import com.example.vital_hub.client.spring.controller.Api;
import com.example.vital_hub.client.spring.objects.CompetitionHistoryListResponse;
import com.example.vital_hub.helper.KeyboardHelper;
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

public class CompetitionHistoryActivity extends AppCompatActivity {
    boolean isLoading = false;
    ImageView backButton;
    RecyclerView competitionHistoryList;
    CompetitionHistoryListAdapter competitionHistoryListAdapter;
    List<CompetitionHistoryListResponse> competitionHistoryListResponse;
    ArrayList <CompetitionHistoryListResponse> competitionHistories;
    EditText searchBar;
    LinearLayoutManager layoutManager;
    SharedPreferences prefs;
    String jwt;
    Map<String, String> headers;
    Integer pageNum = 0;
    String temp = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.competition_history_layout);
        initHeaderForRequest();

        KeyboardHelper.setupKeyboardHiding(this);
        competitionHistories = new ArrayList<>();
        competitionHistoryList = findViewById(R.id.competition_history_list);
        backButton = findViewById(R.id.back_button);
        searchBar = findViewById(R.id.search_bar);
        competitionHistoryListAdapter = new CompetitionHistoryListAdapter(competitionHistories);
        competitionHistoryList.setAdapter(competitionHistoryListAdapter);
        layoutManager = new LinearLayoutManager(this);
        competitionHistoryList.setLayoutManager(layoutManager);
        fetchCompetitionHistoryList(pageNum);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        searchBar.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean isFocused) {
                if (!isFocused) {
                    KeyboardHelper.hideKeyboard(view);
                }
            }
        });
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String searchTerm = charSequence.toString();
                competitionHistories.clear();
                pageNum = 0;
                if (searchTerm.equals("")) {
                    fetchCompetitionHistoryList(pageNum);
                } else {
                    fetchSpecificCompetitionHistoryList(searchTerm, pageNum);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
    private void initHeaderForRequest() {
        prefs = getSharedPreferences("UserData", MODE_PRIVATE);
        jwt = prefs.getString("jwt", null);
        headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + jwt);
    }

    private void fetchCompetitionHistoryList(Integer pageNum) {
        Api.initGetCompetitionHistoryList(headers, pageNum);
        Api.getCompetitionHistoryList.clone().enqueue(new Callback<List<CompetitionHistoryListResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<CompetitionHistoryListResponse>> call, @NonNull Response<List<CompetitionHistoryListResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    competitionHistoryListResponse = response.body();
                    assert competitionHistoryListResponse != null;
                    competitionHistories.addAll(competitionHistoryListResponse);
                    competitionHistoryListAdapter.notifyDataSetChanged();
                } else {
                    openPopup("Error", "Error code: " + response.code(), Styles.FAILED);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<CompetitionHistoryListResponse>> call, @NonNull Throwable t) {
                openPopup("Error", "Error code: " + t.getMessage(), Styles.FAILED);
            }

        });

        competitionHistoryList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) competitionHistoryList.getLayoutManager();
                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == competitionHistories.size() - 1) {
                        isLoading = true;
                        getMoreData();
                    }
                }
            }
        });
    }
    private void getMoreData() {
        competitionHistories.add(null);
        competitionHistories.remove(competitionHistories.size() - 1);
        temp = searchBar.getText().toString();
        pageNum++;
        fetchSpecificCompetitionHistoryList(temp, pageNum);
        isLoading = false;
    }
    private void fetchSpecificCompetitionHistoryList(String searchTerm, Integer pageNum) {
        Api.initGetCompetitionHistoryList(headers, pageNum);
        Api.getCompetitionHistoryList.clone().enqueue(new Callback<List<CompetitionHistoryListResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<CompetitionHistoryListResponse>> call, @NonNull Response<List<CompetitionHistoryListResponse>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().size() != 0) {
                    competitionHistoryListResponse = response.body();
                    assert competitionHistoryListResponse != null;
                    filterCompetitionHistories(searchTerm);
                    competitionHistoryListAdapter.notifyDataSetChanged();
                } else {
                    openPopup("Error", "Error code: " + response.code(), Styles.FAILED);
                }
            }
            @Override
            public void onFailure(@NonNull Call<List<CompetitionHistoryListResponse>> call, @NonNull Throwable t) {
                openPopup("Error", "Error code: " + t.getMessage(), Styles.FAILED);
            }

        });
    }
    private void filterCompetitionHistories(String searchTerm) {
        for (CompetitionHistoryListResponse history : competitionHistoryListResponse) {
            if (history.getTitle().toLowerCase().contains(searchTerm.toLowerCase())) {
                competitionHistories.add(history);
                if (competitionHistories.size() < 3) {
                    getMoreData();
                }
            }
        }
    }
    private void openPopup(String heading, String description, Styles styles) {
        PopupDialog.getInstance(this)
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


}

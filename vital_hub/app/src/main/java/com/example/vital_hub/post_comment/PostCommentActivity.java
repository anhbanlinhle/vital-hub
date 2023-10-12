package com.example.vital_hub.post_comment;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vital_hub.R;
import com.example.vital_hub.home_page.HomePagePost;

import java.util.ArrayList;

public class PostCommentActivity extends AppCompatActivity {
    private RecyclerView cmtRecycler;
    private ArrayList<Comment> arrayList;
    private boolean isLoading = false;
    CommentRecyclerAdapter recyclerAdapter;

    private ImageButton back_button;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_comment_layout);

        back_button = findViewById(R.id.back_button);

        back_button.setOnClickListener(view -> finish());

        arrayList = new ArrayList<>();

        cmtRecycler = findViewById(R.id.comment_recycler);

        arrayList.add(new Comment(new HomePagePost(R.drawable.ic_launcher_background, R.drawable.app_icon, "title", "message")));

        populateData(0);

        recyclerAdapter = new CommentRecyclerAdapter(arrayList);
        cmtRecycler.setAdapter(recyclerAdapter);
        cmtRecycler.setLayoutManager(new LinearLayoutManager(this));

        cmtRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) cmtRecycler.getLayoutManager();
                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == arrayList.size() - 1) {
                        isLoading = true;
                        getMoreData();
                    }
                }
            }
        });
    }

    private void populateData(int currentSize) {
        currentSize++;
        int nextSize = currentSize + 10;
        for (; currentSize < nextSize; currentSize++) {
            arrayList.add(new Comment(R.drawable.ic_launcher_background, String.valueOf(currentSize), "Comment lorem ifsum"));
        }
        arrayList.add(null);
    }

    private void getMoreData() {
        // ADD DATA FROM DB
        arrayList.remove(arrayList.size() - 1);
        populateData(arrayList.size());
        recyclerAdapter.notifyDataSetChanged();
        isLoading = false;
    }
}

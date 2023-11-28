package com.example.vital_hub.home_page;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Objects;

public class BottomPaddingDecoration extends RecyclerView.ItemDecoration {
    private int bottomPadding;

    public BottomPaddingDecoration(int bottomPadding) {
        this.bottomPadding = bottomPadding;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        int itemPosition = parent.getChildAdapterPosition(view);
        int itemCount = Objects.requireNonNull(parent.getAdapter()).getItemCount();

        if (itemPosition == itemCount - 1) {
            outRect.bottom = bottomPadding;
        }
    }
}


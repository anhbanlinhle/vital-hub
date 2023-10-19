package com.example.vital_hub.helper;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public abstract class EndlessScrollListener extends RecyclerView.OnScrollListener {

    private static final int VISIBLE_THRESHOLD = 1; // Ngưỡng số lượng item còn lại trước khi tải thêm dữ liệu
    private int previousTotal = 0; // Tổng số item đã hiển thị trước đó
    private boolean isLoading = false; // Trạng thái tải dữ liệu
    private LinearLayoutManager layoutManager;

    public EndlessScrollListener(LinearLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }


    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {

        if (dy > 0) {
            int visibleItemCount = recyclerView.getChildCount();
            int totalItemCount = layoutManager.getItemCount();
            int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

            // Kiểm tra xem đang tải dữ liệu hay không
            if (isLoading) {
                if (totalItemCount > previousTotal) {
                    isLoading = false;
                    previousTotal = totalItemCount;
                }
            }

            // Kiểm tra xem đã cuộn đến vị trí cần tải dữ liệu mới chưa
            if (!isLoading && (totalItemCount - visibleItemCount) <= (firstVisibleItemPosition + VISIBLE_THRESHOLD)) {
                onLoadMore();
                isLoading = true;
            }
        }
    }

    // Phương thức này được gọi khi cần tải thêm dữ liệu
    public abstract void onLoadMore();
}
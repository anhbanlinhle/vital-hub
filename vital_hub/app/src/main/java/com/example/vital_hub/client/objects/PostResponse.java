package com.example.vital_hub.client.objects;

import com.example.vital_hub.home_page.HomePagePost;

public class PostResponse {
    private HomePagePost[] data;

    public PostResponse(HomePagePost[] data) {
        this.data = data;
    }

    public HomePagePost[] getData() {
        return data;
    }
}

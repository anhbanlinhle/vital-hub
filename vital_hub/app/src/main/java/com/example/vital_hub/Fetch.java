package com.example.vital_hub;

import androidx.appcompat.app.AppCompatActivity;

import com.example.vital_hub.client.Api;
import com.example.vital_hub.client.ResponseObject;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fetch extends AppCompatActivity {
    TextView result;
    Button getSingle;
    Button getMultiple;
    Button post;
    Button put;
    Button header;
    EditText title;
    EditText content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fetch);

        result = findViewById(R.id.result);
        getSingle = findViewById(R.id.getSingle);
        getMultiple = findViewById(R.id.getMultiple);
        post = findViewById(R.id.post);
        put = findViewById(R.id.put);
        header = findViewById(R.id.header);
        title = findViewById(R.id.title);
        content = findViewById(R.id.content);

        getSingle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchSingleGet();
            }
        });

        getMultiple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchMultipleGet();
            }
        });

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchPost();
            }
        });
    }

    private void fetchSingleGet() {
        Api.getSingle.clone().enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                if (!response.isSuccessful()) {
                    result.setText("Code: " + response.code());
                    return;
                }

                ResponseObject object = response.body();

                String content = "";
                content += object.getParam1() +"\n"
                        + object.getParam2() + "\n"
                        + object.getParam3();
                result.setText(content);
            }
            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {
                result.setText(t.getMessage());
            }
        });
    }

    private void fetchMultipleGet() {
        Api.getMultiple.clone().enqueue(new Callback<List<ResponseObject>>() {
            @Override
            public void onResponse(Call<List<ResponseObject>> call, Response<List<ResponseObject>> response) {
                if (!response.isSuccessful()) {
                    result.setText("Code: " + response.code());
                    return;
                }

                List<ResponseObject> objects = response.body();

                String content = "";
                for (ResponseObject object: objects) {
                    content += object.getParam1() +"\n"
                            + object.getParam2() + "\n"
                            + object.getParam3() + "\n\n";
                }
                result.setText(content);
            }
            @Override
            public void onFailure(Call<List<ResponseObject>> call, Throwable t) {
                result.setText(t.getMessage());
            }
        });
    }

    private void fetchPost() {

    }
}
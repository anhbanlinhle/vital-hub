package com.example.vital_hub;

import static com.example.vital_hub.client.controller.Api.getHeader;
import static com.example.vital_hub.client.controller.Api.initGetHeader;
import static com.example.vital_hub.client.controller.Api.initGetMultiple;
import static com.example.vital_hub.client.controller.Api.initGetSingle;
import static com.example.vital_hub.client.controller.Api.initPost;
import static com.example.vital_hub.client.controller.Api.initPut;
import static com.example.vital_hub.client.controller.Api.postRequest;
import static com.example.vital_hub.client.controller.Api.putRequest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.example.vital_hub.client.controller.Api;
import com.example.vital_hub.client.objects.ResponseObject;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    EditText param1;
    EditText param2;
    SwitchCompat param3;
    String jwt;
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
        param1 = findViewById(R.id.param1);
        param2 = findViewById(R.id.param2);
        param3 = findViewById(R.id.param3);

        param1.addTextChangedListener(requestTextWatcher);
        param2.addTextChangedListener(requestTextWatcher);
        post.setEnabled(false);
        put.setEnabled(false);

        SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);
        jwt = prefs.getString("jwt", null);


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

        put.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchPut();
            }
        });

        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchHeader();
            }
        });
    }

    private void fetchSingleGet() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + jwt);
        initGetSingle(headers);
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
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + jwt);
        initGetMultiple(headers);
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
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + jwt);
        ResponseObject object = new ResponseObject(
                param1.getText().toString(),
                Integer.parseInt(param2.getText().toString()),
                param3.isChecked());
        initPost(headers, object);

        postRequest.clone().enqueue(new Callback<ResponseObject>() {
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
                            + object.getParam3() + "\n\n";
                result.setText(content);
            }

            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {
                result.setText(t.getMessage());
            }
        });
    }

    private void fetchPut() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + jwt);
        ResponseObject object = new ResponseObject(
                param1.getText().toString(),
                Integer.parseInt(param2.getText().toString()),
                param3.isChecked());
        initPut(headers, object);

        putRequest.clone().enqueue(new Callback<ResponseObject>() {
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
                        + object.getParam3() + "\n\n";
                result.setText(content);
            }

            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {
                result.setText(t.getMessage());
            }
        });
    }

    private void fetchHeader() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + jwt);
        initGetHeader(headers);
        getHeader.clone().enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                if (!response.isSuccessful()) {
                    result.setText("Code: " + response.code());
                    return;
                }
                ResponseObject object = response.body();

                result.setText(object.getData());
            }

            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {
                result.setText(t.getMessage());
            }
        });
    }

    private TextWatcher requestTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String input1 = param1.getText().toString().trim();
            String input2 = param2.getText().toString().trim();

            post.setEnabled(!input1.isEmpty() && !input2.isEmpty());
            put.setEnabled(!input1.isEmpty() && !input2.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable editable) {
            String input1 = param1.getText().toString().trim();
            String input2 = param2.getText().toString().trim();

            post.setEnabled(!input1.isEmpty() && !input2.isEmpty());
            put.setEnabled(!input1.isEmpty() && !input2.isEmpty());
        }
    };
}
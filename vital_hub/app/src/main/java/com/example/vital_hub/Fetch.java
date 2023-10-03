package com.example.vital_hub;

import static com.example.vital_hub.client.Api.call;

import androidx.appcompat.app.AppCompatActivity;
import com.example.vital_hub.client.ResponseObject;

import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fetch extends AppCompatActivity {
    TextView result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fetch);

        result = findViewById(R.id.result);

        call.enqueue(new Callback<List<ResponseObject>>() {
            @Override
            public void onResponse(Call<List<ResponseObject>> call, Response<List<ResponseObject>> response) {
                if (!response.isSuccessful()) {
                    result.setText("Code: " + response.code());
                    return;
                }

                List<ResponseObject> objects = response.body();

                for (ResponseObject object: objects) {
                    String content = "";
                    content += object.getId() +"\n"
                        + object.getGmail() + "\n\n";
                    result.append(content);
                }
            }
            @Override
            public void onFailure(Call<List<ResponseObject>> call, Throwable t) {
                result.setText(t.getMessage());
            }
        });
    }
}
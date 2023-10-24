package com.example.vital_hub.test;

import static com.example.vital_hub.client.controller.Api.initRetrofitAndController;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.vital_hub.R;

public class TestServer extends AppCompatActivity {
    Button commit;
    TextView ip1, ip2, ip3, ip4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_server);

        ip1 = findViewById(R.id.ip1);
        ip2 = findViewById(R.id.ip2);
        ip3 = findViewById(R.id.ip3);
        ip4 = findViewById(R.id.ip4);
        commit = findViewById(R.id.commit);

        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ip = ip1.getText().toString() + "." + ip2.getText().toString() + "." + ip3.getText().toString() + "." + ip4.getText().toString();
                SharedPreferences.Editor editor = getSharedPreferences("UserData", MODE_PRIVATE).edit();
                editor.putString("server", ip);
                editor.apply();
                SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);
                initRetrofitAndController(prefs.getString("server", "10.0.2.2"));
                finish();
            }
        });
    }
}
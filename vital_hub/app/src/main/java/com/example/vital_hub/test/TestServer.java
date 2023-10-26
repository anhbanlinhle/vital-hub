package com.example.vital_hub.test;

import static com.example.vital_hub.client.controller.Api.initRetrofitAndController;
import static com.example.vital_hub.client.fastapi.controller.VideoApi.initFastapi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.vital_hub.R;

public class TestServer extends AppCompatActivity {
    Button commit, clear;
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
        clear = findViewById(R.id.clear);

        SharedPreferences.Editor editor = getSharedPreferences("UserData", MODE_PRIVATE).edit();
        SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);

        ip1.addTextChangedListener(requestTextWatcher);
        ip2.addTextChangedListener(requestTextWatcher);
        ip3.addTextChangedListener(requestTextWatcher);
        ip4.addTextChangedListener(requestTextWatcher);
        commit.setEnabled(false);

        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ip = ip1.getText().toString() + "." + ip2.getText().toString() + "." + ip3.getText().toString() + "." + ip4.getText().toString();
                editor.putString("server", ip);
                editor.apply();
                initRetrofitAndController(prefs.getString("server", "10.0.2.2"));
                initFastapi(prefs.getString("server", "10.0.2.2"));
                finish();
            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.remove("server");
                editor.apply();
                initRetrofitAndController(prefs.getString("server", "10.0.2.2"));
                initFastapi(prefs.getString("server", "10.0.2.2"));
                finish();
            }
        });
    }

    private TextWatcher requestTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String input1 = ip1.getText().toString().trim();
            String input2 = ip2.getText().toString().trim();
            String input3 = ip3.getText().toString().trim();
            String input4 = ip4.getText().toString().trim();

            commit.setEnabled(!input1.isEmpty() && !input2.isEmpty() && !input3.isEmpty() && !input4.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable editable) {
            String input1 = ip1.getText().toString().trim();
            String input2 = ip2.getText().toString().trim();
            String input3 = ip3.getText().toString().trim();
            String input4 = ip4.getText().toString().trim();

            commit.setEnabled(!input1.isEmpty() && !input2.isEmpty() && !input3.isEmpty() && !input4.isEmpty());
        }
    };
}
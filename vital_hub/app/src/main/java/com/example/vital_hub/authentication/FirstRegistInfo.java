package com.example.vital_hub.authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.example.vital_hub.R;

public class FirstRegistInfo extends AppCompatActivity implements TextWatcher {

    String[] sex = {"MALE", "FEMALE"};

    AutoCompleteTextView editSex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_regist_info);

        this.firstDeclaration();
    }

    private void firstDeclaration() {
        editSex = (AutoCompleteTextView) findViewById(R.id.edit_sex);
        editSex.addTextChangedListener(this);
        editSex.setAdapter(new ArrayAdapter<>(this,android.R.layout.simple_dropdown_item_1line, sex));
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
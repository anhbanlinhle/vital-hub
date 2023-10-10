package com.example.vital_hub.authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.vital_hub.R;
import com.example.vital_hub.client.objects.RegistRequestObject;
import com.example.vital_hub.utils.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirstRegistInfo extends AppCompatActivity implements TextWatcher {

    private final String[] sex = {"MALE", "FEMALE"};

    private AutoCompleteTextView editSex;

    private EditText name;
    private EditText phone;
    private EditText dob;
    private EditText height;
    private EditText weight;
    private EditText exerciseDays;
    private EditText description;

    private TextView sexWarning;
    private TextView phoneWarning;
    private TextView nameWarning;
    private TextView dobWarning;

    private TextView inadequateInfoWarning;

    private List<EditText> requiredFieldsList;

    private Button button;
    SharedPreferences prefs;
    String jwt;
    Map<String, String> headers;
    RegistRequestObject body;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_regist_info);

        this.firstDeclaration();
    }

    private void firstDeclaration() {
        name = (EditText) findViewById(R.id.edit_name);
        phone = (EditText) findViewById(R.id.edit_phone);
        dob = (EditText) findViewById(R.id.edit_dob);
        editSex = (AutoCompleteTextView) findViewById(R.id.edit_sex);
        height = (EditText) findViewById(R.id.edit_height);
        weight = (EditText) findViewById(R.id.edit_weight);
        exerciseDays = (EditText) findViewById(R.id.edit_exercise_day);
        description = (EditText) findViewById(R.id.edit_description);

        sexWarning = (TextView) findViewById(R.id.sex_warning);
        phoneWarning = (TextView) findViewById(R.id.phone_warning);
        nameWarning = (TextView) findViewById(R.id.name_warning);
        dobWarning = (TextView) findViewById(R.id.dob_warning);
        inadequateInfoWarning = (TextView) findViewById(R.id.warning_inadequate_info);

        requiredFieldsList = new ArrayList<>();
        requiredFieldsList.add(name);
        requiredFieldsList.add(phone);
        requiredFieldsList.add(editSex);

        button = (Button) findViewById(R.id.btn_submit);


        name.requestFocus();

        this.editSexSetting();
        this.requiredFieldsSetting();
        this.buttonSetting();
    }

    private void editSexSetting() {
        editSex.addTextChangedListener(this);
        editSex.setAdapter(new ArrayAdapter<>(this,android.R.layout.simple_dropdown_item_1line, sex));
        editSex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editSex.showDropDown();
            }
        });
        editSex.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    editSex.showDropDown();
                } else {
                    if (StringUtil.isEmpty(editSex.getText())) {
                        sexWarning.setText("Invalid sex");
                    } else {
                        sexWarning.setText(null);
                        if (adequateInformation()) {
                            inadequateInfoWarning.setText(null);
                        }
                    }
                }
            }
        });
    }

    private void requiredFieldsSetting() {
        name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    if (StringUtil.isEmpty(name.getText())) {
                        nameWarning.setText("Invalid name");
                    } else {
                        nameWarning.setText(null);
                        if (adequateInformation()) {
                            inadequateInfoWarning.setText(null);
                        }
                    }
                }
            }
        });

        phone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    if (StringUtil.isEmpty(phone.getText())) {
                        phoneWarning.setText("Invalid phone number");
                    } else {
                        phoneWarning.setText(null);
                        if (adequateInformation()) {
                            inadequateInfoWarning.setText(null);
                        }
                    }
                }
            }
        });

        dob.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    if (StringUtil.isEmpty(dob.getText())) {
                        dobWarning.setText("Invalid phone number");
                    } else {
                        dobWarning.setText(null);
                        if (adequateInformation()) {
                            inadequateInfoWarning.setText(null);
                        }
                    }
                }
            }
        });
    }

    private void buttonSetting() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (adequateInformation()) {

                } else {
                    inadequateInfoWarning.setText("Please fill all the fields required");
                }
            }
        });
    }

    private boolean adequateInformation() {
        for (EditText editText : requiredFieldsList) {
            if (StringUtil.isEmpty(editText.getText())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        String content = editable.toString();
        if (!Arrays.asList(sex).contains(content)) {
            sexWarning.setText("Invalid sex");
        } else {
            sexWarning.setText(null);
            if (adequateInformation()) {
                inadequateInfoWarning.setText(null);
            }
        }
    }


}
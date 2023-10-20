package com.example.vital_hub.authentication;

import static com.example.vital_hub.client.controller.Api.initPostRegist;
import static com.example.vital_hub.client.controller.Api.postRegist;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vital_hub.MainActivity;
import com.example.vital_hub.R;
import com.example.vital_hub.client.objects.RegistRequestObject;
import com.example.vital_hub.client.objects.RegistResponseObject;
import com.example.vital_hub.utils.StringUtil;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FirstRegistInfo extends AppCompatActivity implements TextWatcher {

    private final String[] sex = {"MALE", "FEMALE"};

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private AutoCompleteTextView editSex;

    private EditText name;
    private EditText phone;
    private EditText dob;
    private EditText height;
    private EditText weight;
    private EditText exerciseDays;
    private EditText description;

    final Calendar myCalendar = Calendar.getInstance();

    DatePickerDialog.OnDateSetListener dateSetListener;

    private TextView sexWarning;
    private TextView phoneWarning;
    private TextView nameWarning;
    private TextView dobWarning;

    private TextView inadequateInfoWarning;

    private List<EditText> requiredFieldsList;
    Intent signInIntent;
    private Button button;
    SharedPreferences prefs;
    String jwt;
    Map<String, String> headers;
    RegistRequestObject body;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_regist_info);
        signInIntent = getIntent();
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
        requiredFieldsList.add(dob);

        button = (Button) findViewById(R.id.btn_submit);

        dateSetListener = (view, year, month, day) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH,month);
            myCalendar.set(Calendar.DAY_OF_MONTH,day);
            updateLabel();
        };

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
                        dobWarning.setText("Invalid date of birth");
                    } else {
                        dobWarning.setText(null);
                        if (adequateInformation()) {
                            inadequateInfoWarning.setText(null);
                        }
                    }
                }
            }
        });

        dob.setOnClickListener(v -> {
            new DatePickerDialog(FirstRegistInfo.this,
                    dateSetListener,
                    myCalendar.get(Calendar.YEAR),
                    myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH))
                    .show();
        });
    }

    private void buttonSetting() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (adequateInformation()) {
                    initHeadersAndBodyForRequest();
                    initPostRegist(headers, body);
                    postRegist.clone().enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.code() != 200) {
                                Toast.makeText(FirstRegistInfo.this, "Error occured. Code: " + response.code(), Toast.LENGTH_LONG).show();
                                return;
                            }

                            SharedPreferences.Editor editor = getSharedPreferences("UserData", MODE_PRIVATE).edit();
                            editor.putString("jwt", signInIntent.getStringExtra("jwt"));
                            editor.putString("email", signInIntent.getStringExtra("email"));
                            editor.putString("name", signInIntent.getStringExtra("name"));
                            editor.commit();

                            Intent intent = new Intent(FirstRegistInfo.this, MainActivity.class);
                            startActivity(intent);
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Toast.makeText(FirstRegistInfo.this, t.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });

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

    private void initHeadersAndBodyForRequest() {
        prefs = getSharedPreferences("UserData", MODE_PRIVATE);
        jwt = prefs.getString("jwt", null);
        headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + jwt);

        body = new RegistRequestObject (
                name.getText().toString(),
                phone.getText().toString(),
                dob.getText().toString(),
                editSex.getText().toString(),
                StringUtil.isEmpty(height.getText()) ? null : Double.valueOf(height.getText().toString()),
                StringUtil.isEmpty(weight.getText()) ? null : Double.valueOf(weight.getText().toString()),
                StringUtil.isEmpty(exerciseDays.getText()) ? null : Integer.valueOf(exerciseDays.getText().toString()),
                StringUtil.isEmpty(description.getText()) ? null : description.getText().toString(),
                signInIntent.getStringExtra("email"),
                "cc"
        );
    }

    private void updateLabel(){
        String myFormat="dd/MM/yyyy";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        dob.setText(dateFormat.format(myCalendar.getTime()));
    }

}
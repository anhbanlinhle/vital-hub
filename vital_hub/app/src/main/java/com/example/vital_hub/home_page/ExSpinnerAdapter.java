package com.example.vital_hub.home_page;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.vital_hub.R;
import com.example.vital_hub.client.spring.objects.ExerciseResponse;

import java.util.ArrayList;

public class ExSpinnerAdapter extends ArrayAdapter<ExerciseResponse> {
    private Context ctx;
    private ArrayList<ExerciseResponse> arrayList;

    public ExSpinnerAdapter(Context context, ArrayList<ExerciseResponse> arrayList) {
        super(context,  R.layout.exercise_item, R.id.ex_list_text, arrayList);
        this.ctx = context;
        this.arrayList = arrayList;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.exercise_item, parent, false);

        TextView score = view.findViewById(R.id.ex_list_text);
        ImageView typeIcon = view.findViewById(R.id.ex_list_icon);
        TextView calo = view.findViewById(R.id.calo_text);

        score.setText(arrayList.get(position).getScore());
        calo.setText(arrayList.get(position).getCalo().toString());
        switch (arrayList.get(position).getType()) {
            case "RUNNING":
                typeIcon.setImageResource(R.drawable.running_ex);
                break;
            case "BICYCLING":
                typeIcon.setImageResource(R.drawable.bike_ex);
                break;
            case "PUSHUP":
                typeIcon.setImageResource(R.drawable.pushup_ex);
                break;
            case "GYM":
                typeIcon.setImageResource(R.drawable.gym_ex);
                break;
        }

        return view;
    }
}

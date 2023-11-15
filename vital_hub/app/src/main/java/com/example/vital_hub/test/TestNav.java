package com.example.vital_hub.test;

import com.example.vital_hub.test.test_frags.HomeFragment;
import com.example.vital_hub.test.test_frags.ExerciseFragment;
import com.example.vital_hub.test.test_frags.CompetitionFragment;
import com.example.vital_hub.test.test_frags.ProfileFragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.vital_hub.R;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class TestNav extends AppCompatActivity {
    MeowBottomNavigation bottomNavigation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_nav);

        bottomNavigation = findViewById(R.id.bottomNavigation);
        bottomNavigation.add(new MeowBottomNavigation.Model(1, R.drawable.outline_home_24));
        bottomNavigation.add(new MeowBottomNavigation.Model(2, R.drawable.baseline_fitness_center_24));
        bottomNavigation.add(new MeowBottomNavigation.Model(3, R.drawable.baseline_equalizer_24));
        bottomNavigation.add(new MeowBottomNavigation.Model(4, R.drawable.baseline_person_pin_24));


        replaceFragment(new HomeFragment());
        bottomNavigation.show(1, true);

        navigateThroughScreens();
    }

    void navigateThroughScreens() {
        bottomNavigation.setOnClickMenuListener(new Function1<MeowBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(MeowBottomNavigation.Model model) {
                switch (model.getId()){
                    case 1:
                        replaceFragment(new HomeFragment());
                        break;
                    case 2:
                        replaceFragment(new ExerciseFragment());
                        break;
                    case 3:
                        replaceFragment(new CompetitionFragment());
                        break;
                    case 4:
                        replaceFragment(new ProfileFragment());
                        break;
                }
                return null;
            }
        });
    }

    void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout, fragment);
        transaction.commit();
    }
}
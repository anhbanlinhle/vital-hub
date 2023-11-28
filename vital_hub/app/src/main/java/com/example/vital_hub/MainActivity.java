package com.example.vital_hub;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.transition.Explode;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.vital_hub.competition.CompetitionFragment;
import com.example.vital_hub.exercises.ExerciseGeneralFragment;
import com.example.vital_hub.home_page.HomeFragment;
import com.example.vital_hub.profile.UserProfileFragment;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class MainActivity extends AppCompatActivity {
    MeowBottomNavigation bottomNavigation;
    ConstraintLayout mainLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = this.getWindow();
        window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        window.setEnterTransition(new Explode());
        window.setExitTransition(new Explode());

        setContentView(R.layout.activity_main);

        bottomNavigation = findViewById(R.id.bottomNavigation);
        bottomNavigation.add(new MeowBottomNavigation.Model(1, R.drawable.nav_home));
        bottomNavigation.add(new MeowBottomNavigation.Model(2, R.drawable.nav_exercise));
        bottomNavigation.add(new MeowBottomNavigation.Model(3, R.drawable.nav_competition));
        bottomNavigation.add(new MeowBottomNavigation.Model(4, R.drawable.nav_profile));

        mainLayout = findViewById(R.id.main_layout);

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setNavigationBarColor(ContextCompat.getColor(this, R.color.color_green));
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.color_green));

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
                        replaceFragment(new ExerciseGeneralFragment());
                        break;
                    case 3:
                        replaceFragment(new CompetitionFragment());
                        break;
                    case 4:
                        replaceFragment(new UserProfileFragment());
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
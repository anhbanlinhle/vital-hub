package com.example.vital_hub.exercises;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.vital_hub.R;
import com.example.vital_hub.UserProfile;
import com.example.vital_hub.competition.CompetitionActivity;
import com.example.vital_hub.home_page.HomePageActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class ChooseExerciseActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    private TabLayout tabLayout;
    private TextView back;
    private ViewPager2 viewPager2;

    private ExerciseViewPagerAdapter exerciseViewPagerAdapter;

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_exercise);

        firstDeclaration();
        binding();
    }

    private void firstDeclaration() {
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.exercise);

        back = findViewById(R.id.back_to_home_from_ex);
        tabLayout = findViewById(R.id.tab_layout_exercise);
        viewPager2 = findViewById(R.id.view_pager_exercise);
        exerciseViewPagerAdapter = new ExerciseViewPagerAdapter(this);
        viewPager2.setAdapter(exerciseViewPagerAdapter);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChooseExerciseActivity.this, ExerciseGeneralActivity.class));
            }
        });

        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
            if (position == 0) {
                tab.setText("GROUPS");
            } else {
                tab.setText("EXERCISES");
            }
        }).attach();
    }

    private void binding() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.profile) {
            startActivity(new Intent(getApplicationContext(), UserProfile.class));
            overridePendingTransition(0, 0);
            return true;
        } else if (item.getItemId() == R.id.home) {
            startActivity(new Intent(getApplicationContext(), HomePageActivity.class));
            overridePendingTransition(0, 0);
            return true;
        } else if (item.getItemId() == R.id.exercise) {
            return true;
        } else if (item.getItemId() == R.id.competition) {
            startActivity(new Intent(getApplicationContext(), CompetitionActivity.class));
            overridePendingTransition(0, 0);
            return true;
        } else {
            return false;
        }
    }
}
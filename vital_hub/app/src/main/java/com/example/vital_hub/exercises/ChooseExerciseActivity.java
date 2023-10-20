package com.example.vital_hub.exercises;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.example.vital_hub.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class ChooseExerciseActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager2 viewPager2;

    private ExerciseViewPagerAdapter exerciseViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_exercise);

        firstDeclaration();
        binding();
    }

    private void firstDeclaration() {
        tabLayout = findViewById(R.id.tab_layout_exercise);
        viewPager2 = findViewById(R.id.view_pager_exercise);
        exerciseViewPagerAdapter = new ExerciseViewPagerAdapter(this);
        viewPager2.setAdapter(exerciseViewPagerAdapter);

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
}
package com.example.vital_hub.exercises;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.vital_hub.exercises.fragment.ExerciseFragment;
import com.example.vital_hub.exercises.fragment.GroupFragment;

public class ExerciseViewPagerAdapter extends FragmentStateAdapter {

    private static final int COUNT = 2;
    public ExerciseViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return new GroupFragment();
        } else {
            return new ExerciseFragment();
        }
    }

    @Override
    public int getItemCount() {
        return COUNT;
    }
}

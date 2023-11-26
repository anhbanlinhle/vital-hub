package com.example.vital_hub.exercises;

import android.os.Bundle;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;


import com.example.vital_hub.R;
import com.example.vital_hub.exercises.category.CardStackAdapter;
import com.example.vital_hub.exercises.category.Items;
import com.example.vital_hub.exercises.category.ItemsDiffCallback;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.StackFrom;
import com.yuyakaido.android.cardstackview.SwipeableMethod;

import java.util.ArrayList;
import java.util.List;

public class ExerciseGeneralFragment extends Fragment implements CardStackListener {
    DrawerLayout drawerLayout;
    CardStackView cardStackView;
    CardStackAdapter adapter;
    CardStackLayoutManager manager;
    public ExerciseGeneralFragment() {
        // Required empty public constructor
    }

    public static ExerciseGeneralFragment newInstance() {
        ExerciseGeneralFragment fragment = new ExerciseGeneralFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test_exercise, container, false);

        setUpCardStack(view);
        return view;
    }

    private void setUpCardStack(View view) {
        manager = new CardStackLayoutManager(requireContext(), this);
        manager.setStackFrom(StackFrom.None);
        manager.setVisibleCount(4);
        manager.setTranslationInterval(8.0f);
        manager.setScaleInterval(0.95f);
        manager.setSwipeThreshold(0.3f);
        manager.setMaxDegree(0.0f);
        manager.setDirections(Direction.HORIZONTAL);
        manager.setCanScrollHorizontal(true);
        manager.setCanScrollVertical(false);
        manager.setSwipeableMethod(SwipeableMethod.Manual);
        manager.setOverlayInterpolator(new LinearInterpolator());
        cardStackView = view.findViewById(R.id.card_stack_view);
        cardStackView.setLayoutManager(manager);
        adapter = new CardStackAdapter(createItems());
        cardStackView.setAdapter(adapter);
        RecyclerView.ItemAnimator itemAnimator = cardStackView.getItemAnimator();
        if (itemAnimator instanceof DefaultItemAnimator) {
            ((DefaultItemAnimator) itemAnimator).setSupportsChangeAnimations(false);
        }
    }

    private void paginate() {
        List<Items> old = adapter.getItems();
        List<Items> newSpots = new ArrayList<>(old);
        newSpots.addAll(createItems());
        ItemsDiffCallback callback = new ItemsDiffCallback(old, newSpots);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);
        adapter.setItems(newSpots);
        result.dispatchUpdatesTo(adapter);
    }

    private List<Items> createItems() {
        List<Items> items = new ArrayList<>();
        items.add(new Items("Gym", "Gym"));
        items.add(new Items("Running", "Running"));
        items.add(new Items("Bicycle", "Bicycle"));
        items.add(new Items("Pushup", "Pushup"));
        return items;
    }

    @Override
    public void onCardDragging(Direction direction, float ratio) {

    }

    @Override
    public void onCardSwiped(Direction direction) {
        if (manager.getTopPosition() == adapter.getItemCount() - 5) {
            paginate();
        }
    }

    @Override
    public void onCardRewound() {

    }

    @Override
    public void onCardCanceled() {

    }

    @Override
    public void onCardAppeared(View view, int position) {

    }

    @Override
    public void onCardDisappeared(View view, int position) {

    }
}
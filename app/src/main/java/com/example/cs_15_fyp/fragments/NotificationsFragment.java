package com.example.cs_15_fyp.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.cs_15_fyp.R;

public class NotificationsFragment extends Fragment {

    private RecyclerView notificationsRecycler;

    public NotificationsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);

        // Initialize RecyclerView
        notificationsRecycler = view.findViewById(R.id.notifications_recycler);
        notificationsRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        // TODO: Set your adapter here
        // notificationsRecycler.setAdapter(new YourNotificationsAdapter(...));

        // Optionally, set click listener for back arrow
        view.findViewById(R.id.back_arrow).setOnClickListener(v -> {
            requireActivity().onBackPressed();
        });

        return view;
    }
}
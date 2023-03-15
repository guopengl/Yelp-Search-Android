package com.example.testapplication.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.testapplication.R;
import com.example.testapplication.adapter.ReviewListAdapter;
import com.example.testapplication.data.ReviewData;
import com.example.testapplication.view.MyListView;

import java.util.ArrayList;
import java.util.List;

public class ReviewsFragment extends Fragment {

   private static final String ARG_PARAM1 = "reviewDataList";

   private MyListView lvReview;
   private ReviewListAdapter adapter;

   private ArrayList<ReviewData> reviewDataList;


   public ReviewsFragment() {
      // Required empty public constructor
   }


   public static ReviewsFragment newInstance(ArrayList<ReviewData> reviewDataList) {
      ReviewsFragment fragment = new ReviewsFragment();
      Bundle args = new Bundle();
      args.putSerializable(ARG_PARAM1, reviewDataList);
      fragment.setArguments(args);
      return fragment;
   }

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      if (getArguments() != null) {
         reviewDataList = (ArrayList<ReviewData>) getArguments().getSerializable(ARG_PARAM1);
      }
   }

   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container,
                            Bundle savedInstanceState) {
      // Inflate the layout for this fragment
      return inflater.inflate(R.layout.fragment_reviews, container, false);
   }

   @Override
   public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
      lvReview = view.findViewById(R.id.lv_review);
      adapter = new ReviewListAdapter(getActivity(), reviewDataList);
      lvReview.setAdapter(adapter);

   }
}
package com.example.testapplication.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.testapplication.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PhotoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PhotoFragment extends Fragment {

   private ImageView ivBusinessPhoto;

   private static final String ARG_PARAM1 = "param1";

   private String photoUrl;


   public PhotoFragment() {
      // Required empty public constructor
   }

   public static PhotoFragment newInstance(String photoUrl) {
      PhotoFragment fragment = new PhotoFragment();
      Bundle args = new Bundle();
      args.putString(ARG_PARAM1, photoUrl);
      fragment.setArguments(args);
      return fragment;
   }

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      if (getArguments() != null) {
         photoUrl = getArguments().getString(ARG_PARAM1);
      }
   }

   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container,
                            Bundle savedInstanceState) {
      // Inflate the layout for this fragment
      return inflater.inflate(R.layout.fragment_photo, container, false);
   }

   @Override
   public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
      ivBusinessPhoto = view.findViewById(R.id.iv_business_photo);
      Glide.with(this).load(photoUrl).into(ivBusinessPhoto);
   }
}
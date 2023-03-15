package com.example.testapplication.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.testapplication.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapLocationFragment extends Fragment {

   private static final String ARG_PARAM1 = "latitude";
   private static final String ARG_PARAM2 = "longitude";

   private GoogleMap mGoogleMap;
   private String latitude;
   private String longitude;

   public MapLocationFragment() {
      // Required empty public constructor
   }


   public static MapLocationFragment newInstance(String latitude, String longitude) {
      MapLocationFragment fragment = new MapLocationFragment();
      Bundle args = new Bundle();
      args.putString(ARG_PARAM1, latitude);
      args.putString(ARG_PARAM2, longitude);
      fragment.setArguments(args);
      return fragment;
   }

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      if (getArguments() != null) {
         latitude = getArguments().getString(ARG_PARAM1);
         longitude = getArguments().getString(ARG_PARAM2);
      }
   }

   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container,
                            Bundle savedInstanceState) {
      // Inflate the layout for this fragment

      return inflater.inflate(R.layout.fragment_map_location, container, false);
   }

   @Override
   public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
      SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
      mapFragment.getMapAsync(new OnMapReadyCallback() {
         @Override
         public void onMapReady(@NonNull GoogleMap googleMap) {
            mGoogleMap = googleMap;
            LatLng loc = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
            mGoogleMap.addMarker(new MarkerOptions()
                    .position(loc));
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 15));
         }
      });
   }
}
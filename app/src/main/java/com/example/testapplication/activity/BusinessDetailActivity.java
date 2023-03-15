package com.example.testapplication.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.testapplication.R;
import com.example.testapplication.adapter.TabViewPagerAdapter;
import com.example.testapplication.data.DetailData;
import com.example.testapplication.data.ReviewData;
import com.example.testapplication.fragment.BusinessDetailsFragment;
import com.example.testapplication.fragment.MapLocationFragment;
import com.example.testapplication.fragment.ReviewsFragment;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BusinessDetailActivity extends AppCompatActivity {
   private TextView tvBusinessName;
   private Button btnFacebook, btnTwitter;
   private TabLayout tabLayout;
   private ViewPager viewPager;
   private TabViewPagerAdapter adapter;
   private List<Fragment> fragmentList = new ArrayList<>();;
   private List<String> titleList = new ArrayList<>();

   private ProgressBar pbBusinessDetails;
   private String businessId, businessName, businessUrl;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_business_detail);

      //get the id of the clicked business
      businessId = getIntent().getSerializableExtra("businessId").toString();
      businessName = getIntent().getSerializableExtra("businessName").toString();
      businessUrl = getIntent().getSerializableExtra("businessUrl").toString();
      initView(); //also initialize titleList, set toolbar

      adapter = new TabViewPagerAdapter(getSupportFragmentManager(), titleList);
      viewPager.setAdapter(adapter);
      tabLayout.setupWithViewPager(viewPager);

      setupViewAndFragments();  //two call: 1. first call: create first two fragments,
                                //2. create reviewsFragment

   }

   private void initView() {
      tvBusinessName = findViewById(R.id.tv_business_name);
      btnFacebook = findViewById(R.id.btn_facebook);
      btnTwitter = findViewById(R.id.btn_twitter);
      tabLayout = findViewById(R.id.tab_layout);
      viewPager = findViewById(R.id.view_pager);
      pbBusinessDetails = findViewById(R.id.pb_business_details);

      titleList.add("Business details");
      titleList.add("Map location");
      titleList.add("Reviews");

      tvBusinessName.setText(businessName);
      btnFacebook.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW);
            browserIntent.setData(Uri.parse("https://www.facebook.com/sharer/sharer.php?u="+ businessUrl)); //browserIntent.setData(Uri.parse("https://www.google.com"));
            startActivity(browserIntent);
         }
      });
      btnTwitter.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW);
            browserIntent.setData(Uri.parse("https://twitter.com/share?text=Check " + businessName + " on Yelp."
                    + "&url=" + businessUrl)); //browserIntent.setData(Uri.parse("https://www.google.com"));
            startActivity(browserIntent);
         }
      });
   }

   private void setupViewAndFragments() {
      RequestQueue queue = Volley.newRequestQueue(this);
      String url = "https://yyxz12138-cs571-assign8.wl.r.appspot.com/detail?id="+ businessId;

      JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
              (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                 @Override
                 public void onResponse(JSONObject response) {
                    //create BusinessDetailsFragment
                    String name = "", address = "", phoneNumber = "", category = "",
                            priceRange = "", status = "", link = "";
                    List<String> photosUrlList = new ArrayList<>();

                    try {
                       name = response.getString("name");
                    } catch (JSONException e) {}

                    try {
                       JSONObject location = response.getJSONObject("location");
                       JSONArray displayAddress = location.getJSONArray("display_address");
                       for(int i = 0; i< displayAddress.length(); i++){
                          address += displayAddress.getString(i);
                          if(i != displayAddress.length() - 1){
                             address += ", ";
                          }
                       }
                    } catch (JSONException e) {}

                    try {
                       phoneNumber = response.getString("display_phone");
                       if(phoneNumber.length() == 0){
                          phoneNumber = "N/A";
                       }
                    } catch (JSONException e) {
                       phoneNumber = "N/A";
                    }

                    try {
                       JSONArray categories = response.getJSONArray("categories");
                       for(int i = 0; i< categories.length(); i++){
                          JSONObject mCategory = categories.getJSONObject(i);
                          category += mCategory.getString("title");
                          if(i != categories.length() - 1){
                             category += " | ";
                          }
                       }
                       if(category.length() == 0){
                          category = "N/A";
                       }
                    } catch (JSONException e) {
                       category = "N/A";
                    }

                    try {
                       priceRange = response.getString("price");
                    } catch (JSONException e) {
                       priceRange = "N/A";
                    }

                    try {
                       JSONArray hours = response.getJSONArray("hours");
                       status = hours.getJSONObject(0).getString("is_open_now");
                    } catch (JSONException e) {
                       status = "N/A";
                    }

                    try {
                       link = response.getString("url");
                    } catch (JSONException e) {}

                    try {
                       JSONArray photos = response.getJSONArray("photos");
                       for(int i = 0; i< photos.length(); i++){
                          photosUrlList.add(photos.getString(i));
                       }
                    } catch (JSONException e) {}
                    fragmentList.add(BusinessDetailsFragment.newInstance(new DetailData(businessId ,name, address, phoneNumber, category, priceRange, status, link, photosUrlList)));

                    //create MapLocationFragment
                    String latitude = "", longitude = "";
                    try {
                       JSONObject coordinates = response.getJSONObject("coordinates");
                       latitude = coordinates.getString("latitude");
                       longitude = coordinates.getString("longitude");
                    } catch (JSONException e) {}
                    fragmentList.add(MapLocationFragment.newInstance(latitude,longitude));

                    addReviewsFragment();
                 }
              }, new Response.ErrorListener() {
                 @Override
                 public void onErrorResponse(VolleyError error) {}
              });
      queue.add(jsonObjectRequest);
   }

   private void addReviewsFragment() {
      RequestQueue queue = Volley.newRequestQueue(this);
      String url = "https://yyxz12138-cs571-assign8.wl.r.appspot.com/reviews?id=" + businessId;

      JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
              (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                 @Override
                 public void onResponse(JSONObject response) {
                    try {
                       ArrayList<ReviewData> reviewDataList = new ArrayList<>();
                       JSONArray reviews = response.getJSONArray("reviews");

                       for (int i = 0; i < reviews.length(); i++) {
                          JSONObject review = reviews.getJSONObject(i);
                          JSONObject user = review.getJSONObject("user");

                          String name = user.getString("name"),
                                 rating = review.getString("rating"),
                                 text = review.getString("text"),
                                 timeCreated = review.getString("time_created");
                          reviewDataList.add(new ReviewData(name, rating, text, timeCreated));
                       }
                       fragmentList.add(ReviewsFragment.newInstance(reviewDataList));

                    } catch (JSONException e) {}

                    adapter.setFragmentList(fragmentList);
                    pbBusinessDetails.setVisibility(View.GONE);
                    viewPager.setVisibility(View.VISIBLE);
                 }
              }, new Response.ErrorListener() {
                 @Override
                 public void onErrorResponse(VolleyError error) {}
              });
      queue.add(jsonObjectRequest);
   }


   public void back(View view) {
      super.onBackPressed();
   }

}
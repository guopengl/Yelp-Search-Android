package com.example.testapplication.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.testapplication.R;
import com.example.testapplication.adapter.AutoCompleteAdapter;
import com.example.testapplication.adapter.BusinessListAdapter;
import com.example.testapplication.data.BookingData;
import com.example.testapplication.data.BusinessData;
import com.example.testapplication.view.MyListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
   private AutoCompleteTextView atvKeyword;
   private List<String> autoCompleteWordsList = new ArrayList<>();
   private AutoCompleteAdapter atvAdapter;

   private EditText etDistance, etLocation;
   private Spinner spinnerCategory;
   private CheckBox checkbox;
   private MyListView businessList;
   private BusinessListAdapter adapter;

   private ScrollView svBusinessList;
   private ProgressBar pbAutoComplete, pbBusinessSearch;
   private TextView tvNoResult;

   private String inputKeyword,
           inputDistanceInMiles,
           inputDistanceInMeters,
           inputLocation,
           inputCategory,
           longitude, latitude;
   private boolean checkboxIsChecked;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);
      initView();
      atvAdapter = new AutoCompleteAdapter(this, android.R.layout.simple_list_item_1, autoCompleteWordsList);
      atvKeyword.setAdapter(atvAdapter);
      adapter = new BusinessListAdapter(this);
      businessList.setAdapter(adapter);
   }

   private void initView() {
      atvKeyword = findViewById(R.id.atv_keyword);
      etDistance = findViewById(R.id.et_distance);
      spinnerCategory = findViewById(R.id.spinner_category);
      etLocation = findViewById(R.id.et_location);
      checkbox = findViewById(R.id.checkbox);
      businessList = findViewById(R.id.business_list);

      svBusinessList = findViewById(R.id.sv_business_list);
      pbAutoComplete = findViewById(R.id.pb_auto_complete);
      pbBusinessSearch = findViewById(R.id.pb_business_search);
      tvNoResult = findViewById(R.id.tv_no_result);

      checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
         @Override
         public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
            if (compoundButton.isChecked()) {
               etLocation.setText("");
               etLocation.setVisibility(View.INVISIBLE);
            } else {
               etLocation.setVisibility(View.VISIBLE);
            }
         }
      });

      atvKeyword.addTextChangedListener(new TextWatcher() {
         @Override
         public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

         }

         @Override
         public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

         }

         @Override
         public void afterTextChanged(Editable editable) {
            if (atvKeyword.isPerformingCompletion()) {  //user selects an item
               return;
            }

            String keyword = atvKeyword.getText().toString();
            if (keyword.length() == 0) {
               return;
            }

            pbAutoComplete.setVisibility(View.VISIBLE);
            updateAutoCompleteList(keyword);
         }
      });

   }

   private void updateAutoCompleteList(String keyword) {
      autoCompleteWordsList.clear();

      RequestQueue queue = Volley.newRequestQueue(this);
      String url = "https://yyxz12138-cs571-assign8.wl.r.appspot.com/autocomplete?text=" + keyword;

      JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
              (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                 @Override
                 public void onResponse(JSONObject response) {
                    try {
                       JSONArray categories = response.getJSONArray("categories");
                       JSONArray terms = response.getJSONArray("terms");
                       for (int i = 0; i < categories.length(); i++) {
                          JSONObject category = categories.getJSONObject(i);
                          autoCompleteWordsList.add(category.getString("title"));
                       }
                       for (int i = 0; i < terms.length(); i++) {
                          JSONObject term = terms.getJSONObject(i);
                          autoCompleteWordsList.add(term.getString("text"));
                       }
                    } catch (JSONException e) {
                    }

                    pbAutoComplete.setVisibility(View.INVISIBLE);
                    atvAdapter.notifyDataSetChanged();
                 }
              }, new Response.ErrorListener() {
                 @Override
                 public void onErrorResponse(VolleyError error) {}
              });
      queue.add(jsonObjectRequest);
   }


   /* submit button clicked
   */
   public void submit(View view) {
      //get input value
      inputKeyword = atvKeyword.getText().toString();
      inputDistanceInMiles = etDistance.getText().toString();
      inputLocation = etLocation.getText().toString();
      checkboxIsChecked = checkbox.isChecked();
      inputCategory = getInputCategory();

      //check required input: set error if not filled
      if (!checkInputRequired()) {
         return;
      }

      //get distance in meters
      try {
         double distanceInMeters = Double.parseDouble(inputDistanceInMiles) * 1609.344;
         inputDistanceInMeters = Math.round(distanceInMeters) + "";
      } catch (NumberFormatException exception) {
         etDistance.setError("Entered distance is not a number");
         return;
      }

      svBusinessList.setVisibility(View.GONE);
      pbBusinessSearch.setVisibility(View.VISIBLE);
      tvNoResult.setVisibility(View.GONE);

      if (checkboxIsChecked) {
         makeCallUsingIp();
      } else {
         makeCallUsingInput();
      }

   }

   private String getInputCategory() {
      switch (spinnerCategory.getSelectedItemPosition()) {
         case 0:
            return "All";
         case 1:
            return "arts";
         case 2:
            return "health";
         case 3:
            return "hotelstravel";
         case 4:
            return "food";
         default:
            return "professional";
      }
   }

   private boolean checkInputRequired() {
      if (inputKeyword.length() == 0) {
         atvKeyword.setError("This field is required");
         return false;
      }
      if (inputDistanceInMiles.length() == 0) {
         etDistance.setError("This field is required");
         return false;
      }
      if (!checkboxIsChecked && inputLocation.length() == 0) {
         etLocation.setError("This field is required");
         return false;
      }
      return true;
   }

   private void makeCallUsingIp() {
      RequestQueue queue = Volley.newRequestQueue(this);
      String url = "https://ipinfo.io/?token=";

      JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
              (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                 @Override
                 public void onResponse(JSONObject response) {
                    try {
                       String loc = response.getString("loc");
                       String[] locArray = loc.split(",");  //[lat,long]
                       latitude = locArray[0];
                       longitude = locArray[1];
                       searchYelpCall();
                    } catch (JSONException e) {}  //always success,no error
                 }
              }, new Response.ErrorListener() {
                 @Override
                 public void onErrorResponse(VolleyError error) {}
              });
      queue.add(jsonObjectRequest);
   }

   private void makeCallUsingInput() {
      RequestQueue queue = Volley.newRequestQueue(this);
      String encodedLocation = "";
      try {
         encodedLocation = URLEncoder.encode(inputLocation, StandardCharsets.UTF_8.toString());
      } catch (UnsupportedEncodingException e) {
      }

      String url = String.format("https://yyxz12138-cs571-assign8.wl.r.appspot.com/geocoding?address=%s", encodedLocation);

      JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
              (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                 @Override
                 public void onResponse(JSONObject response) {
                    try {
                       JSONArray results = response.getJSONArray("results");
                       JSONObject result = results.getJSONObject(0);
                       JSONObject geometry = result.getJSONObject("geometry");
                       JSONObject location = geometry.getJSONObject("location");
                       latitude = location.getString("lat");
                       longitude = location.getString("lng");

                       searchYelpCall();
                    } catch (JSONException e) {
                       etLocation.setError("Entered location does not exist");
                       pbBusinessSearch.setVisibility(View.GONE);
                    }
                 }
              }, new Response.ErrorListener() {
                 @Override
                 public void onErrorResponse(VolleyError error) {}
              });
      queue.add(jsonObjectRequest);
   }

   private void searchYelpCall() {
      //https://yyxz12138-cs571-assign8.wl.r.appspot.com/searchyelp?term=sushi&latitude=33.8491816&longitude=-118.3884078&categories=food&radius=8047

      RequestQueue queue = Volley.newRequestQueue(this);
      String url = String.format("https://yyxz12138-cs571-assign8.wl.r.appspot.com/searchyelp?term=%s&latitude=%s&longitude=%s&categories=%s&radius=%s",
              inputKeyword, latitude, longitude, inputCategory, inputDistanceInMeters);

      JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
              (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                 // Log.i("TAG", businesses.toString());
                 @Override
                 public void onResponse(JSONObject response) {
                    ArrayList<BusinessData> businessDatas = new ArrayList<>();
                    try {
                       JSONArray businesses = response.getJSONArray("businesses"); //array
                       for (int i = 0; i < businesses.length(); i++) {
                          JSONObject business = businesses.getJSONObject(i); //object
                          String id = business.getString("id"),
                                  picUrl = business.getString("image_url"),
                                  businessName = business.getString("name"),
                                  rating = business.getString("rating"),
                                  distanceInMiles = business.getString("distance"),
                                  url = business.getString("url");
                          double distanceInmiles = Double.parseDouble(distanceInMiles) / 1609.344;
                          String distanceInMeters = String.format("%.2f", distanceInmiles);
                          businessDatas.add(new BusinessData(id, picUrl, businessName, rating, distanceInMeters, url));
                       }
                    } catch (JSONException e) {}

                    runOnUiThread(new Runnable() {
                       @Override
                       public void run() {
                          pbBusinessSearch.setVisibility(View.GONE);
                          if(businessDatas.size() != 0){
                             svBusinessList.setVisibility(View.VISIBLE);
                             adapter.setBusinessDatas(businessDatas);
                          }
                          else{
                             tvNoResult.setVisibility(View.VISIBLE);
                          }
                       }
                    });
                 }
              }, new Response.ErrorListener() {
                 @Override
                 public void onErrorResponse(VolleyError error) {}
              });
      queue.add(jsonObjectRequest);
   }


   public void clear(View view) {
      atvKeyword.setText("");
      atvKeyword.setError(null);
      etDistance.setText("");
      etDistance.setError(null);
      spinnerCategory.setSelection(0);
      etLocation.setText("");
      etLocation.setError(null);
      etLocation.setVisibility(View.VISIBLE);
      checkbox.setChecked(false);
      adapter.setBusinessDatas(new ArrayList<>());
      tvNoResult.setVisibility(View.GONE);
   }

   public void openBookings(View view) {
      Intent intent = new Intent(MainActivity.this, BookingsActivity.class);
      startActivity(intent);
   }
}
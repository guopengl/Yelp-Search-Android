package com.example.testapplication.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.testapplication.R;
import com.example.testapplication.adapter.BookingRecyclerViewAdapter;
import com.example.testapplication.data.BookingData;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;


public class BookingsActivity extends AppCompatActivity {

   private TextView tvNoBookings;
   private RecyclerView rvBookingList;
   private BookingRecyclerViewAdapter adapter;

   private SharedPreferences sharedPreferences;
   private SharedPreferences.Editor editor;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_bookings);
      initView();

      //List<BookingData> bookingDataList = BookingListData.getInstance().getBookingDataList();
      Gson gson = new Gson();
      String bookingDataListJson = sharedPreferences.getString("bookingDataList","[]");
      Type type = new TypeToken<ArrayList<BookingData>>(){}.getType();
      ArrayList<BookingData> bookingDataList = gson.fromJson(bookingDataListJson,type);

      adapter = new BookingRecyclerViewAdapter(bookingDataList);
      rvBookingList.setLayoutManager(new LinearLayoutManager(this));
      rvBookingList.setAdapter(adapter);

      if(bookingDataList.size() == 0){
         rvBookingList.setVisibility(View.GONE);
         tvNoBookings.setVisibility(View.VISIBLE);
      }

      ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
         @Override
         public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
         }

         @Override
         public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            if(direction == ItemTouchHelper.LEFT){
               bookingDataList.remove(position);
               editor.putString("bookingDataList", new Gson().toJson(bookingDataList)).apply();
               adapter.notifyDataSetChanged();
               Snackbar.make(rvBookingList, "Removing existing Reservation", Snackbar.LENGTH_LONG).show();

               if(bookingDataList.size() == 0){
                  rvBookingList.setVisibility(View.GONE);
                  tvNoBookings.setVisibility(View.VISIBLE);
               }
            }
         }

         @Override
         public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(Color.rgb(204, 0 , 0))
                    .addSwipeLeftActionIcon(R.drawable.ic_delete)
                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
         }
      };

      ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
      itemTouchHelper.attachToRecyclerView(rvBookingList);
   }


   private void initView() {
      rvBookingList = findViewById(R.id.rv_bookingList);
      tvNoBookings = findViewById(R.id.tv_no_bookings);
      sharedPreferences = getSharedPreferences("bookingDataList",MODE_PRIVATE);
      editor = sharedPreferences.edit();
   }

   public void back(View view) {
      super.onBackPressed();
   }
}
package com.example.testapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testapplication.R;
import com.example.testapplication.data.BookingData;

import java.util.List;

public class BookingRecyclerViewAdapter extends RecyclerView.Adapter<BookingRecyclerViewAdapter.ViewHolder> {

   private List<BookingData> bookingDataList;

   public BookingRecyclerViewAdapter(List<BookingData> bookingDataList) {
      this.bookingDataList = bookingDataList;
   }

   @NonNull
   @Override
   public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.booking_item_layout, parent,false);
      ViewHolder viewHolder = new ViewHolder(view);
      return viewHolder;
   }

   @Override
   public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
      holder.tvSerialNumber.setText(position + 1 + "");
      holder.tvBusinessName.setText(bookingDataList.get(position).getBusinessName());
      holder.tvDate.setText(bookingDataList.get(position).getDate());
      holder.tvTime.setText(bookingDataList.get(position).getTime());
      holder.tvEmail.setText(bookingDataList.get(position).getEmail());
   }

   @Override
   public int getItemCount() {
      return bookingDataList.size();
   }

   class ViewHolder extends RecyclerView.ViewHolder{
      TextView tvSerialNumber, tvBusinessName, tvDate, tvTime, tvEmail;

      public ViewHolder(@NonNull View itemView) {
         super(itemView);

         tvSerialNumber = itemView.findViewById(R.id.tv_serial_number);
         tvBusinessName = itemView.findViewById(R.id.tv_business_name);
         tvDate = itemView.findViewById(R.id.tv_date);
         tvTime = itemView.findViewById(R.id.tv_time);
         tvEmail = itemView.findViewById(R.id.tv_email);
      }
   }
}

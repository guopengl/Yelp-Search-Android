package com.example.testapplication.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.testapplication.R;
import com.example.testapplication.adapter.PhotoViewPagerAdapter;
import com.example.testapplication.data.BookingData;
import com.example.testapplication.data.DetailData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BusinessDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BusinessDetailsFragment extends Fragment {

   private TextView tvAddress, tvPhoneNumber, tvCategory, tvPriceRange, tvStatus, tvLink;
   private Button btnReservation;
   private ViewPager viewPagerPhotos;
   private PhotoViewPagerAdapter adapter;

   private SharedPreferences sharedPreferences;
   private SharedPreferences.Editor editor;

   private static final String ARG_PARAM1 = "DetailData";
   private DetailData detailData;

   public BusinessDetailsFragment() {
      // Required empty public constructor
   }

   /**
    * Use this factory method to create a new instance of
    * this fragment using the provided parameters.
    */
   public static BusinessDetailsFragment newInstance(DetailData detailData) {
      BusinessDetailsFragment fragment = new BusinessDetailsFragment();
      Bundle args = new Bundle();
      args.putSerializable(ARG_PARAM1, detailData);
      fragment.setArguments(args);
      return fragment;
   }

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      if (getArguments() != null) {
         detailData = (DetailData) getArguments().getSerializable(ARG_PARAM1);
      }
   }

   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container,
                            Bundle savedInstanceState) {
      // Inflate the layout for this fragment
      return inflater.inflate(R.layout.fragment_business_details, container, false);
   }

   @Override
   public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
      initView(view);
      tvAddress.setText(detailData.getAddress());
      tvPhoneNumber.setText(detailData.getPhoneNumber());
      tvCategory.setText(detailData.getCategory());
      tvPriceRange.setText(detailData.getPriceRange());

      if(detailData.getStatus().equals("true")){
         tvStatus.setText("Open Now");
         tvStatus.setTextColor(Color.GREEN);
      }
      else{
         tvStatus.setText("Closed");
         tvStatus.setTextColor(Color.RED);
      }

      tvLink.setMovementMethod(LinkMovementMethod.getInstance());
      tvLink.setOnClickListener(new View.OnClickListener() {
         public void onClick(View v) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW);
            browserIntent.setData(Uri.parse(detailData.getLink())); //browserIntent.setData(Uri.parse("https://www.google.com"));
            startActivity(browserIntent);
         }
      });

      List<Fragment> fragmentList = new ArrayList<>();
      for(int i = 0; i <detailData.getPhotosUrl().size(); i++){
         //create photo fragments and add them to list
         fragmentList.add(PhotoFragment.newInstance(detailData.getPhotosUrl().get(i)));
      }
      adapter = new PhotoViewPagerAdapter(getChildFragmentManager(),fragmentList);
      viewPagerPhotos.setAdapter(adapter);

      btnReservation.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            showReservationDialog();
         }
      });
   }

   private void showReservationDialog() {
      Dialog dialog = new Dialog(getActivity());
      dialog.setContentView(R.layout.reservation_dialog);

      //UI for dialog
      TextView tvBusinessName = dialog.findViewById(R.id.tv_business_name);
      EditText etEmail = dialog.findViewById(R.id.et_email),
              etDate = dialog.findViewById(R.id.et_date),
              etTime = dialog.findViewById(R.id.et_time);
      Button btnCancel = dialog.findViewById(R.id.btn_cancel),
              btnSubmit = dialog.findViewById(R.id.btn_submit);

      tvBusinessName.setText(detailData.getName());

      //open date picker dialog
      etDate.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            showDatePickerDialog(etDate);
         }
      });

      //open time picker dialog
      etTime.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            showTimePickerDialog(etTime);
         }
      });

      btnCancel.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            dialog.dismiss();
         }
      });

      btnSubmit.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            String name = detailData.getName(),
                    email = etEmail.getText().toString(),
                    date = etDate.getText().toString(),
                    time = etTime.getText().toString();
            //check input validation
            if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
               Toast.makeText(getContext(), "Invalid Email Address", Toast.LENGTH_LONG).show();
               dialog.dismiss();
               return;
            }
            if(date.length() == 0){
               Toast.makeText(getContext(), "Date input required", Toast.LENGTH_LONG).show();
               dialog.dismiss();
               return;
            }
            if(!isValidTime(time)){
               Toast.makeText(getContext(), "Time should be between 10AM AND 5PM",Toast.LENGTH_LONG).show();
               dialog.dismiss();
               return;
            }

            Gson gson = new Gson();
            String bookingDataListJson = sharedPreferences.getString("bookingDataList","[]");
            Type type = new TypeToken<ArrayList<BookingData>>(){}.getType();
            ArrayList<BookingData> bookingDataList = gson.fromJson(bookingDataListJson,type);

            int i = 0;
            for (i = 0; i < bookingDataList.size(); i++) {
               if(bookingDataList.get(i).getId().equals(detailData.getId())){  //duplicate book, update it
                  bookingDataList.set(i, new BookingData(detailData.getId(), name, date, time, email));
                  editor.putString("bookingDataList", gson.toJson(bookingDataList)).apply();
                  break;
               }
            }

            if(i == bookingDataList.size()){  //no duplicate
               bookingDataList.add(new BookingData(detailData.getId(),name, date, time, email));
               editor.putString("bookingDataList", gson.toJson(bookingDataList)).apply();
            }

            Toast.makeText(getContext(), "Reservation Booked", Toast.LENGTH_LONG).show();
            dialog.dismiss();
         }
      });
      dialog.show();
   }

   private void showDatePickerDialog(EditText etDate) {
      Calendar calendar = Calendar.getInstance();
      int year = calendar.get(Calendar.YEAR);
      int month = calendar.get(Calendar.MONTH);
      int day = calendar.get(Calendar.DAY_OF_MONTH);
      DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
         @Override
         public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            month++;
            String monthString = month >=10 ? ""+month : "0"+month;
            String dayString =  day >=10 ? ""+day : "0"+day;
            etDate.setText(monthString +"-"+ dayString + "-" +year);
         }
      }, year, month, day);
      datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
      datePickerDialog.show();
   }
   private void showTimePickerDialog(EditText etTime) {
      Calendar calendar = Calendar.getInstance();
      int hour = calendar.get(Calendar.HOUR_OF_DAY);
      int minute = calendar.get(Calendar.MINUTE);

      TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
         @Override
         public void onTimeSet(TimePicker timePicker, int hour, int minute) {
            String minuteString = minute >=10 ? ""+minute : "0"+minute;
            etTime.setText(hour + ":" + minuteString);
         }
      },hour, minute, false);
      timePickerDialog.show();
   }

   private boolean isValidTime(String time) {
      if(time.length() == 0) return false;
      String[] hourAndMinute = time.split(":");
      int hour = Integer.parseInt(hourAndMinute[0]);
      if(hour < 10 || hour > 17) return false;
      else return true;
   }


   private void initView(View view) {
      tvAddress = view.findViewById(R.id.tv_address);
      tvPhoneNumber = view.findViewById(R.id.tv_phone_number);
      tvCategory = view.findViewById(R.id.tv_category);
      tvPriceRange = view.findViewById(R.id.tv_price_range);
      tvStatus = view.findViewById(R.id.tv_status);
      tvLink = view.findViewById(R.id.tv_link);
      viewPagerPhotos = view.findViewById(R.id.view_pager_photos);
      btnReservation = view.findViewById(R.id.btn_reservation);

      sharedPreferences = getContext().getSharedPreferences("bookingDataList", Context.MODE_PRIVATE);
      editor = sharedPreferences.edit();
   }


}
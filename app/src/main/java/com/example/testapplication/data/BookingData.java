package com.example.testapplication.data;

public class BookingData {
   private String id, businessName, date, time, email;

   public BookingData(String id, String businessName, String date, String time, String email) {
      this.id = id;
      this.businessName = businessName;
      this.date = date;
      this.time = time;
      this.email = email;
   }

   public String getId() {
      return id;
   }

   public String getBusinessName() {
      return businessName;
   }

   public String getDate() {
      return date;
   }

   public String getTime() {
      return time;
   }

   public String getEmail() {
      return email;
   }
}

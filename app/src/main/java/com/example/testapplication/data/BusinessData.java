package com.example.testapplication.data;

import java.io.Serializable;

public class BusinessData{
   private String id, picUrl, businessName, rating, distance, url;

   public BusinessData(String id, String picUrl, String businessName, String rating, String distance ,String url) {
      this.id = id;
      this.picUrl = picUrl;
      this.businessName = businessName;
      this.rating = rating;
      this.distance = distance;
      this.url = url;
   }

   public String getId() {
      return id;
   }

   public String getPicUrl() {
      return picUrl;
   }

   public String getBusinessName() {
      return businessName;
   }

   public String getRating() {
      return rating;
   }

   public String getDistance() {
      return distance;
   }

   public String getUrl() {
      return url;
   }
}

package com.example.testapplication.data;

import java.io.Serializable;
import java.util.List;

public class DetailData implements Serializable {
   private String id, name, address, phoneNumber, category, priceRange, status, link;
   private List<String> photosUrlList;

   public DetailData( String id,
           String name, String address, String phoneNumber, String category, String priceRange, String status, String link, List<String> photosUrlList) {
      this.id = id;
      this.name = name;
      this.address = address;
      this.phoneNumber = phoneNumber;
      this.category = category;
      this.priceRange = priceRange;
      this.status = status;
      this.link = link;
      this.photosUrlList = photosUrlList;
   }

   public String getId() {
      return id;
   }

   public String getName() {
      return name;
   }

   public String getAddress() {
      return address;
   }

   public String getPhoneNumber() {
      return phoneNumber;
   }

   public String getCategory() {
      return category;
   }

   public String getPriceRange() {
      return priceRange;
   }

   public String getStatus() {
      return status;
   }

   public String getLink() {
      return link;
   }

   public List<String> getPhotosUrl() {
      return photosUrlList;
   }
}

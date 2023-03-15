package com.example.testapplication.data;

import java.io.Serializable;

public class ReviewData implements Serializable {
   private String name, rating, text, timeCreated;

   public ReviewData(String name, String rating, String text, String timeCreated) {
      this.name = name;
      this.rating = rating;
      this.text = text;
      this.timeCreated = timeCreated;
   }

   public String getName() {
      return name;
   }

   public String getRating() {
      return rating;
   }

   public String getText() {
      return text;
   }

   public String getTimeCreated() {
      return timeCreated;
   }
}

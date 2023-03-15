package com.example.testapplication.adapter;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;

import androidx.annotation.NonNull;

import java.util.List;

public class AutoCompleteAdapter extends ArrayAdapter<String> {
   public AutoCompleteAdapter(@NonNull Context context, int resource) {
      super(context, resource);
   }

   public AutoCompleteAdapter(@NonNull Context context, int resource, int textViewResourceId) {
      super(context, resource, textViewResourceId);
   }

   public AutoCompleteAdapter(@NonNull Context context, int resource, @NonNull String[] objects) {
      super(context, resource, objects);
   }

   public AutoCompleteAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull String[] objects) {
      super(context, resource, textViewResourceId, objects);
   }

   public AutoCompleteAdapter(@NonNull Context context, int resource, @NonNull List<String> objects) {
      super(context, resource, objects);
   }

   public AutoCompleteAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull List<String> objects) {
      super(context, resource, textViewResourceId, objects);
   }

   @NonNull
   @Override
   public Filter getFilter() {
      return new Filter() {
         @Override
         protected FilterResults performFiltering(CharSequence constraint) {
            return null;
         }

         @Override
         protected void publishResults(CharSequence constraint, FilterResults results) {
         }
      };
   }
}

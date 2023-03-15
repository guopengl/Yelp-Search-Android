package com.example.testapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.testapplication.R;
import com.example.testapplication.activity.BusinessDetailActivity;
import com.example.testapplication.data.BusinessData;
import com.example.testapplication.data.ReviewData;

import java.util.List;

public class ReviewListAdapter extends BaseAdapter {

   private Context context;
   private List<ReviewData> reviewDataList;

   public ReviewListAdapter(Context context, List<ReviewData> reviewDataList) {
      this.context = context;
      this.reviewDataList = reviewDataList;
   }

   @Override
   public int getCount() {
      return reviewDataList.size();
   }

   @Override
   public Object getItem(int i) {
      return reviewDataList.get(i);
   }

   @Override
   public long getItemId(int i) {
      return i;
   }

   @Override
   public View getView(int i, View view, ViewGroup viewGroup) {
      ReviewListAdapter.ViewHolder viewHolder = null;
      if(view == null){
         view = LayoutInflater.from(context).inflate(R.layout.review_item_layout, null);
         viewHolder = new ReviewListAdapter.ViewHolder();
         viewHolder.tvName = view.findViewById(R.id.tv_name);
         viewHolder.tvRating = view.findViewById(R.id.tv_rating);
         viewHolder.tvText = view.findViewById(R.id.tv_text);
         viewHolder.tvTimeCreated = view.findViewById(R.id.tv_time_created);

         view.setTag(viewHolder);
      }
      else{
         viewHolder = (ReviewListAdapter.ViewHolder) view.getTag();
      }

      ReviewData reviewData = reviewDataList.get(i); //one business data to be used
      //set
      viewHolder.tvName.setText(reviewData.getName());
      viewHolder.tvRating.setText("Ratings: " + reviewData.getRating() + "/5");
      viewHolder.tvText.setText(reviewData.getText());

      String[] dateAndTime = reviewData.getTimeCreated().split(" ");
      viewHolder.tvTimeCreated.setText(dateAndTime[0]);

      return view;
   }

   class ViewHolder{
      TextView tvName, tvRating, tvText, tvTimeCreated;
   }
}
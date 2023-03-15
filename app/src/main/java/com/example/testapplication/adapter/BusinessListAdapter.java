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

import org.w3c.dom.Text;

import java.util.ArrayList;

public class BusinessListAdapter extends BaseAdapter {
   private Context context;
   private ArrayList<BusinessData> businessDatas = new ArrayList<>();

   public BusinessListAdapter(Context context) {
      this.context = context;
   }

   public void setBusinessDatas(ArrayList<BusinessData> businessDatas) {
      this.businessDatas.clear();
      this.businessDatas.addAll(businessDatas);
      notifyDataSetChanged();
   }

   @Override
   public int getCount() {
      return businessDatas.size();
   }

   @Override
   public Object getItem(int i) {
      return businessDatas.get(i);
   }

   @Override
   public long getItemId(int i) {
      return i;
   }

   @Override
   public View getView(int i, View view, ViewGroup viewGroup) {
      ViewHolder viewHolder = null;
      if(view == null){
         view = LayoutInflater.from(context).inflate(R.layout.business_item_layout, null);
         viewHolder = new ViewHolder();
         viewHolder.tvSerialNumber = view.findViewById(R.id.tv_serial_number);
         viewHolder.ivImage = view.findViewById(R.id.iv_image);
         viewHolder.tvBusinessName = view.findViewById(R.id.tv_business_name);
         viewHolder.tvRating = view.findViewById(R.id.tv_rating);
         viewHolder.tvDistance = view.findViewById(R.id.tv_distance);

         view.setTag(viewHolder);
      }
      else{
         viewHolder = (ViewHolder) view.getTag();
      }

      BusinessData businessData = businessDatas.get(i); //one business data to be used
      viewHolder.tvSerialNumber.setText(i+1+"");
      Glide.with(context).load(businessData.getPicUrl()).error(R.mipmap.ic_launcher).into(viewHolder.ivImage);
      viewHolder.tvBusinessName.setText(businessData.getBusinessName());
      viewHolder.tvRating.setText(businessData.getRating());
      viewHolder.tvDistance.setText(businessData.getDistance());

      view.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            Intent intent = new Intent(context, BusinessDetailActivity.class);
            intent.putExtra("businessId", businessData.getId());
            intent.putExtra("businessName",businessData.getBusinessName());
            intent.putExtra("businessUrl",businessData.getUrl());
            context.startActivity(intent);
         }
      });

      return view;
   }

   class ViewHolder{
      TextView tvSerialNumber, tvBusinessName, tvRating, tvDistance;
      ImageView ivImage;
   }
}

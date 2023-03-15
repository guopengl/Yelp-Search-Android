package com.example.testapplication.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;

import com.example.testapplication.R;

public class SplashActivity extends AppCompatActivity {

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_splash);

      new CountDownTimer(1500, 1000) {

         public void onTick(long millisUntilFinished) {}

         public void onFinish() {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
         }
      }.start();
   }
}
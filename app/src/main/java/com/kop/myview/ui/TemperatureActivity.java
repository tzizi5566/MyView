package com.kop.myview.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.kop.myview.R;

public class TemperatureActivity extends AppCompatActivity {

  private static final String TAG = "TemperatureActivity";

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_temperature);
  }
}

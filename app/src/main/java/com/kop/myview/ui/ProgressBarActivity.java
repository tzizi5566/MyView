package com.kop.myview.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.kop.myview.R;
import com.kop.myview.widget.ProgressBar1;
import com.kop.myview.widget.ProgressBar2;
import com.kop.myview.widget.ProgressBar3;
import com.kop.myview.widget.ProgressBar4;
import com.kop.myview.widget.ProgressBar5;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;

public class ProgressBarActivity extends AppCompatActivity {

  private static final String TAG = "ProgressBarActivity";

  @BindView(R.id.progress1) ProgressBar1 mProgress1;
  @BindView(R.id.progress2) ProgressBar2 mProgress2;
  @BindView(R.id.progress3) ProgressBar3 mProgress3;
  @BindView(R.id.progress4) ProgressBar4 mProgress4;
  @BindView(R.id.progress5) ProgressBar5 mProgress5;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_progress_bar);
    ButterKnife.bind(this);

    Observable.interval(700, TimeUnit.MILLISECONDS).takeWhile(new Predicate<Long>() {
      @Override public boolean test(@NonNull Long aLong) throws Exception {
        return aLong <= 100;
      }
    }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Long>() {
      @Override public void accept(@NonNull Long aLong) throws Exception {
        mProgress1.setProgress(aLong);
        mProgress2.setProgress(aLong);
        mProgress3.setProgress(aLong);
        mProgress4.setPercent(aLong);
      }
    });

    mProgress5.startAnim(0F, 1F);
  }
}

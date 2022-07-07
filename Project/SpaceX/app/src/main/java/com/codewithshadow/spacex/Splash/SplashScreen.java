package com.codewithshadow.spacex.Splash;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import com.codewithshadow.spacex.Activity.MainActivity;
import com.codewithshadow.spacex.R;

public class SplashScreen extends AppCompatActivity {
    Handler mhandler;
    Runnable mrunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        //Runnable

        mrunnable = new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(SplashScreen.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        };
        mhandler = new Handler();
        mhandler.postDelayed(mrunnable,1000);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mhandler!=null && mrunnable!=null)
        {
            mhandler.removeCallbacks(mrunnable);
        }
    }

    public void onBackPressed(){
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }
}
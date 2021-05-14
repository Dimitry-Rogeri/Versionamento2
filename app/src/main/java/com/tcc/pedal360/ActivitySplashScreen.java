package com.tcc.pedal360;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class ActivitySplashScreen extends AppCompatActivity {
private int SLEEP_TIMER = 3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash_screen);
        getSupportActionBar().hide();

        LogoLaucher logoLaucher = new LogoLaucher();
        logoLaucher.start();
    }

    private class LogoLaucher extends Thread {
        public void run(){
            try{
                sleep(1000 * SLEEP_TIMER);
            } catch (InterruptedException e){
                e.printStackTrace();
            }

            Intent intent = new Intent(ActivitySplashScreen.this, ActivityRegister.class);
            startActivity(intent);
            ActivitySplashScreen.this.finish();
        }
    }
}
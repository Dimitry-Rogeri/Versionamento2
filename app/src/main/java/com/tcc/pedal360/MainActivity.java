package com.tcc.pedal360;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.tcc.pedal360.fragments.fragment1;
import com.tcc.pedal360.fragments.fragment2;
import com.tcc.pedal360.fragments.fragment3;
import com.tcc.pedal360.fragments.fragment4;
import com.tcc.pedal360.fragments.fragment5;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(onNav);

        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new fragment1()).commit();


    }

    BottomNavigationView.OnNavigationItemSelectedListener onNav = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selected = null;
            switch (item.getItemId()) {
                case R.id.profile_bottom:
                    selected = new fragment1();
                    break;

                case R.id.alertas_bottom:
                    selected = new fragment2();
                    break;

                case R.id.eventos_bottom:
                    selected = new fragment3();
                    break;

                case R.id.marcador_bottom:
                    selected = new fragment4();
                    break;

                case R.id.mapas_bottom:
                    selected = new fragment5();
                    break;
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, selected).commit();

            return true;
        }
    };


}
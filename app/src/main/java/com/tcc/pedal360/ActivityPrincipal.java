package com.tcc.pedal360;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.tcc.pedal360.alertaslocation.ActivityMaps;

public class ActivityPrincipal extends AppCompatActivity {

    TextView mAlertas, mRotas;
    ImageView mProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

    mAlertas = findViewById(R.id.alertas);
    mRotas = findViewById(R.id.rotas);
    mProfile = findViewById(R.id.ivProfile);

    mProfile.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent0 = new Intent(ActivityPrincipal.this, ActivityPerfil.class);
            startActivity(intent0);
        }
    });

    mAlertas.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(ActivityPrincipal.this, ActivityAlertas.class);
            startActivity(intent);
        }
    });

    mRotas.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent1 = new Intent(ActivityPrincipal.this, ActivityLocate.class);
            startActivity(intent1);
        }
    });
    }
    public void logout(View view){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(ActivityPrincipal.this, ActivityRegister.class));
        finish();
    }

}



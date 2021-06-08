package com.tcc.pedal360;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tcc.pedal360.alertaslocation.ActivityAlertas;
import com.tcc.pedal360.alertaslocation.ActivityLocate;
import com.tcc.pedal360.perfil.ActivityPerfil;

import de.hdodenhof.circleimageview.CircleImageView;

public class ActivityPrincipal extends AppCompatActivity {

    TextView mAlertas, mRotas, mProdutores;
    CircleImageView mProfile;

        FirebaseUser firebaseUser;
        DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

    mAlertas = findViewById(R.id.alertas);
    mRotas = findViewById(R.id.rotas);
    mProfile = findViewById(R.id.profile_image);
    mProdutores = findViewById(R.id.tvProdutores);

    firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    reference    = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

    mProdutores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentProdutores = new Intent (ActivityPrincipal.this, Produtores.class);
                startActivity(intentProdutores);
            }
        });

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



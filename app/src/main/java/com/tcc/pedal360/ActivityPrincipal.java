package com.tcc.pedal360;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ActivityPrincipal extends AppCompatActivity {

    TextView mProdutores;
    Button mComecar;

        FirebaseUser firebaseUser;
        DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);


    mProdutores = findViewById(R.id.tvProdutores);
    mComecar    = findViewById(R.id.comecar);

    firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    reference    = FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid());

    mProdutores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentProdutores = new Intent (ActivityPrincipal.this, Produtores.class);
                startActivity(intentProdutores);
            }
        });

    mComecar.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(ActivityPrincipal.this, MainActivity.class);
            startActivity(intent);
        }
    });

    }
    public void logout(View view){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(ActivityPrincipal.this, ActivityRegister.class));
        finish();
    }

}



package com.tcc.pedal360.perfil;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tcc.pedal360.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ActivityPerfil extends AppCompatActivity {

    ImageView mFoto;
    Button mEscolher, mChat;
    private static final int PICK_IMAGE = 100;
    Uri imageUri;
    FirebaseUser firebaseUser;
    DatabaseReference reference;

    TextView username;
    CircleImageView profile_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference    = FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid());

       // username = findViewById(R.id.username);
       // profile_image = findViewById(R.id.profile_image);



        //mFoto = findViewById(R.id.ivFotoPerfil);
        //mEscolher = findViewById(R.id.buttonFoto);
       // mChat = findViewById(R.id.BtnChat);

        mEscolher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

    }

    private void openGallery(){
        Intent gallery = new Intent (Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == PICK_IMAGE){
            imageUri = data.getData();
            mFoto.setImageURI(imageUri);
        }
    }


}
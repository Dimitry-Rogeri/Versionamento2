package com.tcc.pedal360.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;
import com.tcc.pedal360.R;
import com.tcc.pedal360.fragments.perfil.ActivityPosts;
import com.tcc.pedal360.fragments.perfil.ActivityProfile;

public class fragment1 extends Fragment implements View.OnClickListener {

    ImageButton editar;
    ImageView imageView;
    TextView nameEt, bioEt, post;
    ValueEventListener valueEventListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment1, container, false);
        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        imageView = getActivity().findViewById(R.id.iv_f1);
        nameEt    = getActivity().findViewById(R.id.tv_name_f1);
        bioEt     = getActivity().findViewById(R.id.tv_bio_f1);
        editar    = getActivity().findViewById(R.id.ib_edit_f1);
        post      = getActivity().findViewById(R.id.tv_post_f1);

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ActivityPosts.class);
                startActivity(intent);
            }
        });

        editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (getActivity(), ActivityProfile.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

        }
    }

    @Override
    public void onStart() {
        super.onStart();

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference ref = database.getReferenceFromUrl("https://pedal-360-default-rtdb.firebaseio.com/All%20users");

        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                if (snapshot.exists()) {

                    String nome = (String) snapshot.child("name").getValue();
                    String bio = (String) snapshot.child("bio").getValue();
                    String url = (String) snapshot.child("url").getValue();

                    Picasso.get().load(url).into(imageView);
                    nameEt.setText(nome);
                    bioEt.setText(bio);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentId = user.getUid();

        DocumentReference reference;
        FirebaseFirestore firestore = FirebaseFirestore .getInstance();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("All users");
        reference = firestore.collection("user").document(currentId);


        /*reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.getResult().exists()){
                    String bioResult = task.getResult().getString("bio");
                    String nameResult = task.getResult().getString("name");
                    String url = task.getResult().getString("url");

                    Picasso.get().load(url).into(imageView);
                    nameEt.setText(nameResult);
                    bioEt.setText(bioResult);
                }
            }
        });*/
    }
}



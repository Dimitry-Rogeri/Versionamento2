package com.tcc.pedal360.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.tcc.pedal360.R;
import com.tcc.pedal360.alertaslocation.ActivityLocate;
import com.tcc.pedal360.fragments.perfil.ActivityMaps;

public class fragment5 extends Fragment implements View.OnClickListener {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment5, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ImageView imageView = getActivity().findViewById(R.id.abrir_marcadores);
        ImageView imageView1 = getActivity().findViewById(R.id.abrir_rotas);

        imageView.setOnClickListener(this);
        imageView1.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.abrir_marcadores:
                Intent intent = new Intent (getActivity(), ActivityMaps.class);
                startActivity(intent);
                break;
        }

        switch (view.getId()){
            case R.id.abrir_rotas:
                Intent intent1 = new Intent(getActivity(), ActivityLocate.class);
                startActivity(intent1);
                break;
        }

    }
}

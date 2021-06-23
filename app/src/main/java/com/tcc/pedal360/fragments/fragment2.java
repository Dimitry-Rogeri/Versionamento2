package com.tcc.pedal360.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.tcc.pedal360.R;
import com.tcc.pedal360.fragments.alertas.ActivityAlertas;

public class fragment2 extends Fragment implements View.OnClickListener {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment2, container, false);
        return view;
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ImageView textView = getActivity().findViewById(R.id.iv_alertas);
        TextView cartilha = getActivity().findViewById(R.id.cartilha);

        cartilha.setOnClickListener(this);
        textView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_alertas:
                Intent intent = new Intent (getActivity(), ActivityAlertas.class);
                startActivity(intent);
        }
        switch (view.getId()){
            case R.id.cartilha:
                Intent intent = new Intent (getActivity(), ActivityCartilha.class);
                startActivity(intent);
        }
    }
}

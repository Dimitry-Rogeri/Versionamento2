package com.tcc.pedal360.fragments.alertas;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.tcc.pedal360.R;
import com.tcc.pedal360.alertaslocation.LocationTrack;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class ActivityAlertas extends AppCompatActivity implements LocationListener {

    final int SEND_SMS_PERMISSION_REQUEST_CODE = 1;
    private LocationManager locationManager;
    private static final int REQUEST_CODE = 101;
    public double latitude;
    public double longitude;

    public Criteria criteria;
    public String bestProvider;
    Location location;
    LocationTrack locationTrack;

    private Marker currentLocationMaker;
    private LatLng currentLocationLatLong;

    Location currentLocation;
    com.google.android.gms.location.FusedLocationProviderClient fusedLocationProviderClient;


    TextView latlong;
    EditText number, sms;
    Button send, salvar;
    SharedPreferences sharedPreferences;
    public static final String mypreference = "mypref";
    public static final String Telefone = "telefoneKey";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        setContentView(R.layout.activity_alertas);

        number = findViewById(R.id.etTelefone);
        sms = findViewById(R.id.etMessage);

        send = findViewById(R.id.alertaBtn);
        salvar = findViewById(R.id.salvarBtn);


        SimpleMaskFormatter smf = new SimpleMaskFormatter("(NN)NNNNN-NNNN");
        MaskTextWatcher mtw = new MaskTextWatcher(number, smf);
        number.addTextChangedListener(mtw);


        sharedPreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);

        if (sharedPreferences.contains(Telefone)) {
            number.setText(sharedPreferences.getString(Telefone, ""));
        }

        salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Number1 = number.getText().toString();

                if (Number1.equals("")) {
                    Toast.makeText(ActivityAlertas.this, "Contato não inserido", Toast.LENGTH_SHORT).show();
                    number.requestFocus();

                } else {
                    Save();
                    Toast.makeText(ActivityAlertas.this, "Atualização Concluída!", Toast.LENGTH_SHORT).show();


                }
            }
        });


        send.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                String sNumber = number.getText().toString();
                if (ContextCompat.checkSelfPermission(ActivityAlertas.this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                    sendMessage();
                    getLocation();


                } else {
                    ActivityCompat.requestPermissions(ActivityAlertas.this, new String[]{Manifest.permission.SEND_SMS}, 100);
                }
            }

        });


    } // FIM DO ONCREATE

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            sendMessage();
            getLocation();


        } else {
            Toast.makeText(getApplicationContext(), "Erro", Toast.LENGTH_LONG).show();
        }
    }


    protected void getLocation() {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }

            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            criteria = new Criteria();
            bestProvider = String.valueOf(locationManager.getBestProvider(criteria, true)).toString();
            String sNumber = number.getText().toString().trim();

            Location location = locationManager.getLastKnownLocation(bestProvider);
            if (location != null || sNumber.equals("")) {

                latitude = location.getLatitude();
                longitude = location.getLongitude();
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(sNumber, null, "www.google.com/maps/search/?api=1&query=" + String.valueOf(latitude) + "," + String.valueOf(longitude), null, null);

            }

    }

    private boolean isLocationEnabled(ActivityAlertas activityAlertas) {
        return true;
    }


    public void Save() {
        String n = number.getText().toString();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Telefone, n);
        editor.commit();

    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, 1);
    }


    private void sendMessage() {
        String sNumber = number.getText().toString().trim();
        String sMessage = sms.getText().toString().trim();


        if (!sNumber.equals("") && !sMessage.equals("")) {

            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(sNumber, null, sMessage, null, null);
            Toast.makeText(getApplicationContext(), "Mensagem enviada com sucesso !", Toast.LENGTH_SHORT).show();


        } else {
            Toast.makeText(getApplicationContext(), "Preencha os campos primeiro", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onLocationChanged(@NonNull Location location) {


        String sNumber = number.getText().toString().trim();
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(sNumber, null, "www.google.com/maps/search/?api=1&query=" + String.valueOf(latitude) + "," + String.valueOf(longitude), null, null);

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }
}




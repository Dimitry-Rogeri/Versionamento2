package com.tcc.pedal360.fragments.perfil;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tcc.pedal360.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class ActivityMaps extends FragmentActivity implements OnMapReadyCallback, LocationListener, GoogleMap.OnInfoWindowClickListener {

    private GoogleMap mMap;
    private View MapClick;
    private Marker currentLocationMaker, oficina, lanchonetes, newmap;
    private LatLng currentLocationLatLong;
    private DatabaseReference mDatabase;
    Marker m = null;
    SharedPreferences prefs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        startGettingLocations();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        getMarkers();

        prefs = this.getSharedPreferences("LatLng", MODE_PRIVATE);


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if ((prefs.contains("Lat")) && (prefs.contains("Lng"))) {
            String lat = prefs.getString("Lat", "");
            String lng = prefs.getString("Lng", "");
            LatLng l = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
            mMap.addMarker(new MarkerOptions().position(l));
        }

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                mMap.addMarker(new MarkerOptions().position(latLng).title("teste"));

                prefs.edit().putString("Lat", String.valueOf(latLng.latitude)).commit();
                prefs.edit().putString("Lng", String.valueOf(latLng.longitude)).commit();



            }
        });


        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(Marker marker) {
                marker.showInfoWindow();
                mMap.setOnInfoWindowClickListener(ActivityMaps.this::onInfoWindowClick);
                return false;
            }


        });


    }

        @Override
        public void onLocationChanged (@NonNull Location location){

            if (currentLocationMaker != null) {
                currentLocationMaker.remove();
            }
            //Add marker
            currentLocationLatLong = new LatLng(location.getLatitude(), location.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(currentLocationLatLong);
            markerOptions.title("Localização atual");
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
            currentLocationMaker = mMap.addMarker(markerOptions);

            //Move to new location
            CameraPosition cameraPosition = new CameraPosition.Builder().zoom(15).target(currentLocationLatLong).build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            Toast.makeText(this, "Localização atualizada", Toast.LENGTH_SHORT).show();

            //LocationDatabase locationData = new LocationDatabase(location.getLatitude(), location.getLongitude());
            // mDatabase.child("location").child(String.valueOf(new Date().getTime())).setValue(locationData);
            getMarkers();
        }

        private ArrayList findUnAskedPermissions (ArrayList < String > wanted) {
            ArrayList result = new ArrayList();

            for (String perm : wanted) {
                if (!hasPermission(perm)) {
                    result.add(perm);
                }
            }

            return result;
        }

        private boolean hasPermission (String permission){
            if (canAskPermission()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
                }
            }
            return true;
        }

        private boolean canAskPermission () {
            return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
        }

        public void showSettingsAlert () {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("GPS desativado!");
            alertDialog.setMessage("Ativar GPS?");
            alertDialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            });

            alertDialog.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            alertDialog.show();
        }
        private void startGettingLocations () {

            LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            boolean isGPS = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean isNetwork = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            boolean canGetLocation = true;
            int ALL_PERMISSIONS_RESULT = 101;
            long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;// Distance in meters
            long MIN_TIME_BW_UPDATES = 1000 * 10;// Time in milliseconds

            ArrayList<String> permissions = new ArrayList<>();
            ArrayList<String> permissionsToRequest;

            permissions.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
            permissions.add(android.Manifest.permission.ACCESS_COARSE_LOCATION);
            permissionsToRequest = findUnAskedPermissions(permissions);

            //Check if GPS and Network are on, if not asks the user to turn on
            if (!isGPS && !isNetwork) {
                showSettingsAlert();
            } else {
                // check permissions

                // check permissions for later versions
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (permissionsToRequest.size() > 0) {
                        requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]),
                                ALL_PERMISSIONS_RESULT);
                        canGetLocation = false;
                    }
                }
            }


            //Checks if FINE LOCATION and COARSE Location were granted
            if (ActivityCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(this, "Permissão negada", Toast.LENGTH_SHORT).show();
                return;
            }

            //Starts requesting location updates
            if (canGetLocation) {
                if (isGPS) {
                    lm.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                } else if (isNetwork) {
                    // from Network Provider

                    lm.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                }
            } else {
                Toast.makeText(this, "Não é possível obter a localização", Toast.LENGTH_SHORT).show();
            }
        }



        private void getMarkers () {

            mDatabase.child("location").addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            //Get map of users in datasnapshot
                            if (dataSnapshot.getValue() != null)
                                getAllLocations((Map<String, Object>) dataSnapshot.getValue());
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            //handle databaseError
                        }
                    });
        }

        private void getAllLocations (Map < String, Object > locations){

            for (Map.Entry<String, Object> entry : locations.entrySet()) {

                Date newDate = new Date(Long.valueOf(entry.getKey()));
                Map singleLocation = (Map) entry.getValue();
                LatLng latLng = new LatLng((Double) singleLocation.get("latitude"), (Double) singleLocation.get("longitude"));
                addGreenMarker(newDate, latLng);
            }

        }

        private void addGreenMarker (Date newDate, LatLng latLng){
            SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title(dt.format(newDate));
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            mMap.addMarker(markerOptions);
        }

        @Override
        public void onStatusChanged (String provider,int status, Bundle extras){

        }

        @Override
        public void onProviderEnabled (@NonNull String provider){

        }

        @Override
        public void onProviderDisabled (@NonNull String provider){

        }


    @Override
    public void onInfoWindowClick(Marker marker) {

        marker.getTitle();


    }
}



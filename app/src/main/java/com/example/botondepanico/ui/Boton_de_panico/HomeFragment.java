package com.example.botondepanico.ui.Boton_de_panico;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Paint;
import android.icu.text.SimpleDateFormat;
import android.icu.text.UnicodeSet;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.botondepanico.Pojos.UserModel;
import com.example.botondepanico.R;
import com.example.botondepanico.Reference.Data_Reference;
import com.example.botondepanico.databinding.FragmentHomeBinding;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import pl.bclogic.pulsator4droid.library.PulsatorLayout;

public class HomeFragment extends Fragment implements OnMapReadyCallback {

    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private GoogleMap mMap;
    private Location previousLocation, currentLocation;
    private AutocompleteSupportFragment autocompleteSupportFragment;
    SupportMapFragment mapFragment;
    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    Button sos;
    Vibrator vibrator;
    long lastDown, lastDuration;
    private DatabaseReference mDatabase;
    FirebaseAuth firebaseAuth;
    private String ubicacion_actual;
    private double Latitude;
    private double Longitude;
    PulsatorLayout pulsatorLayout;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        String id = firebaseAuth.getCurrentUser().getUid();
        pulsatorLayout = (PulsatorLayout) root.findViewById(R.id.pulsator);
        pulsatorLayout.start();
        starLocationGet();
        sos = (Button) root.findViewById(R.id.sos);
        sos.setOnTouchListener(new View.OnTouchListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    vibrator.vibrate(new long[]{0, 900, 900, 900}, 2);
                    lastDown = System.currentTimeMillis();
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    vibrator.cancel();
                    lastDuration = System.currentTimeMillis() - lastDown;
                    if (lastDuration >= 3000) {
                        SendSos();
                    } else {
                        Snackbar.make(getView(), "Mantenga  precionado 3 segundos para enviar una alerta a la central", Snackbar.LENGTH_LONG).show();
                    }
                }

                return true;
            }


        });

        return root;
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void SendSos() {


        Snackbar.make(getView(), "Enviando alerta.. ", Snackbar.LENGTH_SHORT).show();
        int p = (int) (Math.random() * 25 + 1);
        int s = (int) (Math.random() * 25 + 1);
        int t = (int) (Math.random() * 25 + 1);
        int c = (int) (Math.random() * 25 + 1);
        int number_1 = (int) (Math.random() * 1012 + 2111);
        int number_2 = (int) (Math.random() * 1012 + 2111);
        int number_3 = (int) (Math.random() * 1012 + 2111);
        int number_4 = (int) (Math.random() * 1012 + 2111);

        String[] elements = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l"
                , "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
        final String idIncident =
                number_1 + number_2 + number_3 + number_4 + "XaltSOS";

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        Date date = new Date();
        String fecha = dateFormat.format(date);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm aa");
        String hora = simpleDateFormat.format(new Date());
        String Uid = mDatabase.push().getKey();

        Long tsLong = System.currentTimeMillis()/1000;
        String ts = tsLong.toString();
        String id = firebaseAuth.getCurrentUser().getUid();
        Map<String, Object> map = new HashMap<>();
        map.put("IdUser", id);
        map.put("Id_Incident", idIncident);
        map.put("Name", Data_Reference.currentClient.getName());
        map.put("LastName", Data_Reference.currentClient.getLastName());
        map.put("Phone", Data_Reference.currentClient.getPhone());
        map.put("Email", Data_Reference.currentClient.getEmail());
        map.put("Location", ubicacion_actual);
        map.put("latitude", Latitude);
        map.put("longitude", Longitude);
        map.put("Type_of_alert", "Botón de panico");
        map.put("Status", "Pendiente");
        map.put("Date", fecha);
        map.put("Hour", hora);
        map.put("last_time_stamp",ts);
        map.put("Uid",Uid);

        mDatabase.child(Data_Reference.SOS).child(Uid).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task2) {
                if (task2.isSuccessful()) {
                    Snackbar.make(getActivity().findViewById(android.R.id.content), "Alerta Enviada a la central.", Snackbar.LENGTH_SHORT).show();
                } else {
                    Snackbar.make(getActivity().findViewById(android.R.id.content), "Problemas con la conexión, no se registro la alerta.", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void starLocationGet() {
        buildLocationRequest();
        buildLocationCallBack();
        updateLocation();
    }

    private void buildLocationCallBack() {

        if (locationCallback == null) {
            locationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    super.onLocationResult(locationResult);
                    LatLng newPosition = new LatLng(locationResult.getLastLocation().getLatitude(), locationResult
                            .getLastLocation().getLongitude());
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newPosition, 17f));
                    previousLocation = currentLocation;
                    currentLocation = locationResult.getLastLocation();
                    if (locationResult.getLastLocation() != null) {
                        pulsatorLayout.setVisibility(View.VISIBLE);
                        setLocation(locationResult.getLastLocation());
                    }
                }
            };
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;



            Dexter.withContext(getContext())
                    .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    .withListener(new PermissionListener() {
                        @Override
                        public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                                    && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                Snackbar.make(mapFragment.getView(),getString(R.string.permission_requiere),Snackbar.LENGTH_SHORT).show();
                                return;
                            }

                            mMap.setMyLocationEnabled(true);
                            mMap.getUiSettings().setMyLocationButtonEnabled(true);
                            mMap.setOnMyLocationButtonClickListener(() -> {
                                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                    return false;
                                }
                                fusedLocationProviderClient.getLastLocation()
                                        .addOnFailureListener(e -> Snackbar.make(getView(), e.getMessage(), Snackbar.LENGTH_SHORT)
                                                .show()).addOnSuccessListener(location -> {

                                    if(location != null) {
                                        LatLng userLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 17f));
                                    }
                                });

                                return true;
                            });

                            //boton del Layuout
                            View locationButton = ((View)mapFragment.getView().findViewById(Integer.parseInt("1")).getParent())
                                    .findViewById(Integer.parseInt("2"));
                            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
                            // right bottom
                            params.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
                            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
                            params.setMargins(0,0,0,250);

                            //update Location
                            buildLocationRequest();
                            buildLocationCallBack();
                            updateLocation();

                            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                                return;
                            }
                            fusedLocationProviderClient.getLastLocation()
                                    .addOnFailureListener(e -> Snackbar.make(getView(), e.getMessage(), Snackbar.LENGTH_SHORT)
                                            .show()).addOnSuccessListener(location -> {

                                if (location != null) {

                                    pulsatorLayout.setVisibility(View.VISIBLE);

                                }
                            });


                        }

                        @Override
                        public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                            Snackbar.make(getView(),permissionDeniedResponse.getPermissionName()+"need enable",Snackbar
                                    .LENGTH_SHORT).show();
                        }
                        @Override
                        public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        }
                    })
                    .check();
            try {
                boolean success = mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getContext(), R.raw.estilo_noche));
                if(!success)
                    Snackbar.make(getView(),"Error al cargar el estilo de mapa, contacte con soporte",Snackbar.LENGTH_SHORT).show();
            }catch (Exception e){
                Snackbar.make(getView(),e.getMessage(),Snackbar.LENGTH_SHORT).show();
            }

        }

    private void updateLocation() {

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
    }

    private void buildLocationRequest()  {

        if(locationRequest == null){
            locationRequest = new LocationRequest();
            locationRequest.setSmallestDisplacement(10f);
            locationRequest.setInterval(5000);
            locationRequest.setFastestInterval(3000);
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        }


    }


    public void setLocation (Location loc){
        if(loc.getLatitude()!=0.0 && loc.getLongitude()!=0.0){
            try {
                Geocoder geocoder = new Geocoder(getContext() , Locale.getDefault());
                List<Address> list  = geocoder.getFromLocation( loc.getLatitude(),loc.getLongitude(), 1);
                if (!list.isEmpty()){
                    Address dirCalle = list.get(0);
                    ubicacion_actual= Data_Reference.formatAdress(dirCalle.getAddressLine(0));
                    Latitude = loc.getLatitude();
                    Longitude = loc.getLongitude();

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
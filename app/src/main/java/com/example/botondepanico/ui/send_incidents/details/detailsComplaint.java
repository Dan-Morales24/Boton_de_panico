package com.example.botondepanico.ui.send_incidents.details;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.botondepanico.Pojos.IncidentSosModel;
import com.example.botondepanico.R;
import com.example.botondepanico.Reference.Data_Reference;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;


public class detailsComplaint extends Fragment implements OnMapReadyCallback {

    SupportMapFragment mapFragment;
    private GoogleMap mMap;
    private DatabaseReference databaseReference;
    private CircleImageView circleImageView;
    private TextView Title, Information;
    private String Name, LastName,Status,TypeOfAlert,Location,Date,Hour;
    private double  Latitude,Longitude;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view;
        view = inflater.inflate(R.layout.fragment_details_complaint, container, false);

        circleImageView = (CircleImageView)view.findViewById(R.id.item_image_panic);
        Title = (TextView)view.findViewById(R.id.TitletypeAlert);
        Information = (TextView)view.findViewById(R.id.InformationAlert);
        databaseReference = FirebaseDatabase.getInstance().getReference();

        getParentFragmentManager().setFragmentResultListener("key", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {

              String Id= result.getString("Id");

               GetInformation(Id);

            }
        });

        return view;
    }

    private void GetInformation(String id) {



        databaseReference.child("SOS").orderByChild("Id_Incident").equalTo(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {

                        Name = ds.child("Name").getValue().toString();
                        LastName = ds.child("LastName").getValue().toString();
                        Status = ds.child("Status").getValue().toString();
                        Location = ds.child("Location").getValue().toString();
                        Latitude=ds.child("latitude").getValue(Double.class);
                        Longitude=ds.child("longitude").getValue(Double.class);
                        TypeOfAlert=ds.child("Type_of_alert").getValue().toString();
                        Date=ds.child("Date").getValue().toString();
                        Hour=ds.child("Hour").getValue().toString();

                     }

                    SetInformation(Name,LastName,Status,Location,TypeOfAlert,Date,Hour);



                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    private void SetInformation(String name, String lastName, String status, String location, String typeOfAlert, String date, String hour) {

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.mapComplaint);
        mapFragment.getMapAsync(this);


        Glide.with(getActivity()) .load(Data_Reference.currentClient.getAvatar())
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .error(R.mipmap.ic_launcher_round)
                .into(circleImageView);

                Title.setText(typeOfAlert);
                Information.setText("EL dia "+date+" "+name+" "+lastName+" registro una "+typeOfAlert+" la cual " +
                        "fue registrada en la central a las "+hour+" quedando registrado en la ubicación "
                        +location+" actualmente el estatus de esta alerta es "+status+".");


    }



    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng Position = new LatLng(Latitude, Longitude);
        mMap.addMarker(new MarkerOptions()
                .position(Position)
                .title("Alerta de "+Name));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Position, 16f));


    }
}
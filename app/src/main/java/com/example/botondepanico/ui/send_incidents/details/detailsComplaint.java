package com.example.botondepanico.ui.send_incidents.details;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.botondepanico.Pojos.IncidentSosModel;
import com.example.botondepanico.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class detailsComplaint extends Fragment {

    private TextView idView;
    private DatabaseReference databaseReference;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.fragment_details_complaint, container, false);
        idView = (TextView) view.findViewById(R.id.idDetailsComplaint);
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
                        String idSosAlert = ds.child("Id_Incident").getValue().toString();
                        String name = "Nombre: " + ds.child("Name").getValue().toString();
                        String status = ds.child("Status").getValue().toString();
                        String location = "Ubicación: " + ds.child("Location").getValue().toString();

                        idView.setText(name);
                            Toast.makeText(getContext(),"Nombre: "+name,Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
}
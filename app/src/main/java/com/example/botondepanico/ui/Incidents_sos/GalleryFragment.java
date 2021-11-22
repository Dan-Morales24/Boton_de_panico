package com.example.botondepanico.ui.Incidents_sos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.botondepanico.Adapters.SosAdapter;
import com.example.botondepanico.Pojos.IncidentSosModel;
import com.example.botondepanico.R;
import com.example.botondepanico.databinding.FragmentGalleryBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GalleryFragment extends Fragment {

    private GalleryViewModel galleryViewModel;
    private FragmentGalleryBinding binding;
    private DatabaseReference databaseReference;
    FirebaseDatabase database;
    private FirebaseAuth firebaseAuth;
    private SosAdapter sosAdapter;
    private RecyclerView recyclerView;
    private ArrayList<IncidentSosModel> incidentSosModels = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        firebaseAuth=FirebaseAuth.getInstance();
        recyclerView = (RecyclerView)root.findViewById(R.id.recyclerViewMensajes);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        databaseReference = FirebaseDatabase.getInstance().getReference();
        GetIncidentsFromFirebase();



        return root;
    }

    private void GetIncidentsFromFirebase() {

        String id = firebaseAuth.getCurrentUser().getUid();

        databaseReference.child("SOS").orderByChild("IdUser").equalTo(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot ds : snapshot.getChildren()){
                        String idSosAlert= "Id: "+ds.child("Id_Incident").getValue().toString();
                        String name = "Nombre: "+ds.child("Name").getValue().toString();
                        String status = ds.child("Status").getValue().toString();
                       String location ="Ubicación: "+ds.child("Location").getValue().toString();


                        incidentSosModels.add(new IncidentSosModel(idSosAlert,name,location,status));

                    }
                    sosAdapter = new SosAdapter(incidentSosModels, R.layout.information_view);
                    recyclerView.setAdapter(sosAdapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
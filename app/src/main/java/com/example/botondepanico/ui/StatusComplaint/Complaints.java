package com.example.botondepanico.ui.StatusComplaint;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.botondepanico.Adapters.ComplaintAdapter;
import com.example.botondepanico.Adapters.SosAdapter;
import com.example.botondepanico.Pojos.ComplaintModel;
import com.example.botondepanico.Pojos.IncidentSosModel;
import com.example.botondepanico.R;
import com.example.botondepanico.Reference.Data_Reference;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class Complaints extends Fragment {

    private DatabaseReference databaseReference;
    private RecyclerView recyclerView;
    private FirebaseAuth firebaseAuth;
    private ComplaintAdapter complaintAdapter;
    private ArrayList<ComplaintModel> complaintModels = new ArrayList<>();
    private ProgressBar progressBar;


    public Complaints() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_complaints, container, false);

        firebaseAuth=FirebaseAuth.getInstance();
        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerViewComplaints);
        progressBar= (ProgressBar)view.findViewById(R.id.progressBarComplaint);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        databaseReference = FirebaseDatabase.getInstance().getReference();
        GetComplaintsFromFirebase();


        return view;
    }

    private void GetComplaintsFromFirebase() {

        String id = firebaseAuth.getCurrentUser().getUid();

        databaseReference.child(Data_Reference.SendIncident).orderByChild("IdUser").equalTo(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot ds : snapshot.getChildren()){
                        String Title = ds.child("TitleComplaint").getValue().toString();
                        String IdSosAlert=ds.child("Id_Incident").getValue().toString();
                        String status = ds.child("Status").getValue().toString();
                        String location =ds.child("Location").getValue().toString();
                        String hour =ds.child("Hour").getValue().toString();

                        complaintModels.add(new ComplaintModel(Title,IdSosAlert,location,status,hour));

                    }
                    progressBar.setVisibility(View.GONE);
                    complaintAdapter = new ComplaintAdapter(complaintModels, R.layout.information_view_complaint);
                    recyclerView.setAdapter(complaintAdapter);

                    complaintAdapter.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Bundle bundle = new Bundle();
                            final NavController navController = Navigation.findNavController(v);
                            bundle.putString("Id",complaintModels.get(recyclerView.getChildAdapterPosition(v)).getIdSosAlert());
                            getParentFragmentManager().setFragmentResult("key",bundle);
                            navController.navigate(R.id.action_nav_view_status_to_detailsComplaintCitizen);
                            complaintModels.clear();
                        }
                    });

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
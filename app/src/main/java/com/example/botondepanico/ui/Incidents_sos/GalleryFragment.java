package com.example.botondepanico.ui.Incidents_sos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.botondepanico.Activities.MainActivity;
import com.example.botondepanico.Adapters.SosAdapter;
import com.example.botondepanico.Pojos.IncidentSosModel;
import com.example.botondepanico.R;
import com.example.botondepanico.databinding.FragmentGalleryBinding;
import com.google.android.gms.dynamic.SupportFragmentWrapper;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class GalleryFragment extends Fragment {

    private FragmentGalleryBinding binding;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    View root;
    private SosAdapter sosAdapter;
    private RecyclerView recyclerView;
    private ArrayList<IncidentSosModel> incidentSosModels = new ArrayList<>();
    private ImageView avatar;
    private ProgressBar progressBar;
    private TextView NoData;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentGalleryBinding.inflate(inflater, container, false);
         root = binding.getRoot();

        firebaseAuth=FirebaseAuth.getInstance();
        recyclerView = (RecyclerView)root.findViewById(R.id.recyclerViewMensajes);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

   //     avatar = (ImageView) root.findViewById(R.id.item_image_panic);
        progressBar = (ProgressBar) root.findViewById(R.id.progressBarPanic);
        NoData = (TextView) root.findViewById(R.id.textNoData);
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
                        String idSosAlert= ds.child("Id_Incident").getValue().toString();
                        String status = ds.child("Status").getValue().toString();
                        String location = ds.child("Location").getValue().toString();
                        String hour =ds.child("Hour").getValue().toString();


                        incidentSosModels.add(new IncidentSosModel(idSosAlert,location,status,hour));

                    }

                    progressBar.setVisibility(View.GONE);
                    sosAdapter = new SosAdapter(incidentSosModels, R.layout.information_view);

                    recyclerView.setAdapter(sosAdapter);


                    sosAdapter.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Bundle bundle = new Bundle();
                             final NavController navController = Navigation.findNavController(v);
                            bundle.putString("Id",incidentSosModels.get(recyclerView.getChildAdapterPosition(v)).getIdSosAlert());
                            getParentFragmentManager().setFragmentResult("key",bundle);
                            navController.navigate(R.id.action_nav_gallery_to_detailsComplaint2);
                            incidentSosModels.clear();

                        }
                    });

                }
                    else{
                        progressBar.setVisibility(View.GONE);
                        NoData.setVisibility(View.VISIBLE);


                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Snackbar.make(getActivity().findViewById(android.R.id.content),"Error, verifica tu conexion a internet.",Snackbar.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        root = null;
    }
}
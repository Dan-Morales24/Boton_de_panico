package com.example.botondepanico.ui.Notificactions;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.botondepanico.Adapters.NotificationsAdapter;
import com.example.botondepanico.Adapters.SosAdapter;
import com.example.botondepanico.Pojos.IncidentSosModel;
import com.example.botondepanico.Pojos.NotificationsModel;
import com.example.botondepanico.R;
import com.example.botondepanico.databinding.FragmentSlideshowBinding;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class SlideshowFragment extends Fragment {

    private FragmentSlideshowBinding binding;
    private DatabaseReference databaseReference;
    private NotificationsAdapter notificationsAdapter;
    private RecyclerView recyclerView;
    private ArrayList<NotificationsModel> notificationsModels = new ArrayList<>();
    private ProgressBar progressBar;
    private TextView NoData;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        recyclerView = (RecyclerView)root.findViewById(R.id.recyclerViewNotifications);
        progressBar = (ProgressBar)root.findViewById(R.id.progressBarNotifications);
        NoData = (TextView)root.findViewById(R.id.textNoDataNotifications);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        GetNotificationsFromFirebase();


        return root;
    }

    private void GetNotificationsFromFirebase() {

        databaseReference.child("Notifications").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){

                    for (DataSnapshot ds : snapshot.getChildren()){
                        String Id = ds.child("Id").getValue().toString();
                        String Title= ds.child("TitleNotification").getValue().toString();
                        String BriefDescription = ds.child("Description").getValue().toString();
                        String ImageNotification =ds.child("ImageNotification").getValue().toString();
                        String HourPublished = ds.child("Hour").getValue().toString();


                        notificationsModels.add(new NotificationsModel(Id,Title,BriefDescription,ImageNotification,HourPublished));

                    }

                    progressBar.setVisibility(View.GONE);
                    notificationsAdapter = new NotificationsAdapter(notificationsModels, R.layout.information_view_notification);

                    recyclerView.setAdapter(notificationsAdapter);


                    notificationsAdapter.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Bundle bundle = new Bundle();
                            final NavController navController = Navigation.findNavController(v);
                            bundle.putString("Id",notificationsModels.get(recyclerView.getChildAdapterPosition(v)).getId());
                            getParentFragmentManager().setFragmentResult("key",bundle);
                            navController.navigate(R.id.action_nav_slideshow_to_detailsNotification);
                            notificationsModels.clear();

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
        binding = null;
    }
}
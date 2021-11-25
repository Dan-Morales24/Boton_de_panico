package com.example.botondepanico.ui.StatusComplaint.details;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.botondepanico.R;
import com.example.botondepanico.Reference.Data_Reference;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.security.PrivateKey;

import de.hdodenhof.circleimageview.CircleImageView;


public class DetailsComplaintCitizen extends Fragment {

    //comments
    private CircleImageView circleImageView;
    private TextView Title, NoComments,Comments,Information;
    private DatabaseReference databaseReference;
    private String Name, LastName,Status,Location,TypeOfAlert,Date,Hour,TitleComplaint,Coments,Description;

    public DetailsComplaintCitizen() {
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
        View view;
        view =  inflater.inflate(R.layout.fragment_details_complaint_citizen, container, false);

        Information = (TextView)view.findViewById(R.id.InformationComplaintText);
        Title = (TextView) view.findViewById(R.id.TitletypeComplaint);
        NoComments = (TextView) view.findViewById(R.id.textNoComments);
        Comments = (TextView) view.findViewById(R.id.Coments);
        circleImageView = (CircleImageView)view.findViewById(R.id.item_image_Complaint);
        databaseReference = FirebaseDatabase.getInstance().getReference();

        getParentFragmentManager().setFragmentResultListener("key", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                String Id = result.getString("Id");
                     getInformation(Id);
            }
        });






        return view;
    }

    private void getInformation(String id) {



        databaseReference.child("Incident_Notification").orderByChild("Id_Incident").equalTo(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {

                        Name = ds.child("Name").getValue().toString();
                        LastName = ds.child("LastName").getValue().toString();
                        Status = ds.child("Status").getValue().toString();
                        Location = ds.child("Location").getValue().toString();
                         TitleComplaint=ds.child("TitleComplaint").getValue().toString();
                        Date=ds.child("Date").getValue().toString();
                        Hour=ds.child("Hour").getValue().toString();
                        Description =ds.child("Description").getValue().toString();
                        Coments = ds.child("Comments").getValue().toString();


                    }

                    SetInformation(Name,LastName,Status,Location,Date,Hour,TitleComplaint,Coments);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }

    private void SetInformation(String name, String lastName, String status, String location, String date, String hour, String titleComplaint, String coments) {


        Glide.with(getActivity()) .load(R.drawable.icono_denuncias)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .error(R.mipmap.ic_launcher_round)
                .into(circleImageView);


        Title.setText(titleComplaint);

        Information.setText("EL dia "+date+" "+name+" "+lastName+" registro un evento llamado "+titleComplaint+" la cual " +
                "fue registrada en la central a las "+hour+" quedando registrado en la ubicación "
                +location+" actualmente el estatus de esta alerta es "+status+".");



        if(coments.equals("")){


            NoComments.setVisibility(View.VISIBLE);
            Comments.setVisibility(View.GONE);

        }
            else {
                Comments.setVisibility(View.VISIBLE);
                NoComments.setVisibility(View.GONE);

                Comments.setText(coments);

        }




    }
}
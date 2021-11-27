package com.example.botondepanico.ui.Notificactions.details;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

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
import java.util.PrimitiveIterator;


public class DetailsNotification extends Fragment {

    private ImageView ImageDescription;
    private TextView TitleNotification;
    private TextView DateNotification;
    private TextView HourNotification;
    private TextView TypeNotification;
    private TextView DescriptionNotification;
    private TextView textDate;
    private TextView textHour;
    private LinearLayout linearLayout;
    private ProgressBar progressBar;
    private DatabaseReference databaseReference;
    String getImageNotification;
    String getTitleDescription;
    String getDateNotification;
    String getHourNotification;
    String getTypeNotification;
    String getDescriptionNotification;



    public DetailsNotification() {
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
        View view =  inflater.inflate(R.layout.fragment_details_notification, container, false);

        ImageDescription = (ImageView) view.findViewById(R.id.imageViewNotification);
        TitleNotification = (TextView) view.findViewById(R.id.textViewTitleNotification);
        DateNotification = (TextView) view.findViewById(R.id.textViewDateNotification);
        HourNotification = (TextView) view.findViewById(R.id.textViewHourNotification);
        TypeNotification = (TextView) view.findViewById(R.id.textViewTypeNotification);
        DescriptionNotification = (TextView) view.findViewById(R.id.textViewDescriptionNotification);
        textDate=(TextView)view.findViewById(R.id.textDate);
        textHour=(TextView)view.findViewById(R.id.textHour);
        linearLayout=(LinearLayout)view.findViewById(R.id.visibilityNotifications);
        progressBar=(ProgressBar)view.findViewById(R.id.progressBarNotifications);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        getParentFragmentManager().setFragmentResultListener("key", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {

                String Id= result.getString("Id");

                GetNotifications(Id);

            }
        });



        return view;

    }

    private void GetNotifications(String id) {

        databaseReference.child("Notifications").orderByChild("Id").equalTo(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                         getImageNotification =ds.child("ImageNotification").getValue().toString();
                         getTitleDescription =ds.child("TitleNotification").getValue().toString();
                         getDateNotification =ds.child("Date").getValue().toString();
                         getHourNotification =ds.child("Hour").getValue().toString();
                         getTypeNotification =ds.child("NotificationType").getValue().toString();
                         getDescriptionNotification=ds.child("Description").getValue().toString();
                    }


                    SetInformationNotification(getImageNotification,getTitleDescription,getDateNotification,getHourNotification,getTypeNotification,getDescriptionNotification);
                    progressBar.setVisibility(View.GONE);
                    linearLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void SetInformationNotification(String getImageNotification, String getTitleDescription, String getDateNotification, String getHourNotification, String getTypeNotification, String getDescriptionNotification) {

        Glide.with(getActivity()) .load(getImageNotification)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .error(R.mipmap.ic_launcher_round)
                .into(ImageDescription);

        TitleNotification.setText(getTitleDescription);
        DateNotification.setText(getDateNotification);
        HourNotification.setText(getHourNotification);
        TypeNotification.setText(getTypeNotification);
        DescriptionNotification.setText(getDescriptionNotification);

    }


}
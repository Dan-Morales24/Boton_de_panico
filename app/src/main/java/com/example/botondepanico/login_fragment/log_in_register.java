package com.example.botondepanico.login_fragment;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.botondepanico.R;
import com.example.botondepanico.Reference.Data_Reference;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;


public class log_in_register extends Fragment {

    private DatabaseReference mDatabase;
    FirebaseAuth firebaseAuth;
    String NameSet,LastNameSet, PhoneSet, EmailSet, PasswordSet;
    EditText Name,LastName,Number,Email,Password;
    Button Register;
    private ProgressDialog progressDialog;

    public log_in_register() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        firebaseAuth=FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_log_in_register, container, false);
        Name = (EditText) view.findViewById(R.id.Name);
        LastName= (EditText) view.findViewById(R.id.LastName);
        Email = (EditText) view.findViewById(R.id.Email);
        Number = (EditText) view.findViewById(R.id.Phone);
        Password = (EditText) view.findViewById(R.id.Password);

        Register = (Button) view.findViewById(R.id.RegisterData);
        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterClient();
            }
        });
        return view;
    }



    private void RegisterClient() {


        //Set Data
        NameSet = Name.getText().toString().trim();
        LastNameSet = LastName.getText().toString().trim();
        PhoneSet = Number.getText().toString().trim();
        EmailSet = Email.getText().toString().trim();
        PasswordSet = Password.getText().toString().trim();

        if (!NameSet.isEmpty() && !LastNameSet.isEmpty() && !PhoneSet.isEmpty() && !EmailSet.isEmpty() && !PasswordSet.isEmpty()) {
            if (PasswordSet.length() >= 6) {
                progressDialog = ProgressDialog.show(getContext(), "Espera un momento",
                        "Estamos guardando tus datos", true);
                registerUser();
            }
                else {
                Toast.makeText(getContext(), "La contraseña debe de tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
                    }
                 }
                    else {
                Toast.makeText(getContext(), "Faltan datos", Toast.LENGTH_SHORT).show();
                 }
              }

    private void registerUser(){

        firebaseAuth.createUserWithEmailAndPassword(EmailSet, PasswordSet).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){

                    Map<String, Object> map = new HashMap<>();
                    map.put("Name",NameSet);
                    map.put("LastName",LastNameSet);
                    map.put("Phone",PhoneSet);
                    map.put("Email",EmailSet);
                    map.put("Password",PasswordSet);

                        String id = firebaseAuth.getCurrentUser().getUid();
                        mDatabase.child(Data_Reference.Client_Info_reference).child(id).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task2) {
                                if (task2.isSuccessful()){
                                    progressDialog.cancel();
                                    Toast.makeText(getContext(), "Conductor registrado", Toast.LENGTH_SHORT).show();
                                }

                                else {

                                    progressDialog.cancel();
                                    Toast.makeText(getContext(), "Ya registrado", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });

                }

                else {

                    progressDialog.cancel();
                    Toast.makeText(getContext(), "No se pudo registrar el usuario: "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                }


            }
        });


    }
}
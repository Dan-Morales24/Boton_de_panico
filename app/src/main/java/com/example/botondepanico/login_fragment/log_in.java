package com.example.botondepanico.login_fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.botondepanico.Activities.MainActivity;
import com.example.botondepanico.Pojos.UserModel;
import com.example.botondepanico.R;
import com.example.botondepanico.Reference.Data_Reference;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class log_in extends Fragment {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Button Login, Register;
    EditText User, Password;
    String SharedName,SharedLastName,SharedPhone,SharedEmail,SharedAvatar;
    private ProgressDialog progressDialog;
    private FirebaseDatabase database;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;


    public log_in() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {


        sharedPreferences = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        UserModel userModel = new UserModel();
       // userModel.setName(sharedPreferences.contains(Name));
        if(sharedPreferences.contains("Name") && sharedPreferences.contains("Email")){

            SharedName = sharedPreferences.getString("Name","Nombre");
            SharedLastName = sharedPreferences.getString("LastName","Apellido");
            SharedEmail = sharedPreferences.getString("Email","Correo Electronico");
            SharedPhone = sharedPreferences.getString("Phone","Numero de telefono");
            SharedAvatar = sharedPreferences.getString("Avatar","Avatar");

            userModel.setName(SharedName);
            userModel.setLastName(SharedLastName);
            userModel.setEmail(SharedEmail);
            userModel.setPhone(SharedPhone);
            userModel.setAvatar(SharedAvatar);
            Data_Reference.currentClient = userModel;

            startActivity(new Intent(getContext(), MainActivity.class));
            getActivity().finish();

        }
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {




        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_log_in, container, false);

        Login = (Button)view.findViewById(R.id.Login);
        Register = (Button)view.findViewById(R.id.register);
        User = (EditText)view.findViewById(R.id.Inicio_Correo);
        Password=(EditText)view.findViewById(R.id.Inicio_Password);
        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.log_in_register);
            }
        });
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterUser();
            }
        });
        return view;
    }

    private void RegisterUser() {

        String userSet, passwordSet;
        userSet = User.getText().toString();
        passwordSet = Password.getText().toString();
        if(!userSet.isEmpty() && !passwordSet.isEmpty()){

            progressDialog = ProgressDialog.show(getContext(), "Espera un poquito..",
                    "Estoy verificando tus datos.", true);
            loginUser(userSet,passwordSet);
        }

        else{
            Toast.makeText(getContext(), "Faltan datos por completar ", Toast.LENGTH_SHORT).show();
        }

    }

    private void loginUser(String userSet, String passwordSet) {

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference(Data_Reference.Client_Info_reference);
        firebaseAuth = FirebaseAuth.getInstance();





        firebaseAuth.signInWithEmailAndPassword(userSet, passwordSet).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    checkUserFromFirebase();
                    FirebaseUser user = firebaseAuth.getCurrentUser();
              //      Toast.makeText(getContext(), "Si existe el usuario" , Toast.LENGTH_SHORT).show();
                    progressDialog.cancel();
                }

                else {
                    Toast.makeText(getContext(), "Error en la consulta de datos" , Toast.LENGTH_SHORT).show();
                    progressDialog.cancel();
                }

            }
        });


    }

    private void checkUserFromFirebase() {

        databaseReference.child(FirebaseAuth.getInstance().getCurrentUser()
                .getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    UserModel userModel = snapshot.getValue(UserModel.class);

                    String SharedName = snapshot.child("Name").getValue(String.class);
                    String SharedlastName = snapshot.child("LastName").getValue(String.class);
                    String SharedPhone = snapshot.child("Phone").getValue(String.class);
                    String SharedEmail = snapshot.child("Email").getValue(String.class);
                    String SharedAvatar = snapshot.child("Avatar").getValue(String.class);

                    sharedPreferences = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
                    editor = sharedPreferences.edit();
                    editor.putString("Name", SharedName);
                    editor.putString("LastName", SharedlastName);
                    editor.putString("Phone", SharedPhone);
                    editor.putString("Email", SharedEmail);
                    editor.putString("Avatar",SharedAvatar);
                    editor.commit();

                    goToButtonOfPanic(userModel);
                }
                else{



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void goToButtonOfPanic(UserModel userModel) {

        Data_Reference.currentClient = userModel;
        progressDialog.cancel();
        startActivity(new Intent(getContext(), MainActivity.class));
        getActivity().finish();

    }
}
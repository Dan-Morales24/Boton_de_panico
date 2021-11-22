package com.example.botondepanico.ui.Edit_Information;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.botondepanico.Interfaces.UpdateData;
import com.example.botondepanico.Pojos.UserModel;
import com.example.botondepanico.R;
import com.example.botondepanico.Reference.Data_Reference;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;


public class Edit_information extends Fragment {


    String NameShared,LastNameShared,PhoneShared;
    String SetName,SetlastName,SetPhone;
    EditText NameChange,LastNameChange, PhoneChange;
    Button ChangeData;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    UpdateData callback;



    public Edit_information() {
        // Required empty public constructor
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        firebaseAuth=FirebaseAuth.getInstance();
        sharedPreferences = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        databaseReference = FirebaseDatabase.getInstance().getReference();


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_information, container, false);

        NameChange = (EditText) view.findViewById(R.id.NameChange);
        LastNameChange = (EditText) view.findViewById(R.id.LastNameChange);
        PhoneChange = (EditText) view.findViewById(R.id.PhoneChange);
        ChangeData = (Button) view.findViewById(R.id.ChangeData);


        NameShared = sharedPreferences.getString("Name","Nombre");
        LastNameShared = sharedPreferences.getString("LastName","Apellidos");
        PhoneShared = sharedPreferences.getString("Phone","Numero de telefono");
        NameChange.setText(NameShared);
        LastNameChange.setText(LastNameShared);
        PhoneChange.setText(PhoneShared);


        ChangeData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.updateDataDrawer();
                SetName = NameChange.getText().toString();
                SetlastName = LastNameChange.getText().toString();
                SetPhone = PhoneChange.getText().toString();

                if(NameShared.equals(SetName)&&LastNameShared.equals(SetlastName)&&PhoneShared.equals(SetPhone)){

                    Toast.makeText(getContext(), "Datos actualizados.", Toast.LENGTH_LONG).show();

                }


                    else {


                     Map<String,Object> personMap = new HashMap<>();
                    personMap.put("Name",SetName);
                    personMap.put("LastName",SetlastName);
                    personMap.put("Phone",SetPhone);

                    String id = firebaseAuth.getCurrentUser().getUid();
                    databaseReference.child(Data_Reference.Client_Info_reference).child(id).updateChildren(personMap)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    editor = sharedPreferences.edit();
                                    editor.putString("Name", SetName);
                                    editor.putString("LastName", SetlastName);
                                    editor.putString("Phone", SetPhone);
                                    editor.commit();

                                    NameShared = sharedPreferences.getString("Name","Nombre");
                                    LastNameShared = sharedPreferences.getString("LastName","Apellidos");
                                    PhoneShared = sharedPreferences.getString("Phone","Numero de telefono");

                                    UserModel userModel = new UserModel();
                                    userModel.setName(SetName);
                                    userModel.setLastName(SetlastName);
                                    userModel.setPhone(SetPhone);

                                    Data_Reference.currentClient = userModel;
                                    Toast.makeText(getContext(), "Tarea completada", Toast.LENGTH_LONG).show();
                                    callback.updateDataDrawer();

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(getContext(), "No se pudieron actualizar los datos", Toast.LENGTH_LONG).show();


                        }
                    });

                }

            }
        });

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        callback = (UpdateData) context;
        super.onAttach(context);
    }
}
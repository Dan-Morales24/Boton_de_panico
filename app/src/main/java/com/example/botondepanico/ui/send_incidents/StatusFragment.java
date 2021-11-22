package com.example.botondepanico.ui.send_incidents;

import static android.app.Activity.RESULT_OK;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.botondepanico.R;
import com.example.botondepanico.Reference.Data_Reference;
import com.example.botondepanico.databinding.FragmentSlideshowBinding;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class StatusFragment extends Fragment {

    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Button LoadData;
    private StorageReference storageReference;
    private ProgressDialog progressDialog;
    Uri path;

    private EditText TitleName,Name,LastName,NumberPhone, Email, LocationIncident, Description;
    private Button SendData;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_status, container, false);
        TitleName = (EditText) view.findViewById(R.id.NameIncidentStatus);
        Name = (EditText) view.findViewById(R.id.NamePersonIncident);
        LastName = (EditText) view.findViewById(R.id.LastNamePersonIncident);
        NumberPhone = (EditText) view.findViewById(R.id.PhonePersonIncident);
        Email = (EditText) view.findViewById(R.id.EmailPersonIncident);
        Description = (EditText) view.findViewById(R.id.ComentsIncident);
        LocationIncident = (EditText) view.findViewById(R.id.LocationPersonIncident);


        SendData = (Button) view.findViewById(R.id.Submit);
        LoadData = (Button) view.findViewById(R.id.UpImage);
        firebaseAuth=FirebaseAuth.getInstance();
        sharedPreferences = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();

        Name.setText(sharedPreferences.getString("Name","Nombre"));
        LastName.setText(sharedPreferences.getString("LastName","Apellido"));
        NumberPhone.setText(sharedPreferences.getString("Phone","Numero de telefono"));
        Email.setText(sharedPreferences.getString("Email","Email"));
        SendData.setOnClickListener(v -> { SubmitInformation(); });
        LoadData.setOnClickListener(v -> { loadDataInFirebase(); });

        return view;
    }


    private void loadDataInFirebase() {

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("*/*");
        startActivityForResult(Intent.createChooser(intent, "Seleccionar la aplicacion"),10);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            path=data.getData();
        }
    }

    private void SubmitInformation() {

        String TitleGet = TitleName.getText().toString();
        String NameGet= Name.getText().toString();
        String LastNameGet = LastName.getText().toString();
        String NumberphoneGet = NumberPhone.getText().toString();
        String EmailGet = Email.getText().toString();
        String DescriptionGet = Description.getText().toString();
        String LocationGet = LocationIncident.getText().toString();

        if(!TitleGet.isEmpty() && !NameGet.isEmpty() && !LastNameGet.isEmpty() && !NumberphoneGet.isEmpty() && !EmailGet.isEmpty() && !DescriptionGet.isEmpty() && !LocationGet.isEmpty() ){
            progressDialog = ProgressDialog.show(getContext(), "Ten paciencia..",
                    "Estamos enviando tu denuncia a la central.", true);

            int p = (int) (Math.random() * 25 +1);int s = (int) (Math.random() * 25 + 1);
            int t = (int) (Math.random() * 25 +1);int c = (int) (Math.random() * 25 + 1);
            int number_1 = (int) (Math.random() * 1012 + 2111);
            int number_2 = (int) (Math.random() * 1012 + 2111);
            int number_3 = (int) (Math.random() * 1012 + 2111);
            int number_4 = (int) (Math.random() * 1012 + 2111);

            String[] elements = {"a","b","c","d","e","f","g","h","i","j","k","l"
                    ,"m","n","o","p","q","r","s","t","u","v","w","x","y","z"};
            final  String idIncident =
                    number_1 + number_2+number_3+number_4+"CitizenComplaint";

            String id = firebaseAuth.getCurrentUser().getUid();

            if(path!=null){
                StorageReference avatarFolder = storageReference.child("IncidentsComplaint/" +idIncident);
                UploadTask uploadTask = avatarFolder.putFile(path);

                Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                       if(!task.isSuccessful()){
                           throw Objects.requireNonNull(task.getException());
                       }
                        return avatarFolder.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        Uri downloaduri = task.getResult();
                        Map<String, Object> map = new HashMap<>();
                        map.put("IdUser",id);
                        map.put("Id_Incident",idIncident);
                        map.put("TitleComplaint", TitleGet);
                        map.put("Name",NameGet);
                        map.put("LastName",LastNameGet);
                        map.put("NumberPhone",NumberphoneGet);
                        map.put("Email",EmailGet);
                        map.put("Description",DescriptionGet);
                        map.put("Location",LocationGet);
                        map.put("Type_of_alert","Denuncia ciudadana");
                        map.put("Status","Pendiente");
                        map.put("IncidentComplaint",downloaduri.toString());

                        databaseReference.child(Data_Reference.SendIncident).push().setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task2) {
                                if (task2.isSuccessful()){
                                    Snackbar.make(getActivity().findViewById(android.R.id.content),"Denuncia ciudadana enviada correctamente.",Snackbar.LENGTH_SHORT).show();
                                    TitleName.setText("");
                                    Name.setText("");
                                    LastName.setText("");
                                    NumberPhone.setText("");
                                    Email.setText("");
                                    Description.setText("");
                                    progressDialog.cancel();
                                }


                                else {
                                    Snackbar.make(getActivity().findViewById(android.R.id.content),"Problemas con la conexión, intente de nuevo.",Snackbar.LENGTH_SHORT).show();
                                    progressDialog.cancel();
                                }
                            }
                        });

                    }


                });


            }else{

                Map<String, Object> map = new HashMap<>();
                map.put("IdUser",id);
                map.put("Id_Incident",idIncident);
                map.put("TitleComplaint", TitleGet);
                map.put("Name",NameGet);
                map.put("LastName",LastNameGet);
                map.put("NumberPhone",NumberphoneGet);
                map.put("Email",EmailGet);
                map.put("Description",DescriptionGet);
                map.put("Location",LocationGet);
                map.put("Type_of_alert","Denuncia ciudadana");
                map.put("Status","Pendiente");

                databaseReference.child(Data_Reference.SendIncident).push().setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task2) {
                        if (task2.isSuccessful()){
                            Snackbar.make(getActivity().findViewById(android.R.id.content),"Denuncia ciudadana enviada correctamente.",Snackbar.LENGTH_SHORT).show();
                            TitleName.setText("");
                            Name.setText("");
                            LastName.setText("");
                            NumberPhone.setText("");
                            Email.setText("");
                            Description.setText("");
                            progressDialog.cancel();
                        }


                        else {
                            progressDialog.cancel();
                            Snackbar.make(getActivity().findViewById(android.R.id.content),"Problemas con la conexión, intente de nuevo.",Snackbar.LENGTH_SHORT).show();
                        }
                    }
                });


            }

        }

        else {
            Toast.makeText(getContext(), "Faltan datos por completar", Toast.LENGTH_LONG).show();

        }








    }


}
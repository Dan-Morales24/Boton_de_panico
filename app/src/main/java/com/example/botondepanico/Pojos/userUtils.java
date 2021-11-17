package com.example.botondepanico.Pojos;

import android.view.View;

import com.example.botondepanico.Reference.Data_Reference;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Map;

public class userUtils {
    public static void updateUser(View view, Map<String, Object> updateData){
        FirebaseDatabase.getInstance()
                //RIDER
                .getReference(Data_Reference.Client_Info_reference)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .updateChildren(updateData)
                .addOnFailureListener(e -> Snackbar.make(view, e.getMessage(), Snackbar.LENGTH_SHORT).show())
                .addOnSuccessListener(aVoid -> Snackbar.make(view, "Actualizacion de informacion completada",Snackbar.LENGTH_SHORT).show());
    }





}

package com.example.botondepanico.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.botondepanico.R;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_BotonDePanico_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


    }
}
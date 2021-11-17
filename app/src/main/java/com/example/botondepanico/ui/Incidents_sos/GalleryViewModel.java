package com.example.botondepanico.ui.Incidents_sos;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class GalleryViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public GalleryViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Esta fragmento es de Incidentes");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
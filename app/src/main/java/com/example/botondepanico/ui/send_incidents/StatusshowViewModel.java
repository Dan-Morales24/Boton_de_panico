package com.example.botondepanico.ui.send_incidents;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class StatusshowViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public StatusshowViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Este es el fragmento de Status");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
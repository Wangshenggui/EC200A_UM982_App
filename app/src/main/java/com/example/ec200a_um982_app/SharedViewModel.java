package com.example.ec200a_um982_app;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {
    private MutableLiveData<String> data = new MutableLiveData<>();

    public MutableLiveData<String> getData() {
        return data;
    }

    public void setData(String input) {
        data.setValue(input);
    }
}

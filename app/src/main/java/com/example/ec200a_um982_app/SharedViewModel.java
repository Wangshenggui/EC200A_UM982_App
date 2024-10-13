package com.example.ec200a_um982_app;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {
    private final MutableLiveData<String> dataGroup1 = new MutableLiveData<>();
    private final MutableLiveData<String> dataGroup2 = new MutableLiveData<>();

    public void setDataGroup1(String value) {
        dataGroup1.setValue(value);
    }

    public MutableLiveData<String> getDataGroup1() {
        return dataGroup1;
    }

    public void setDataGroup2(String value) {
        dataGroup2.setValue(value);
    }

    public MutableLiveData<String> getDataGroup2() {
        return dataGroup2;
    }
}

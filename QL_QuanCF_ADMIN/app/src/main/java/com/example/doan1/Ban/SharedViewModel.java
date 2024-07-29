package com.example.doan1.Ban;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {
    private final MutableLiveData<Boolean> tableStatusUpdated = new MutableLiveData<>();

    public void setTableStatusUpdated(boolean updated) {
        tableStatusUpdated.setValue(updated);
    }

    public LiveData<Boolean> getTableStatusUpdated() {
        return tableStatusUpdated;
    }
}

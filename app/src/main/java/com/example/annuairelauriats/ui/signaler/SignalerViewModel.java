package com.example.annuairelauriats.ui.signaler;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class SignalerViewModel extends ViewModel {



    private MutableLiveData<String> mText;

    public SignalerViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Signaler fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}

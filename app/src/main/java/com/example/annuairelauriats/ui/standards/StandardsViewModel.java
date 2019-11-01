package com.example.annuairelauriats.ui.standards;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class StandardsViewModel extends ViewModel {



    private MutableLiveData<String> mText;

    public StandardsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Standards fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }

}

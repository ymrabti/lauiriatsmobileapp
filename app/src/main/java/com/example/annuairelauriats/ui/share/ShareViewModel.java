package com.example.annuairelauriats.ui.share;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ShareViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ShareViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Merci de nous aider en partagant notre application !");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
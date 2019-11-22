package com.example.annuairelauriats.ui.gallery;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class GalleryViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private MutableLiveData<String> personnaliser_filter;

    public GalleryViewModel() {
        mText = new MutableLiveData<>();personnaliser_filter = new MutableLiveData<>();
        mText.setValue("Liste des Laureats");personnaliser_filter.setValue("personnaliser votre filtre");
    }
    public LiveData<String> getText() {
        return mText;
    }
    public LiveData<String> getPersonnaliser() {
        return personnaliser_filter;
    }
}
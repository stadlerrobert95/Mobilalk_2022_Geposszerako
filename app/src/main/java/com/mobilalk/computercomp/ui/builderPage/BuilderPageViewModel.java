package com.mobilalk.computercomp.ui.builderPage;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BuilderPageViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public BuilderPageViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Builder fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
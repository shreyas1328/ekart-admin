package com.shreyas.ekart.ViewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.shreyas.ekart.models.DetailsModel;
import com.shreyas.ekart.repository.ContentRepository;

import java.util.List;

public class ContentViewModel extends AndroidViewModel {

    private ContentRepository contentRepository;
    private LiveData<List<DetailsModel>> mList;

    public ContentViewModel(Application application) {
        super(application);
        contentRepository = new ContentRepository(application);
        mList = contentRepository.getAllItems();
    }

    public LiveData<List<DetailsModel>> getAllData() {
        return mList;
    }

    public void insertData(DetailsModel model) {
         contentRepository.insertData(model);
    }

    public void updateData(DetailsModel model) {
        contentRepository.upateData(model);
    }

    public void deleteData(int id) {
        contentRepository.deleteData(id);
    }

    public void deleteAll(DetailsModel model) {
         contentRepository.deleteAll(model);
    }

    public LiveData<DetailsModel> getParticularItem(int id) {
        return contentRepository.getParticularItem(id);
    }
}

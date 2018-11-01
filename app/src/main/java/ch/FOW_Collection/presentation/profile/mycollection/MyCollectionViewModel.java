package ch.FOW_Collection.presentation.profile.mycollection;

import android.util.Pair;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import ch.FOW_Collection.data.repositories.*;
import ch.FOW_Collection.domain.models.*;
import com.google.common.base.Strings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static androidx.lifecycle.Transformations.map;
import static ch.FOW_Collection.domain.liveData.LiveDataExtensions.zip;

public class MyCollectionViewModel extends ViewModel implements CurrentUser {

    private static final String TAG = "MyCollectionViewModel";

    private final LiveData<List<MyCard>> myCollection;

    public MyCollectionViewModel() {
        MyCollectionRepository myCollectionRepository = new MyCollectionRepository();
        MutableLiveData<String> currentUserId = new MutableLiveData<>();
        // todo make it work MyCollection
        myCollection = null; //myCollectionRepository.getCollectionByUser(currentUserId.getValue());
        currentUserId.setValue(getCurrentUser().getUid());
    }

    public LiveData<List<MyCard>> getMyCollection() {return myCollection;}

    public void setSearchTerm(String searchTerm) {
        // todo make it work MyCollection
        throw new UnsupportedOperationException();
    }

    public LiveData<List<MyCard>> getMyFilteredCards() {
        // todo make it work MyCollection
        throw new UnsupportedOperationException();
    }
}
package ch.FOW_Collection.presentation.profile.mycollection;

import android.util.Pair;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import ch.FOW_Collection.data.repositories.*;
import ch.FOW_Collection.domain.models.*;
import ch.FOW_Collection.presentation.MainViewModel;
import com.google.common.base.Strings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static androidx.lifecycle.Transformations.map;
import static ch.FOW_Collection.domain.liveData.LiveDataExtensions.zip;

public class MyCollectionViewModel extends MainViewModel {

    private static final String TAG = "MyCollectionViewModel";

    public MyCollectionViewModel() {
    }

    public void setSearchTerm(String searchTerm) {
        // todo make it work MyCollection
        throw new UnsupportedOperationException();
    }

    public LiveData<List<MyCard>> getMyFilteredCards() {
        // todo make it work MyCollection
        throw new UnsupportedOperationException();
    }
}
package ch.FOW_Collection.presentation.profile.mycollection;

import android.util.Pair;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import ch.FOW_Collection.data.repositories.*;
import ch.FOW_Collection.domain.models.*;
import com.google.android.gms.tasks.Task;
import ch.FOW_Collection.presentation.MainViewModel;
import com.google.common.base.Strings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static androidx.lifecycle.Transformations.map;
import static ch.FOW_Collection.domain.liveData.LiveDataExtensions.zip;

public class MyCollectionViewModel extends MainViewModel {

    private static final String TAG = "MyCollectionViewModel";

    private final MutableLiveData<String> searchTerm = new MutableLiveData<>();
    private final LiveData<List<MyCard>> myCollection;
    private final LiveData<List<MyCard>> myFilteredCards;
    private final MyCollectionRepository myCollectionRepository = new MyCollectionRepository();
    private final MutableLiveData<String> currentUserId;

    public MyCollectionViewModel() {

        currentUserId = new MutableLiveData<>();

        myCollection = myCollectionRepository.getMyCollection(currentUserId);
        myFilteredCards = map(zip(searchTerm, myCollection), MyCollectionViewModel::filter);

        currentUserId.setValue(getCurrentUserId().getValue());
    }

    public LiveData<List<MyCard>> getMyCollection() {return myCollection;}

    private static List<MyCard> filter(Pair<String, List<MyCard>> input) {
        String searchTerm1 = input.first;
        List<MyCard> myCards = input.second;
        if (Strings.isNullOrEmpty(searchTerm1)) {
            return myCards;
        }
        if (myCards == null) {
            return Collections.emptyList();
        }
        ArrayList<MyCard> filtered = new ArrayList<>();
        for (MyCard card : myCards) {
            if (card.getId().toLowerCase().contains(searchTerm1.toLowerCase())) {
                filtered.add(card);
            }
        }
        return filtered;
    }

    public LiveData<List<MyCard>> getMyFilteredCards() {
        return myFilteredCards;
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm.setValue(searchTerm);
    }

    public Task<Void> toggleCardInCollection(String itemId){
        return myCollectionRepository.toggleCardInCollection(currentUserId.getValue(), itemId);
    }

    public Task<Void> addOneToCardAmount(MyCard myCard, String type){
        return myCollectionRepository.addOneToCardAmount(currentUserId.getValue(), myCard, type);
    }

    public Task<Void> subOneFromCardAmount(MyCard myCard, String type){
        return myCollectionRepository.subOneFromCardAmount(currentUserId.getValue(), myCard, type);
    }
}

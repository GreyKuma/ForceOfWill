package ch.FOW_Collection.presentation.explore.search;

import android.util.Pair;

import ch.FOW_Collection.presentation.MainViewModel;
import com.google.common.base.Strings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import ch.FOW_Collection.data.repositories.CardsRepository;
import ch.FOW_Collection.data.repositories.CurrentUser;
import ch.FOW_Collection.data.repositories.SearchesRepository;
import ch.FOW_Collection.data.repositories.WishlistRepository;
import ch.FOW_Collection.domain.models.Card;
import ch.FOW_Collection.domain.models.Search;

import static androidx.lifecycle.Transformations.map;
import static androidx.lifecycle.Transformations.switchMap;
import static ch.FOW_Collection.domain.liveData.LiveDataExtensions.zip;

public class SearchViewModel extends MainViewModel {

    private static final String TAG = "SearchViewModel";
    private final MutableLiveData<String> searchTerm = new MutableLiveData<>();

    private final LiveData<List<Card>> filteredCards;
    private final SearchesRepository searchesRepository;
    private final LiveData<List<Search>> myLatestSearches;

    public SearchViewModel() {
        searchesRepository = new SearchesRepository();
        filteredCards = map(zip(searchTerm, getAllCards()), SearchViewModel::filter);
        myLatestSearches = switchMap(getCurrentUserId(), SearchesRepository::getLatestSearchesByUser);
    }

    private static List<Card> filter(Pair<String, List<Card>> input) {
        String searchTerm1 = input.first;
        List<Card> allCards = input.second;
        if (Strings.isNullOrEmpty(searchTerm1)) {
            return allCards;
        }
        if (allCards == null) {
            return Collections.emptyList();
        }
        ArrayList<Card> filtered = new ArrayList<>();
        for (Card card : allCards) {
            //TODO getName().getDe() should work
            if (card.getIdStr().toLowerCase().contains(searchTerm1.toLowerCase())) {
                filtered.add(card);
            }
        }
        return filtered;
    }

    public LiveData<List<Search>> getMyLatestSearches() {
        return myLatestSearches;
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm.setValue(searchTerm);
    }

    public LiveData<List<Card>> getFilteredCards() {
        return filteredCards;
    }

    public void addToSearchHistory(String term) {
        searchesRepository.addSearchTerm(term);
    }
}
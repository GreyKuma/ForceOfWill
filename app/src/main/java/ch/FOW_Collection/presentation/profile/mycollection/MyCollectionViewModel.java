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
    private final MutableLiveData<String> searchTerm = new MutableLiveData<>();

    private final WishlistRepository wishlistRepository;
    private final LiveData<List<MyCard>> myFilteredCards;

    public MyCollectionViewModel() {

        wishlistRepository = new WishlistRepository();
        CardsRepository cardsRepository = new CardsRepository();
        MyCollectionRepository myCollectionRepository = new MyCollectionRepository();
        RatingsRepository ratingsRepository = new RatingsRepository();

        LiveData<List<Card>> allCards = cardsRepository.getAllCards();
        MutableLiveData<String> currentUserId = new MutableLiveData<>();
        LiveData<List<Wish>> myWishlist = wishlistRepository.getMyWishlist(currentUserId);
        LiveData<List<Rating>> myRatings = ratingsRepository.getMyRatings(currentUserId);

        LiveData<List<MyCard>> myCards = myCollectionRepository.getMyCards(allCards, myWishlist, myRatings);

        myFilteredCards = map(zip(searchTerm, myCards), MyCollectionViewModel::filter);

        currentUserId.setValue(getCurrentUser().getUid());
    }

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
            if (card.getCard().getName().getDe().toLowerCase().contains(searchTerm1.toLowerCase())) {
                filtered.add(card);
            }
        }
        return filtered;
    }

    public LiveData<List<MyCard>> getMyFilteredCards() {
        return myFilteredCards;
    }

    public void toggleItemInWishlist(String cardId) {
        wishlistRepository.toggleUserWishlistItem(getCurrentUser().getUid(), cardId);
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm.setValue(searchTerm);
    }
}
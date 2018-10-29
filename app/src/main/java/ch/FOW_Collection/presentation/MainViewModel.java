package ch.FOW_Collection.presentation;

import android.util.Pair;
import ch.FOW_Collection.data.repositories.*;
import ch.FOW_Collection.domain.models.*;
import ch.FOW_Collection.presentation.profile.mycollection.MyCollectionActivity;
import com.firebase.ui.firestore.FirestoreArray;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.Query;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import ch.FOW_Collection.domain.liveData.FirestoreQueryLiveDataArray;

import java.util.List;

/**
 * This is the viewmodel for the {@link MainActivity}, which is also used by the three pages/fragments contained in it.
 */
public class MainViewModel extends ViewModel implements CurrentUser {

    private static final String TAG = "MainViewModel";

    private final CardsRepository cardsRepository;
    private final CardEditionsRepository cardEditionsRepository;

//    private final BeersRepository beersRepository;
//    private final LikesRepository likesRepository;
    private final RatingsRepository ratingsRepository;
    private final WishlistRepository wishlistRepository;

    private final LiveData<List<Wish>> myWishlist;
    private final LiveData<List<MyCard>> myCollection;
    private final LiveData<List<Rating>> myRatings;
//    private final LiveData<List<MyBeer>> myBeers;

    public MainViewModel() {
        /*
         * TODO We should really be injecting these!
         */
        cardsRepository = new CardsRepository();
        cardEditionsRepository = new CardEditionsRepository();

//        beersRepository = new BeersRepository();
//        likesRepository = new LikesRepository();
        wishlistRepository = new WishlistRepository();
        ratingsRepository = new RatingsRepository();
//        MyBeersRepository myBeersRepository = new MyBeersRepository();
        MyCollectionRepository myCollectionRepository = new MyCollectionRepository();

//        LiveData<List<Beer>> allBeers = beersRepository.getAllBeers();
        LiveData<List<Card>> allCards = cardsRepository.getAllCards();

        MutableLiveData<String> currentUserId = new MutableLiveData<>();
        myWishlist = wishlistRepository.getMyWishlist(currentUserId);
        myRatings = ratingsRepository.getMyRatings(currentUserId);
        myCollection = myCollectionRepository.getMyCards(allCards, myWishlist, myRatings);
//        myCollection = myCollectionRepository.getMyCollection();
        // myBeers = myBeersRepository.getMyBeers(allBeers, myWishlist, myRatings);

        /*
         * Set the current user id, which is used as input for the getMyWishlist and getMyRatings calls above.
         * Settings the id does not yet cause any computation or data fetching, only when an observer is subscribed
         * to the LiveData will the data be fetched and computations depending on it will be executed. LiveData works
         * similar to Java 8 streams or Rx observables in that regard, but have a less rich API for combining such
         * streams of data.
         * */
        currentUserId.setValue(getCurrentUser().getUid());
    }



//    public LiveData<List<MyBeer>> getMyBeers() {
//        return myBeers;
//    }

//    public LiveData<List<Rating>> getMyRatings() {
//        return myRatings;
//    }

    public LiveData<List<MyCard>> getMyCollection(){
        return myCollection;
    }

    public LiveData<List<Wish>> getMyWishlist() {
        return myWishlist;
    }

//    public LiveData<List<String>> getBeerCategories() {
//        return beersRepository.getBeerCategories();
//    }
//
//    public LiveData<List<String>> getBeerManufacturers() {
//        return beersRepository.getBeerManufacturers();
//    }

//    public void toggleLike(Rating rating) {
//        likesRepository.toggleLike(rating);
//    }

    public Task<Void> toggleItemInWishlist(String itemId) {
        return wishlistRepository.toggleUserWishlistItem(getCurrentUser().getUid(), itemId);
    }

//    public LiveData<List<Pair<Rating, Wish>>> getAllRatingsWithWishes() {
//        return ratingsRepository.getAllRatingsWithWishes(myWishlist);
//    }

    public FirestoreQueryLiveDataArray<Card> getCardsTopRated(int limit) {
        return cardsRepository.getCardsTopRated(limit);
    }

    public FirestoreQueryLiveDataArray<CardEdition> getCardEditions() {
        return cardEditionsRepository.getAllEditions();
    }
}

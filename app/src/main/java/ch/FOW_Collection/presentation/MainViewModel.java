package ch.FOW_Collection.presentation;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import ch.FOW_Collection.data.repositories.BeersRepository;
import ch.FOW_Collection.data.repositories.CardEditionsRepository;
import ch.FOW_Collection.data.repositories.CardsRepository;
import ch.FOW_Collection.data.repositories.CurrentUser;
import ch.FOW_Collection.domain.liveData.FirestoreQueryLiveDataArray;
import ch.FOW_Collection.domain.models.Card;
import ch.FOW_Collection.domain.models.CardEdition;

/**
 * This is the viewmodel for the {@link MainActivity}, which is also used by the three pages/fragments contained in it.
 */
public class MainViewModel extends ViewModel implements CurrentUser {

    private static final String TAG = "MainViewModel";

    private final CardsRepository cardsRepository;
    private final CardEditionsRepository cardEditionsRepository;

    private final BeersRepository beersRepository;/*
    private final LikesRepository likesRepository;
    private final RatingsRepository ratingsRepository;
    private final WishlistRepository wishlistRepository;

    private final LiveData<List<Wish>> myWishlist;
    private final LiveData<List<Rating>> myRatings;
    private final LiveData<List<MyBeer>> myBeers;*/

    public MainViewModel() {
        /*
         * TODO We should really be injecting these!
         */
        cardsRepository = new CardsRepository();
        cardEditionsRepository = new CardEditionsRepository();

        beersRepository = new BeersRepository();/*
        likesRepository = new LikesRepository();
        wishlistRepository = new WishlistRepository();
        ratingsRepository = new RatingsRepository();
        MyBeersRepository myBeersRepository = new MyBeersRepository();

        LiveData<List<Beer>> allBeers = beersRepository.getAllBeers();
*/
        MutableLiveData<String> currentUserId = new MutableLiveData<>();
        // myWishlist = wishlistRepository.getMyWishlist(currentUserId);
        // myRatings = ratingsRepository.getMyRatings(currentUserId);
        /// myBeers = myBeersRepository.getMyBeers(allBeers, myWishlist, myRatings);

        /*
         * Set the current user id, which is used as input for the getMyWishlist and getMyRatings calls above.
         * Settings the id does not yet cause any computation or data fetching, only when an observer is subscribed
         * to the LiveData will the data be fetched and computations depending on it will be executed. LiveData works
         * similar to Java 8 streams or Rx observables in that regard, but have a less rich API for combining such
         * streams of data.
         * */
        currentUserId.setValue(getCurrentUser().getUid());
    }


    /*
    public LiveData<List<MyBeer>> getMyBeers() {
        return myBeers;
    }

    public LiveData<List<Rating>> getMyRatings() {
        return myRatings;
    }

    public LiveData<List<Wish>> getMyWishlist() {
        return myWishlist;
    }

    public LiveData<List<String>> getBeerCategories() {
        return beersRepository.getBeerCategories();
    }

    public LiveData<List<String>> getBeerManufacturers() {
        return beersRepository.getBeerManufacturers();
    }

    public void toggleLike(Rating rating) {
        likesRepository.toggleLike(rating);
    }

    public Task<Void> toggleItemInWishlist(String itemId) {
        return wishlistRepository.toggleUserWishlistItem(getCurrentUser().getUid(), itemId);
    }

    public LiveData<List<Pair<Rating, Wish>>> getAllRatingsWithWishes() {
        return ratingsRepository.getAllRatingsWithWishes(myWishlist);
    }*/

    public FirestoreQueryLiveDataArray<Card> getCardsTopRated(int limit) {
        return cardsRepository.getCardsTopRated(limit);
    }

    public FirestoreQueryLiveDataArray<CardEdition> getCardEditions() {
        return cardEditionsRepository.getAllEditions();
    }
}
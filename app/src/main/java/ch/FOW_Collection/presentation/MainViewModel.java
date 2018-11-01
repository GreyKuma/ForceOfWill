package ch.FOW_Collection.presentation;

import ch.FOW_Collection.data.repositories.*;
import ch.FOW_Collection.domain.models.*;
import ch.FOW_Collection.presentation.profile.mycollection.MyCollectionActivity;
import com.firebase.ui.firestore.FirestoreArray;
import ch.FOW_Collection.domain.models.Rating;
import ch.FOW_Collection.domain.models.Wish;
import com.google.android.gms.tasks.Task;

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

    protected final CardsRepository cardsRepository;
    protected final CardEditionsRepository cardEditionsRepository;

    protected final RatingsRepository ratingsRepository;
    protected final WishlistRepository wishlistRepository;

    protected final LiveData<List<Wish>> myWishlist;
    protected final LiveData<List<MyCard>> myCollection;
    protected final LiveData<List<Rating>> myRatings;

    protected final UserRepository userRepository;
    protected final MutableLiveData<String> currentUserId;
    protected final LiveData<User> currentUser;

    public MainViewModel() {
        /*
         * TODO We should really be injecting these!
         */
        cardsRepository = new CardsRepository();
        cardEditionsRepository = new CardEditionsRepository();

        wishlistRepository = new WishlistRepository();
        ratingsRepository = new RatingsRepository();
        MyCollectionRepository myCollectionRepository = new MyCollectionRepository();

        userRepository = new UserRepository();

        currentUserId = new MutableLiveData<>();
        currentUser = userRepository.getUserById(currentUserId);

        myWishlist = wishlistRepository.getWishlistByUserId(currentUserId);
        myRatings = ratingsRepository.getRatingsByUserId(currentUserId);

        // todo make it work MyCollection
        myCollection = null; // myCollectionRepository.getCollectionByUser(currentUserId.getValue());

        MutableLiveData<String> currentUserId = new MutableLiveData<>();
        myWishlist = wishlistRepository.getMyWishlist(currentUserId);
        myRatings = ratingsRepository.getMyRatings(currentUserId);
        myCollection = myCollectionRepository.getMyCollection(currentUserId);
        // myBeers = myBeersRepository.getMyBeers(allBeers, myWishlist, myRatings);

        /*
         * Set the current user id, which is used as input for the getMyWishlist and getMyRatings calls above.
         * Settings the id does not yet cause any computation or data fetching, only when an observer is subscribed
         * to the LiveData will the data be fetched and computations depending on it will be executed. LiveData works
         * similar to Java 8 streams or Rx observables in that regard, but have a less rich API for combining such
         * streams of data.
         * */
        currentUserId.setValue(getCurrentFirebaseUser().getUid());
    }



//    public LiveData<List<MyBeer>> getMyBeers() {
//        return myBeers;
//    }

    public MutableLiveData<String> getCurrentUserId() {
        return currentUserId;
    }

    public LiveData<User> getCurrentUser() {
        return currentUser;
    }

    public LiveData<List<Rating>> getMyRatings() {
        return myRatings;
    }

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
        return wishlistRepository.toggleUserWishlistItem(currentUserId.getValue(), itemId);
    }

//    public LiveData<List<Pair<Rating, Wish>>> getAllRatingsWithWishes() {
//        return ratingsRepository.getAllRatingsWithWishes(myWishlist);
//    }

    /*
     * CardRepository
     */


    public FirestoreQueryLiveDataArray<Card> getAllCards() {
        return cardsRepository.getAllCards();
    }

    public LiveData<Card> getCardById(String cardId) {
        return cardsRepository.getCardById(cardId);
    }

    public FirestoreQueryLiveDataArray<Card> getCardsTopRated(int limit) {
        return cardsRepository.getCardsTopRated(limit);
    }

    /*
     * CardEditionRepository
     */

    public FirestoreQueryLiveDataArray<CardEdition> getCardEditions() {
        return cardEditionsRepository.getAllEditions();
    }
}

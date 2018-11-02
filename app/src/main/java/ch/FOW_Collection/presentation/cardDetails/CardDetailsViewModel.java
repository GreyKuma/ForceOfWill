package ch.FOW_Collection.presentation.cardDetails;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import ch.FOW_Collection.data.repositories.CardsRepository;
import ch.FOW_Collection.data.repositories.CurrentUser;
import ch.FOW_Collection.data.repositories.MyCollectionRepository;
import ch.FOW_Collection.data.repositories.RatingsRepository;
import ch.FOW_Collection.data.repositories.WishlistRepository;
import ch.FOW_Collection.domain.models.Card;
import ch.FOW_Collection.domain.models.Rating;
import ch.FOW_Collection.domain.models.Wish;
import ch.FOW_Collection.presentation.MainViewModel;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Transaction;

import java.util.List;
import java.util.Map;

//import ch.FOW_Collection.domain.models.Beer;
//import ch.FOW_Collection.domain.models.Rating;
//import ch.FOW_Collection.domain.models.Wish;

public class CardDetailsViewModel extends MainViewModel {

    private final MutableLiveData<String> cardId = new MutableLiveData<>();
    private final LiveData<Card> card;
    private final LiveData<List<Rating>> ratings;
    private final LiveData<Rating> ownRating;
    private final LiveData<Wish> wish;

    //private final LikesRepository likesRepository;
    private final WishlistRepository wishlistRepository;
    private final MyCollectionRepository myCollectionRepository;

    public CardDetailsViewModel() {
        // TODO We should really be injecting these!
        CardsRepository cardsRepository = new CardsRepository();
        RatingsRepository ratingsRepository = new RatingsRepository();
        //likesRepository = new LikesRepository();
        myCollectionRepository = new MyCollectionRepository();
        wishlistRepository = new WishlistRepository();

        //MutableLiveData<String> currentUserId = new MutableLiveData<>();
        card = cardsRepository.getCardById(cardId);
        wish = wishlistRepository.getMyWishForCard(getCurrentUserId(), getCard());
        ratings = ratingsRepository.getRatingsByCardId(cardId);
        ownRating = ratingsRepository.getRatingsByUserIdAndCardId(getCurrentUserId(), cardId);
    }

    public LiveData<Card> getCard() {
        return card;
    }

    public LiveData<Wish> getWish() {
        return wish;
    }

    public LiveData<List<Rating>> getRatings() {
        return ratings;
    }

    public LiveData<Rating> getOwnRating() {
        return ownRating;
    }

    public void setCardId(String cardId) {
        this.cardId.setValue(cardId);
    }

    public void toggleLike(Rating rating) {
        if (!rating.getUserId().equals(currentUserId.getValue())) {
            if (rating.getLikes().containsKey(currentUserId.getValue())) {
                rating.getLikes().remove(currentUserId.getValue());
            } else {
                rating.getLikes().put(currentUserId.getValue(), true);
            }
            ratingsRepository.putRating(rating);
        }
    }

    public Task<Void> toggleItemInWishlist(String itemId) {
        return wishlistRepository.toggleUserWishlistItem(getCurrentUserId().getValue(), itemId);
    }

    public Task<Void> toggleItemInCollection(String itemId){
        return myCollectionRepository.toggleCardInCollection(getCurrentUserId().getValue(), itemId);
    }
}

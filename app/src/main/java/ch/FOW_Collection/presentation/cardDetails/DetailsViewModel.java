package ch.FOW_Collection.presentation.details;

import ch.FOW_Collection.data.repositories.*;
import com.google.android.gms.tasks.Task;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import ch.FOW_Collection.domain.models.Beer;
import ch.FOW_Collection.domain.models.Card;
import ch.FOW_Collection.domain.models.Rating;
import ch.FOW_Collection.domain.models.Wish;

public class DetailsViewModel extends ViewModel implements CurrentUser {

    private final MutableLiveData<String> cardId = new MutableLiveData<>();
    private final LiveData<Card> card;
    private final LiveData<List<Rating>> ratings;
    private final LiveData<Wish> wish;

    private final LikesRepository likesRepository;
    private final WishlistRepository wishlistRepository;

    public DetailsViewModel() {
        // TODO We should really be injecting these!
//        BeersRepository beersRepository = new BeersRepository();
        CardsRepository cardsRepository = new CardsRepository();
        RatingsRepository ratingsRepository = new RatingsRepository();
        likesRepository = new LikesRepository();
        wishlistRepository = new WishlistRepository();

        MutableLiveData<String> currentUserId = new MutableLiveData<>();
        card = cardsRepository.getCard(cardId);
        wish = wishlistRepository.getMyWishForCard(currentUserId, getCard());
        ratings = ratingsRepository.getRatingsForBeer(cardId);
        currentUserId.setValue(getCurrentUser().getUid());
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

    public void setCardId(String cardId) {
        this.cardId.setValue(cardId);
    }

    public void toggleLike(Rating rating) {
        likesRepository.toggleLike(rating);
    }

    public Task<Void> toggleItemInWishlist(String itemId) {
        return wishlistRepository.toggleUserWishlistItem(getCurrentUser().getUid(), itemId);
    }
}
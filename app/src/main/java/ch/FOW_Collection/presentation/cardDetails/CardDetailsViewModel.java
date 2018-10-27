package ch.FOW_Collection.presentation.cardDetails;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import ch.FOW_Collection.data.repositories.CardsRepository;
import ch.FOW_Collection.data.repositories.CurrentUser;
import ch.FOW_Collection.domain.models.Card;

//import ch.FOW_Collection.domain.models.Beer;
//import ch.FOW_Collection.domain.models.Rating;
//import ch.FOW_Collection.domain.models.Wish;

public class CardDetailsViewModel extends ViewModel implements CurrentUser {

    private final MutableLiveData<String> cardId = new MutableLiveData<>();
    private final LiveData<Card> card;
    //private final LiveData<List<Rating>> ratings;
    //private final LiveData<Wish> wish;

    //private final LikesRepository likesRepository;
    //private final WishlistRepository wishlistRepository;

    public CardDetailsViewModel() {
        // TODO We should really be injecting these!
        CardsRepository cardsRepository = new CardsRepository();
        //BeersRepository beersRepository = new BeersRepository();
        //RatingsRepository ratingsRepository = new RatingsRepository();
        //likesRepository = new LikesRepository();
        //wishlistRepository = new WishlistRepository();

        MutableLiveData<String> currentUserId = new MutableLiveData<>();
        card = cardsRepository.getCardById(cardId);
        //card = cardRepository.getBeer(cardId);
        //wish = wishlistRepository.getMyWishForBeer(currentUserId, getCard());
        //ratings = ratingsRepository.getRatingsForBeer(cardId);
        currentUserId.setValue(getCurrentUser().getUid());
    }

    public LiveData<Card> getCard() {
        return card;
    }

    /*
    public LiveData<Wish> getWish() {
        return wish;
    }

    public LiveData<List<Rating>> getRatings() {
        return ratings;
    }*/

    public void setCardId(String cardId) {
        this.cardId.setValue(cardId);
    }
/*
    public void toggleLike(Rating rating) {
        likesRepository.toggleLike(rating);
    }

    public Task<Void> toggleItemInWishlist(String itemId) {
        return wishlistRepository.toggleUserWishlistItem(getCurrentUser().getUid(), itemId);
    }*/
}
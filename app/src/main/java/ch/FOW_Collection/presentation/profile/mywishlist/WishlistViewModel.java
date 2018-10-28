package ch.FOW_Collection.presentation.profile.mywishlist;

import android.util.Pair;

import com.google.android.gms.tasks.Task;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import ch.FOW_Collection.data.repositories.CardsRepository;
import ch.FOW_Collection.data.repositories.CurrentUser;
import ch.FOW_Collection.data.repositories.WishlistRepository;
import ch.FOW_Collection.domain.models.Card;
import ch.FOW_Collection.domain.models.Wish;

public class WishlistViewModel extends ViewModel implements CurrentUser {

    private static final String TAG = "WishlistViewModel";

    private final MutableLiveData<String> currentUserId = new MutableLiveData<>();
    private final WishlistRepository wishlistRepository;
    private final CardsRepository cardsRepository;

    public WishlistViewModel() {
        wishlistRepository = new WishlistRepository();
        cardsRepository = new CardsRepository();

        currentUserId.setValue(getCurrentUser().getUid());
    }

    public LiveData<List<Pair<Wish, Card>>> getMyWishlistWithCards() {
        return wishlistRepository.getMyWishlistWithCards(currentUserId, cardsRepository.getAllCards());
    }

    public Task<Void> toggleItemInWishlist(String itemId) {
        return wishlistRepository.toggleUserWishlistItem(getCurrentUser().getUid(), itemId);
    }

}